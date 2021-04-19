package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Source;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SourceMapper {
    Source selectById(int id);
    List<Source> selectByFatherid(int fatherid);//第二级科目标签页面论文查询需要，添加@Param注解 By MY
    List<Source> selecyByUserid(int userid);
    List<Source> selectOnlyByStatus(int status);
    List<Source> selectByTitle(String title);/*这里是否与elasticsearch搜索有关*/
    Source selectByFulltitle(String title);
    List<Source> selectByStatus(int status, int offset,int limit);
    int insertSource(Source source);
    int delectById(int id);
    int updateDownloadcount(int id,int downloadcount);
    int updateSourcestatus(Source source);
    int countstatus(int status);
    int countByAuthorId(int id);
}
