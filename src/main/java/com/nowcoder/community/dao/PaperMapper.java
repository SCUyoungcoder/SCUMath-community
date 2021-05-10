package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Paper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface PaperMapper {
    //@Param注解用于给参数取别名，如果只有一个参数，并且在<if>里使用，则必须加别名
    Paper selectById(int id);
    List<Paper> selecyByUserid(int userid);
    List<Paper> selectOnlyByStatus(int status);
    List<Paper> selectByTitle(String title);/*这里是否与elasticsearch搜索有关*/
    Paper selectByFilepath(String filepath);
    Paper selectByIdAndCreateTime(int id , Date gmtCreate);
    List<Paper> selectByStatus(int status, int offset,int limit);
    int insertPaper(Paper paper);
    int delectById(int id);
    int updateDownloadcount(int id,int downloadcount);
    int updatePaperstatus(Paper paper);
    int countByAuthorIdAndStatus(int id,int status);
    List<Paper> selectByAuthorIdAndStatus(int id,int status);
    List<Paper> newSelectByThreeStatus(int status1,int status2,int status3);
    List<Paper> newSelectByAuthorIdAndThreeStatus(int userId,int status1,int status2,int status3);
}
