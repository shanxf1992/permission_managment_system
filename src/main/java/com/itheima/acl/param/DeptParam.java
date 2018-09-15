package com.itheima.acl.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * DeptParam  : 对应 部门表 SysDept 的参数表, 用于对 部门表 SysDept中的某些参数进行验证.
 * 验证的方式是通过自定以的 BeanValidator 进行, 改验证方式是通过注解完成
 */
@Getter
@Setter
@ToString
public class DeptParam {

    //

    //SysDept 部门表中, 需要验证的字段
    private Integer id;
    @NotBlank
    @Length(max = 15, min = 2, message = "部门名称长度需要在 2-15 个字之间")
    private String name;

    private Integer parentId = 0;
    @NotNull(message = "展示顺序不能为空")
    private Integer seq;
    @Length(max = 150 , message = "备注的长度不能超过 150 字")
    private String remark;

}
