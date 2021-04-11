package com.nowcoder.community.service;

import com.nowcoder.community.dao.BlogPicMaper;
import com.nowcoder.community.entity.BlogPic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogPicService {
    @Autowired
    BlogPicMaper blogPicMaper;

    public int DeleteByBlogId(int blogid){return blogPicMaper.deleteByBlogId(blogid);}

    public int InsertBlogPic(BlogPic blogPic){return blogPicMaper.insertBlogPic(blogPic);}
}
