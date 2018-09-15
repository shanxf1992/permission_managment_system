package com.itheima.acl.service;

import com.google.common.collect.Lists;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysAcl;
import com.itheima.acl.domain.SysAclExample;
import com.itheima.acl.domain.SysUser;
import com.itheima.acl.mapper.SysAclMapper;
import com.itheima.acl.mapper.SysRoleAclMapper;
import com.itheima.acl.mapper.SysRoleUserMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 该Service 用户获取用户 具有的权限点 和 角色具有的权限点
 */

@Service
public class SysCoreService {

    @Value("#{sysAclMapper}")
    private SysAclMapper sysAclMapper;
    @Value("#{sysRoleUserMapper}")
    private SysRoleUserMapper sysRoleUserMapper;
    @Value("#{sysRoleAclMapper}")
    private SysRoleAclMapper sysRoleAclMapper;

    // 获取 当前 用户具有的权限点的集合
    public List<SysAcl> getCurrentUserAclList() {
        //获取当前用户的id
        Integer userId = RequestHolder.getUserThreadLocal().get().getId();
        return getUserAclList(userId);
    }

    //获取当前角色具有的权限点的集合
    public List<SysAcl> getRoleAclList(Integer roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }

    /**
     * 根据用户 id 获取其具有的权限点
     *      1 根据用户id 获取该用户具有的角色的 id集合
     *      2 根据角色的 id  获取角色所有的 权限的集合
     * @param userId
     * @return
     */
    public List<SysAcl> getUserAclList(Integer userId) {
        // 如果该用户是 超级管理员 就取出所用的权限
        if (isSuperAdmin()) {
            return sysAclMapper.getAll();
        }
        // 否则取出用户已经分配的角色 Id
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);

        //如果当前用户没有分配任何角色
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }

        //如果有, 取出对应角色具有的权限点集合, 这样就获取了 用户具有的所有的权限点的总和
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);

        // 如果取到的值是个空值( 有可能对应的角色被取消掉了 )
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }

        // 最后根据 权限点的 id 的集合, 查询权限点对象的集合 返回
        return sysAclMapper.getByIdList(userAclIdList);
    }

    //判断用户是否是 超级管理员, 该用户可以进行权限分配( 自定义设置 , 如果用户邮箱包含 admin 字样, 就认为是管理员)
    //可以配置文件中获取, 直接指定
    public boolean isSuperAdmin() {
        SysUser sysUser = RequestHolder.getUserThreadLocal().get();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }

    //校验一个用户是否具有访问某个 url 的权限
    public boolean hasUurlAcl(String url) {
        //1 判断用户是否是超级管理员
        if (isSuperAdmin()) {
            return true;
        }
        // 2 取出 url 对应的 acl
        SysAclExample example = new SysAclExample();
        SysAclExample.Criteria criteria = example.createCriteria();
        criteria.andUrlEqualTo(url);
        List<SysAcl> acls = sysAclMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(acls)) {
            return true;
        }
        // 3 判断规则, 如果当前的 url 对应 多个权限点, 则用户具有其中一条就可以访问(各系统互不相同)
        List<SysAcl> currentUserAclList = getCurrentUserAclList();
        Set<Integer> userAclSet = currentUserAclList.stream().map(userAcl -> userAcl.getId()).collect(Collectors.toSet());

        // 记录是否具有有效的权限点
        boolean hasValidAcl = false;
        for (SysAcl acl : acls) {
            // 如果权限点为空, 获取状态不 正常, 则不需要校验
            if (acl == null || acl.getStatus() != 1) {
                continue;
            }
            hasValidAcl = true;
            if (userAclSet.contains(acl.getId())) {
                return true;
            }
        }

        if (!hasValidAcl) {
            return true;
        }
        return false;
    }

}
