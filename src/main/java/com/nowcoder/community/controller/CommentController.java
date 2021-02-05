package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.PaperOfClassService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
/*@RequestMapping("/comment")*/
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaperOfClassService paperOfClassService;
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/add/{paperid}", method = RequestMethod.POST)
    //@ResponseBody
    public String addDiscussPost(@PathVariable("paperid") int paperid, Comment comment) {
        User user = hostHolder.getUser();
        comment.setUserid(user.getId());
        comment.setCreatetime(new Date());
        commentService.addComment(comment);
        return "redirect:/detail?id="+paperid;
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
