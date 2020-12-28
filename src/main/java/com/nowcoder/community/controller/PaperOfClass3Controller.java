package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.ElasticsearchClassService;
import com.nowcoder.community.service.PaperOfClassService;
import com.nowcoder.community.service.UserService;
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

//第二级 对应科目标签的论文展示页
@Controller
public class PaperOfClass3Controller {

    @Autowired
    private PaperOfClassService paperOfClassService;//一个可能并没有什么用的【待去掉】
    @Autowired
    private UserService userService;
    @Autowired
    private ElasticsearchClassService elasticsearchClassService;//基于ES搜索引擎的查询service


    //【POST用法，现已弃用】需要第一级的前端表单<method="post" action="/paperofclass"> 传给此页面名为“classname”的参数【已弃用】
    //【POST用法，现已弃用】此方法下行要获取“科目标签”，只要直接声明参数，与表单名称（classname）一致，即可传过来【已弃用】

    //paperofclass?classname=XXX  6.6课选用GET方式传参，参数通过
    @RequestMapping(path = "/paperofclass", method = RequestMethod.GET)
    public String searchpaperofclass(String classname, Page page, Model model) {//此处前端的classname参数的形式是String
        //搜索论文(6.6课22：56开始介绍查询的controller)
        org.springframework.data.domain.Page<Paper> searchResult =
                elasticsearchClassService.searchPaperByClass(classname,page.getCurrent() - 1 ,page.getLimit());
        //聚合数据——上面操作得到的只是Paper实体类，里面包含的信息需要查出来，处理一下聚合起来
        List<Map<String,Object>> papers = new ArrayList<>();//声明聚合的结果——一个集合，里面封装的Map，命名为papers，这是我最终实例化的结果
        Map<String,Object> map =  new HashMap<>();//每次实例化一个Map,封装聚合的数据
        if(searchResult != null){
            for (Paper paper : searchResult){//遍历，每次都会得到一个paper（论文）

                //把论文放进去
                /*paper.setFatherid(userService.findUserById(paper.getUserid()).getUsername());*/
                map.put("paper",paper);
                //如果还有啥需要往里放的还可以添加，写好相应的service，然后继续map.put()即可

                papers.add(map);//得到聚合数据map后，装进集合里
            }
        }
        /*System.out.println(papers);*/
        model.addAttribute("papers",papers);//得到的最终数据要传给模板/页面
        model.addAttribute("classname",classname);//希望返回的页面上也带上刚刚的classname参数信息
        //分页信息
        page.setPath("/paperofclass?classname=" + classname);//路径
        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());//多少数据？多少页数？从searchResult里取
        System.out.print(papers);
        System.out.print(page);
        return "/paperofclass";//返回第二级网页
    }

}
