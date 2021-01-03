package com.nowcoder.community.service;

import com.nowcoder.community.dao.PaperMapper;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.Paper;
import org.apache.ibatis.annotations.Param;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperOfClassService {

    @Autowired
    private PaperMapper paperMapper;

    public Paper selectpaperByTitle(String title){ return paperMapper.selectByFulltitle(title);}
    public Paper selectPaperById(int id){return paperMapper.selectById(id);}
    public int updateDownloadcount(int id,int downloadcount){return paperMapper.updateDownloadcount(id,downloadcount);}
    /*public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);*//*上传文件例子*/
    /*将paper信息存到数据库*/
    public int updatastatus(Paper paper){return paperMapper.updatePaperstatus(paper);}
    public void uploadpaper(Paper paper){         /*不需要返回消息*/
        /*if (paper == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(paper.getTitle())){
            map.put("titleMsg","论文题目不能为空");
            return map;
        }*/
        paperMapper.insertPaper(paper);
        /*return map;*//*上传论文部分，，，，待完成?*/
    }
    public List<Paper> selectpaperByStatus(int status,int offset,int limit){
        return paperMapper.selectByStatus(status,offset,limit);
    }
    public int countstatus(int status){return paperMapper.countstatus(status);}
    /*public Page<Paper> selectpaperByStatus(int status , int current, int limit){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current,limit))
                .build();
        //return paperMapper.selectByStatus(status);
        return paperMapper.
    }*/
//【看完第六章视频后考虑是否有用】
}
