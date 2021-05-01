package com.nowcoder.community.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper {
    int findBlogLikeById(int userId,int blogId);
    int deleteBlogByTwoId(int userId,int blogId);
    int insertBlogLike(int userId,int blogId);
    int deleteBlogLike(int id);
    int countBlogLike(int blogId);

    int findQuestionLikeById(int userId,int questionId);
    int insertQuestionLike(int userId,int questionId);
    int deleteQuestionLike(int id);
    int countQuestionLike(int questionId);
}
