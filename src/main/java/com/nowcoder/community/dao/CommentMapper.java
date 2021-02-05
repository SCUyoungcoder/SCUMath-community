package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    Comment selectById(int id);
    List<Comment> selectByEntity(int entitytype,int entityid,int status);
    List<Comment> selectByEntityandtargitid(int entitytype,int entityid,int targetid);
    List<Comment> selectByTargetid(int targetid);
    List<Comment> selectByType(int type);
    List<Comment> selectByUserid(int userid);
    List<Comment> selectCommentsByTable(int status ,int table);
    int insertComment(Comment comment);
    int deleteById(int id);

}
