package com.nowcoder.community.service;

import com.nowcoder.community.dao.PictureMapper;
import com.nowcoder.community.entity.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PictureService {
    @Autowired
    private PictureMapper pictureMapper;

    public int InsertPicture(Picture picture){return pictureMapper.insertPicture(picture);}

    public int UpdateFather(int id,int fatherId,int fatherType){
        return pictureMapper.updateFather(id,fatherId,fatherType);
    }
    public int DeleteByFather(int fatherId,int fatherType){
        return pictureMapper.deleteByFather(fatherId,fatherType);
    }

    public int DeleteById(int id){return pictureMapper.deleteById(id);}

    public Picture SelectBySaveName(String saveName){
        return pictureMapper.selectBySaveName(saveName);
    }

    public List<Picture> SelectByType(int fatherType){
        return pictureMapper.selectByType(fatherType);
    }

    public List<Picture> SelectByFather(int fatherid,int fathertype){
        return pictureMapper.selectByFather(fatherid,fathertype);
    }
    public List<Picture> SelectByTime(Date date1, Date date2){
        return pictureMapper.selectByTime(date1,date2);
    }
}
