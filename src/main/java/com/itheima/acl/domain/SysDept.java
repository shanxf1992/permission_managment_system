package com.itheima.acl.domain;

import lombok.Builder;
import java.util.Date;

@Builder
public class SysDept {
    private Integer id; //部门 id

    private String name; // 部门 名称

    private Integer parentId; // 该部门的父部门的id

    private String level; // 该部门的级别

    private Integer seq; //该部门的展示顺序

    private String remark; // 备注

    private String operator; //最后一次操作该表的人

    private Date operateTime; //最后一次操作的时间

    private String opetateIp; // 最后一次操作的操作者的 ip

    public SysDept() { }

    public SysDept(Integer id, String name, Integer parentId, String level, Integer seq,
                   String remark, String operator, Date operateTime, String opetateIp) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.seq = seq;
        this.remark = remark;
        this.operator = operator;
        this.operateTime = operateTime;
        this.opetateIp = opetateIp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOpetateIp() {
        return opetateIp;
    }

    public void setOpetateIp(String opetateIp) {
        this.opetateIp = opetateIp == null ? null : opetateIp.trim();
    }

    @Override
    public String toString() {
        return "SysDept{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", level='" + level + '\'' +
                ", seq=" + seq +
                ", remark='" + remark + '\'' +
                ", operator='" + operator + '\'' +
                ", operateTime=" + operateTime +
                ", opetateIp='" + opetateIp + '\'' +
                '}';
    }
}