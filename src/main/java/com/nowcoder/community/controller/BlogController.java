package com.nowcoder.community.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.*;
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
import java.util.*;


@Controller
@RequestMapping("/blog")
public class BlogController {
    @Value("${community.path.uploadpic}")
    private String uploadPicPath;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private UploadPicService uploadPic;

    @LoginRequired
    @RequestMapping(path = "/write",method = RequestMethod.GET)
    public String toWrite(Model model){
        User user = hostHolder.getUser();
        List<Classification> classificationList = classificationService.AllClassifications();
        model.addAttribute("classificationList",classificationList);
        model.addAttribute("user",user);
        return "blog/write";
    }
    @RequestMapping(path = "/list",method = RequestMethod.GET)
    public String showList(Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<Blog> info = new PageInfo<>(blogService.SelectAllByStatus(0));
        List<Classification> classifications = classificationService.AllClassifications();
        model.addAttribute("info", info);
        model.addAttribute("categoryList",classifications);
        return "blog/list";
    }
    @RequestMapping(path = "/category",method = RequestMethod.GET)
    public String readCategory(Model model,int cid,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<Blog> info = new PageInfo<>(blogService.SelectByCategoryId(cid));
        List<Classification> classifications = classificationService.AllClassifications();
        model.addAttribute("info", info);
        model.addAttribute("categoryList",classifications);
        return "blog/list";
    }
    @LoginRequired
    @RequestMapping(path = "/write",method = RequestMethod.POST)
    public String save(BlogWriteForm blogWriteForm) {
        User user = hostHolder.getUser();
        //接受博客表单vo
        System.out.println(blogWriteForm);
        Blog blog = new Blog();
        String bid = CommunityUtil.generateUUID();
        blog.setBid(bid);
        blog.setTitle(blogWriteForm.getTitle());
        blog.setContent(blogWriteForm.getContent());
        if (user.getType()==1){
            blog.setStatus(0);
        }
        else {
            blog.setStatus(1);
        }
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
        String[] findPicName = blogWriteForm.getContent().split("/blog/UsingTheComplexLinkGetThePicForPicManage/");
        for(int i = 1;i<findPicName.length;i++){
            String newPic = findPicName[i].split("\\)")[0];
            Picture findSql = pictureService.SelectBySaveName(newPic);
            if (findSql!=null){
                pictureService.UpdateFather(findSql.getId(),blog.getId(),1);
            }
        }

        return "redirect:/blog/read/" + bid;

    }
    @LoginRequired
    @RequestMapping(path = "/edit",method = RequestMethod.POST)
    public String editBlog(Blog blog){
        String bid = blog.getBid();
        String Content = blog.getContent();
        Blog oldBlog = blogService.SelectByBid(bid);
        oldBlog.setTitle(blog.getTitle());
        oldBlog.setContent(blog.getContent());
        oldBlog.setCategoryId(blog.getCategoryId());
        oldBlog.setCategoryName(classificationService.GetByClassificationId(blog.getCategoryId()).getName());
        blogService.UpdateBlog(oldBlog);
        //数据库中删除本博客没用的图片
        int newtype = (int)(Math.random()*90+10);
        //System.out.println(pictureService.UpdateFahterTypeByFather(newtype,blog.getId(),1));
        List<Picture> pictureList = pictureService.SelectByFather(oldBlog.getId(),1);
        //System.out.println(blog.getId());
        if (!pictureList.isEmpty()){
            System.out.println("修改数据库");
            for (Picture picture:pictureList){
                pictureService.UpdateFather(picture.getId(),picture.getFatherId(),newtype);
            }
        }
        String[] findPicName = Content.split("/blog/UsingTheComplexLinkGetThePicForPicManage/");
        for(int i = 1;i<findPicName.length;i++){
            String newPic = findPicName[i].split("\\)")[0];
            Picture findInSql = pictureService.SelectBySaveName(newPic);
            if (findInSql!=null){
                pictureService.UpdateFather(findInSql.getId(),oldBlog.getId(),1);
            }
        }
        //本地删除本博客没用的图片
        List<Picture> pictures = pictureService.SelectByFather(oldBlog.getId(),newtype);
        System.out.println(pictures);
        if (!pictures.isEmpty()){
            for (Picture pic:pictures){
                String path = uploadPicPath+"/"+pic.getSaveName();
                System.out.println(path);
                File file = new File(path);
                if (!file.isDirectory()){
                    System.out.println("正在删除文件");
                    file.delete();
                }

            }
            pictureService.DeleteByFather(oldBlog.getId(),newtype);
        }
        /*List<Picture> pictures = pictureService.SelectByFather(oldBlog.getId(),1)
        pictureService.DeleteByFather(oldBlog.getId(),1);
        String[] findPicName = Content.split("/blog/UsingTheComplexLinkGetThePicForPicManage/");
        for(int i = 1;i<findPicName.length;i++){
            String newPic = findPicName[i].split("\\)")[0];
            Picture findSql = pictureService.SelectBySaveName(newPic);
            if (findSql!=null){
                pictureService.UpdateFather(findSql.getId(),blog.getId(),1);
            }
        }*/
        return "redirect:/blog/read/" + bid;
    }
    @LoginRequired
    @RequestMapping(path = "/edit/{bid}",method = RequestMethod.GET)
    public String getEdit(Model model,@PathVariable("bid") String bid){
        User user = hostHolder.getUser();
        Blog blog = blogService.SelectByBid(bid);
        if (blog.getAuthorId()==user.getId()||user.getType()==1){
            model.addAttribute("blog",blog);
            List<Classification> classifications = classificationService.AllClassifications();
            model.addAttribute("categoryList",classifications);
            return "blog/edit";
        }else {
            return "error/404";
        }
    }

    @RequestMapping(value = "/read/{bid}",method = RequestMethod.GET)
    public String read(Model model,@PathVariable("bid") String bid ,Page page){
        Blog blog = blogService.SelectByBid(bid);
        User user = hostHolder.getUser();
        if (user!=null){
            if (user.getId()!=blog.getAuthorId()){
                blogService.UpdateViews(blog.getId(),blog.getViews() + 1);
            }
        }
        else {
            blogService.UpdateViews(blog.getId(),blog.getViews() + 1);
        }
        //List<Comment> blogComments = commentService.selectcommentByEntity(2,blog.getId(),0);//status=评论，entitytype=论文

        List<Comment> blogComments = commentService.selectByEntityAndPage(2,blog.getId(),0,page.getCurrent()-1,page.getLimit());
        List<Map<String,Object>> coms = new ArrayList<>();
        if (blogComments!=null) {
            for (Comment blogComment : blogComments) {
                Map<String,Object> map = new HashMap<>();
                map.put("comment",blogComment);
                map.put("user",userService.findUserById(blogComment.getUserid()));
                List<Comment> commentComments =commentService.selectcommentByEntity(1,blogComment.getId(),0);//status=评论，entitytype=评论
                List<Map<String,Object>> ccs = new ArrayList<>();
                if (commentComments!=null){

                    for (Comment commentComment:commentComments){
                        Map<String,Object> cc = new HashMap<>();
                        cc.put("reply",commentComment);
                        cc.put("user",userService.findUserById(commentComment.getUserid()));
                        User target = commentComment.getTargetid() == 0 ? null:userService.findUserById(commentComment.getTargetid());
                        cc.put("target",target);
                        /*if (commentComment.getTargetid()!=0){       *//*上传评论时，如果没有targetid数据库默认置0——正常情况应该不会发生这种事情*//*
                            cc.put("targetname",userService.findUserById(commentComment.getTargetid()).getUsername());//由targetid拿到其targetname
                        }
                        else {
                            cc.put("targetname",null);
                        }
                        cc.put("id",commentComment.getId());
                        cc.put("userid",commentComment.getUserid());
                        cc.put("type",commentComment.getType());//论文详情页评论区，只需要评论，这里把评论的类型（type = 0:主评论，1：次评论）
                        cc.put("username",userService.findUserById(commentComment.getUserid()).getUsername());
                        cc.put("content",":"+commentComment.getContent());
                        cc.put("createtime",commentComment.getCreatetime());*/
                        ccs.add(cc);
                    }

                }
                map.put("replys",ccs);
                int replyCount = commentService.findCommentCount(1, blogComment.getId());
                map.put("replyCount", replyCount);
                /*else {
                    map.put("replys",null);                *//*是否需要*//*
                }*/
                /*map.put("id",blogComment.getId());
                map.put("username",userService.findUserById(blogComment.getUserid()).getUsername());
                map.put("userid",blogComment.getUserid());
                map.put("content",blogComment.getContent());
                map.put("createtime",blogComment.getCreatetime());*/
                coms.add(map);
            }
        }
        page.setRows(blogComments == null?0:(int)commentService.CountByEntity(blog.getId(),2)/10);
        //page.setRows(3);
        page.setPath("/blog/read/"+bid);

        model.addAttribute("comments",coms);
        model.addAttribute("blog",blog);
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
    @RequestMapping(value = "/UsingTheComplexLinkGetThePicForPicManage/{picname}",method = RequestMethod.GET)
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
            out.close();;
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //传输结束后，删除文件，可以不删除，在生成的图片中回对此进行覆盖
/*        File file1 = new File("E:\\Java\\eclipse_code\\NLP\\WebContent\\source\\wordcloud.png");
        file1.delete();
        System.out.println("文件删除！");*/

    }
    @LoginRequired
    @RequestMapping(path = "/delete/{bid}" ,method = RequestMethod.GET)
    public String delete(@PathVariable("bid") String bid){
        Blog blog = blogService.SelectByBid(bid);
        User user = hostHolder.getUser();
        if (blog==null){
            return "error/404";
        }
        if (blog.getAuthorId() == user.getId() || user.getType() == 1){
            blogService.DeleteBlogById(blog.getId());
            List<Comment> delectComents = commentService.selectcommentByEntity(2,blog.getId(),0);
            if (delectComents!=null){
                for (Comment dc:delectComents){
                    commentService.DeleteByEntity(dc.getId(),1);
                    commentService.DeleteById(dc.getId());
                }
            }
            List<Picture> pictures = pictureService.SelectByFather(blog.getId(),1);
            if (pictures!=null){
                for (Picture pic:pictures){
                    String path = uploadPicPath+"/"+pic.getSaveName();
                    File file = new File(path);
                    System.out.println(file);
                    System.out.println(path);
                    if (!file.isDirectory()){
                        System.out.println("正在删除");
                        file.delete();
                    }
                }
                pictureService.DeleteByFather(blog.getId(),1);
            }
            return "redirect:/blog/list";
        }
        else {
            return "error/404";
        }
    }
    @LoginRequired
    @PostMapping(path = "/pass")
    @ResponseBody
    public String pass(String bid) {
        System.out.println(bid);
        User user = hostHolder.getUser();
        Blog blog = blogService.SelectByBid(bid);
        if (user.getId()==blog.getAuthorId()||user.getType()==1){
            blogService.UpdateStatus(blog.getId(),0);
            return CommunityUtil.getJSONString(0);
        }
        else {
            return CommunityUtil.getJSONString(1,"没有权限！");
        }


    }














}