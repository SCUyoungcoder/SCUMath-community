package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Paper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaperMapper {
    //@Param注解用于给参数取别名，如果只有一个参数，并且在<if>里使用，则必须加别名
    Paper selectById(int id);
    List<Paper> selectByFatherid(@Param("fatherid") int fatherid);//第二级科目标签页面论文查询需要，添加@Param注解 By MY
    List<Paper> selecyByUserid(int userid);
    List<Paper> selectOnlyByStatus(int status);
    /*Paper selectByTitle(String title);*/
    List<Paper> selectByTitle(String title);/*这里是否与elasticsearch搜索有关*/
    Paper selectByFulltitle(String title);
    List<Paper> selectByStatus(int status, int offset,int limit);
    int insertPaper(Paper paper);
    int delectById(int id);
    int updateDownloadcount(int id,int downloadcount);
    int updatePaperstatus(Paper paper);
    int countstatus(int status);
    int countByAuthorId(int id);
}
