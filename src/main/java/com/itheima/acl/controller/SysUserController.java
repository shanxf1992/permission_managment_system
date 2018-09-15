package com.itheima.acl.controller;

import com.itheima.acl.beans.PageQuery;
import com.itheima.acl.beans.PageResult;
import com.itheima.acl.common.JsonData;
import com.itheima.acl.domain.SysUser;
import com.itheima.acl.param.UserParam;
import com.itheima.acl.service.SysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * SysUserController : 处理用户信息的请求
 * save(): 新增用户的方法
 * updata(): 更新用户信息
 */
@Controller
@RequestMapping("/sys/User")
public class SysUserController {

    @Value("#{sysUserService}")
    private SysUserService sysUserService;

    @RequestMapping("save.json")
    @ResponseBody
    public JsonData saveUser(UserParam userParam) {
        sysUserService.save(userParam);
        return JsonData.success();
    }

    @RequestMapping("update.json")
    @ResponseBody
    public JsonData updateUser(UserParam userParam) {
        sysUserService.update(userParam);
        return JsonData.success();
    }

    @RequestMapping("page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId")Integer deptId, PageQuery pageQuery) {

        PageResult<SysUser> pageResult = sysUserService.selectUserByDept(deptId, pageQuery);
        return JsonData.success(pageResult);
    }

    @RequestMapping("noAuth.page")
    @ResponseBody
    public ModelAndView noAuth() {
        return new ModelAndView("noAuth");
    }
}
