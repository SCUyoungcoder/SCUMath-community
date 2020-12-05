package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/student",method = RequestMethod.POST)
    @ResponseBody
    public Date findtime(HttpServletRequest request,HttpServletResponse response){
        return userService.findbyid(Integer.parseInt(request.getParameter("id"))).getCreateTime();
    }

  /*  @RequestMapping(path = "/student" , method = RequestMethod.POST)
    @ResponseBody
    public Date findtime(int id){
        Date u = userService.findbyid(id).getCreateTime();
        System.out.printf(u.toString());
        return u;
    }*/
}
