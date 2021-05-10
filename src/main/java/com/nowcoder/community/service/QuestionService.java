package com.nowcoder.community.service;

import com.nowcoder.community.dao.QuestionMapper;
import com.nowcoder.community.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;
    public int DeleteQuestionById(int id){return questionMapper.deleteQuestionById(id);}
    public int UpdateQuestion(Question question){return questionMapper.updateQuestion(question);}
    public int UpdateViews(int id,int count){return questionMapper.updateViews(id,count);}

    public int UpdateStatus(int id ,int status){return questionMapper.updateStatus(id,status);}
    public int InsertQuestion(Question question){return questionMapper.insertQuestion(question);}

    public Question SelectByQid(String qid){return questionMapper.selectByQid(qid);}
    public Question SelectById(int id){return questionMapper.selectById(id);}
    public Question SelectByIdAndCreateTime(int id, Date gmtCreate){return questionMapper.selectByIdAndCreateTime(id,gmtCreate);}

    public List<Question> SelectByAuthorId(int userId){return questionMapper.selectByAuthorId(userId);}
    public List<Question> SelectAllByStatus(int status){return questionMapper.selectAllByStatus(status);}
    public List<Question> SelectByCategoryId(int categoryId){return questionMapper.selectByCategory(categoryId);}
    public int CountByAuthorId(int authorid){return questionMapper.countByAuthorId(authorid);}
}
