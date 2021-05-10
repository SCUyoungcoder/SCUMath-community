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
    private ElasticsearchService elasticsearchService;
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
    @RequestMapping(path = "/source/search",method = RequestMethod.GET)
    public String searchSource(String keyword,String fieldname,String sortname,Page page,Model model){
        if (sortname==null){
            sortname="downloadcount";
        }
        org.springframework.data.domain.Page<Paper> searchSource =elasticsearchService.newSearchSourceByFieldname(keyword,2,6,8,fieldname,sortname,page.getCurrent() - 1,page.getLimit());
        List<Map<String ,Object>> sources = new ArrayList<>();
        if (searchSource !=null){
            for (Paper paper:searchSource){/*针对论文的搜索，是否需要写针对悬赏的搜索*/
                paper.setFatherid((userService.findUserById(paper.getUserid())).getUsername());
                switch (paper.getStatus()){
                    case 2:
                        paper.setFatherid("代码");
                        break;
                    case 6:
                        paper.setFatherid("软件");
                        break;
                    case 8:
                        paper.setFatherid("文献");
                        break;
                }
            }
        }
        model.addAttribute("blogs",searchSource);
        model.addAttribute("fieldname" ,fieldname);
        model.addAttribute("label","source");
        page.setPath("/source/search?keyword="+keyword+"&fieldname="+fieldname+"&sortname"+sortname);
        page.setRows(searchSource == null?0:searchSource.getTotalPages());
        return "search/list";
    }
    @LoginRequired
    @RequestMapping(path = "/source/upload",method = RequestMethod.POST)
    public String uploadSource(MultipartFile paperfile, Paper paper, Model model){
        User user = hostHolder.getUser();
        if (paperfile == null){
            return "error/5xx";
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
            return "error/5xx";
        }
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
            paper.setStatus(paper.getStatus()-1);
        }
        paperOfClassService.uploadpaper(paper);
        paperRepository.save(paper);
        return "redirect:/source/detail?id="+paper.getId();
    }

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
            String statusName =null;
            switch (paper.getStatus()){
                case 2:
                case 3:
                    statusName = "代码";
                    break;
                case 6:
                case 7:
                    statusName = "软件";
                    break;
                case 8:
                case 9:
                    statusName = "文献";
                    break;
            }
            model.addAttribute("statusName",statusName);
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
        if (paper1.getStatus()%2==1){
            paper.setStatus(paper.getStatus()+1);
        }
        paper1.setStatus(paper.getStatus());
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
    @LoginRequired
    @RequestMapping(path = "/source/list",method = RequestMethod.GET)
    public String sourceList(Model model,
                            @RequestParam(defaultValue = "0") int status,
                            @RequestParam(defaultValue = "a") String category,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<Paper> info ;
        if (status == 0){
            if (category.length()==1){
                //info = new PageInfo<>(paperOfClassService.selectPaperOnlyByStatus(2));
                info = new PageInfo<>(paperOfClassService.NewSelectByThreeStatus(2,6,8));
            }
            else {
                //List<Paper> papers = elasticsearchClassService.searchPaperByClassAndStatus(category,2);
                List<Paper> papers = elasticsearchClassService.NewSearchPaperByClassAndThreeStatus(category,2,6,8);
                info = new PageInfo<>(papers);
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
        }
        else {
            if (category.length()==1){
                info = new PageInfo<>(paperOfClassService.selectPaperOnlyByStatus(status));
            }
            else {
                List<Paper> papers = elasticsearchClassService.searchPaperByClassAndStatus(category,status);
                info = new PageInfo<>(papers);
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
            model.addAttribute("status",status);
            String thisStatusName = null;
            switch (status){
                case 2:
                    thisStatusName = "代码";
                    break;
                case 6:
                    thisStatusName = "软件";
                    break;
                case 8:
                    thisStatusName = "文献";
                    break;
            }
            model.addAttribute("thisStatusName",thisStatusName);
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
                switch (p.getStatus()){
                    case 2:
                        p.setFatherid("代码");
                        break;
                    case 6:
                        p.setFatherid("软件");
                        break;
                    case 8:
                        p.setFatherid("文献");
                        break;
                }
                newPage.add(p);
            }
        }

        info.setList(newPage);
        List<Classification> classifications = classificationService.AllClassifications();
        model.addAttribute("category",category);
        model.addAttribute("info", info);
        model.addAttribute("categoryList",classifications);
        return "source/list";
    }
    //******************下面少了responseBody就报JSONString回显的错
    @AdminRequired
    @RequestMapping(path = "/source/pass",method = RequestMethod.POST)
    @ResponseBody
    public String passSource(int pid){
        Paper paper = paperOfClassService.selectPaperById(pid);
        paper.setStatus(paper.getStatus()-1);
        paperOfClassService.updatastatus(paper);
        paperRepository.save(paper);
        return CommunityUtil.getJSONString(0);
    }

    //第三级页面——论文详情页//******************已重构
    @LoginRequired
    @RequestMapping(path = "/source/detail",method = RequestMethod.GET)
    public String SourceDetail(int id,Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int limit){
        Paper paper = paperOfClassService.selectPaperById(id);
        User user = hostHolder.getUser();
        if (user!=null){
            if (paper.getStatus()==3 && user.getType()==1){
                model.addAttribute("checkLabel",4);
            }
        }
        PageHelper.startPage(page,limit);
        PageInfo<Comment> papercomments = new PageInfo<>(commentService.selectcommentByEntity(4,id,0));
        //List<Comment> papercomments = commentService.selectcommentByEntity(4,id,0);//status=评论，entitytype=论文
        List<Map<String,Object>> coms = new ArrayList<>();
        if (papercomments.getList().size()>0) {
            for (Comment questionComment : papercomments.getList()) {
                Map<String,Object> map = new HashMap<>();
                map.put("comment",questionComment);
                User user1 = userService.findUserById(questionComment.getUserid());
                Map<String,Object> user11 = new HashMap<>();
                user11.put("id",user1.getId());
                user11.put("username",user1.getUsername());
                map.put("user",user11);
                //map.put("user",userService.findUserById(questionComment.getUserid()));
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
        String[] fatherids = paper.getFatherid().split(",");
        List<String> fathernames = new ArrayList<>();
        for (String fatherid:fatherids){
            fathernames.add(classificationService.GetNameBySearchname(fatherid).getName());
        }
        if (paper.getStatus()== 2){
            paper.setTitle(paper.getTitle()+paper.getFilepath().substring(paper.getFilepath().lastIndexOf('.')));
        }
        model.addAttribute("info",papercomments);
        model.addAttribute("userid",paper.getUserid());
        model.addAttribute("username",userService.findUserById(paper.getUserid()).getUsername());
        model.addAttribute("paper",paper);
        model.addAttribute("fathernames",fathernames);
        model.addAttribute("comments",coms);
        return "/source/read";
    }

}
