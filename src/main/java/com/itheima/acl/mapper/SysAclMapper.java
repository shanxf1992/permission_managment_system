package com.itheima.acl.mapper;

import com.itheima.acl.beans.PageQuery;
import com.itheima.acl.domain.SysAcl;
import com.itheima.acl.domain.SysAclExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("sysAclMapper")
public interface SysAclMapper {
    int countByExample(SysAclExample example);

    int deleteByExample(SysAclExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    List<SysAcl> selectByExample(SysAclExample example);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysAcl record, @Param("example") SysAclExample example);

    int updateByExample(@Param("record") SysAcl record, @Param("example") SysAclExample example);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    // 根据模块 id 查询 模块下的权限点的总记录数
    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    //根据权限模块 查询该模块下的权限列表
    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("page") PageQuery pageQuery);

    int countNameAndAclModuleId(@Param("aclModuleId") Integer aclModuleId, @Param("name") String name, @Param("id")Integer id);

    //获取所用权限点
    List<SysAcl> getAll();

    //根据权限 id 的集合, 获取权限的集合
    List<SysAcl> getByIdList(@Param("idList") List<Integer> idList);
}