package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Paper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaperMapper {
    Paper selectById(int id);
    List<Paper> selectByFatherid(int fatherid);
    List<Paper> selecyByUserid(int userid);
    /*Paper selectByTitle(String title);*/
    List<Paper> selectByTitle(String title);
    int insertPaper(Paper paper);
    int delectById(int id);
    int updateDownloadcount(int id,int downloadcount);
}
