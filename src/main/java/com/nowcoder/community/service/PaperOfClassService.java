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

import java.text.ParsePosition;
import java.util.Date;
import java.util.List;

@Service
public class PaperOfClassService {

    @Autowired
    private PaperMapper paperMapper;

    public Paper selectPaperById(int id){return paperMapper.selectById(id);}
    public Paper SelectByIdAndCreateTime(int id, Date gmtCreate){return paperMapper.selectByIdAndCreateTime(id,gmtCreate);}
    public int updateDownloadcount(int id,int downloadcount){return paperMapper.updateDownloadcount(id,downloadcount);}
    public List<Paper> selectPaperOnlyByStatus(int status){return paperMapper.selectOnlyByStatus(status);}
    public int updatastatus(Paper paper){return paperMapper.updatePaperstatus(paper);}
    public int uploadpaper(Paper paper){
        return paperMapper.insertPaper(paper);
    }
    public Paper selectByFilepath(String filepath){return paperMapper.selectByFilepath(filepath);}
    public List<Paper> selectpaperByStatus(int status,int offset,int limit){
        return paperMapper.selectByStatus(status,offset,limit);
    }
    public int delectPaperById(int id){return paperMapper.delectById(id);}
    public int CountByAutherIdAndStatus(int autherid,int status){return paperMapper.countByAuthorIdAndStatus(autherid,status);}
    public List<Paper> SelectByAuthorIdAndStatus(int autherid,int status){return paperMapper.selectByAuthorIdAndStatus(autherid,status);}

    public List<Paper> NewSelectByThreeStatus(int status1,int status2,int status3){
        return paperMapper.newSelectByThreeStatus(status1,status2,status3);
    }
    public List<Paper> NewSelectByAutherIdAndThreeStatus(int userId,int status1,int status2,int status3){
        return paperMapper.newSelectByAuthorIdAndThreeStatus(userId,status1,status2,status3);
    }
}
