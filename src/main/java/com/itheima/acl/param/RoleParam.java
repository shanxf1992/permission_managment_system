package com.itheima.acl.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.Inet4Address;

@ToString
@Getter
@Setter
public class RoleParam {

    private Integer id;

    @NotBlank(message = "角色名称不能为空")
    @Length(min = 2, max = 20, message = "角色名称长度需要在4~32个字之间")
    private String name;

    @Length(min = 0, max = 200, message = "备注长度需要在200个字以内")
    private String remark;

    @Min(value = 1, message = "角色类型不合法")
    @Max(value = 2 , message = "角色类型不合法")
    private int type = 1;

    @NotNull(message = "角色状态不能为空")
    @Min(value = 0, message = "角色状态不合法")
    @Max(value = 1, message = "角色状态不合法")
    private Integer status;

}
