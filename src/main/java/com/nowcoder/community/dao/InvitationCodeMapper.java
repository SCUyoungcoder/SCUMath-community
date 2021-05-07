package com.nowcoder.community.dao;

import com.nowcoder.community.entity.InvitationCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvitationCodeMapper {
    List<InvitationCode> getAllCode();
    int deleteCodeById(int id);
    int insertCode(InvitationCode invitationCode);
    InvitationCode selectByCode(String code);
    InvitationCode selectById(int id);
}
