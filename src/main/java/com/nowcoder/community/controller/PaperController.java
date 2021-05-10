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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.File;
import java.util.*;


@Controller
public class PaperController {
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
    private PaperOfClassService paperOfClassService;//一个可能并没有什么用的【待去掉】
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private ElasticsearchClassService elasticsearchClassService;//基于ES搜索引擎的查询service
    @Autowired
    private ElasticsearchService elasticsearchService;

    @LoginRequired
    @RequestMapping(path = "/paper/search",method = RequestMethod.GET)
    public String searchpaper(String keyword,String fieldname,String sortname,Page page,Model model){
        if (sortname==null){
            sortname="downloadcount";
        }
        org.springframework.data.domain.Page<Paper> searchPaper =elasticsearchService.searchPaperByFieldname(keyword,0,fieldname,sortname,page.getCurrent() - 1,page.getLimit());
        List<Map<String ,Object>> sources = new ArrayList<>();
        if (searchPaper !=null){
            for (Paper paper:searchPaper){/*针对论文的搜索，是否需要写针对悬赏的搜索*/
                paper.setFatherid((userService.findUserById(paper.getUserid())).getUsername());
            }
        }
        model.addAttribute("blogs",searchPaper);
        model.addAttribute("fieldname" ,fieldname);
        model.addAttribute("label","paper");
        page.setPath("/paper/search?keyword="+keyword+"&fieldname="+fieldname+"&sortname"+sortname);
        page.setRows(searchPaper == null?0:searchPaper.getTotalPages());
        return "search/list";
    }
    @LoginRequired
    @RequestMapping(path = "/paper/upload",method = RequestMethod.POST)
    public String uploadPaper(MultipartFile paperfile,Paper paper,Model model){
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
            paper.setStatus(0);
        }
        else {
            paper.setStatus(1);
        }
        paperOfClassService.uploadpaper(paper);
        paperRepository.save(paper);
        return "redirect:/detail?id="+paper.getId();
    }

    @LoginRequired
    @RequestMapping(path = "/paper/write",method = RequestMethod.GET)
    public String uploadPaper(Model model){
        User user = hostHolder.getUser();
        List<Classification> classificationList = classificationService.AllClassifications();
        model.addAttribute("classificationList",classificationList);
        model.addAttribute("user",user);
        return "paper/write";
    }
    @LoginRequired
    @RequestMapping(path = "/paper/edit/{id}",method = RequestMethod.GET)
    public String editPaper(Model model,@PathVariable("id") int id){
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
                if (paper.getStatus()==1 && user.getType()==1){
                    model.addAttribute("checkLabel",3);
                }
                model.addAttribute("fathers",fatherss);
                model.addAttribute("otherfathers",otherfatherss);
                model.addAttribute("paper",paper);
                return "paper/edit";
            }
            else {
                return "error/404";
            }
    }
    @AdminRequired
    @RequestMapping(path = "/paper/edit",method = RequestMethod.POST)
    public String postEditPaper(Paper paper,Model model){
        Paper paper1 = paperOfClassService.selectPaperById(paper.getId());
        paper1.setContent(paper.getContent());
        paper1.setFatherid(paper.getFatherid());
        paperOfClassService.updatastatus(paper1);
        paperRepository.save(paper1);
        return "redirect:/detail?id="+paper.getId();
    }
    @LoginRequired
    @RequestMapping(path = "/paper/delete/{id}",method = RequestMethod.GET)
    public String deletePaper(@PathVariable("id") int id){
        Paper paper = paperOfClassService.selectPaperById(id);
        User user = hostHolder.getUser();
        if (paper == null){
            return "error/404";
        }
        if (paper.getUserid() == user.getId() || user.getType() == 1){
            paperOfClassService.delectPaperById(paper.getId());
            elasticsearchClassService.deletepaper(paper.getId());
            List<Comment> delectComents = commentService.selectcommentByEntity(0,paper.getId(),0);
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
            if (paper.getStatus()==1 && user.getType()==1){
                return "redirect:/user/approval/3";
            }
            else {
                return "redirect:/paper/list";
            }
        }
        else {
            return "error/404";
        }
    }
    @LoginRequired
    @RequestMapping(path = "/paper/list",method = RequestMethod.GET)
    public String paperList(Model model,
                            @RequestParam(defaultValue = "a") String category,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<Paper> info ;
        if (category.length()==1){
            info = new PageInfo<>(paperOfClassService.selectPaperOnlyByStatus(0));
        }
        else {
            List<Paper> papers = elasticsearchClassService.searchPaperByClassAndStatus(category,0);
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
                paperOfClassService.selectPaperOnlyByStatus(0);
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
                    user=userService.findUserById(p.getUserid());
                }
                p.setFilepath(user.getUsername());
                newPage.add(p);
            }
        }
        info.setList(newPage);
        List<Classification> classifications = classificationService.AllClassifications();
        model.addAttribute("category",category);
        model.addAttribute("info", info);
        model.addAttribute("categoryList",classifications);
        //model.addAttribute("checkLabel",0);
        return "paper/list";
    }
    //******************下面少了responseBody就报JSONString回显的错
    @AdminRequired
    @RequestMapping(path = "/paper/pass",method = RequestMethod.POST)
    @ResponseBody
    public String passPaper(int pid){
        Paper paper = paperOfClassService.selectPaperById(pid);
        paper.setStatus(0);
        paperOfClassService.updatastatus(paper);
        paperRepository.save(paper);
        return CommunityUtil.getJSONString(0);
    }

    @LoginRequired
    @RequestMapping(path = "/detail",method = RequestMethod.GET)
    public String PaperDetail(int id,Model model,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int limit){
        Paper paper = paperOfClassService.selectPaperById(id);
        User user = hostHolder.getUser();
        if (user!=null){
            if (paper.getStatus()==1 && user.getType()==1){
                model.addAttribute("checkLabel",3);
            }
        }
        PageHelper.startPage(page,limit);
        PageInfo<Comment> papercomments = new PageInfo<>(commentService.selectcommentByEntity(0,id,0));
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
        if (paper.getStatus()== 0){
            paper.setTitle(paper.getTitle()+paper.getFilepath().substring(paper.getFilepath().lastIndexOf('.')));
        }
        model.addAttribute("info",papercomments);
        model.addAttribute("userid",paper.getUserid());
        model.addAttribute("username",userService.findUserById(paper.getUserid()).getUsername());
        model.addAttribute("paper",paper);
        model.addAttribute("fathernames",fathernames);
        model.addAttribute("comments",coms);
        return "/paper/read";
    }
}
