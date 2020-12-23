package com.nowcoder.community.entity;


import java.util.Date;

public class Paper {
    private int id;
    private int fatherid;
    private int userid;
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

    public int getFatherid() {
        return fatherid;
    }

    public void setFatherid(int fatherid) {
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
        return "paper{" +
                "id=" + id +
                ", father_id=" + fatherid +
                ", user_id='" + userid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", file_path='" + filepath + '\'' +
                ", status=" + status +
                ", create_time=" + createtime +
                ", download_count=" + downloadcount +
                '}';
    }
}
