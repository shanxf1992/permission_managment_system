package com.itheima.acl.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.itheima.acl.domain.SysAcl;
import com.itheima.acl.domain.SysAclModule;
import com.itheima.acl.domain.SysDept;
import com.itheima.acl.dto.AclDto;
import com.itheima.acl.dto.AclModuleLevelDto;
import com.itheima.acl.dto.DeptLevelDto;
import com.itheima.acl.mapper.SysAclMapper;
import com.itheima.acl.mapper.SysAclModuleMapper;
import com.itheima.acl.mapper.SysDeptMapper;
import com.itheima.acl.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.acl.Acl;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SysTreeService : 用来生成树结构
 */

@Service("sysTreeService")
public class SysTreeService {

    @Value("#{sysDeptMapper}")
    private SysDeptMapper sysDeptMapper;

    @Value("#{aclModuleMapper}")
    private SysAclModuleMapper aclModuleMapper;

    @Value("#{sysCoreService}")
    private SysCoreService sysCoreService;

    @Value("#{sysAclMapper}")
    private SysAclMapper sysAclMapper;

    // 该方法用来返回部门树
    public List<DeptLevelDto> deptTree() {

        //1 需要从数据库中取出部门列表
        List<SysDept> deptList = sysDeptMapper.getDeptList();

        //2 将 SysDept 列表中的对象, 封装到 DeptLevelDto 中
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDto deptLevelDto = DeptLevelDto.adapt(dept);
            dtoList.add(deptLevelDto);
        }

        //3 将 dtoList 结构做成树形结构, 即将 dtoList 中的每个 dto 中的 deptLists 属性值 填充上
        return deptList2Tree(dtoList);
    }

    private List<DeptLevelDto> deptList2Tree(List<DeptLevelDto> dtoList) {

        /**
         * 使用 MutilMap<key, value>
         *  key: level
         *  value: DeptLevelDto
         */
        Multimap<String, DeptLevelDto> multimap = ArrayListMultimap.create();

        //获取 最顶层的 DeptDtoList
        List<DeptLevelDto> rootList = Lists.newArrayList();

        // 遍历 dtoList , 将其添加到 multimap 中
        for (DeptLevelDto dto : dtoList) {
            multimap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        //对应 root 层部门进行排序 从小到大, 因为同一层级部门之间是有顺序的
        Collections.sort(dtoList, new Comparator<DeptLevelDto>() {
            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });

        //接下来, 递归为每个 DeptLeveDto 中的 deptLists 赋值
        transformDeptTree(rootList, LevelUtil.ROOT, multimap);

        //递归结束后, rootList 中的所有部门之间的树形关系就建立完成了
        return rootList;
    }

    /**
     * 递归生产部门树
     *
     * @param rootList: 当前层的结构
     * @param level     : 当前部门的级别
     * @param multimap  :
     * @return
     */
    private void transformDeptTree(List<DeptLevelDto> rootList, String level, Multimap<String, DeptLevelDto> multimap) {

        //遍历当前层的元素
        for (int i = 0; i < rootList.size(); i++) {
            // 获取每个元素对象
            DeptLevelDto dto = rootList.get(i);
            // 计算下一层级 level
            String nextLevel = LevelUtil.getLevel(level, dto.getId());
            //获取下一层的集合, 并进行排序
            List<DeptLevelDto> nextLevelDtos = (List<DeptLevelDto>) multimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(nextLevelDtos)) {
                //排序
                Collections.sort(nextLevelDtos, new Comparator<DeptLevelDto>() {
                    @Override
                    public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                        return o1.getSeq() - o2.getSeq();
                    }
                });

                dto.setDeptLists(nextLevelDtos);
                transformDeptTree(nextLevelDtos, nextLevel, multimap);
            }
        }
    }


    /**
     * 权限模块组成的权限树
     */
    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> list = aclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : list) {
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }
        // level -> [aclModule1, aclModule2, ...]
        Multimap<String, AclModuleLevelDto> levelModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto dto : dtoList) {
            levelModuleMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按seq从小到大排序
        Collections.sort(rootList, aclModuleSeqComparator);
        // 转换成树形结构
        transformAclTree(rootList, LevelUtil.ROOT, levelModuleMap);

        return rootList;
    }

    /**
     * 递归处理权限树的每一层
     */
    private void transformAclTree(List<AclModuleLevelDto> dtoList, String level, Multimap<String, AclModuleLevelDto> levelModuleMap) {
        for (int i = 0; i < dtoList.size(); i++) {
            // 遍历该层的每个元素
            AclModuleLevelDto aclModuleLevel = dtoList.get(i);
            // 当前处理的层级值
            String nextLevel = LevelUtil.getLevel(level, aclModuleLevel.getId());
            // 取出下一层的列表
            List<AclModuleLevelDto> tempModuleList = (List<AclModuleLevelDto>) levelModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempModuleList)) {
                // 排序
                Collections.sort(tempModuleList, aclModuleSeqComparator);
                // 设置
                aclModuleLevel.setAclModuleList(tempModuleList);
                // 进入下一层进行处理
                transformAclTree(tempModuleList, nextLevel, levelModuleMap);
            }
        }
    }


    /**
     * 获取 特定角色对应的权限树
     * 需要取出所有的权限模块, 权限点, 做成树
     */
    public List<AclModuleLevelDto> roleTree(Integer roleId) {
        //1 取出当前用户已经分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        //2 取出当前角色已经分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);

        //3 定义 set 来存储取出来的用户已分配的 acl id
        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        //4 定义 set 来存储角色分配的 acl id 的集合
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        //3 取出当前系统所有的权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();
        //6 声明一个 aclDto 的集合
        List<AclDto> aclDtoList = Lists.newArrayList();
        // 循环判断对应角色是否具有某个权限
        for (SysAcl acl : allAclList) {
            AclDto aclDto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {
                aclDto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())) {
                aclDto.setChecked(true);
            }
            //包含系统中所有的权限点
            aclDtoList.add(aclDto);
        }

        return aclListToTree(aclDtoList);
    }

    //生成权限模块树
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }

        //调用权限模块方法, 生成模块树
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();

        for (AclDto aclDto : aclDtoList) {
            if ( aclDto.getStatus() == 1) {
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }

        //将权限点绑定到, 模块树上
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }


    //将权限点绑定到, 模块树上
    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return ;
        }

        for (AclModuleLevelDto dto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                //将权限点绑定到, 模块树上
                Collections.sort(aclDtoList, aclSeqComparator);
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);

        }
    }


    private Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    private Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

}
