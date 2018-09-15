package com.itheima.acl.dto;

import com.google.common.collect.Lists;
import com.itheima.acl.domain.SysDept;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * DeptLevelDto : 该类继承至 SysDept, 对 SysDept 类进行扩展
 *
 */

@Setter
@Getter
public class DeptLevelDto extends SysDept {

    //
    private List<DeptLevelDto> deptLists = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept dept) {

        DeptLevelDto deptLevelDto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, deptLevelDto);
        return deptLevelDto;
    }
}
