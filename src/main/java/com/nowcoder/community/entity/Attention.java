package com.nowcoder.community.entity;

public class Attention {
    int id;
    int userId;
    int focusId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFocusId() {
        return focusId;
    }

    public void setFocusId(int focusId) {
        this.focusId = focusId;
    }

    @Override
    public String toString() {
        return "Attention{" +
                "id=" + id +
                ", userId=" + userId +
                ", focusId=" + focusId +
                '}';
    }
}
