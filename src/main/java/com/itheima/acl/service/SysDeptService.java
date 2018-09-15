package com.itheima.acl.service;

import com.google.common.base.Preconditions;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysDept;
import com.itheima.acl.exception.ParamException;
import com.itheima.acl.mapper.SysDeptMapper;
import com.itheima.acl.param.DeptParam;
import com.itheima.acl.util.BeanValidator;
import com.itheima.acl.util.IpUtil;
import com.itheima.acl.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * SysDeptService : 将该类交由spring管理
 *      save(): 用于添加新增的 部门对象
 *      update(): 用于更新部门 SysDept 的参数到数据库
 *          当前部门的更新会引起子部门的变化, 所以子部门也需要更新, 并且更新过程必须一致,
 *          所以需要引入事务 Transactional
 *
 */
@Service("sysDeptService")
public class SysDeptService {

    //需要注入 Mapper 层 sysDepMapper, 对象处理数据库操作
    @Value("#{sysDeptMapper}")
    private SysDeptMapper sysDeptMapper;

    @Value("#{sysLogService}")
    private SysLogService sysLogService;

    //用于更新部门 SysDept 的参数到数据库
    public void update(DeptParam deptParam) {
        //1 首先需要对数据进行合法型验证
        BeanValidator.check(deptParam);


        //2 确认需要更新的部门是否存在
        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(before, "需要更新的部门不存在");

        //3 如果没有抛出异常, 说明验证通过, 然后开始组装 部门类 SysDept
        // 组装 部门类 之前, 需要验证 需要添加的部门, 在当前部门下是否重复, 如果重复, 就不允许添加
        if (existParam(deptParam.getParentId(), deptParam.getName(), deptParam.getId()))
            throw new ParamException("同一层级下, 存在相同名称的部门");

        /**
         * 需要组装的 参数
         *  level: 当前部门的级别, 需要计算
         *  opreator: 当前操作者 的名称
         *  operateTime: 当前操作时间
         *  operateId: 当前操作的 ip
         */
        SysDept after = SysDept.builder()
                .id(deptParam.getId())
                .parentId(deptParam.getParentId())
                .name(deptParam.getName())
                .remark(deptParam.getRemark())
                .seq(deptParam.getSeq())
                .build();

        //添加 level 信息
        String parentLevel = getParentLevel(deptParam.getParentId());
        after.setLevel(LevelUtil.getLevel(parentLevel, deptParam.getParentId()));

        //添加operator 信息, 需要登陆以后才能获取
        after.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        //operateTime
        after.setOperateTime(new Date());
        //operateIp
        after.setOpetateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));

        //更新当前部门时, 子部门的中的 parentId 和 level 可能会发生改变, 改过成需要一致, 需要使用事务
        updateWithChildDept(before, after);

        // 添加日志
        sysLogService.saveDeptLog(before, after);
    }

    @Transactional
    public void updateWithChildDept(SysDept before, SysDept after) {
        //1 首先, 判断是否需要更新子部门, 判断 level 是否相同
        String beforeLevel = before.getLevel();
        String afterLevel = after.getLevel();

        //2 如果不相同, 需要更新 该部门所有子部门的 level 的前缀
        if (!afterLevel.equals(beforeLevel)) {
            List<SysDept> deptList = sysDeptMapper.selectChildDeptByLevel(beforeLevel);
            if (CollectionUtils.isNotEmpty(deptList)) {
                // 循环更新子部门的 level
                for (SysDept sysDept : deptList) {
                    String childLevel = sysDept.getLevel();
                    if (childLevel.indexOf(afterLevel) == 0){
                        // 将子部门的level 的 前缀该为 afterLevel 即可
                        childLevel = afterLevel + childLevel.substring(afterLevel.length());
                    }
                    sysDept.setLevel(childLevel);
                }
                // 然后批量更新子部门
                sysDeptMapper.batchUpdateChildLevel(deptList);
            }
        }

        //3 最后, 更新该部门
        sysDeptMapper.updateByPrimaryKey(after);

    }

    //用于添加 新的 部门 SysDept 的参数到数据库
    public void save(DeptParam deptParam) {
        //1 首先需要对数据进行合法型验证
        BeanValidator.check(deptParam);

        //2 如果没有抛出异常, 说明验证通过, 然后开始组装 部门类 SysDept
        // 组装 部门类 之前, 需要验证 需要添加的部门, 在当前部门下是否重复, 如果重复, 就不允许添加
        if (existParam(deptParam.getParentId(), deptParam.getName(), deptParam.getId()))
            throw new ParamException("同一层级下, 存在相同名称的部门");

        /**
         * 需要组装的 参数
         *  level: 当前部门的级别, 需要计算
         *  opreator: 当前操作者 的名称
         *  operateTime: 当前操作时间
         *  operateId: 当前操作的 ip
         */
        SysDept dept = SysDept.builder()
                .id(deptParam.getId())
                .parentId(deptParam.getParentId())
                .name(deptParam.getName())
                .remark(deptParam.getRemark())
                .seq(deptParam.getSeq())
                .build();

        //添加 level 信息
        String parentLevel = getParentLevel(deptParam.getParentId());
        dept.setLevel(LevelUtil.getLevel(parentLevel, deptParam.getParentId()));

        //添加operator 信息, 需要登陆以后才能获取
        dept.setOperator(RequestHolder.getUserThreadLocal().get().getUsername());
        //operateTime
        dept.setOperateTime(new Date());
        //operateIp
        dept.setOpetateIp(IpUtil.getRemoteIp(RequestHolder.getRequestThreadLocal().get()));

        //然后将添加的部门信息保存
        sysDeptMapper.insertSelective(dept);
        //保存日志
        sysLogService.saveDeptLog(null, dept);
    }

    // 用于验证, 在同一个 父部门 parentID 下, 是否存在重复的部门名称
    // id 在新增的时候,传递的是空值, 更新的时候 不为空
    public boolean existParam(Integer parentId, String name, Integer id) {
        return sysDeptMapper.countDeptByParentIdAndName(parentId, name, id) > 0;
    }

    //根据当前部门的 id, 获取当前部门的 Level
    public String getParentLevel(Integer deptId) {

        //获取当前部门对象
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);

        if(dept == null) return null;

        String level = dept.getLevel();
        return level;
    }
}
