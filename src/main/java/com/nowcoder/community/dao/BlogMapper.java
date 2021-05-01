package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface BlogMapper {
    int insertBlog(Blog blog);
    int deleteBlogById(int id);
    Blog selectByBid(String bid);
    List<Blog> selectByAvatar(String Avatar , int offset, int limit);
    List<Blog> selectAllByAvatar(String Avatar);
    List<Blog> selectByAvatarAndClassname(String Avatar,String classname);
    Blog selectById(int id);
    Blog selectByIdAndCreateTime(int id, Date gmtCreate);
    int updateViews(int id,int count);
    int updateBlog(Blog blog);
    int updateStatus(int id,int status);
    List<Blog> selectBySort(int sort);
    List<Blog> selectAllByStatus(int status);
    List<Blog> selectByCategory(int categoryId);
    List<Blog> selectByAuthorId(int userId);
    int countByAuthorId(int id);
}
