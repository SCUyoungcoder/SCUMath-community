package com.nowcoder.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PaperOfClassService paperOfClassService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String findClassification(Model model) {
        /*List<Classification> allClassList = classificationService.AllClassifications();
        List<Map<String, Object>> allClass = new ArrayList<>();
        for (Classification class1 : allClassList) {
            Map<String, Object> map = new HashMap<>();
            map.put("class1", class1);
            allClass.add(map);
        }
        List<Paper> papers = paperOfClassService.selectpaperByStatus(2,0,10);
        List<Map<String,Object>> maps = new ArrayList<>();
        for (Paper paper:papers){
            Map<String,Object> map = new HashMap<>();
            map.put("ownername", userService.findUserById(paper.getUserid()).getUsername());
            map.put("reward",paper);
            maps.add(map);
        }
        model.addAttribute("rewards",maps);
        model.addAttribute("allClass", allClass);*/
        List<Paper> notices = paperOfClassService.selectpaperByStatus(5,0,3);
        for (Paper paper:notices){
            paper.setFatherid(userService.findUserById(paper.getUserid()).getUsername());
        }
        model.addAttribute("notice",notices);
        return "/index";
    }
    @LoginRequired
    @RequestMapping(path = "/attention",method = RequestMethod.GET)
    public String getItemOfAttention(Model model,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int limit,
                                     @RequestParam(defaultValue = "0") int userId){
        User user = hostHolder.getUser();
        List<Map<String,Object>> attentions = new ArrayList<>();
        List<Attention> attentionList = attentionService.SelectByUserId(user.getId());
        if(attentionList.isEmpty()){
            PageInfo<Itemindex> info1 = new PageInfo<>();
            model.addAttribute("info",info1);
            model.addAttribute("attentions",attentions);
            return "/list";
        }else {
            for (Attention attention:attentionList){
                Map<String ,Object> map = new HashMap<>();
                int id = attention.getFocusId();
                map.put("userId",id);
                map.put("userName",userService.findUserById(id).getUsername());
                attentions.add(map);
            }
            model.addAttribute("attentions",attentions);
        }
        if(userId==0){
            attentionList  = attentionService.SelectByUserId(user.getId());
            System.out.println(attentionList);
        }
        else {
            attentionList = new ArrayList<>();
            Attention attention = new Attention();
            attention.setFocusId(userId);
            attention.setUserId(user.getId());
            attentionList.add(attention);
        }
        StringBuilder sql = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        if (!attentionList.isEmpty()){
            /*Map<String ,Object> map = new HashMap<>();*/
            int id;
            id = attentionList.get(0).getFocusId();
            sql.append(" author_id = ");
            sql2.append(" user_id = ");
            sql2.append(id);
            sql.append(id);
            /*map.put("userId",id);
            map.put("userName",userService.findUserById(id).getUsername());
            attentions.add(map);*/
            attentionList.remove(0);
            if (!attentionList.isEmpty()){
                for (Attention attention:attentionList){
                    id = attention.getFocusId();
                    sql.append(" or author_id = ");
                    sql2.append(" or user_id = ");
                    sql2.append(id);
                    sql.append(id);
                    /*Map<String ,Object> map1 = new HashMap<>();
                    map1.put("userId",id);
                    map1.put("userName",userService.findUserById(id).getUsername());
                    attentions.add(map1);*/
                }
            }
        }
        System.out.println(sql.toString());
        System.out.println(sql2.toString());
        PageHelper.startPage(page,limit);
        PageInfo<Itemindex> info = new PageInfo<>(attentionService.SelectAllItemByUserIds(sql2.toString(),sql.toString()));
        List<Itemindex> items = new ArrayList<>();
        if (info.getTotal()>0){
            for (Itemindex itemindex:info.getList()){
                Paper paper = paperOfClassService.SelectByIdAndCreateTime(itemindex.getId(),itemindex.getGmtCreate());
                if (paper!=null){
                    if (paper.getStatus()==0){
                        itemindex.setType("论文");
                    }else {
                        itemindex.setType("资源");
                    }
                    itemindex.setUserId(paper.getUserid());
                    itemindex.setNum(paper.getDownloadcount());
                    itemindex.setUserName(userService.findUserById(paper.getUserid()).getUsername());
                }else {
                    Question question = questionService.SelectByIdAndCreateTime(itemindex.getId(),itemindex.getGmtCreate());
                    if (question!=null){
                        itemindex.setType("问答");
                        itemindex.setUserName(question.getAuthorName());
                        itemindex.setUserId(question.getAuthorId());
                        itemindex.setBid(question.getQid());
                        itemindex.setNum(question.getViews());
                    }else{
                        Blog blog = blogService.SelectByIdAndCreateTime(itemindex.getId(),itemindex.getGmtCreate());
                        itemindex.setType("博客");
                        itemindex.setUserId(blog.getAuthorId());
                        itemindex.setUserName(blog.getAuthorName());
                        itemindex.setBid(blog.getBid());
                        itemindex.setNum(blog.getViews());
                    }
                }
            }
        }
        System.out.println(attentions);
        model.addAttribute("info",info);
        /*model.addAttribute("attentions",attentions);*/
        return "/list";

    }
    // ajax示例
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0, "操作成功!");
    }
}