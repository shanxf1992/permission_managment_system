package com.itheima.acl.mapper;

import com.itheima.acl.beans.PageQuery;
import com.itheima.acl.domain.SysUser;
import com.itheima.acl.domain.SysUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("sysUserMapper")
public interface SysUserMapper {
    int countByExample(SysUserExample example);

    int deleteByExample(SysUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    List<SysUser> selectByExample(SysUserExample example);

    SysUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysUser record, @Param("example") SysUserExample example);

    int updateByExample(@Param("record") SysUser record, @Param("example") SysUserExample example);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    //根据手机号吗查询 用户信息
    int selectUserByTelephone(@Param("telephone") String telephone, @Param("id")Integer id);

    //根据邮箱查询 用户信息
    int selectUserByMail(@Param("mail")String mail, @Param("id")Integer id);

    SysUser selectUserByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    List<SysUser> selectPageByDeptId(@Param("deptId") int deptId, @Param("start") int start,@Param("rows") int row);
}