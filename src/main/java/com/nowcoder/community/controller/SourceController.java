package com.nowcoder.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.annotation.AdminRequired;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.dao.elasticsearth.PaperRepository;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Controller
public class SourceController {
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private PaperOfClassService paperOfClassService;
    @Autowired
    private UserService userService;
    @Autowired
    private ElasticsearchClassService elasticsearchClassService;//基于ES搜索引擎的查询service
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/source/upload",method = RequestMethod.POST)
    public String uploadSource(MultipartFile paperfile, Paper paper, Model model){
        User user = hostHolder.getUser();
        if (paperfile == null){


        }
        String fileName = paperfile.getOriginalFilename();
        /*获取前缀存入数据库*/
        String title = fileName.substring(0,fileName.lastIndexOf("."));
        /*截取后缀*/
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String newName = CommunityUtil.generateUUID() +suffix;
        newName = System.currentTimeMillis()+"_"+newName;
        String path = uploadPath;
        //判断后缀是否为空
        if (StringUtils.isBlank(suffix)) {


            /*model.addAttribute("error", "文件的格式不正确!");
            if (user.getType()==1){
                return "/site/adminmanage";
            }else {
                return "/site/usermanage";
            }*/
        }
        /*if (paper.getContent().length()> 250){
            ////????
        }*/
        File targetFile = new File(path,newName);
        if(!targetFile.exists()){
            targetFile.getParentFile().mkdirs();
        }
        try{
            paperfile.transferTo(targetFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        paper.setTitle(title);
        String filepath = domain+"/user/file/"+newName;
        paper.setFilepath(filepath);
        paper.setDownloadcount(0);
        Date dayy = new Date();
        paper.setGmtcreate(dayy);
        paper.setUserid(user.getId());
        if (user.getType() == 1){
            paper.setStatus(2);
        }
        else {
            paper.setStatus(3);
        }
        paperOfClassService.uploadpaper(paper);
        paperRepository.save(paper);
        return "redirect:/source/detail?id="+paper.getId();
    }




    //******************以上为usercontroller中paper操作改过来
    @LoginRequired
    @RequestMapping(path = "/source/write",method = RequestMethod.GET)
    public String uploadSource(Model model){
        User user = hostHolder.getUser();
        List<Classification> classificationList = classificationService.AllClassifications();
        model.addAttribute("classificationList",classificationList);
        model.addAttribute("user",user);
        return "source/write";
    }
    @LoginRequired
    @RequestMapping(path = "/source/edit/{id}",method = RequestMethod.GET)
    public String editSource(Model model,@PathVariable("id") int id){
        User user = hostHolder.getUser();
        Paper paper = paperOfClassService.selectPaperById(id);
        if (paper.getUserid()==user.getId()||user.getType()==1){
            /*List<Map<String,Object>> classify = new ArrayList<>();*/
            String[] allfather={"logic","compute","number","algebra","geometry","topology","analysis","ODE","PDE","dynamical","functional","probability","statistics","opsearch","combinatorial","fuzzy","quantum","applied"};
            String[] fathers =paper.getFatherid().split(",");
            List<String> otherfathers = new ArrayList<>();
            for (String str:allfather){
                otherfathers.add(str);
            }
            for (String str:fathers){
                if (otherfathers.contains(str)){
                    otherfathers.remove(str);
                }
            }
            List<Map<String,Object>> fatherss = new ArrayList<>();
            for (String str:fathers){
                Map<String,Object> map1 = new HashMap<>();
                map1.put("searchname",str);
                System.out.println(str);
                map1.put("name",classificationService.GetNameBySearchname(str).getName());
                fatherss.add(map1);
            }
            List<Map<String,Object>> otherfatherss = new ArrayList<>();
            for (String str:otherfathers){
                Map<String,Object> map2 = new HashMap<>();
                map2.put("searchname",str);
                map2.put("name",classificationService.GetNameBySearchname(str).getName());
                otherfatherss.add(map2);
            }
            paper.setTitle(paper.getTitle()+paper.getFilepath().substring(paper.getFilepath().lastIndexOf(".")));
            if (paper.getStatus()==3 && user.getType()==1){
                model.addAttribute("checkLabel",4);
            }
            model.addAttribute("fathers",fatherss);
            model.addAttribute("otherfathers",otherfatherss);
            model.addAttribute("paper",paper);
            return "source/edit";
        }
        else {
            return "error/404";
        }
    }
    @AdminRequired
    @RequestMapping(path = "/source/edit",method = RequestMethod.POST)
    public String postEditSource(Paper paper,Model model){
        Paper paper1 = paperOfClassService.selectPaperById(paper.getId());
        paper1.setContent(paper.getContent());
        paper1.setFatherid(paper.getFatherid());
        paperOfClassService.updatastatus(paper1);
        paperRepository.save(paper1);
        return "redirect:/source/detail?id="+paper.getId();
    }
    @LoginRequired
    @RequestMapping(path = "/source/delete/{id}",method = RequestMethod.GET)
    public String deleteSource(@PathVariable("id") int id){
        Paper paper = paperOfClassService.selectPaperById(id);
        User user = hostHolder.getUser();
        if (paper == null){
            return "error/404";
        }
        if (paper.getUserid() == user.getId() || user.getType() == 1){
            paperOfClassService.delectPaperById(paper.getId());
            elasticsearchClassService.deletepaper(paper.getId());
            List<Comment> delectComents = commentService.selectcommentByEntity(4,paper.getId(),0);
            if (delectComents!=null){
                for (Comment dc:delectComents){
                    commentService.DeleteByEntity(dc.getId(),1);
                    commentService.DeleteById(dc.getId());
                }
            }
            String fileName = paper.getFilepath().split("http://localhost:8080/user/file/")[1];
            if (fileName!=null){
                String path = uploadPath+"/"+fileName;
                File file = new File(path);
                if (!file.isDirectory()){
                    file.delete();
                }
            }
            if (paper.getStatus()==3 && user.getType()==1){
                return "redirect:/user/approval/4";
            }
            else {
                return "redirect:/source/list";
            }
        }
        else {
            return "error/404";
        }
    }
    @RequestMapping(path = "/source/list",method = RequestMethod.GET)
    public String sourceList(Model model,
                            @RequestParam(defaultValue = "a") String category,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<Paper> info ;
        if (category.length()==1){
            info = new PageInfo<>(paperOfClassService.selectPaperOnlyByStatus(2));
        }
        else {
            List<Paper> papers = elasticsearchClassService.searchPaperByClassAndStatus(category,2);
            info = new PageInfo<>(papers);
            //System.out.println(papers.size());
            int numOfPage = papers.size()/limit;
            List<Paper> showPapers = new ArrayList<>();
            if (papers.size()%limit!=0){
                numOfPage++;
                if (papers.size()/10+1>page){
                    showPapers = papers.subList((page-1)*limit,(page)*limit);
                }
                else {
                    showPapers = papers.subList((page-1)*limit,papers.size());
                }
            }
            else {
                if (papers.size()==0){

                    showPapers = null;
                }
                else {
                    showPapers = papers.subList((page-1)*limit,(page)*limit);
                }
            }
            if (numOfPage ==0){
                paperOfClassService.selectPaperOnlyByStatus(2);
                //PageHelper,第一个查询自动添加limit和page
                numOfPage++;
            }
            /*papers=papers.subList(1,4); //包含1不包含4*/
            info.setList(showPapers);
            int num[]= new int[numOfPage];
            for (int i=0;i<numOfPage;i++){
                num[i] = i+1;
            }
            info.setNavigatepageNums(num);
            if (page==1){
                info.setHasPreviousPage(false);
            }
            else {
                info.setHasPreviousPage(true);
            }
            if (page == numOfPage){
                info.setHasNextPage(false);
            }
            else {
                info.setHasNextPage(true);
            }
            info.setPrePage(page-1);
            info.setNextPage(page+1);
            info.setPageNum(page);
            model.addAttribute("thisCategoryName",classificationService.GetNameBySearchname(category).getName());
        }
        List<Paper> newPage = new ArrayList<>();
        if (info.getList()!=null){
            for (Paper p:info.getList()){
                User user=userService.findUserById(p.getUserid());
                if (user==null){
                    //paperOfClassService.selectPaperOnlyByStatus(0);
                    user=userService.findUserById(p.getUserid());
                }
                p.setFilepath(user.getUsername());
                //p.setFilepath(commentService.SelectUsernameById(p.getUserid()));
                newPage.add(p);
            }
        }

        info.setList(newPage);
        List<Classification> classifications = classificationService.AllClassifications();
        model.addAttribute("category",category);
        model.addAttribute("info", info);
        model.addAttribute("categoryList",classifications);
        //model.addAttribute("checkLabel",0);
        return "source/list";
    }
    //******************下面少了responseBody就报JSONString回显的错
    @AdminRequired
    @RequestMapping(path = "/source/pass",method = RequestMethod.POST)
    @ResponseBody
    public String passSource(int pid){
        Paper paper = paperOfClassService.selectPaperById(pid);
        paper.setStatus(2);
        paperOfClassService.updatastatus(paper);
        return CommunityUtil.getJSONString(0);
    }

    //第三级页面——论文详情页//******************已重构
    @RequestMapping(path = "/source/detail",method = RequestMethod.GET)
    public String SourceDetail(int id,Page page,Model model){
        Paper paper = paperOfClassService.selectPaperById(id);
        User user = hostHolder.getUser();
        if (user!=null){
            if (paper.getStatus()==3 && user.getType()==1){
                model.addAttribute("checkLabel",4);
            }
        }
        List<Comment> papercomments = commentService.selectcommentByEntity(4,id,0);//status=评论，entitytype=论文
        List<Map<String,Object>> coms = new ArrayList<>();
        /*if (papercomments!=null) {
            for (Comment papercomment : papercomments) {
                Map<String,Object> map = new HashMap<>();
                List<Comment> commentcomments =commentService.selectcommentByEntity(1,papercomment.getId(),0);//status=评论，entitytype=评论
                if (commentcomments!=null){
                    List<Map<String,Object>> ccs = new ArrayList<>();
                    for (Comment commentcomment:commentcomments){
                        Map<String,Object> cc = new HashMap<>();
                        if (commentcomment.getTargetid()!=0){       *//*上传评论时，如果没有targetid数据库默认置0——正常情况应该不会发生这种事情*//*
                            cc.put("targetname",userService.findUserById(commentcomment.getTargetid()).getUsername());//由targetid拿到其targetname
                        }
                        else {
                            cc.put("targetname",null);
                        }
                        cc.put("id",commentcomment.getId());
                        cc.put("userid",commentcomment.getUserid());
                        cc.put("type",commentcomment.getType());//论文详情页评论区，只需要评论，这里把评论的类型（type = 0:主评论，1：次评论）
                        cc.put("username",userService.findUserById(commentcomment.getUserid()).getUsername());
                        cc.put("content",":"+commentcomment.getContent());
                        cc.put("createtime",commentcomment.getCreatetime());
                        ccs.add(cc);
                    }
                    map.put("commentcomments",ccs);
                }
                else {
                    map.put("commentcomments",null);                *//*是否需要*//*
                }
                map.put("id",papercomment.getId());
                map.put("username",userService.findUserById(papercomment.getUserid()).getUsername());
                map.put("userid",papercomment.getUserid());
                map.put("content",papercomment.getContent());
                map.put("createtime",papercomment.getCreatetime());
                coms.add(map);
            }
        }*/
        if (papercomments!=null) {
            for (Comment questionComment : papercomments) {
                Map<String,Object> map = new HashMap<>();
                map.put("comment",questionComment);
                map.put("user",userService.findUserById(questionComment.getUserid()));
                List<Comment> commentComments =commentService.selectcommentByEntity(1,questionComment.getId(),0);//status=评论，entitytype=评论
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
                int replyCount = commentService.findCommentCount(1, questionComment.getId());
                map.put("replyCount", replyCount);
                /*else {
                    map.put("replys",null);                *//*是否需要*//*
                }*/
                /*map.put("id",questionComment.getId());
                map.put("username",userService.findUserById(questionComment.getUserid()).getUsername());
                map.put("userid",questionComment.getUserid());
                map.put("content",questionComment.getContent());
                map.put("createtime",questionComment.getCreatetime());*/
                coms.add(map);
            }
        }
        String[] fatherids = paper.getFatherid().split(",");
        List<String> fathernames = new ArrayList<>();
        for (String fatherid:fatherids){
            fathernames.add(classificationService.GetNameBySearchname(fatherid).getName());
        }
        if (paper.getStatus()== 2){
            paper.setTitle(paper.getTitle()+paper.getFilepath().substring(paper.getFilepath().lastIndexOf('.')));
        }

        model.addAttribute("userid",paper.getUserid());
        model.addAttribute("username",userService.findUserById(paper.getUserid()).getUsername());
        model.addAttribute("paper",paper);
        model.addAttribute("fathernames",fathernames);
        model.addAttribute("comments",coms);
        return "/source/read";
    }

}
