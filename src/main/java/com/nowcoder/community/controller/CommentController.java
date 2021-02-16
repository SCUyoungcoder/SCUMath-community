package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.PaperOfClassService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        List<Map<String,Object>> coms = new ArrayList<>();
        List<Comment> comments1 = commentService.selectcommentByTarget(user.getId());
        List<Comment> midcomments = commentService.selectByUserid(user.getId());
        List<Comment> comments2 = new ArrayList<>();
        for (Comment c:midcomments){
            List<Comment> cs = commentService.selectcommentByEntityandtargit(1,c.getId(),user.getId());
            comments2.addAll(cs);
        }
        List<Comment> comments = new ArrayList<>();
        for (Comment c1:comments1){
            for (Comment c2:comments2){
                if (c1.getId()>c2.getId()){
                    comments.add(c2);
                }
                else {
                    break;
                }
            }
            comments.add(c1);
        }
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
            cc.put("comment",comment);
            cc.put("username",userService.findUserById(comment.getUserid()).getUsername());
            coms.add(cc);
        }
        model.addAttribute("comments",coms);
        return "/site/msg";
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
