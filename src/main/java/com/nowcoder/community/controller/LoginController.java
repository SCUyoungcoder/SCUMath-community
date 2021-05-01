package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService loginService;
    @Autowired
    private Producer kaptchaProducer;

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        //return "/site/register";
        return "/register";
    }

    /*index的引用路径在HomeController里面用了，去那里找
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getindexPage(Model model){
    *//*用login里的方式更改登录都页面的改变*//*
        *//*model.addAttribute("loginMsg","登录");
        model.addAttribute("logoutMsg","退出");
        model.addAttribute("manageMsg","管理");*//*
        return "/index";
    }*/
    @RequestMapping(path = "/login", method = RequestMethod.POST)/*。。。。。remreber me。。用户验证码放session里 response创建cookie*/
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, HttpSession session, HttpServletResponse response) {
        // 检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确!");
            return "/site/login";
        }

        // 检查账号,密码                      两个常量，勾选了记住我，记住时间很长，否则相对短
        int expiredSeconds = rememberme ? 3600*24*100 : 3600*12;
        Map<String, Object> map = loginService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {/*map里包含ticket就成功了，需要跳转到index页面*/
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            /*cookie.setPath(contextPath);*//*cookie有效的路径为整个项目，在application.properties里*/
            cookie.setMaxAge(expiredSeconds);/*cookie有效时间*/
            response.addCookie(cookie);
            return "redirect:/index";/*重定向到首页*/
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));/*如果不是usernameMsy的问题get到的是null，不影响*/
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/login";
        //return "/site/login";
    }

    @RequestMapping(path = "/register",method=RequestMethod.POST)
    public String register(Model model, User user){
        Map<String,Object>map = loginService.register(user);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封注册邮件,请尽快激活");
            model.addAttribute("target","/index");
            return "/site/registerresult";
        }else {
            model.addAttribute("usernameMsy",map.get("usernameMsy"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));

            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int result = loginService.activation(userId, code);
        if (result == 0) {
            model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target", "/login");
        } else if (result == 1) {
            model.addAttribute("msg", "无效操作,该账号已经激活过了!");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
            model.addAttribute("target", "/index");
        }
        return "/site/registerresult";
    }



    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        loginService.logout(ticket);
        return "redirect:/login";
    }


    /*生成验证码*/
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码.返回对象是图片，故用response手动输出。图片与实际验证码生成，验证码是敏感数据，存入session
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将突图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }
    //cookie示例 不够隐私，每次自动返回cookie增加数据量。浏览器关闭cookie消失
    /*                                                  4个测试用例，项目整合时期删除*/
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody/*将返回值转换为json格式*/
    public String setCookie(HttpServletResponse response) {
        // 创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置cookie生效的范围
        /*cookie.setPath("/community/alpha");*/
        /*cookie.setPath("/");*//*不写或/即为在整个文件夹有效*/
        // 设置cookie的生存时间
        cookie.setMaxAge(60 * 10);
        // 发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }
    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }

    // session示例 存在服务端，加大服务器内存压力，如非特隐私没必要用session。session实际用cookie传

    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }



}
