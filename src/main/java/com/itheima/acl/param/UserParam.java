package com.itheima.acl.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserParam {

    private Integer id; // 用户Id

    @NotBlank(message = "用户名不可以为空")
    @Length(min = 6, max = 15, message = "用户名在 5 - 15 个字之间")
    private String username; // 用户名

    @NotBlank(message = "手机号码不能为空")
    @Length(min = 11, max = 11, message = "手机号码不合法")
    private String telephone; // 手机号码

    @NotBlank(message = "邮箱不能为空")
    private String mail; //邮箱

    @NotNull(message = "必须指定部门名称")
    private Integer deptId; //用户所在部门的id号

    @NotNull(message = "必须指定用户状态")
    @Min(value = 0, message = "状态不合法")
    @Max(value = 2, message = "状态不合法")
    private Integer status; //用户当前状态

    @Length(min = 0, max = 200, message = "长度需要在200字以内")
    private String remark = ""; //备注

}

