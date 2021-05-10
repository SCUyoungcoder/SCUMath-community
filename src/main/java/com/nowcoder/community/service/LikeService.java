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
    public int DeleteBlogByTwoId(int userId,int blogId){return likeMapper.deleteBlogByTwoId(userId,blogId);}

}
