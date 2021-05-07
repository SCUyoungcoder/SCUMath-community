package com.nowcoder.community.entity;

public class InvitationCode {
    private int id;
    private String code;
    private int adminId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "InvitationCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", adminId=" + adminId +
                '}';
    }
}
