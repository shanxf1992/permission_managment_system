package com.itheima.acl.service;


import com.google.common.base.Preconditions;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysRole;
import com.itheima.acl.exception.ParamException;
import com.itheima.acl.mapper.SysRoleMapper;
import com.itheima.acl.param.RoleParam;
import com.itheima.acl.util.BeanValidator;
import com.itheima.acl.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by jimin on 16/1/18.
 */
@Service
public class SysRoleService {

    @Value("#{sysRoleMapper}")
    private SysRoleMapper sysRoleMapper;

    @Value("#{sysLogService}")
    private SysLogService sysLogService;

    public void save(RoleParam param) {
        //1 检验参数合法性
        BeanValidator.check(param);
        //2 角色名称是否存在
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        // 封装参数
        SysRole sysRole = SysRole.builder()
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();
        //添加operator 信息, 需要登陆以后才能获取
        sysRole.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        //operateTime
        sysRole.setOperateTime(new Date());
        //operateIp
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysRoleMapper.insertSelective(sysRole);
        sysLogService.saveRoleLog(null, sysRole);
    }

    public void update(RoleParam param) {
        //1 检验参数合法性
        BeanValidator.check(param);
        //2 角色名称是否存在
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        //检查待更新的角色是否存在
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        // 封装参数
        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();
        //添加operator 信息, 需要登陆以后才能获取
        after.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        //operateTime
        after.setOperateTime(new Date());
        //operateIp
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        sysRoleMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveRoleLog(before, after);
    }

    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

//
//    public void deleteById(int id) {
//        SysRole role = findById(id);
//        Preconditions.checkNotNull(role, "该角色不存在,无法执行删除操作");
//        Preconditions.checkArgument(sysRoleAclDao.countByRoleId(id) == 0, "该角色下还有权限点,不允许删除!");
//        Preconditions.checkArgument(sysRoleUserDao.countByRoleId(id) == 0, "该角色下还有用户,不允许删除!");
//        sysRoleDao.deleteById(id);
//        sysLogService.saveRoleLog(role, null);
//    }

    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }
}
