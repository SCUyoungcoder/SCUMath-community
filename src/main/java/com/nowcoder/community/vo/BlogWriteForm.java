package com.nowcoder.community.vo;

import org.apache.ibatis.annotations.Mapper;

@Mapper
/*
博客表单vo
 */
public class BlogWriteForm {

    private String title;

    private String content;

    private Integer categoryId;

    private int authorId;

    private String authorName;

    private String authorAvatar;

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public String toString() {
        return "BlogWriteForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", categoryId=" + categoryId +
                ", authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorAvatar='" + authorAvatar + '\'' +
                '}';
    }
}
