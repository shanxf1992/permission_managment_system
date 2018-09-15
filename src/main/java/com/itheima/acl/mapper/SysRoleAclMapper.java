package com.itheima.acl.mapper;

import com.itheima.acl.domain.SysAcl;
import com.itheima.acl.domain.SysRoleAcl;
import com.itheima.acl.domain.SysRoleAclExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("sysRoleAclMapper")
public interface SysRoleAclMapper {
    int countByExample(SysRoleAclExample example);

    int deleteByExample(SysRoleAclExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    List<SysRoleAcl> selectByExample(SysRoleAclExample example);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysRoleAcl record, @Param("example") SysRoleAclExample example);

    int updateByExample(@Param("record") SysRoleAcl record, @Param("example") SysRoleAclExample example);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    // 根据角色id 获取对应的权限 id 的集合
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    //TODO: 根据 roleId 从 role_acl 表中删除对应的记录
    void deleteByRoleId(int roleId);
}