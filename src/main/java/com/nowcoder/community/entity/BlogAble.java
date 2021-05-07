package com.nowcoder.community.entity;

public class BlogAble {
    int id;
    int blogId;
    int userId;
    int type;
    int entityId;

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

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "BlogAble{" +
                "id=" + id +
                ", blogId=" + blogId +
                ", userId=" + userId +
                ", type=" + type +
                ", entityId=" + entityId +
                '}';
    }
}
