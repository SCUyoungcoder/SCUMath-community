package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.Loginticket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    /*请求开始前将用户信息存储下来*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 查询凭证
            Loginticket loginticket = loginService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginticket != null && loginticket.getStatus() == 0 && loginticket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginticket.getUserid());
                // 在本次请求中持有用户
                hostHolder.setUser(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }

        return true;
    }
    /*在模板调用前将想要增加的信息写入*/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }
    /*在本次请求中保留信息，请求结束删除信息*/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
