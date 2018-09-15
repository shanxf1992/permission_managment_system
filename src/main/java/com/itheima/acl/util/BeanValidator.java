package com.itheima.acl.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itheima.acl.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * BeanValidator : 用于对Bean 类型的参数进行校验, 判断参数是否符合要求
 *
 * 需要注意的是, validator 的校验是基于注解的, @NotBlank @NotNull @NotEmpty
 */
public class BeanValidator {

    //1 首先需要一个 ValidatorFactory 工厂类, 作为项目全局的 校验工厂
    public static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     *  2 定义普通的校验方法 validator 用于校验单个 bean 可以校验多种类型
     *  返回值为 Map<String, String>
     *      key: 表示 bean 中不满足条件的字段
     *      value: 表示对应字段不满足条件的原因
     *  T : 传入的 bean 的类型
     *  Class... :
     */
    public static <T> Map<String, String> validate(T t, Class... groups) {
        //1 从工厂中获取 Validator
        Validator validator = validatorFactory.getValidator();

        //2 通过 validator 自定的获取校验的结果
        Set<ConstraintViolation<T>> validateResult = validator.validate(t, groups);

        //3 如果没有值, 说明验证通过
        if (validateResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            //如果不为空, 说名验证不通过, 获取其中的 错误信息
            LinkedHashMap<String, String> errors = Maps.newLinkedHashMap();
               
            //遍历 validateResult 取出错误信息
            Iterator<ConstraintViolation<T>> iterator = validateResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> error = iterator.next();
                //取出错误字段 PropertyPath 和 错误信息 Message
                errors.put(error.getPropertyPath().toString(), error.getMessage());
            }
            return errors;
        }
    }

    /**
     * 3 校验方法 validator 用于校验多个 bean 可以校验多种类型
     *      返回值为 Map<String, String>
     *      Collection : 传入的 bean 的集合
     */
    public static Map<String, String> validateList(Collection<?> collection) {
        //首先判断当前传入的 Collection 是否为空
        // Preconditions: google 提供的一个做基础校验的工具
        Preconditions.checkNotNull(collection);
        Iterator<?> iterator = collection.iterator();

        // 循环进行校验, 调用 validator 方法对单个 bean 进行校验,
        Map<String, String> errors;
        do {
            if (!iterator.hasNext()) {
                // 返回空集合, 说名全部校验通过
                return Collections.EMPTY_MAP;
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);

        } while (errors.isEmpty());

        //如果有 bean 不合法, 就返回错误信息
        return errors;
    }

    // 进行校验时, 需要根据传入的 bean 的数量, 区分调用 validator 还是 validateList 方法, 很不方便
    // 封装一个统一的方法, 能够使用这一个方法对所有的 参数 进行校验,
    public static Map<String, String> validateObject(Object first, Object... objects) {
        //首先, 需要判断传入的是 单个bean 还是 bean 的集合, 区分调用对应的方法
        // 如果传入的是 集合, 调用 vaildateList 方法进行验证
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            System.out.println("one param.. ...");
            return validate(first, new Class[0]);
        }
    }

    public static void check(Object param) throws ParamException {
        Map<String, String> map = validateObject(param);

        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }
    }
}
