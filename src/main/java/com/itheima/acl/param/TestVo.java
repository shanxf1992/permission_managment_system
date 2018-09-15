package com.itheima.acl.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class TestVo {

    @NotBlank
    private String msg;
    @NotNull
    private Integer id;
    @NotEmpty
    private List<String> lists;

    @Override
    public String toString() {
        return "TestVo{" +
                "msg='" + msg + '\'' +
                ", id=" + id +
                ", lists=" + lists +
                '}';
    }
}
