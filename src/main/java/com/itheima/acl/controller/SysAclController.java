package com.itheima.acl.controller;

import com.itheima.acl.beans.PageQuery;
import com.itheima.acl.beans.PageResult;
import com.itheima.acl.common.JsonData;
import com.itheima.acl.domain.SysAcl;
import com.itheima.acl.param.AclParam;
import com.itheima.acl.service.SysAclModuleService;
import com.itheima.acl.service.SysAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/sys/acl")
public class SysAclController {

    @Value("#{sysAclService}")
    private SysAclService sysAclService;

    @RequestMapping("/save.json")
    public JsonData save(AclParam aclParam) {
        sysAclService.save(aclParam);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    public JsonData update( AclParam aclParam) {
        sysAclService.update(aclParam);
        return JsonData.success();
    }

    @RequestMapping("/page.json")
    public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        PageResult<SysAcl> pageByAclModule = sysAclService.getPageByAclModule(aclModuleId, pageQuery);
        return JsonData.success(pageByAclModule);
    }


}
