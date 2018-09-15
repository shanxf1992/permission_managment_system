package com.itheima.acl.beans;

import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;

/**
 * Created by jimin on 15/11/4.
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    private List<T> data = Lists.newArrayList();
    private int total = 0;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
