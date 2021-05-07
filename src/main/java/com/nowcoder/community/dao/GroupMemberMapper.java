package com.nowcoder.community.dao;

import com.nowcoder.community.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMemberMapper {
    int insertGroupMember(GroupMember groupMember);
    GroupMember selectByGroupIdAndUserId(int groupId,int userId);
    GroupMember selectById(int id);
    GroupMember selectByGroupIdAndUserName(int groupId,String userName);
    List<GroupMember> selectByGroupId(int groupId);
    int deleteGroupMemberById(int id);
    int deleteByGroupId(int groupId);
}
