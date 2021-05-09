package com.nowcoder.community.service;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Picture;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

@Service
public class UploadPicService {
    @Autowired
    PictureService pictureService;

    public JSONObject uploadImgFile(HttpServletRequest request,String filePath, MultipartFile file){

        System.out.println(file.getSize());
        if (file.getSize()>1024*1024){
            return null;
        }
        Picture pic = new Picture();
        // 获取完整的文件名
        String trueFileName = file.getOriginalFilename();
        // 获取文件后缀名
        String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
        // 生成新文件的名字
        String fileName = CommunityUtil.generateUUID() + suffix;
        fileName = System.currentTimeMillis()+"_"+ fileName;
        //String fileName = trueFileName;
        // 获取项目地址
        String itemPath = getItemPath(request)+"blog/UsingTheComplexLinkGetThePicForPicManage/";

        //图片信息存入数据库
        pic.setName(trueFileName);
        pic.setSaveName(fileName);
        pic.setFatherType(0);
        pic.setCreateTime(new Date(System.currentTimeMillis()));
        int newpic = pictureService.InsertPicture(pic);

        // 判断当前要上传的路径是否存在
        File targetFile = new File(filePath, fileName);
        if(!targetFile.exists()){
            targetFile.getParentFile().mkdirs();
        }

        //保存文件
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject res = new JSONObject();
        // 图片上传后地址
        res.put("url", itemPath+fileName); ///图片地址和上传后的文件名
        // 图片上传的状态 1成功0失败
        res.put("success", 1);
        // 图片上传回传的信息
        res.put("message", "upload success!");

        return res;
    }
    public String getItemPath(HttpServletRequest request){
        String scheme = request.getScheme(); // 获取链接协议，http
        String serverName = request.getServerName(); // 获取服务器名称 localhost
        int serverPort = request.getServerPort(); // 获取端口 8080
        String path = scheme+"://"+serverName+":"+serverPort+"/";
        return path;
    }

}
