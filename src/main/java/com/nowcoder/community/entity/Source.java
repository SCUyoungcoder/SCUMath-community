package com.nowcoder.community.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

public class Source {
    private int id;

    private String fatherid;//数据库里记录论文所属标签信息的fatherid用String类型存储  --By MY


    private int userid;

    /*拆分存储。存储时尽量多拆分，搜索时没必要拆分这么细....？？？英文分词器？？？*/

    private String title;


    private String content;


    private String filepath;


    private int status;


    private Date createtime;


    private int downloadcount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFatherid() {
        return fatherid;
    }

    public void setFatherid(String fatherid) {
        this.fatherid = fatherid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
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

    public int getDownloadcount() {
        return downloadcount;
    }

    public void setDownloadcount(int downloadcount) {
        this.downloadcount = downloadcount;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", fatherid='" + fatherid + '\'' +
                ", userid=" + userid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", filepath='" + filepath + '\'' +
                ", status=" + status +
                ", createtime=" + createtime +
                ", downloadcount=" + downloadcount +
                '}';
    }
}
