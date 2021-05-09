package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.AdminRequired;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;



/////////////没用，不知道为什么，我写到LoginRequiredinterceptor里去了
@Component
public class AdminRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("AdminRequired is run");
        if (handler instanceof HandlerMethod) {/*判断拦截下来的是否是方法*/
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            System.out.println("AdminRequired is run");
            AdminRequired adminRequired = method.getAnnotation(AdminRequired.class);
            if (adminRequired != null && hostHolder.getUser() == null) {/*当前方法需要登录，而未获取到用户信息*/
                response.sendRedirect(request.getContextPath() + "/login");
                /*request取到路径加上login，跳转到login*/
                return false;
            }
            if (adminRequired != null && hostHolder.getUser().getType()==0){
                response.sendRedirect(request.getContextPath()+"/index");
                return false;
            }
        }
        return true;
    }
}
