package com.nowcoder.community.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.annotation.LoginRequired;
//import com.nowcoder.community.dao.elasticsearth.QuestionRepository;
import com.nowcoder.community.dao.elasticsearth.QuestionRepository;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.vo.QuestionWriteForm;
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
@RequestMapping("/question")
public class QuestionController {
    @Value("${community.path.uploadpic}")
    private String uploadPicPath;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private QuestionService questionService;
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
    @RequestMapping(path = "/search",method = RequestMethod.GET)
    public String searchQuestion(String keyword,String fieldname,String sortname,Page page,Model model){
        if (sortname == null ){
            sortname = "views";
        }
        org.springframework.data.domain.Page<Question> searchQuestion = elasticsearchService.searchQuestionByFieldname(keyword,0,fieldname,sortname,page.getCurrent() - 1,page.getLimit());
        model.addAttribute("blogs",searchQuestion);
        model.addAttribute("fieldname",fieldname);
        model.addAttribute("label","question");
        //model.addAttribute("text","");
        page.setPath("/question/search?keyword="+keyword+"&fieldname="+fieldname+"&sortname"+sortname);
        page.setRows(searchQuestion == null?0:searchQuestion.getTotalPages());
        return "search/list";
    }
    @LoginRequired
    @RequestMapping(path = "/done/{qid}",method = RequestMethod.GET)
    public String questionDone(Model model,@PathVariable("qid") String qid,int commentId){
        Question question = questionService.SelectByQid(qid);
        User user = hostHolder.getUser();
        if (user.getId()!=question.getAuthorId() || question.getStatus()==1){
            return "error/404";
        }
        question.setSort(1);
        questionService.UpdateQuestion(question);
        questionRepository.save(question);
        commentService.UpdateTable(commentId,1);
        return "redirect:/question/read/"+qid;
    }
    @LoginRequired
    @RequestMapping(path = "/write",method = RequestMethod.GET)
    public String toWrite(Model model){
        User user = hostHolder.getUser();
        List<Classification> classificationList = classificationService.AllClassifications();
        model.addAttribute("classificationList",classificationList);
        model.addAttribute("user",user);
        return "question/write";
    }
    @LoginRequired
    @RequestMapping(path = "/list",method = RequestMethod.GET)
    public String showList(Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<Question> info = new PageInfo<>(questionService.SelectAllByStatus(0));
        List<Classification> classifications = classificationService.AllClassifications();
        model.addAttribute("info", info);
        model.addAttribute("categoryList",classifications);
        //model.addAttribute("checkLabel",0);
        return "question/list";
    }
    @LoginRequired
    @RequestMapping(path = "/category",method = RequestMethod.GET)
    public String readCategory(Model model,int cid,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<Question> info = new PageInfo<>(questionService.SelectByCategoryId(cid));
        List<Classification> classifications = classificationService.AllClassifications();
        model.addAttribute("info", info);
        model.addAttribute("thisCategory",classificationService.GetByClassificationId(cid));
        model.addAttribute("categoryList",classifications);
        //model.addAttribute("checkLabel",0);
        return "question/list";
    }
    @LoginRequired
    @RequestMapping(path = "/write",method = RequestMethod.POST)
    public String save(QuestionWriteForm questionWriteForm) {
        User user = hostHolder.getUser();
        //接受博客表单vo
        System.out.println(questionWriteForm);
        Question question = new Question();
        String qid = CommunityUtil.generateUUID();
        question.setQid(qid);
        question.setTitle(questionWriteForm.getTitle());
        question.setContent(questionWriteForm.getContent());
        if (user.getType()==1){
            question.setStatus(0);
        }
        else {
            question.setStatus(1);
        }
        question.setSort(0);
        question.setViews(0);
        //设置用户相关
        question.setAuthorId(questionWriteForm.getAuthorId());
        question.setAuthorAvatar(questionWriteForm.getAuthorAvatar());
        question.setAuthorName(questionWriteForm.getAuthorName());
        //设置栏目相关
        Classification classification = classificationService.GetByClassificationId(questionWriteForm.getCategoryId());
        question.setCategoryId(classification.getId());
        question.setCategoryName(classification.getName());
        question.setGmtCreate(new Date(System.currentTimeMillis()));
        question.setGmtUpdate(new Date(System.currentTimeMillis()));
        questionService.InsertQuestion(question);
        questionRepository.save(question);
        String[] findPicName = questionWriteForm.getContent().split("/blog/UsingTheComplexLinkGetThePicForPicManage/");
        for(int i = 1;i<findPicName.length;i++){
            String newPic = findPicName[i].split("\\)")[0];
            Picture findSql = pictureService.SelectBySaveName(newPic);
            if (findSql!=null){
                pictureService.UpdateFather(findSql.getId(),question.getId(),2);
            }
        }

        return "redirect:/question/read/" + qid;

    }
    @LoginRequired
    @RequestMapping(path = "/edit",method = RequestMethod.POST)
    public String editQuestion(Question question){
        String qid = question.getQid();
        String Content = question.getContent();
        Question oldQuestion = questionService.SelectByQid(qid);
        oldQuestion.setTitle(question.getTitle());
        oldQuestion.setContent(question.getContent());
        oldQuestion.setCategoryId(question.getCategoryId());
        oldQuestion.setCategoryName(classificationService.GetByClassificationId(question.getCategoryId()).getName());
        questionService.UpdateQuestion(oldQuestion);
        questionRepository.save(oldQuestion);
        //数据库中删除本博客没用的图片
        int newtype = (int)(Math.random()*90+10);
        //System.out.println(pictureService.UpdateFahterTypeByFather(newtype,question.getId(),1));
        List<Picture> pictureList = pictureService.SelectByFather(oldQuestion.getId(),2);
        //System.out.println(question.getId());
        if (!pictureList.isEmpty()){
            System.out.println("修改数据库");
            for (Picture picture:pictureList){
                pictureService.UpdateFather(picture.getId(),picture.getFatherId(),newtype);
            }
        }
        String[] findPicName = Content.split("/question/UsingTheComplexLinkGetThePicForPicManage/");
        for(int i = 1;i<findPicName.length;i++){
            String newPic = findPicName[i].split("\\)")[0];
            Picture findInSql = pictureService.SelectBySaveName(newPic);
            if (findInSql!=null){
                pictureService.UpdateFather(findInSql.getId(),oldQuestion.getId(),2);
            }
        }
        //本地删除本博客没用的图片
        List<Picture> pictures = pictureService.SelectByFather(oldQuestion.getId(),newtype);
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
            pictureService.DeleteByFather(oldQuestion.getId(),newtype);
        }
        /*List<Picture> pictures = pictureService.SelectByFather(oldQuestion.getId(),1)
        pictureService.DeleteByFather(oldQuestion.getId(),1);
        String[] findPicName = Content.split("/question/UsingTheComplexLinkGetThePicForPicManage/");
        for(int i = 1;i<findPicName.length;i++){
            String newPic = findPicName[i].split("\\)")[0];
            Picture findSql = pictureService.SelectBySaveName(newPic);
            if (findSql!=null){
                pictureService.UpdateFather(findSql.getId(),question.getId(),1);
            }
        }*/
        return "redirect:/question/read/" + qid;
    }
    @LoginRequired
    @RequestMapping(path = "/edit/{qid}",method = RequestMethod.GET)
    public String getEdit(Model model,@PathVariable("qid") String qid){
        User user = hostHolder.getUser();
        Question question = questionService.SelectByQid(qid);
        if (question.getAuthorId()==user.getId()||user.getType()==1){
            model.addAttribute("question",question);
            List<Classification> classifications = classificationService.AllClassifications();
            model.addAttribute("categoryList",classifications);
            if (question.getStatus()==1 && user.getType()==1){
                model.addAttribute("checkLabel",2);
            }
            return "question/edit";
        }else {
            return "error/404";
        }
    }
    @LoginRequired
    @RequestMapping(value = "/read/{qid}",method = RequestMethod.GET)
    public String read(Model model,@PathVariable("qid") String qid ,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int limit){
        Question question = questionService.SelectByQid(qid);
        User user = hostHolder.getUser();
        if (user!=null){
            if (question.getStatus()==1 && user.getType()==1){
                model.addAttribute("checkLabel",2);
            }
            if (user.getId()!=question.getAuthorId()){
                question.setViews(question.getViews()+1);
                questionService.UpdateViews(question.getId(),question.getViews() );
                questionRepository.save(question);
            }
        }
        else {
            question.setViews(question.getViews()+1);
            questionService.UpdateViews(question.getId(),question.getViews());
            questionRepository.save(question);
        }

        //List<Comment> questionComments = commentService.selectcommentByEntity(2,question.getId(),0);//status=评论，entitytype=论文
        PageHelper.startPage(page,limit);
        PageInfo<Comment> questionComments = new PageInfo<>(commentService.selectcommentByEntity(3,question.getId(),0));
        if (page==1 && question.getSort()==1){
            Comment comment = commentService.SelectByTableAndEntity(3,question.getId(),1);
            if (comment!=null){
                List<Comment> c1 = questionComments.getList();
                if (c1.contains(comment)){
                    c1.remove(comment);

                }
                comment.setTable(1);
                c1.add(0,comment);
                questionComments.setList(c1);
                System.out.println(c1);
            }
        }
        //List<Comment> questionComments = commentService.selectByEntityAndPage(3,question.getId(),0,page.getCurrent()-1,page.getLimit());
        List<Map<String,Object>> coms = new ArrayList<>();
        if (questionComments.getList().size()>0) {
            for (Comment questionComment : questionComments.getList()) {
                Map<String,Object> map = new HashMap<>();
                map.put("comment",questionComment);
                User user1 = userService.findUserById(questionComment.getUserid());
                Map<String,Object> user11 = new HashMap<>();
                user11.put("id",user1.getId());
                user11.put("username",user1.getUsername());
                map.put("user",user11);
                List<Comment> commentComments =commentService.selectcommentByEntity(1,questionComment.getId(),0);//status=评论，entitytype=评论
                List<Map<String,Object>> ccs = new ArrayList<>();
                if (commentComments!=null){

                    for (Comment commentComment:commentComments){
                        Map<String,Object> cc = new HashMap<>();
                        cc.put("reply",commentComment);
                        User user2 = userService.findUserById(commentComment.getUserid());
                        Map<String,Object> user22 = new HashMap<>();
                        user22.put("id",user2.getId());
                        user22.put("username",user2.getUsername());
                        cc.put("user",user22);
                        Map<String,Object> target = null;
                        if (commentComment.getTargetid()!=0){
                            target = new HashMap<>();
                            User user33 = userService.findUserById(commentComment.getTargetid());
                            target.put("username",user33.getUsername());
                            target.put("id",user33.getId());
                        }
                        //target = commentComment.getTargetid() == 0 ? null:userService.findUserById(commentComment.getTargetid());
                        cc.put("target",target);
                        ccs.add(cc);
                    }

                }
                map.put("replys",ccs);
                int replyCount = commentService.findCommentCount(1, questionComment.getId());
                map.put("replyCount", replyCount);
                coms.add(map);
            }
        }
        //置顶用户确认的回答
        model.addAttribute("info",questionComments);
        model.addAttribute("comments",coms);
        model.addAttribute("question",question);
        return "question/read";
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

    //返回图片，参考url：https://question.csdn.net/meiqi0538/article/details/79862213/?utm_term=java%E8%BF%94%E5%9B%9E%E5%9B%BE%E7%89%87%E7%BB%99%E5%89%8D%E7%AB%AF&utm_medium=distribute.pc_aggpage_search_result.none-task-question-2~all~sobaiduweb~default-1-79862213&spm=3001.4430
    /*@LoginRequired
    @RequestMapping(value = "/UsingTheComplexLinkGetThePicForPicManage/{picname}",method = RequestMethod.GET)
    public void returnPic(HttpServletResponse response,@PathVariable("picname") String picname){
        String filePath = uploadPicPath + "/"+picname;
        //创建一个文件对象，对应的文件就是python把词云图片生成后的路径以及对应的文件名
        File file = new File(filePath);
        //使用字节流读取本地图片
        *//*ServletOutputStream out=null;
        BufferedInputStream buf=null;*//*
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
*//*        File file1 = new File("E:\\Java\\eclipse_code\\NLP\\WebContent\\source\\wordcloud.png");
        file1.delete();
        System.out.println("文件删除！");*//*

    }*/
    @LoginRequired
    @RequestMapping(path = "/delete/{qid}" ,method = RequestMethod.GET)
    public String delete(@PathVariable("qid") String qid){
        Question question = questionService.SelectByQid(qid);
        User user = hostHolder.getUser();
        if (question==null){
            return "error/404";
        }
        if (question.getAuthorId() == user.getId() || user.getType() == 1){
            questionService.DeleteQuestionById(question.getId());
            questionRepository.deleteById(question.getId());
            List<Comment> delectComents = commentService.selectcommentByEntity(3,question.getId(),0);
            if (delectComents!=null){
                for (Comment dc:delectComents){
                    commentService.DeleteByEntity(dc.getId(),1);
                    commentService.DeleteById(dc.getId());
                }
            }
            List<Picture> pictures = pictureService.SelectByFather(question.getId(),2);
            if (pictures!=null){
                for (Picture pic:pictures){
                    String path = uploadPicPath+"/"+pic.getSaveName();
                    File file = new File(path);
                    if (!file.isDirectory()){
                        file.delete();
                    }
                }
                pictureService.DeleteByFather(question.getId(),2);
            }
            if (question.getStatus()==1 && user.getType()==1){
                return "redirect:/user/approval/2";
            }
            else {
                return "redirect:/question/list";
            }
        }
        else {
            return "error/404";
        }
    }
    @LoginRequired
    @PostMapping(path = "/pass")
    @ResponseBody
    public String pass(String qid) {
        System.out.println(qid);
        User user = hostHolder.getUser();
        Question question = questionService.SelectByQid(qid);
        if (user.getType()==1){
            question.setStatus(0);
            questionService.UpdateStatus(question.getId(),question.getStatus());
            questionRepository.save(question);
            return CommunityUtil.getJSONString(0);
        }
        else {
            return CommunityUtil.getJSONString(1,"没有权限！");
        }


    }














}
