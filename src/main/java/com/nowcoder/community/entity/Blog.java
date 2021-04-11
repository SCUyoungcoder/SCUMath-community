package com.nowcoder.community.entity;

import java.util.Date;

public class Blog {
    /**
     * 自增id
     */
    private int id;

    /**
     * 博客id
     */
    private String bid;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 排序 0 普通  1 置顶
     */
    private int sort;

    /**
     * 浏览量
     */
    private int views;

    /**
     * 作者id
     */
    private int authorId;

    /**
     * 作者名
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 问题分类id
     */
    private int categoryId;

    /**
     * 问题分类名称
     */
    private String categoryName;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtUpdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
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
                ", bid='" + bid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                ", views=" + views +
                ", authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtUpdate=" + gmtUpdate +
                '}';
    }
}
