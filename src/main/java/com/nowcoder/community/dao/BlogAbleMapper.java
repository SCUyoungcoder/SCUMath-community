package com.nowcoder.community.dao;

import com.nowcoder.community.entity.BlogAble;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogAbleMapper {
    int insertBlogAble(BlogAble blogAble);
    int updateBlogAble(BlogAble blogAble);
    List<BlogAble> selectByBlogId(int blogId);
    List<BlogAble> selectByUserIdAndType(int userId,int type);
    List<BlogAble> selectByBlogIdAndType(int blogId,int type);
    BlogAble selectByBlogIdAndTypeAndEntityId(int blogId,int type,int entityId);
    BlogAble selectBlogAbleById(int id);
    int deleteById(int id);
    int deleteByBlogId(int blogId);
}
