package com.nowcoder.community.entity;

import java.util.Date;

public class Itemindex {
    private int id;
    private String Bid;
    private int userId;
    private Date gmtCreate;
    private String userName;
    private String title;
    private String type;
    private int num;

    public String getBid() {
        return Bid;
    }

    public void setBid(String bid) {
        Bid = bid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Itemindex{" +
                "id=" + id +
                ", Bid='" + Bid + '\'' +
                ", userId=" + userId +
                ", gmtCreate=" + gmtCreate +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", num=" + num +
                '}';
    }
}
