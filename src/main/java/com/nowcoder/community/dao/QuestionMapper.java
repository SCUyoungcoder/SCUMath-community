package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface QuestionMapper {

    int insertQuestion(Question qusetion);
    int deleteQuestionById(int id);
    Question selectByQid(String qid);
    Question selectById(int id);
    Question selectByIdAndCreateTime(int id, Date gmtCreate);
    int updateViews(int id,int count);
    int updateQuestion(Question question);
    int updateStatus(int id,int status);
    List<Question> selectBySort(int sort);
    List<Question> selectAllByStatus(int status);
    List<Question> selectByCategory(int categoryId);
    List<Question> selectByAuthorId(int userId);
    int countByAuthorId(int id);
}
