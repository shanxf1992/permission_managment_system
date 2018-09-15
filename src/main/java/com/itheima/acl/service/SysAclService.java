package com.itheima.acl.service;

import com.google.common.base.Preconditions;
import com.itheima.acl.beans.PageQuery;
import com.itheima.acl.beans.PageResult;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysAcl;
import com.itheima.acl.exception.ParamException;
import com.itheima.acl.mapper.SysAclMapper;
import com.itheima.acl.param.AclParam;
import com.itheima.acl.util.BeanValidator;
import com.itheima.acl.util.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("sysAclService")
public class SysAclService {

    @Value("#{sysAclMapper}")
    private SysAclMapper sysAclMapper;

    @Value("#{sysLogService}")
    private SysLogService sysLogService;

    public void save(AclParam aclParam) {
        //1 对参数合法性进行校验
        BeanValidator.check(aclParam);
        //2 验证同一个权限模块下, 是否含有相同名称的权限名称
        if (existParam(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId()))
            throw new ParamException("同一权限模块下, 存在相同名称的权限");
        //3 封装权限点实体类
        SysAcl acl = SysAcl.builder()
                .name(aclParam.getName())
                .aclModuleId(aclParam.getAclModuleId())
                .url(aclParam.getUrl())
                .type(aclParam.getType())
                .status(aclParam.getStatus())
                .seq(aclParam.getSeq())
                .remark(aclParam.getRemark()).build();
        //设置code 值, 每一个权限点都有唯一的一个code 值
        acl.setCode(generatorCode());
        //添加operator 信息, 需要登陆以后才能获取
        acl.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        //operateTime
        acl.setOpetateTime(new Date());
        //operateIp
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        //4 存入数据库
        sysAclMapper.insertSelective(acl);
        sysLogService.saveAclLog(null, acl);
    }

    public void update(AclParam aclParam) {
        //1 对参数合法性进行校验
        BeanValidator.check(aclParam);
        //2 取出数据库中的值, 判断待更新的权限是否存在
        SysAcl before = sysAclMapper.selectByPrimaryKey(aclParam.getId());
        Preconditions.checkNotNull(before, "待更新权限点不存在");
        //3 设置 数据
        SysAcl after = SysAcl.builder()
                .id(aclParam.getId())
                .name(aclParam.getName())
                .aclModuleId(aclParam.getAclModuleId())
                .url(aclParam.getUrl())
                .type(aclParam.getType())
                .status(aclParam.getStatus())
                .seq(aclParam.getSeq())
                .remark(aclParam.getRemark()).build();
        // code 值不进型更新, 设置code 值为更新前的值
        after.setCode(before.getCode());
        //添加operator 信息, 需要登陆以后才能获取
        after.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        //operateTime
        after.setOpetateTime(new Date());
        //operateIp
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        //4 更新到数据库
        sysAclMapper.updateByPrimaryKey(after);
        sysLogService.saveAclLog(before, after);
    }

    //验证同一个权限模块下, 是否含有相同名称的权限名称
    private boolean existParam(Integer aclModuleId, String name, Integer id) {
        return sysAclMapper.countNameAndAclModuleId (aclModuleId, name, id) > 0;
    }

    //生成唯一的code 值
    private String generatorCode() {
        //根据时间和随机数 生成code值
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String code = simpleDateFormat.format(new Date()) + (int) (Math.random() * 100);
        return code;
    }

    public PageResult<SysAcl> getPageByAclModule(int aclModuleId, PageQuery pageQuery) {
        //检验参数的合法性
        BeanValidator.check(pageQuery);
        //分页时需要的 总记录数
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        //如果该模块下有值, 就获取列表
        if (count > 0) {
            List<SysAcl> acls= sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            return PageResult.<SysAcl>builder()
                    .total(count)
                    .data(acls)
                    .build();
        }
        return PageResult.<SysAcl>builder().build();
    }

}
