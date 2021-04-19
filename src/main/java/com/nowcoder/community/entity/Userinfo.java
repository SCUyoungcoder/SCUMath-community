package com.nowcoder.community.entity;

public class Userinfo {
    /**
     * 用户id
     */
    private int id;

    private int userid;
    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * QQ
     */
    private String qq;

    /**
     * WeChat
     */
    private String wechat;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 工作
     */
    private String work;

    /**
     * 地址
     */
    private String address;

    /**
     * 爱好
     */
    private String hobby;

    /**
     * 自我介绍
     */
    private String intro;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realname='" + realname + '\'' +
                ", qq='" + qq + '\'' +
                ", wechat='" + wechat + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", work='" + work + '\'' +
                ", address='" + address + '\'' +
                ", hobby='" + hobby + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }
}
