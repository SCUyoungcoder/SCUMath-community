package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.nowcoder.community.entity.Paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class PaperOfClass3Controller {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private PaperOfClassService paperOfClassService;//一个可能并没有什么用的【待去掉】
    @Autowired
    private UserService userService;
    @Autowired
    private ElasticsearchClassService elasticsearchClassService;//基于ES搜索引擎的查询service

//第二级 对应科目标签的论文展示页
    //【POST用法，现已弃用】需要第一级的前端表单<method="post" action="/paperofclass"> 传给此页面名为“classname”的参数【已弃用】
    //【POST用法，现已弃用】此方法下行要获取“科目标签”，只要直接声明参数，与表单名称（classname）一致，即可传过来【已弃用】

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
        /*System.out.println(papers);*/
        model.addAttribute("papers",papers);//得到的最终数据要传给模板/页面
        model.addAttribute("classname",classificationService.GetNameBySearchname(classname).getName());//希望返回的页面上也带上刚刚的classname参数信息

        //分页信息
        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());//多少数据？多少页数？从searchResult里取
        page.setPath("/paperofclass?classname=" + classname);//路径

        //System.out.print(papers);//测试用
        System.out.print(page.getRows());
        return "/paperofclass";//返回第二级网页
    }

    //第三级页面——论文详情页
    @RequestMapping(path = "/detail",method = RequestMethod.GET)
    public String PaperDerail(int id,Page page,Model model){
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
                        if (commentcomment.getTargetid()!=0){       /*上传评论时，如果没有targetid数据库默认置0——正常情况应该不会发生这种事情*/
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
                    map.put("commentcomments",null);                /*是否需要*/
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
    }
}
