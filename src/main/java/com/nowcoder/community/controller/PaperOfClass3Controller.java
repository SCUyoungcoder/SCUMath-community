package com.nowcoder.community.controller;

import com.nowcoder.community.service.PaperOfClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.nowcoder.community.entity.Paper;

import java.util.Map;

//第二级 对应科目标签的论文展示页
@Controller
public class PaperOfClass3Controller {

    @Autowired
    private PaperOfClassService paperOfClassService;

    //需要第一级的前端表单<method="post" action="/paperofclass"> 传给此页面名为“classname”的参数
    //此方法下行要获取“科目标签”，只要直接声明参数，与表单名称（classname）一致，即可传过来
    @RequestMapping(path = "/paperofclass", method = RequestMethod.POST)
    public String getpaperofclass(Model model,String classname) { //此处前端的classname参数的形式是String还是int？【待定】
        //查询论文操作，对应需要参考1.30页的“分页操作”
        return "/paperofclass";//【第二级网页 待修】
    }

}
