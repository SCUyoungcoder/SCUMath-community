package com.nowcoder.community.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttentionMapper {
    int findById(int userId,int focusId);
    int deleteAttentionById(int id);
    int deleteById(int userId,int focusId);
    int insertAttention(int userId,int focusId);
    int countAttention(int focusId);
    int countAttentionByUserId(int userId);
}
