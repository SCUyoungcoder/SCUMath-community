package com.nowcoder.community.dao;

import com.nowcoder.community.entity.BlogPic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogPicMaper {
    //List<BlogPic> selectByBlogId(int blogid);
    int insertBlogPic(BlogPic blogPic);
    int deleteByBlogId(int blogid);
}
