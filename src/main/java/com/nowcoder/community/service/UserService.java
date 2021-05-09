package com.nowcoder.community.service;

import com.nowcoder.community.dao.*;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private InvitationCodeMapper invitationCodeMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;

    public User selectByUserName(String username){return userMapper.selectByName(username);}
    public int deleteGroupMemberById(int groupMemberId,int groupId){
        Group group = groupMapper.selectGroupById(groupId);
        group.setCountMember(group.getCountMember()-1);
        groupMapper.updateGroup(group);
        return groupMemberMapper.deleteGroupMemberById(groupMemberId);
    }
    public GroupMember getGroupMemberById(int groupMemberId){
        return groupMemberMapper.selectById(groupMemberId);
    }
    public void addGroupMember(String[] memberList,int groupId){
        int i = 1;
        for (;i<memberList.length;i++){
            GroupMember groupMember = groupMemberMapper.selectByGroupIdAndUserName(groupId,memberList[i]);
            if (groupMember!=null){
                continue;
            }
            groupMember = new GroupMember();
            groupMember.setGroupId(groupId);
            User user = userMapper.selectByName(memberList[i]);
            if (user==null){
                continue;
            }
            groupMember.setUserId(user.getId());
            groupMember.setUsername(user.getUsername());
            groupMemberMapper.insertGroupMember(groupMember);
        }
        Group group = groupMapper.selectGroupById(groupId);
        group.setCountMember(group.getCountMember()+i-1);
        groupMapper.updateGroup(group);
    }
    public List<GroupMember> getGroupMemberByGroupId(int groupId){return groupMemberMapper.selectByGroupId(groupId);}
    public Group getGroupById(int groupId){
        return groupMapper.selectGroupById(groupId);
    }
    public int deleteGroupById(int groupId,int userId){
        Group group = groupMapper.selectGroupById(groupId);
        if (group==null){
            return -1;
        }
        if (group.getOwnerId()!=userId){
            return 0;
        }
        groupMemberMapper.deleteByGroupId(groupId);
        return groupMapper.deleteGroupById(groupId);
    }
    public int addGroup(Group group){
        Group group1 = groupMapper.selectGroupByGroupNameAndOwnerId(group.getGroupName(),group.getOwnerId());
        if (group1!=null){
            return 0;
        }
        group.setCountMember(0);
        group.setCreateTime(new Date());
        return groupMapper.insertGroup(group);
    }
    public List<Group> getGroupByOwnerId(int ownerId){return groupMapper.selectGroupByOwnerId(ownerId);}
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
    public List<InvitationCode> getAllCode(){return invitationCodeMapper.getAllCode();}

    public int deleteCodeById(int id){
        InvitationCode invitationCode = invitationCodeMapper.selectById(id);
        if (invitationCode == null){
            return 0;
        }
        return invitationCodeMapper.deleteCodeById(id);
    }

    public int addCode(InvitationCode invitationCode){
        InvitationCode invitationCode1 = invitationCodeMapper.selectByCode(invitationCode.getCode());
        if (invitationCode1 != null){
            return 0;
        }
        return invitationCodeMapper.insertCode(invitationCode);
    }

    public int updatepassword(int id,String password){
        String salt = CommunityUtil.generateUUID().substring(0,5);
        password=CommunityUtil.md5(password+salt);
        return userMapper.updatePassword(id,password,salt);
    }

}