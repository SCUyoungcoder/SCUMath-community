package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginticketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.Loginticket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private LoginticketMapper loginticketMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    /*@Value("${server.servlet.context-path}")
    private String contextPath;*/
    public Map<String,Object> register(User user){
        Map<String,Object>map = new HashMap<>();
        //空之判断
        if (user == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }
        //验证账号是否能用
        User u = userMapper.selectByName(user.getUsername());
        if(u !=null){
            map.put("usernameMsg","该账号已存在!");
            return map;
        }
        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null){
            map.put("emailMsg","该邮箱已被注册！");
            return map;
        }
        //用户注册
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());//激活码
        user.setCreateTime(new Date());
        userMapper.insertUser(user);//insert后自动生成id
        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //http://localhost:8080/xxx/101(用户id)/code(激活码)domain后未加contextPath
        String url = domain+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/register",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){
            return 1;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return 0;
        }else {
            return 2;
        }
    }

    /*expiredSeconds多少秒后凭证过期*/
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码。。。。库里密码是加密的，我们加密后再进行比较
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        Loginticket loginticket = new Loginticket();
        loginticket.setUserid(user.getId());
        loginticket.setTicket(CommunityUtil.generateUUID());/*这里的ticket是一个随机生成的字符串，存进数据库后，将其发给客户端，让其凭此访问服务器*/
        loginticket.setStatus(0);
        loginticket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginticketMapper.insertTicket(loginticket);

        map.put("ticket", loginticket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginticketMapper.updateStatus(ticket, 1);
    }
}