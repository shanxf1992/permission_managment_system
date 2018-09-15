package com.itheima.acl.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class AclModuleParam {

    private Integer id;

    @Length(min = 2, max = 64, message = "权限模块名称长度需要在2~64个字之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "权限模块展示顺序不能为空")
    private Integer seq;

    @NotNull(message = "权限模块状态不能为空")
    @Min(value = 0, message = "状态最小为值0")
    @Max(value = 1, message = "状态最大值为1")
    private Integer status;

    @Length(min = 0, max = 64, message = "备注长度需要在64个字以内")
    private String remark;



}
