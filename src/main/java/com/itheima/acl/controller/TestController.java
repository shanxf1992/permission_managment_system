package com.itheima.acl.controller;

import com.itheima.acl.common.ApplicationContextHelper;
import com.itheima.acl.common.JsonData;
import com.itheima.acl.exception.ParamException;
import com.itheima.acl.mapper.SysAclModuleMapper;
import com.itheima.acl.param.TestVo;
import com.itheima.acl.util.BeanValidator;
import com.itheima.acl.util.JsonMapper;
import com.itheima.acl.domain.SysAclModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
@Slf4j
public class TestController {

    @RequestMapping("hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
//        throw new PermissionException("test exception");
        return JsonData.success("hello, permission");
    }

    @RequestMapping("validate.json")
    @ResponseBody
    public JsonData validate(TestVo testVo) throws ParamException {
        log.info("validate");
        System.out.println(testVo);
        SysAclModuleMapper sysAclModuleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(1);

        log.info(JsonMapper.obj2String(sysAclModule));

        BeanValidator.check(testVo);
        return JsonData.success("test validate");
    }

}
