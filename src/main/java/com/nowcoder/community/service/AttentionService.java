package com.nowcoder.community.service;

import com.nowcoder.community.dao.AttentionMapper;
import com.nowcoder.community.entity.Attention;
import com.nowcoder.community.entity.Itemindex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttentionService {
    @Autowired
    AttentionMapper attentionMapper;
    public List<Attention> SelectByFocusId(int focusId){return attentionMapper.selectByFocusId(focusId);}
    public List<Attention> SelectByUserId(int userId){return attentionMapper.selectByUserId(userId);}
    public int FindById(int userId,int focusId){return attentionMapper.findById(userId,focusId);}
    public int DeleteById(int userId,int focusId){return attentionMapper.deleteById(userId,focusId);}
    public int InsertAttention(int userId,int focusId){return attentionMapper.insertAttention(userId,focusId);}
    public int CountAttentionByFocusId(int focusId){return attentionMapper.countAttention(focusId);}
    public int CountAttentionByUserId(int userId){return attentionMapper.countAttentionByUserId(userId);}

    public List<Itemindex> SelectAllItemByUserIds(String users,String authors){return attentionMapper.selectAllItemByUserIds(users,authors);}
}
