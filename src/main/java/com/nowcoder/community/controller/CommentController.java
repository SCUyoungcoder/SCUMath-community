package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.PaperOfClassService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
/*@RequestMapping("/comment")*/
public class CommentController {
    @Autowired
    private CommentService commentService;
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
