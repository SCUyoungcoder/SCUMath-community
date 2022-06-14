package com.nowcoder.community.service;

import com.nowcoder.community.entity.Picture;
import com.nowcoder.community.util.ImageCompressUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Component
@EnableAsync
public class ImageCompressService {
    @Value("${community.path.uploadpic}")
    private String picPath;
    @Autowired
    private PictureService pictureService;

    @Async
    @Scheduled(cron = "0 0 4 * * ?")     //每天凌晨4点执行压缩图片至200Kb以内
    public void CompressImages() throws Exception {
        CompressFolderImages(picPath);
    }

    private void CompressFolderImages(String folder) throws Exception {
        Date date1 = new Date(System.currentTimeMillis());
        Date date2 = new Date(System.currentTimeMillis() - 3600*25*1000);
        java.sql.Timestamp sqldate1 = new java.sql.Timestamp(date1.getTime());
        java.sql.Timestamp sqldate2 = new java.sql.Timestamp(date2.getTime());
        List<Picture> pictureList = pictureService.SelectByTime(sqldate2,sqldate1);
        System.out.println(pictureList);
        for (Picture picture:pictureList){
            String path = picPath + "/" + picture.getSaveName();
            File file = new File(path);
            CompressImageFile(file);
        }
    }

    private void CompressImageFile(File file) throws Exception {
        if (!file.isFile())
            return;
        if (!IsImageFile(file))
            return;
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            bytes = ImageCompressUtil.CompressPicForScale(bytes, 1024, file.getName());// 图片小于1Mb
            FileUtils.writeByteArrayToFile(file, bytes);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        Thread.sleep(200);
    }

    private boolean IsImageFile(File file) throws Exception {
        boolean flag = false;
        try {
            String fname = file.getName().toLowerCase();
            if(!(fname.endsWith(".png")||
                    fname.endsWith(".bmp")||
                    fname.endsWith(".jpg")||
                    fname.endsWith(".jpeg")||
                    fname.endsWith(".gif")))
                return false;

            BufferedImage bufreader = ImageIO.read(file);
            int width = bufreader.getWidth();
            int height = bufreader.getHeight();
            if (width == 0 || height == 0) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (IOException e) {
            flag = false;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
