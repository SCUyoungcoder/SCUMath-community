package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return "/register";
    }
    @RequestMapping(path = "/login", method = RequestMethod.POST)/*。。。。。remreber me。。用户验证码放session里 response创建cookie*/
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, HttpSession session, HttpServletResponse response) {
        // 检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("regMsg", "验证码不正确!");
            return "/login";
        }
        // 检查账号,密码                      两个常量，勾选了记住我，记住时间很长，否则相对短
        //7天或者3小时
        int expiredSeconds = rememberme ? 3600*24*7 : 3600*3;
        Map<String, Object> map = loginService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {/*map里包含ticket就成功了，需要跳转到index页面*/
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            /*cookie.setPath(contextPath);*//*cookie有效的路径为整个项目，在application.properties里*/
            cookie.setMaxAge(expiredSeconds);/*cookie有效时间*/
            response.addCookie(cookie);
            return "redirect:/index";/*重定向到首页*/
        } else {
            if (map.get("usernameMsg")!=null){
                model.addAttribute("regMsg", map.get("usernameMsg"));/*如果不是usernameMsy的问题get到的是null，不影响*/
            }
            if (map.get("passwordMsg")!=null){
                model.addAttribute("regMsg", map.get("passwordMsg"));
            }

            return "/login";
        }
    }
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/login";
    }

    @RequestMapping(path = "/register",method=RequestMethod.POST)
    public String register(Model model, User user,String code){
        Map<String,Object>map = loginService.register(user,code);
        if(map == null || map.isEmpty()){
            model.addAttribute("regMsg","注册成功，请登录");
            return "/login";
        }else {
            /*model.addAttribute("usernameMsy",map.get("usernameMsy"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));*/
            model.addAttribute("regMsg",map.get("regMsg"));
            model.addAttribute("code",code);
            model.addAttribute("user",user);
            return "/register";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        loginService.logout(ticket);
        SecurityContextHolder.clearContext();
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
}
