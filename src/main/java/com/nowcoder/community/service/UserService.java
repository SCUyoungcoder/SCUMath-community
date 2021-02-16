package com.nowcoder.community.service;

import com.nowcoder.community.dao.PaperMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.Paper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaperMapper paperMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }


    public int updatepassword(int id,String password){
        String salt = CommunityUtil.generateUUID().substring(0,5);
        password=CommunityUtil.md5(password+salt);
        return userMapper.updatePassword(id,password,salt);
    }
}