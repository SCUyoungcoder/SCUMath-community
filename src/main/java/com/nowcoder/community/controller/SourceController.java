package com.nowcoder.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SourceController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ElasticsearchClassService elasticsearchClassService;//基于ES搜索引擎的查询service


   /* @RequestMapping(path = "/source/list",method = RequestMethod.GET)
    public String sourceList(Model model,
                            @RequestParam(defaultValue = "a") String category,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int limit){
        PageHelper.startPage(page,limit);
        PageInfo<Source> info ;
        if (category.length()==1){
            info = new PageInfo<>(sourceService.selectSourceOnlyByStatus(0));
        }
        else {
            List<Source> papers = elasticsearchClassService.searchPaperOnlyByClass(category);
            //es暂未复制
            info = new PageInfo<>(papers);
            System.out.println(papers.size());
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
            *//*papers=papers.subList(1,4); //包含1不包含4*//*
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

        }
        List<Paper> newPage = new ArrayList<>();
        System.out.println(info.getList());
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
        System.out.println(classifications);
        model.addAttribute("category",category);
        model.addAttribute("info", info);
        model.addAttribute("categoryList",classifications);
        return "paper/list";
    }


    //第三级页面——论文详情页
    @RequestMapping(path = "/detail",method = RequestMethod.GET)
    public String PaperDerail(int id, Page page, Model model){
        Paper paper = paperOfClassService.selectPaperById(id);
        List<Comment> papercomments = commentService.selectcommentByEntity(0,id,0);//status=评论，entitytype=论文
        List<Map<String,Object>> coms = new ArrayList<>();
        if (papercomments!=null) {
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
        }
        String[] fatherids = paper.getFatherid().split(",");
        List<String> fathernames = new ArrayList<>();
        for (String fatherid:fatherids){
            fathernames.add(classificationService.GetNameBySearchname(fatherid).getName());
        }
        if (paper.getStatus()== 0){
            paper.setTitle(paper.getTitle()+paper.getFilepath().substring(paper.getFilepath().lastIndexOf('.')));
        }
        model.addAttribute("username",userService.findUserById(paper.getUserid()).getUsername());
        model.addAttribute("paper",paper);
        model.addAttribute("fathernames",fathernames);
        model.addAttribute("comments",coms);
        return "/level3";
    }*/
}
