package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Group;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    int insertGroup(Group group);
    int updateGroup(Group group);
    Group selectGroupById(int id);
    Group selectGroupByGroupNameAndOwnerId(String groupName,int ownerId);
    List<Group> selectGroupByOwnerId(int ownerId);
    int deleteGroupById(int id);
}
