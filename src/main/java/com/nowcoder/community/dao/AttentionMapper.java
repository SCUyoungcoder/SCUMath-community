package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Attention;
import com.nowcoder.community.entity.Itemindex;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttentionMapper {
    List<Attention> selectByFocusId(int focusId);
    List<Attention> selectByUserId(int userId);
    int findById(int userId,int focusId);
    int deleteAttentionById(int id);
    int deleteById(int userId,int focusId);
    int insertAttention(int userId,int focusId);
    int countAttention(int focusId);
    int countAttentionByUserId(int userId);


    List<Itemindex> selectAllItemByUserIds(@Param("users")String users,@Param("authors")String authors);
}
