package com.itheima.acl.dto;


import com.google.common.collect.Lists;
import com.itheima.acl.domain.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by jimin on 16/1/24.
 */
@ToString
@Getter
@Setter
public class AclModuleLevelDto extends SysAclModule {

    /**
     * 组装权限模块层级使用
     */
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    /**
     * 用来显示模块下的权限点, 为显示角色和权限点的关系
     */
    private List<AclDto> aclList = Lists.newArrayList();



    /**
     * 组装权限层级模块下的权限点列表
     */
    public static AclModuleLevelDto adapt(SysAclModule aclModule) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }


}
