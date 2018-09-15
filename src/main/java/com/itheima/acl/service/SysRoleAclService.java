package com.itheima.acl.service;

import com.google.common.collect.Lists;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysRoleAcl;
import com.itheima.acl.domain.SysRoleAclExample;
import com.itheima.acl.mapper.SysRoleAclMapper;
import com.itheima.acl.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("sysRoleAclService")
public class SysRoleAclService {

    @Value("#{sysRoleAclMapper}")
    private SysRoleAclMapper sysRoleAclMapper;

    @Value("#{sysLogService}")
    private SysLogService sysLogService;

    /**
     *  1 取出当前角色 之前分配的权限
     * @param roleId
     * @param aclIdList
     */
    public void changeRoleAcls(int roleId, List<Integer> aclIdList) {
        //1 取出当前角色 之前分配过的权限的 id
        List<Integer> idList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        //2 判断当前修改的权限 和 之前的权限是否一样, 一样就不需要处理
        if (CollectionUtils.isEqualCollection(aclIdList, idList)) {
            return ;
        }
        //3 先删除之前的权限, 然后更新修改的权限, 需要事务管理\
        updateRoleAcls(roleId, aclIdList);
        sysLogService.saveRoleAclLog(roleId, idList, aclIdList);
    }

    // 保证都成功或者都失败
    @Transactional
    public void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        //1 删除之前的权限
        SysRoleAclExample example = new SysRoleAclExample();
        SysRoleAclExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        sysRoleAclMapper.deleteByExample(example);
//        sysRoleAclMapper.deleteByRoleId(roleId);
        //2 跟新修改后的权限
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        // 先创建对象, 然后批量插入
//        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList) {
            SysRoleAcl roleAcl = SysRoleAcl.builder()
                    .roleId(roleId)
                    .aclId(aclId)
                    .build();
            //添加operator 信息, 需要登陆以后才能获取
            roleAcl.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
            //operateTime
            roleAcl.setOperateTime(new Date());
            //operateIp
            roleAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
            sysRoleAclMapper.insert(roleAcl);
//            roleAclList.add(roleAcl);
        }
        //



    }
}
