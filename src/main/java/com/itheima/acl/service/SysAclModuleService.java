package com.itheima.acl.service;

import com.google.common.base.Preconditions;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysAclModule;
import com.itheima.acl.exception.ParamException;
import com.itheima.acl.mapper.SysAclModuleMapper;
import com.itheima.acl.param.AclModuleParam;
import com.itheima.acl.util.BeanValidator;
import com.itheima.acl.util.IpUtil;
import com.itheima.acl.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("sysAclModuleService")
public class SysAclModuleService {

    @Value("#{aclModuleMapper}")
    private SysAclModuleMapper aclModuleMapper;

    @Value("#{sysLogService}")
    private SysLogService sysLogService;

    /**
     * 新增权限模块
     */
    public void save(AclModuleParam aclModuleParam) {
        //1 验证传入的参数是否合法
        BeanValidator.check(aclModuleParam);

        //2 如果没有抛出异常, 说明验证通过, 然后开始组装 部门类 SysDept
        // 组装 部门类 之前, 需要验证 需要添加的部门, 在当前部门下是否重复, 如果重复, 就不允许添加
        if (existParam(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId()))
            throw new ParamException("同一层级下, 存在相同名称的部门");

        /**
         * 需要组装的 参数
         *  opreator: 当前操作者 的名称
         *  operateTime: 当前操作时间
         *  operateId: 当前操作的 ip
         */
        SysAclModule sysAclModule = SysAclModule.builder()
                .id(aclModuleParam.getId())
                .parentId(aclModuleParam.getParentId())
                .name(aclModuleParam.getName())
                .remark(aclModuleParam.getRemark())
                .seq(aclModuleParam.getSeq())
                .status(aclModuleParam.getStatus())
                .build();

        //添加 level 信息
        String parentLevel = getParentLevel(aclModuleParam.getParentId());
        sysAclModule.setLevel(LevelUtil.getLevel(parentLevel, aclModuleParam.getParentId()));
        //添加operator 信息, 需要登陆以后才能获取
        sysAclModule.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        //operateTime
        sysAclModule.setOperateTime(new Date());
        //operateIp
        sysAclModule.setOpetateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));
        aclModuleMapper.insertSelective(sysAclModule);
        sysLogService.saveAclModuleLog(null, sysAclModule);
    }

    /**
     * 更新权限模块
     */
    public void update(AclModuleParam aclModuleParam) {
        //1 验证传入的参数是否合法
        BeanValidator.check(aclModuleParam);

        //2 确认需要更新的部门是否存在
        SysAclModule before = aclModuleMapper.selectByPrimaryKey(aclModuleParam.getId());
        Preconditions.checkNotNull(before, "需要更新的部门不存在");

        //3 如果没有抛出异常, 说明验证通过, 然后开始组装 部门类 SysDept
        // 组装 部门类 之前, 需要验证 需要添加的部门, 在当前部门下是否重复, 如果重复, 就不允许添加
        if (existParam(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId()))
            throw new ParamException("同一层级下, 存在相同名称的部门");

        /**
         * 需要组装的 参数
         *  level: 当前部门的级别, 需要计算
         *  opreator: 当前操作者 的名称
         *  operateTime: 当前操作时间
         *  operateId: 当前操作的 ip
         */
        SysAclModule after = SysAclModule.builder()
                .id(aclModuleParam.getId())
                .parentId(aclModuleParam.getParentId())
                .name(aclModuleParam.getName())
                .remark(aclModuleParam.getRemark())
                .seq(aclModuleParam.getSeq())
                .status(aclModuleParam.getStatus())
                .build();

        //添加 level 信息
        String parentLevel = getParentLevel(aclModuleParam.getParentId());
        after.setLevel(LevelUtil.getLevel(parentLevel, aclModuleParam.getParentId()));

        //添加operator 信息, 需要登陆以后才能获取
        after.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        //operateTime
        after.setOperateTime(new Date());
        //operateIp
        after.setOpetateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));

        //更新当前部门时, 子部门的中的 parentId 和 level 可能会发生改变, 改过成需要一致, 需要使用事务
        updateWithChild(before, after);
        sysLogService.saveAclModuleLog(before, after);

    }

    @Transactional
    public void updateWithChild(SysAclModule before, SysAclModule after) {
        //1 首先, 判断是否需要更新子部门, 判断 level 是否相同
        String beforeLevel = before.getLevel();
        String afterLevel = after.getLevel();

        //2 如果不相同, 需要更新 该部门所有子部门的 level 的前缀
        if (!afterLevel.equals(beforeLevel)) {
            List<SysAclModule> aclModuleList = aclModuleMapper.selectChildByLevel(beforeLevel);
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                // 循环更新子部门的 level4
                for (SysAclModule sysAclModule : aclModuleList) {
                    String childLevel = sysAclModule.getLevel();
                    if (childLevel.indexOf(afterLevel) == 0) {
                        // 将子部门的level 的 前缀该为 afterLevel 即可
                        childLevel = afterLevel + childLevel.substring(afterLevel.length());
                    }
                    sysAclModule.setLevel(childLevel);
                }
                // 然后批量更新子部门
                aclModuleMapper.batchUpdateChildLevel(aclModuleList);
            }
        }
        //3 最后, 更新该部门
        aclModuleMapper.updateByPrimaryKey(after);
    }


    // 用于验证, 在同一个 父部门 parentID 下, 是否存在重复的部门名称
    // id 在新增的时候,传递的是空值, 更新的时候 不为空
    public boolean existParam(Integer parentId, String name, Integer id) {
        System.out.println(parentId + "--" + name + "--" + id);
        return aclModuleMapper.countByNameAndParentId(parentId, name, id) > 0;
    }

    //根据当前部门的 id, 获取当前部门的 Level
    public String getParentLevel(Integer deptId) {

        //获取当前部门对象
        SysAclModule sysAclModule = aclModuleMapper.selectByPrimaryKey(deptId);

        if(sysAclModule == null) return null;

        String level = sysAclModule.getLevel();
        return level;
    }
}
