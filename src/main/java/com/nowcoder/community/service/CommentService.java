package com.nowcoder.community.service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;


    public List<Comment> selectcommentByEntity(int entitytype,int entityid){
        return commentMapper.selectByEntity(entitytype,entityid);
    }
    public List<Comment> selectcommentByTarget(int targetid){
        return commentMapper.selectByTargetid(targetid);
    }
    public Comment selectcommentById(int id){return commentMapper.selectById(id);}
    public int addComment(Comment comment){
        if (comment == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        /*转义HTML标记*/
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        return commentMapper.insertComment(comment);
    }
}
