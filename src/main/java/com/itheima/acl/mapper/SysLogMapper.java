package com.itheima.acl.mapper;

import com.itheima.acl.domain.SysLog;
import com.itheima.acl.domain.SysLogExample;
import com.itheima.acl.domain.SysLogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("sysLogMapper")
public interface SysLogMapper {
    int countByExample(SysLogExample example);

    int deleteByExample(SysLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    List<SysLogWithBLOBs> selectByExampleWithBLOBs(SysLogExample example);

    List<SysLog> selectByExample(SysLogExample example);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysLogWithBLOBs record, @Param("example") SysLogExample example);

    int updateByExampleWithBLOBs(@Param("record") SysLogWithBLOBs record, @Param("example") SysLogExample example);

    int updateByExample(@Param("record") SysLog record, @Param("example") SysLogExample example);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);
}