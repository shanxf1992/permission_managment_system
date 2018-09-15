package com.itheima.acl.mapper;

import com.itheima.acl.domain.SysAclModule;
import com.itheima.acl.domain.SysAclModuleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("aclModuleMapper")
public interface SysAclModuleMapper {
    int countByExample(SysAclModuleExample example);

    int deleteByExample(SysAclModuleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    List<SysAclModule> selectByExample(SysAclModuleExample example);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysAclModule record, @Param("example") SysAclModuleExample example);

    int updateByExample(@Param("record") SysAclModule record, @Param("example") SysAclModuleExample example);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    void batchUpdateChildLevel(@Param("sysAclModuleList") List<SysAclModule> sysAclModules);

    List<SysAclModule> selectChildByLevel(String beforeLevel);

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    List<SysAclModule> getAllAclModule();
}