package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Classification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassificationMapper {
    Classification selectById(int id);
    Classification selectByName(String name);
    int insertClassification(Classification classification);
    int deleteByName(String name);
}
