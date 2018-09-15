package com.itheima.acl.dto;

import com.itheima.acl.domain.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@ToString
public class AclDto extends SysAcl {

    // 渲染角色 和 权限 关系时, 该权限是否默认被选中
    private boolean checked = false;
    // 对应角色是否具有权限操作
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl sysAcl) {
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(sysAcl, aclDto);
        return aclDto;
    }

}
