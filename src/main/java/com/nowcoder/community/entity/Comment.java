package com.nowcoder.community.entity;

import java.util.Date;

public class Comment {
    private int id;
    private int userid;
    private int entitytype;
    private int entityid;
    private int targetid;
    private String content;
    private int status;
    private Date createtime;
    private int type;
    private int table;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getEntitytype() {
        return entitytype;
    }

    public void setEntitytype(int entitytype) {
        this.entitytype = entitytype;
    }

    public int getEntityid() {
        return entityid;
    }

    public void setEntityid(int entityid) {
        this.entityid = entityid;
    }

    public int getTargetid() {
        return targetid;
    }

    public void setTargetid(int targetid) {
        this.targetid = targetid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userid=" + userid +
                ", entitytype=" + entitytype +
                ", entityid=" + entityid +
                ", targetid=" + targetid +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createtime=" + createtime +
                ", type=" + type +
                ", table=" + table +
                '}';
    }
}
