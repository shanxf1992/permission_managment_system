package com.itheima.acl.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itheima.acl.common.JsonData;
import com.itheima.acl.domain.SysUser;
import com.itheima.acl.param.RoleParam;
import com.itheima.acl.service.*;
import com.itheima.acl.util.StringUtil;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jimin on 16/1/23.
 */
@Slf4j
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Value("#{sysRoleService}")
    private SysRoleService sysRoleService;
    @Value("#{sysTreeService}")
    private SysTreeService sysTreeService;
    @Value("#{sysRoleAclService}")
    private SysRoleAclService sysRoleAclService;
    @Value("#{sysRoleUserService}")
    private SysRoleUserService sysRoleUserService;
    @Value("#{sysUserService}")
    private SysUserService sysUserService;

    /**
     * 进入角色管理界面
     * @return
     */
    @RequestMapping("role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    /**
     * 保存新增角色
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save.json")
    public JsonData saveRole(RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    /**
     * 更新角色信息
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update.json")
    public JsonData updateRole(RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    /**
     * 获取所有角色列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list.json")
    public JsonData list() {
        return JsonData.success(sysRoleService.getAll());
    }

    @ResponseBody
    @RequestMapping("roleTree.json")
    public JsonData roleTree(@RequestParam("roleId") Integer roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }

//    @ResponseBody
//    @RequestMapping(value = "/query.json")
//    public JsonData getRole(@RequestParam("id") int id) {
//        return JsonData.success(sysRoleService.findById(id));
//    }
//

    /**
     * 保存对角色权限点的修改
     * @param roleId
     * @param aclIds
     * @return
     */
    @ResponseBody
    @RequestMapping("/changeAcls.json")
    public JsonData roleAcls(@RequestParam("roleId") int roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splierToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.success();
    }

    /**
     * 对应角色 已选的用户列表
     *
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("users.json")
    public JsonData users(@RequestParam("roleId") int roleId) {
        //对应角色已选中的用户列表
        List<SysUser> selectedUsers = sysRoleUserService.getListByRoleId(roleId);
        // 所有的用户列表
        List<SysUser> allUser = sysUserService.getAll();
        // 对应角色 未选中的用户列表
        List<SysUser> unSelectedUser = Lists.newArrayList();

        //从所用用户中 过滤出 未选中的用户列表
        Set<Integer> selectedUserId = selectedUsers.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        for (SysUser user : allUser) {
            // 如果用户有效, 且未被选中
            if (user.getStatus() == 1 && !selectedUserId.contains(user.getId())) {
                unSelectedUser.add(user);
            }
        }
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUsers);
        map.put("unselected", unSelectedUser);
        return JsonData.success(map);
    }

    /**
     * 保存更改的角色对应的用户信息
     * @param roleId
     * @param userIds
     * @return
     */
    @ResponseBody
    @RequestMapping("changeUsers.json")
    public JsonData changeUsers(@RequestParam("roleId")Integer roleId, @RequestParam("userIds") String userIds) {
        // 转换userids 为list
        List<String> stringList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(userIds);
        List<Integer> ids = stringList.stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());

        sysRoleUserService.changesUsers(roleId, ids);
        return JsonData.success();
    }
//
//    @ResponseBody
//    @RequestMapping(value = "/changeUsers.json")
//    public JsonData roleUsers(@RequestParam("roleId") int roleId, @RequestParam("userIds") String userIds) {
//        List<Integer> userIdList = StringUtil.splierToListInt(userIds);
//        sysRoleUserService.changeRoleUsers(roleId, userIdList);
//        return JsonData.success();
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/acls.json")
//    public JsonData acls(@RequestParam("roleId") int roleId) {
//        return JsonData.success(sysRoleAclService.getListByRoleId(roleId));
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/users.json")
//    public JsonData users(@RequestParam("roleId") int roleId) {
//
//        List<SysUser> selectedUsers = sysRoleUserService.getListByRoleId(roleId);
//        List<SysUser> allUsers = sysUserService.getUserList();
//
//        List<SysUser> unselectedUsers = Lists.newArrayList();
//        for (SysUser sysUser : allUsers) {
//            if (sysUser.getStatus() == Status.AVAILABLE.getCode() && !selectedUsers.contains(sysUser)) {
//                unselectedUsers.add(sysUser);
//            }
//        }
//        Map<String, List<SysUser>> map = Maps.newHashMap();
//        map.put("selected", selectedUsers); // 已选择中可能有无效的用户
//        map.put("unselected", unselectedUsers); // 未选择中都是有效的用户
//        return JsonData.success(map);
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/aclTree.json")
//    public JsonData aclTree(@RequestParam("roleId") int roleId) {
//        return JsonData.success(sysTreeService.roleTree(roleId));
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/unselectUserTree.json")
//    public JsonData userTree(@RequestParam("roleId") int roleId) {
//        return JsonData.success(sysTreeService.unselectUserRoleTree(roleId));
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/delete.json")
//    public JsonData delete(@RequestParam("id") int id) {
//        sysRoleService.deleteById(id);
//        return JsonData.success();
//    }
}
