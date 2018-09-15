package com.itheima.acl.mapper;

import com.itheima.acl.domain.SysDept;
import com.itheima.acl.domain.SysDeptExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("sysDeptMapper")
public interface SysDeptMapper {
    int countByExample(SysDeptExample example);

    int deleteByExample(SysDeptExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    List<SysDept> selectByExample(SysDeptExample example);

    SysDept selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysDept record, @Param("example") SysDeptExample example);

    int updateByExample(@Param("record") SysDept record, @Param("example") SysDeptExample example);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    // 获取所有部门的列表
    List<SysDept> getDeptList();

    // 根据当前部门的 level 获取所有子部门
    List<SysDept> selectChildDeptByLevel(@Param("level") String level);

    //批量更新子部门中的level
    void batchUpdateChildLevel(@Param("deptList")List<SysDept> deptList);

    //根据 parentId name 查询同一个部门下, 名为 name  的部门的个数
    int countDeptByParentIdAndName(@Param("parentId") Integer parentId,@Param("name") String name,@Param("id") Integer id);
}