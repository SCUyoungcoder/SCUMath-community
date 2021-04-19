package com.nowcoder.community.service;


import com.nowcoder.community.dao.LikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    LikeMapper likeMapper;

    public int FindBlogLikeById(int userId,int blogId){return likeMapper.findBlogLikeById(userId,blogId);}
    public int InsertBlogLike(int userId,int blogId){return likeMapper.insertBlogLike(userId,blogId);}
    public int DeleteBlogLike(int id){return likeMapper.deleteBlogLike(id);}
    public int CountBlogLike(int blogId){return likeMapper.countBlogLike(blogId);}

    public int FindQuestionLikeById(int userId,int questionId){return likeMapper.findQuestionLikeById(userId,questionId);}
    public int InsertQuestionLike(int userId,int questionId){return likeMapper.insertQuestionLike(userId,questionId);}
    public int DeleteQuestionLike(int id){return likeMapper.deleteQuestionLike(id);}
    public int CountQuestionLike(int questionId){return likeMapper.countQuestionLike(questionId);}

}
