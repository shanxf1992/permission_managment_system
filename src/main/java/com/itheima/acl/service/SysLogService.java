package com.itheima.acl.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.acl.beans.LogType;
import com.itheima.acl.beans.PageQuery;
import com.itheima.acl.beans.PageResult;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.*;
import com.itheima.acl.mapper.SysLogMapper;
import com.itheima.acl.util.BeanValidator;
import com.itheima.acl.util.IpUtil;
import com.itheima.acl.util.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("sysLogService")
public class SysLogService {

    @Value("#{sysLogMapper}")
    private SysLogMapper sysLogMapper;

    /**
     * 定义各个模块 日志保存的方法, 需要注意的点:
     *      1 更新前后的值, 都可能为空
     *      2 更新前的before 没有 id 属性, after 有
     *
     *
     * @param before
     * @param after
     */
    public void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_DEPT);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_USER);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
    public void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
    public void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }

    // 查询
    public PageResult<SysLogWithBLOBs> searchPageList(PageQuery pageQuery) {
        // 1 参数校验
        BeanValidator.check(pageQuery);

        PageHelper.startPage(pageQuery.getPageNo(), pageQuery.getPageSize());
        Page<SysLogWithBLOBs> logPage = (Page<SysLogWithBLOBs>) sysLogMapper.selectByExampleWithBLOBs(null);
        PageResult<SysLogWithBLOBs> pageResult = new PageResult<>();
        pageResult.setData(logPage.getResult());
        pageResult.setTotal((int) logPage.getTotal());

        return pageResult;
    }

}
