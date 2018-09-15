package com.itheima.acl.controller;

import com.itheima.acl.dto.DeptLevelDto;
import com.itheima.acl.service.SysDeptService;
import com.itheima.acl.common.JsonData;
import com.itheima.acl.param.DeptParam;
import com.itheima.acl.service.SysLogService;
import com.itheima.acl.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * SysDept 部门对应的 Controller, 处理部门类相关的操作
 *      saveDept(): 处理 新增 部门的请求
 *      deptTree(): 处理 部门树 的请求
 *      update(): 处理部门 更新 的请求(部门发生更新时, 其下的子部门的 级别 和 parentId 也会发生改变)
 */
@Controller
@Slf4j
@RequestMapping("/sys/dept")
@ResponseBody
public class SysDeptController {

    //具体的操作需要 service 层来处理, 所以需要注入 SysDeptService 对象
    @Value("#{sysDeptService}")
    private SysDeptService sysDeptService;

    @Value("#{sysTreeService}")
    private SysTreeService sysTreeService;

    @RequestMapping("dept.page")
    public ModelAndView page() {
        return new ModelAndView("dept");
    }

    //处理新增部门的请求: 定义 SysDept 对象的存储方法
    @RequestMapping("save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam deptParam) {

        sysDeptService.save(deptParam);
        return JsonData.success();
    }

    //处理部门树的请求
    @RequestMapping("tree.json")
    @ResponseBody
    public JsonData deptTree() {
        List<DeptLevelDto> deptLevelDtos = sysTreeService.deptTree();
        return JsonData.success(deptLevelDtos);
    }

    //处理新增更新的请求
    @RequestMapping("update.json")
    @ResponseBody
    public JsonData update(DeptParam deptParam) {

        sysDeptService.update(deptParam);
        return JsonData.success();
    }


}
