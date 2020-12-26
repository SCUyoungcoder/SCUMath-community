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
    public Paper selectpaperByTitle(String title){ return paperMapper.selectByFulltitle(title);}
    /*public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);*//*上传文件例子*/
    /*将paper信息存到数据库*/
    public Map<String,Object> uploadpaper(Paper paper){
        Map<String,Object>map = new HashMap<>();
        /*if (paper == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(paper.getTitle())){
            map.put("titleMsg","论文题目不能为空");
            return map;
        }*/
        paperMapper.insertPaper(paper);
        return map;/*上传论文部分，，，，待完成?*/
    }
    public int updatepassword(int id,String password){
        String salt = CommunityUtil.generateUUID().substring(0,5);
        password=CommunityUtil.md5(password+salt);
        return userMapper.updatePassword(id,password,salt);
    }
}
