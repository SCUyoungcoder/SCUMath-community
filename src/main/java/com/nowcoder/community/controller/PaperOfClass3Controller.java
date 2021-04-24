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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import java.io.File;
import java.util.*;


@Controller
public class PaperOfClass3Controller {
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

//第二级 对应科目标签的论文展示页
    //【POST用法，现已弃用】需要第一级的前端表单<method="post" action="/paperofclass"> 传给此页面名为“classname”的参数【已弃用】
    //【POST用法，现已弃用】此方法下行要获取“科目标签”，只要直接声明参数，与表单名称（classname）一致，即可传过来【已弃用】

    //***************已无用
    //paperofclass?classname=XXX  6.6课选用GET方式传参，参数通过
    @RequestMapping(path = "/paperofclass", method = RequestMethod.GET)
    public String searchpaperofclass(String classname,Model model,Page page) {//此处前端的classname参数的形式是String
        //搜索论文(6.6课22：56开始介绍查询的controller)
        org.springframework.data.domain.Page<Paper> searchResult =
                elasticsearchClassService.searchPaperByClass(classname,page.getCurrent() - 1 ,page.getLimit());
        //聚合数据——上面操作得到的只是Paper实体类，里面包含的信息需要查出来，处理一下聚合起来
        List<Map<String,Object>> papers = new ArrayList<>();//声明聚合的结果——一个集合，里面封装的Map，命名为papers，这是我最终实例化的结果
        if(searchResult != null){
            for (Paper paper : searchResult){//遍历，每次都会得到一个paper（论文）
                //if( paper.getStatus()==0){
                    Map<String,Object> map =  new HashMap<>();//每次实例化一个Map,封装聚合的数据
                    /*显示用户名*/
                    paper.setFatherid(userService.findUserById(paper.getUserid()).getUsername());
                    map.put("paper",paper);
                    //如果还有啥需要往里放的还可以添加，写好相应的service，然后继续map.put()即可
                    papers.add(map);//得到聚合数据map后，装进集合里
                //}
                /*备注：Map<String,Object> map这一行放出去会出错，每次必须重新定义map，否则得到n条重复数据*/
            }
        }
        model.addAttribute("papers",papers);//得到的最终数据要传给模板/页面
        model.addAttribute("classname",classificationService.GetNameBySearchname(classname).getName());//希望返回的页面上也带上刚刚的classname参数信息

        //分页信息
        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());//多少数据？多少页数？从searchResult里取
        page.setPath("/paperofclass?classname=" + classname);//路径

        return "/paperofclass";//返回第二级网页
    }
    @LoginRequired
    @RequestMapping(path = "/paper/upload",method = RequestMethod.POST)
    public String uploadPaper(MultipartFile paperfile,Paper paper,Model model){
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
                return "redirect:/source/list";
            }
        }
        else {
            return "error/404";
        }
    }
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
        return CommunityUtil.getJSONString(0);
    }

    //第三级页面——论文详情页//******************已重构
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

        //List<Comment> papercomments = commentService.selectcommentByEntity(0,id,0);//status=评论，entitytype=论文
        List<Map<String,Object>> coms = new ArrayList<>();
        if (papercomments.getList().size()>0) {
            for (Comment questionComment : papercomments.getList()) {
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
