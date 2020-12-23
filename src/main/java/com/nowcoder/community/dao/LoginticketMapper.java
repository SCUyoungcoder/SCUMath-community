package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Loginticket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginticketMapper {
    Loginticket selectByTicket(String ticket);
    int insertTicket(Loginticket loginticket);
    int delectByTicket(String ticket);
}

