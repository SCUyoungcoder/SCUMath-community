package com.nowcoder.community.service;

import com.nowcoder.community.dao.AttentionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttentionService {
    @Autowired
    AttentionMapper attentionMapper;

    public int FindById(int userId,int focusId){return attentionMapper.findById(userId,focusId);}
    public int DeleteAttentionById(int id){return attentionMapper.deleteAttentionById(id);}
    public int DeleteById(int userId,int focusId){return attentionMapper.deleteById(userId,focusId);}
    public int InsertAttention(int userId,int focusId){return attentionMapper.insertAttention(userId,focusId);}
    public int CountAttentionByFocusId(int focusId){return attentionMapper.countAttention(focusId);}
    public int CountAttentionByUserId(int userId){return attentionMapper.countAttentionByUserId(userId);}
}
