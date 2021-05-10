package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Loginticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginticketMapper {
    Loginticket selectByTicket(String ticket);
    int insertTicket(Loginticket loginticket);
    int deleteByTicket(String ticket);
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket,int status);
}

