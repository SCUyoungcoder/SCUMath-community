package com.nowcoder.community.service;

import com.nowcoder.community.dao.ClassificationMapper;
import com.nowcoder.community.entity.Classification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificationService {
    @Autowired
    private ClassificationMapper classificationMapper;

    public List<Classification> AllClassifications() {
        return classificationMapper.selectAll();
    }
    public Classification GetNameBySearchname(String searchname){return classificationMapper.selectBySearchname(searchname);}
}