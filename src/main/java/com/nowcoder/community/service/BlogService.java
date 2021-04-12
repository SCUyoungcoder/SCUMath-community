package com.nowcoder.community.service;

import com.nowcoder.community.dao.BlogMapper;
import com.nowcoder.community.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    BlogMapper blogMapper;

    public int UpdateViews(int id,int count){return blogMapper.updateViews(id,count);}

    public int InsertBlog(Blog blog){return blogMapper.insertBlog(blog);}

    public Blog SelectByBid(String bid){return blogMapper.selectByBid(bid);}

    public List<Blog> SelectBySort(int sort){return blogMapper.selectBySort(sort);}

    public List<Blog> SelectAll(){return blogMapper.selectAll();}
}
