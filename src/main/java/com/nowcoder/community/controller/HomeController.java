package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Classification;
import com.nowcoder.community.entity.Paper;
import com.nowcoder.community.service.ClassificationService;
import com.nowcoder.community.service.PaperOfClassService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    ClassificationService classificationService;
    @Autowired
    PaperOfClassService paperOfClassService;
    @Autowired
    UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String findClassification(Model model) {
        List<Classification> allClassList = classificationService.AllClassifications();
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
        model.addAttribute("allClass", allClass);
        return "/index";
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