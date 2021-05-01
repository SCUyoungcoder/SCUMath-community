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
/*@RequestMapping("/comment")*/
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
    private SourceService sourceService;
    @Autowired
    private PaperOfClassService paperOfClassService;
    @Autowired
    private HostHolder hostHolder;

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
                    System.out.println(entityId);
                    entityId = comment1.getEntityid();
                    System.out.println(entityId);
                    System.out.println(comment1.getEntitytype());
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
        model.addAttribute("info",info);
        model.addAttribute("mapList",mapList);
        return "comment/list";
    }

    @LoginRequired
    @RequestMapping(path = "/comment/blog/{bid}")
    public String addBlogComment(@PathVariable("bid") String bid, Comment comment) {
        comment.setUserid(hostHolder.getUser().getId());
        //1论文 2问答 3博客
        if (comment.getEntitytype()==2){
            comment.setTargetid2(blogService.SelectByBid(bid).getAuthorId());
        }else {
            comment.setTargetid2(commentService.selectcommentById(comment.getEntityid()).getUserid());
        }
        comment.setStatus(0);
        //comment.setCid(CommunityUtil.generateUUID());
        comment.setCreatetime(new Date());
        commentService.addComment(comment);
        return "redirect:/blog/read/" + bid;
    }
    @LoginRequired
    @RequestMapping(path = "/comment/question/{qid}")
    public String addQuestionComment(@PathVariable("qid") String qid, Comment comment) {
        comment.setUserid(hostHolder.getUser().getId());
        //1论文 2问答 3博客
//        comment.setTopicCategory(2);
        //comment.setEntitytype(2);
        if (comment.getEntitytype()==3){
            comment.setTargetid2(questionService.SelectByQid(qid).getAuthorId());
        }else {
            comment.setTargetid2(commentService.selectcommentById(comment.getEntityid()).getUserid());
        }
        comment.setStatus(0);
        //comment.setCid(CommunityUtil.generateUUID());
        comment.setCreatetime(new Date());
        commentService.addComment(comment);
        return "redirect:/question/read/" + qid;
    }

    @LoginRequired
    @RequestMapping(path = "/add/{paperid}", method = RequestMethod.POST)
    //@ResponseBody
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
    //@ResponseBody
    public String sourceAddDiscussPost(@PathVariable("paperid") int paperid, Comment comment) {
        User user = hostHolder.getUser();
        comment.setUserid(user.getId());
        comment.setCreatetime(new Date());
        if (comment.getEntitytype()==4){
            comment.setTargetid2(paperOfClassService.selectPaperById(paperid).getUserid());
            //comment.setTargetid2(sourceService.selectSourceById(paperid).getUserid());
        }else {
            comment.setTargetid2(commentService.selectcommentById(comment.getEntityid()).getUserid());
        }
        commentService.addComment(comment);
        return "redirect:/source/detail?id="+paperid;
    }

    @LoginRequired
    @RequestMapping(path = "/addinmsg",method = RequestMethod.POST)
    public String addInMsg(Comment comment){
        User user = hostHolder.getUser();
        comment.setUserid(user.getId());
        comment.setCreatetime(new Date());
        commentService.addComment(comment);
        return "redirect:/mymsg";
    }

    @LoginRequired                                                     /*评论全为空，是否需要考虑*/
    @RequestMapping(path = "/mymsg",method = RequestMethod.GET)
    public String getmymsg(Model model){
        User user = hostHolder.getUser();
        List<Map<String,Object>> messages = new ArrayList<>();//消息页面所需要的全部消息组合messages,每个message需要username在paperid|papername中发表了comment
        List<Comment> comments = commentService.selectcommentByTarget(user.getId());//从表中取 所有发给当前用户的评论
        Collections.reverse(comments);
        for (Comment comment:comments){
            Map<String,Object> cc=new HashMap<>();
            int paperid;
            if (comment.getEntitytype()==1){
                paperid=commentService.selectcommentById(comment.getEntityid()).getEntityid();
            }
            else {
                paperid=comment.getEntityid();
            }
            System.out.println(paperid);
            cc.put("paperid" ,paperid);
            cc.put("papername",paperOfClassService.selectPaperById(paperid).getTitle());
            cc.put("status",paperOfClassService.selectPaperById(paperid).getStatus());
            cc.put("comment",comment);
            cc.put("username",userService.findUserById(comment.getUserid()).getUsername());
            messages.add(cc);
        }
        model.addAttribute("messages",messages);
        return "/site/msg";
    }

    @LoginRequired                                                     /*通知全为空，是否需要考虑*/
    @RequestMapping(path = "/mynotice",method = RequestMethod.GET)
    public String getmynotice(Model model){
        User user = hostHolder.getUser();
        List<Map<String,Object>> notices = new ArrayList<>();//消息页面所需要的全部消息组合messages,每个message需要username在paperid|papername中发表了comment
        List<Comment> noticesOfAll = commentService.findcommentByTable(1,111);//从表中取 所有发给用户的公告
        List<Comment> noticesOfSpecial = commentService.findcommentByTable(1,user.getType());//从表中取 所有发给当前用户类型（0：普通用户；1：超级管理员）的公告
        for(Comment noticeAll:noticesOfAll){
            Map<String,Object> nAll=new HashMap<>();
            nAll.put("table",111);
            nAll.put("comment",noticeAll);
            notices.add(nAll);
        }
        for(Comment noticeSpecial:noticesOfSpecial){
            Map<String,Object> nSpe=new HashMap<>();
            nSpe.put("table",user.getType());
            nSpe.put("comment",noticeSpecial);
            notices.add(nSpe);
        }
        model.addAttribute("notices",notices);
        return "/site/notice";
    }
    /*@RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }*/

}
