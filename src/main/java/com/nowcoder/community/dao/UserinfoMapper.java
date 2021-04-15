package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserinfoMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(Userinfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(Userinfo record);

    Userinfo selectInfoByUserId(String userid);

    void updateByUserid(Userinfo userInfo);
}
