package com.itheima.acl.controller;

import com.itheima.acl.common.JsonData;
import com.itheima.acl.param.AclModuleParam;
import com.itheima.acl.service.SysAclModuleService;
import com.itheima.acl.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {

    @Value("#{sysAclModuleService}")
    private SysAclModuleService sysAclModuleService;

    @Value("#{sysTreeService}")
    private SysTreeService sysTreeService;

    @RequestMapping("/acl.page")
    public ModelAndView page() {
        return new ModelAndView("acl");
    }

    @RequestMapping("save.json")
    @ResponseBody
    public JsonData saveAclModule(AclModuleParam moduleParam) {
        sysAclModuleService.save(moduleParam);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam moduleParam) {
        sysAclModuleService.update(moduleParam);
        return JsonData.success();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        return JsonData.success(sysTreeService.aclModuleTree());
    }

}
