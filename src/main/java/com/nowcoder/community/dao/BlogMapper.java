package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper {
    int insertBlog(Blog blog);
    Blog selectByBid(String bid);
    int updateViews(int id,int count);
}
