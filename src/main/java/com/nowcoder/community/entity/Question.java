package com.nowcoder.community.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "question",type = "questiontype",shards = 6,replicas = 3)
public class Question {
    /**
     * 自增id
     */
    @Id
    private int id;

    /**
     * 问题id
     */
    @Field(type = FieldType.Text)
    private String qid;

    /**
     * 博客标题
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 博客内容
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;

    /**
     * 排序 0 普通  1 置顶
     */
    @Field(type = FieldType.Integer)
    private int sort;

    @Field(type = FieldType.Integer)
    private int status;

    /**
     * 浏览量
     */
    @Field(type = FieldType.Integer)
    private int views;

    /**
     * 作者id
     */
    @Field(type = FieldType.Integer)
    private int authorId;

    /**
     * 作者名
     */
    @Field(type = FieldType.Text)
    private String authorName;

    /**
     * 作者头像
     */
    @Field(type = FieldType.Text)
    private String authorAvatar;

    /**
     * 问题分类id
     */
    @Field(type = FieldType.Integer)
    private int categoryId;

    /**
     * 问题分类名称
     */
    @Field(type = FieldType.Text)
    private String categoryName;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Field(type = FieldType.Date)
    private Date gmtUpdate;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", qid='" + qid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                ", status=" + status +
                ", views=" + views +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtUpdate=" + gmtUpdate +
                '}';
    }
}
