package com.nowcoder.community.service;

import com.nowcoder.community.dao.BlogMapper;
import com.nowcoder.community.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogService {
    @Autowired
    BlogMapper blogMapper;

    public int UpdateViews(int id,int count){return blogMapper.updateViews(id,count);}

    public int InsertBlog(Blog blog){return blogMapper.insertBlog(blog);}

    public Blog SelcetByBid(String bid){return blogMapper.selectByBid(bid);}
}
