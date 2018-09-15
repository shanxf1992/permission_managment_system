package com.itheima.acl.service;

import com.google.common.collect.Lists;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysRoleUser;
import com.itheima.acl.domain.SysRoleUserExample;
import com.itheima.acl.domain.SysUser;
import com.itheima.acl.mapper.SysRoleUserMapper;
import com.itheima.acl.mapper.SysUserMapper;
import com.itheima.acl.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("sysRoleUserService")
public class SysRoleUserService {

    @Value("#{sysRoleUserMapper}")
    private SysRoleUserMapper sysRoleUserMapper;
    @Value("#{sysUserMapper}")
    private SysUserMapper sysUserMapper;
    @Value("#{sysLogService}")
    private SysLogService sysLogService;

    /**
     * 根据角色Id 获取对应的用户
     * @param roleId
     * @return
     */
    public List<SysUser> getListByRoleId(int roleId) {
        //1 获取对应 roleId 角色用户列表
        SysRoleUserExample example = new SysRoleUserExample();
        SysRoleUserExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<SysRoleUser> sysRoleUsers = sysRoleUserMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(sysRoleUsers)) {
            return Lists.newArrayList();
        }

        //2 获取用户列表
        List<SysUser> userList = Lists.newArrayList();
        for (SysRoleUser sysRoleUser : sysRoleUsers) {
            SysUser sysUser = sysUserMapper.selectByPrimaryKey(sysRoleUser.getUserId());
            userList.add(sysUser);
        }
        return userList;
    }

    /**
     * 保存更改的角色对应的用户信息
     * @param roleId
     * @param userIds
     * @return
     */
    @Transactional
    public void changesUsers(Integer roleId, List<Integer> userIds) {
        //1 首先判断 当前修改是否和之前一致, 如果一致就直接返回
        SysRoleUserExample example = new SysRoleUserExample();
        SysRoleUserExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<SysRoleUser> sysRoleUsers = sysRoleUserMapper.selectByExample(example);
        List<Integer> sysRoleUserIds = sysRoleUsers.stream().map(user -> user.getUserId()).collect(Collectors.toList());

        if (CollectionUtils.isEqualCollection(sysRoleUserIds, userIds)) {
            return;
        }

        //2 先删除对应角色的用户信息
        SysRoleUserExample delexample = new SysRoleUserExample();
        SysRoleUserExample.Criteria dlecriteria = delexample.createCriteria();
        dlecriteria.andRoleIdEqualTo(roleId);
        sysRoleUserMapper.deleteByExample(delexample);

        //3 更新对应角色修改之后的信息
        for (Integer userId : userIds) {
            SysRoleUser sysRoleUser = SysRoleUser.builder()
                    .roleId(roleId)
                    .userId(userId)
                    .build();
            //添加operator 信息, 需要登陆以后才能获取
            sysRoleUser.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
            //operateTime
            sysRoleUser.setOperateTime(new Date());
            //operateIp
            sysRoleUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
            //4 更新到数据库
            sysRoleUserMapper.insert(sysRoleUser);
        }

        sysLogService.saveRoleUserLog(roleId, sysRoleUserIds, userIds);

    }
}
