package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Classification;
import com.nowcoder.community.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    ClassificationService classificationService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String findClassification(Model model) {
        List<Classification> allClassList = classificationService.AllClassifications();
        List<Map<String, Object>> allClass = new ArrayList<>();
        for (Classification class1 : allClassList) {
            Map<String, Object> map = new HashMap<>();
            map.put("class1", class1);
            allClass.add(map);
        }
        model.addAttribute("allClass", allClass);
        System.out.print(allClass);
        return "/index";
    }
}