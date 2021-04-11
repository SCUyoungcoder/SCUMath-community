package com.nowcoder.community.entity;

public class BlogPic {
    int id;
    String picName;
    int bolgId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public int getBolgId() {
        return bolgId;
    }

    public void setBolgId(int bolgId) {
        this.bolgId = bolgId;
    }

    @Override
    public String toString() {
        return "BlogPic{" +
                "id=" + id +
                ", picName='" + picName + '\'' +
                ", bolgId=" + bolgId +
                '}';
    }
}
