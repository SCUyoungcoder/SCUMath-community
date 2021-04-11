package com.nowcoder.community.controller;


import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Blog;
import com.nowcoder.community.entity.Classification;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.BlogService;
import com.nowcoder.community.service.ClassificationService;
import com.nowcoder.community.service.UploadPicService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.vo.BlogWriteForm;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/blog")
public class BlogController {
    @Value("${community.path.uploadpic}")
    private String uploadPicPath;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    BlogService blogService;

    @Autowired
    ClassificationService classificationService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UploadPicService uploadPic;

    @LoginRequired
    @RequestMapping(path = "/write",method = RequestMethod.GET)
    public String toWrite(Model model){
        User user = hostHolder.getUser();
        List<Classification> classificationList = classificationService.AllClassifications();
        model.addAttribute("classificationList",classificationList);
        model.addAttribute("user",user);
        return "blog/write";
    }
    @LoginRequired
    @RequestMapping(path = "/write",method = RequestMethod.POST)
    public String save(BlogWriteForm blogWriteForm) {
        //接受博客表单vo
        System.out.println(blogWriteForm);
        Blog blog = new Blog();
        String bid = CommunityUtil.generateUUID();
        blog.setBid(bid);
        blog.setTitle(blogWriteForm.getTitle());
        blog.setContent(blogWriteForm.getContent());
        blog.setSort(0);
        blog.setViews(0);
        //设置用户相关
        blog.setAuthorId(blogWriteForm.getAuthorId());
        blog.setAuthorAvatar(blogWriteForm.getAuthorAvatar());
        blog.setAuthorName(blogWriteForm.getAuthorName());
        //设置栏目相关
        Classification classification = classificationService.GetByClassificationId(blogWriteForm.getCategoryId());
        blog.setCategoryId(classification.getId());
        blog.setCategoryName(classification.getName());
        blog.setGmtCreate(new Date(System.currentTimeMillis()));
        blog.setGmtUpdate(new Date(System.currentTimeMillis()));
        blogService.InsertBlog(blog);

        return "redirect:/blog/read/" + bid;

    }
    @RequestMapping(value = "/read/{bid}",method = RequestMethod.GET)
    public String read(Model model,@PathVariable("bid") String bid){
        Blog blog = blogService.SelcetByBid(bid);
        User user = hostHolder.getUser();
        if (user.getId()!=blog.getAuthorId()){
            blogService.UpdateViews(blog.getId(),blog.getViews() + 1);
        }


        return "blog/read";
    }

    @LoginRequired
    @RequestMapping(path = "/upFile")
    @ResponseBody
    public JSONObject uploadImg(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file, HttpServletRequest request){
        // 使用自定义的上传路径
        String path = uploadPicPath;
        // 调用上传图片的方法

        JSONObject res = uploadPic.uploadImgFile(request,path,file);

        return res;
    }

    //返回图片，参考url：https://blog.csdn.net/meiqi0538/article/details/79862213/?utm_term=java%E8%BF%94%E5%9B%9E%E5%9B%BE%E7%89%87%E7%BB%99%E5%89%8D%E7%AB%AF&utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~sobaiduweb~default-1-79862213&spm=3001.4430
    @LoginRequired
    @RequestMapping(value = "/getPic/{picname}",method = RequestMethod.GET)
    public void returnPic(HttpServletResponse response,@PathVariable("picname") String picname){
        String filePath = uploadPicPath + "/"+picname;
        //创建一个文件对象，对应的文件就是python把词云图片生成后的路径以及对应的文件名
        File file = new File(filePath);
        //使用字节流读取本地图片
        /*ServletOutputStream out=null;
        BufferedInputStream buf=null;*/
        //response.setContentType("image/png");
        try {
            //使用输入读取缓冲流读取一个文件输入流
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            //利用response获取一个字节流输出对象
            OutputStream out = response.getOutputStream();
            //定义个数组，由于读取缓冲流中的内容
            byte[] buffer=new byte[1024];
            //while循环一直读取缓冲流中的内容到输出的对象中
            while(buf.read(buffer)!=-1) {
                out.write(buffer);
            }
            //写出到请求的地方
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //传输结束后，删除文件，可以不删除，在生成的图片中回对此进行覆盖
/*        File file1 = new File("E:\\Java\\eclipse_code\\NLP\\WebContent\\source\\wordcloud.png");
        file1.delete();
        System.out.println("文件删除！");*/

    }

















}
