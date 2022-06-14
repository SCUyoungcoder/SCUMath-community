package com.nowcoder.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.annotation.AdminRequired;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private PaperOfClassService paperOfClassService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private BlogAbleService blogAbleService;

    @AdminRequired
    @RequestMapping(path = "/comment/notice",method = RequestMethod.GET)
    public String getNoticePage(Model model){
        model.addAttribute("checkLabel",5);
        return "comment/write";
    }
    @AdminRequired
    @RequestMapping(path = "/comment/notice",method = RequestMethod.POST)
    public String postNoticePage(Model model,String title,String content){
        User user = hostHolder.getUser();
        Paper notice = new Paper();
        notice.setGmtcreate(new Date());
        notice.setTitle(title);
        notice.setContent(content);
        notice.setUserid(user.getId());
        notice.setStatus(5);
        notice.setDownloadcount(0);
        notice.setFatherid("notice");
        notice.setFilepath("notice");
        paperOfClassService.uploadpaper(notice);
        return "redirect:/index";
    }
    @LoginRequired
    @RequestMapping(path = "/comment/blogApply",method = RequestMethod.GET)
    public String getAllBlogApply(Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int limit){
        User user = hostHolder.getUser();
        PageHelper.startPage(page,limit);
        PageInfo<BlogAble> info = new PageInfo<>(blogAbleService.selectBlogAbleByUserIdAndType(user.getId(),0));
        List<Map<String ,Object>> mapList = new ArrayList<>();
        for (BlogAble blogAble:info.getList()){
            Map<String ,Object> map = new HashMap<>();
            User user1 = userService.findUserById(blogAble.getEntityId());
            Blog blog = blogService.SelectById(blogAble.getBlogId());
            map.put("userName",user1.getUsername());
            map.put("userId",user1.getId());
            map.put("blogAbleId",blogAble.getId());
            map.put("title",blog.getTitle());
            map.put("url","/blog/read/"+blog.getBid());
            map.put("category","在博客");
            mapList.add(map);
        }
        model.addAttribute("blogAbleLabel",1);
        model.addAttribute("info",info);
        model.addAttribute("mapList",mapList);
        return "comment/list";
    }
    @LoginRequired
    @RequestMapping(path = "/comment/list",method = RequestMethod.GET)
    public String getAllCommentForMe(Model model,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int limit){
        User user = hostHolder.getUser();
        PageHelper.startPage(page,limit);
        PageInfo<Comment> info = new PageInfo<>(commentService.SelectByTwoTarget(user.getId()));
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (Comment comment:info.getList()){
            Map<String,Object> map = new HashMap<>();
            User user1 = userService.findUserById(comment.getUserid());
            map.put("userName",user1.getUsername());
            map.put("userId",user1.getId());
            map.put("content",comment.getContent());
            map.put("time",comment.getCreatetime());
            int entityId = comment.getEntityid();
            switch (comment.getEntitytype()){
                case 0:
                    map.put("url","/detail?id="+entityId);
                    map.put("title",paperOfClassService.selectPaperById(entityId).getTitle());
                    map.put("category","在论文");
                    break;
                case 2:
                    Blog blog = blogService.SelectById(entityId);
                    map.put("url","/blog/read/"+blog.getBid());
                    map.put("title",blog.getTitle());
                    map.put("category","在博客");
                    break;
                case 3:
                    Question question = questionService.SelectById(entityId);
                    map.put("url","/question/read/"+question.getQid());
                    map.put("title",question.getTitle());
                    map.put("category","在问答");
                    break;
                case 4:
                    map.put("url","/source/detail?id="+entityId);
                    map.put("title",paperOfClassService.selectPaperById(entityId).getTitle());
                    //map.put("title",sourceService.selectSourceById(entityId).getTitle());
                    map.put("category","在资源");
                    break;
                case 1:
                    Comment comment1 = commentService.selectcommentById(entityId);
                    entityId = comment1.getEntityid();
                    switch (comment1.getEntitytype()){
                        case 0:
                            map.put("url","/detail?id="+entityId);
                            map.put("title",paperOfClassService.selectPaperById(entityId).getTitle());
                            map.put("category","在论文");
                            break;
                        case 2:
                            Blog blog1 = blogService.SelectById(entityId);
                            map.put("url","/blog/read/"+blog1.getBid());
                            map.put("title",blog1.getTitle());
                            map.put("category","在博客");
                            break;
                        case 3:
                            Question question1 = questionService.SelectById(entityId);
                            map.put("url","/question/read/"+question1.getQid());
                            map.put("title",question1.getTitle());
                            map.put("category","在问答");
                            break;
                        case 4:
                            map.put("url","/source/detail?id="+entityId);
                            map.put("title",paperOfClassService.selectPaperById(entityId).getTitle());
                            //map.put("title",sourceService.selectSourceById(entityId).getTitle());
                            map.put("category","在资源");
                            break;
                    }
                    break;
            }
            mapList.add(map);
        }
        model.addAttribute("blogAbleLabel",0);
        model.addAttribute("info",info);
        model.addAttribute("mapList",mapList);
        return "comment/list";
    }
    @LoginRequired
    @RequestMapping(path = "/comment/blog/{bid}",method = RequestMethod.POST)
    public String addBlogComment(@PathVariable("bid") String bid, Comment comment) {
        comment.setUserid(hostHolder.getUser().getId());
        //1论文 2问答 3博客
        if (comment.getEntitytype()==2){
            comment.setTargetid2(blogService.SelectByBid(bid).getAuthorId());
        }else {
            comment.setTargetid2(commentService.selectcommentById(comment.getEntityid()).getUserid());
        }
        comment.setStatus(0);
        comment.setCreatetime(new Date());
        commentService.addComment(comment);
        return "redirect:/blog/read/" + bid;
    }
    @LoginRequired
    @RequestMapping(path = "/comment/question/{qid}",method = RequestMethod.POST)
    public String addQuestionComment(@PathVariable("qid") String qid, Comment comment) {
        comment.setUserid(hostHolder.getUser().getId());
        //1论文 2问答 3博客
        if (comment.getEntitytype()==3){
            comment.setTargetid2(questionService.SelectByQid(qid).getAuthorId());
        }else {
            comment.setTargetid2(commentService.selectcommentById(comment.getEntityid()).getUserid());
        }
        comment.setStatus(0);
        comment.setCreatetime(new Date());
        commentService.addComment(comment);
        return "redirect:/question/read/" + qid;
    }

    @LoginRequired
    @RequestMapping(path = "/add/{paperid}", method = RequestMethod.POST)
    public String addDiscussPost(@PathVariable("paperid") int paperid, Comment comment) {
        User user = hostHolder.getUser();
        comment.setUserid(user.getId());
        comment.setCreatetime(new Date());
        if (comment.getEntitytype()==0){
            comment.setTargetid2(paperOfClassService.selectPaperById(paperid).getUserid());
        }else {
            comment.setTargetid2(commentService.selectcommentById(comment.getEntityid()).getUserid());
        }
        commentService.addComment(comment);
        return "redirect:/detail?id="+paperid;
    }
    @LoginRequired
    @RequestMapping(path = "/source/add/{paperid}", method = RequestMethod.POST)
    public String sourceAddDiscussPost(@PathVariable("paperid") int paperid, Comment comment) {
        User user = hostHolder.getUser();
        comment.setUserid(user.getId());
        comment.setCreatetime(new Date());
        if (comment.getEntitytype()==4){
            comment.setTargetid2(paperOfClassService.selectPaperById(paperid).getUserid());
        }else {
            comment.setTargetid2(commentService.selectcommentById(comment.getEntityid()).getUserid());
        }
        commentService.addComment(comment);
        return "redirect:/source/detail?id="+paperid;
    }
}
