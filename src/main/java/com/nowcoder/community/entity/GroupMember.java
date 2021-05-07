package com.nowcoder.community.entity;

public class GroupMember {
    private int id;
    private int groupId;
    private int userId;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "GroupMember{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null ||getClass() != o.getClass())return false;

        GroupMember groupMember = (GroupMember) o;

        if (!(0 == id && 0 == groupMember.id) && !(id == groupMember.id)) return false;
        if (!(0 == groupId && 0 == groupMember.groupId) && !(groupId == groupMember.groupId)) return false;
        if (!(0 == userId && 0 == groupMember.userId) && !(userId == groupMember.userId)) return false;
        //if (!(null == username && null == groupMember.username) && !username.equals(groupMember.username)) return false;
        //https://blog.csdn.net/Mauanx/article/details/104500718
        return username.equals(groupMember.username);
    }
}
