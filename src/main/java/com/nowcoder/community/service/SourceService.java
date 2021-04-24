package com.nowcoder.community.service;

import com.nowcoder.community.dao.SourceMapper;
import com.nowcoder.community.entity.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceService {
    @Autowired
    private SourceMapper sourceMapper;

    /*public Source selectSourceByTitle(String title){ return sourceMapper.selectByFulltitle(title);}
    public Source selectSourceById(int id){return sourceMapper.selectById(id);}
    public int updateDownloadcount(int id,int downloadcount){return sourceMapper.updateDownloadcount(id,downloadcount);}
    *//*public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);*//**//*上传文件例子*//*
    *//*将source信息存到数据库*//*
    public List<Source> selectSourceOnlyByStatus(int status){return sourceMapper.selectOnlyByStatus(status);}
    public int updatastatus(Source source){return sourceMapper.updateSourcestatus(source);}
    public void uploadsource(Source source){         *//*不需要返回消息*//*
        *//*if (source == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(source.getTitle())){
            map.put("titleMsg","论文题目不能为空");
            return map;
        }*//*
        sourceMapper.insertSource(source);
        *//*return map;*//**//*上传论文部分，，，，待完成?*//*
    }
    public List<Source> selectSourceByStatus(int status,int offset,int limit){
        return sourceMapper.selectByStatus(status,offset,limit);
    }
    public int countstatus(int status){return sourceMapper.countstatus(status);}
    *//*public Page<source> selectsourceByStatus(int status , int current, int limit){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current,limit))
                .build();
        //return sourceMapper.selectByStatus(status);
        return sourceMapper.
    }*//*
//【看完第六章视频后考虑是否有用】
    public int CountByAutherId(int autherid){return sourceMapper.countByAuthorId(autherid);}*/
}
