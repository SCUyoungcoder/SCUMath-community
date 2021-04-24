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

    public List<Comment> selectByEntityAndPage(int entityType, int entityId, int status, int offset, int limit) {
        return commentMapper.selectByEntityAndPage(entityType, entityId, status, offset, limit);
    }

    public List<Comment> selectcommentByEntity(int entitytype, int entityid, int status) {
        return commentMapper.selectByEntity(entitytype, entityid, status);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    public List<Comment> selectByUserid(int userid) {
        return commentMapper.selectByUserid(userid);
    }

    public List<Comment> selectcommentByEntityandtargit(int entitytype, int entityid, int targetid) {
        return commentMapper.selectByEntityandtargitid(entitytype, entityid, targetid);
    }

    public List<Comment> selectcommentByTarget(int targetid) {
        return commentMapper.selectByTargetid(targetid);
    }

    public Comment selectcommentById(int id) {
        return commentMapper.selectById(id);
    }

    public List<Comment> findcommentByTable(int status, int table) { //公告，由用户群组标签查
        return commentMapper.selectCommentsByTable(status, table);
    }
    public List<Comment> SelectByTwoTarget(int userId){return commentMapper.selectByTwoTarget(userId);}

    public int CountByEntity(int entityId, int entityType) {
        return commentMapper.countByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        /*转义HTML标记*/
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        return commentMapper.insertComment(comment);
    }

    public int DeleteById(int id) {
        return commentMapper.deleteById(id);
    }

    public int DeleteByEntity(int entityId, int entityType) {
        return commentMapper.deleteByEntity(entityId, entityType);
    }
}