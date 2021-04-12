package com.nowcoder.community.entity;

import java.util.Date;

public class Picture {
    private int id;
    private String name;
    private String saveName;
    private int fatherId;
    private int fatherType;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public int getFatherType() {
        return fatherType;
    }

    public void setFatherType(int fatherType) {
        this.fatherType = fatherType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", saveName='" + saveName + '\'' +
                ", fatherId=" + fatherId +
                ", fatherType=" + fatherType +
                ", create_time=" + createTime +
                '}';
    }
}
