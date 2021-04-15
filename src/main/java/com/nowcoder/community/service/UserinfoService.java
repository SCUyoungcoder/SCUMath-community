package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserinfoMapper;
import com.nowcoder.community.entity.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserinfoService {
    @Autowired
    private UserinfoMapper userinfoMapper;
    public int insert(Userinfo record) {
        return userinfoMapper.insert(record);
    }


    public int insertSelective(Userinfo record) {
        return userinfoMapper.insertSelective(record);
    }

    public Userinfo selectInfoByUserId(String userid) {
        return userinfoMapper.selectInfoByUserId(userid);
    }

    public void updateByUserid(Userinfo userinfo) {
        userinfoMapper.updateByUserid(userinfo);
    }
}
