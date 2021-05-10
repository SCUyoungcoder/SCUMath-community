package com.nowcoder.community.service;

import com.nowcoder.community.dao.InvitationCodeMapper;
import com.nowcoder.community.dao.LoginticketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.InvitationCode;
import com.nowcoder.community.entity.Loginticket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
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
    private InvitationCodeMapper invitationCodeMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;


    public Map<String,Object> register(User user,String code){
        Map<String,Object>map = new HashMap<>();
        //空之判断
        if (user == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("regMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("regMsg","密码不能为空！");
            return map;
        }
        InvitationCode invitationCode = invitationCodeMapper.selectByCode(code);
        if (invitationCode==null){
            map.put("regMsg","邀请码不正确!");
            return map;
        }
        //验证账号是否能用
        User u = userMapper.selectByName(user.getUsername());
        if(u !=null){
            map.put("regMsg","该账号已存在!");
            return map;
        }
        //用户注册
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(1);
        user.setActivationCode(CommunityUtil.generateUUID());//激活码
        user.setCreateTime(new Date());
        userMapper.insertUser(user);//insert后自动生成id
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
        loginticket.setType(user.getType());
        loginticket.setTicket(CommunityUtil.generateUUID());/*这里的ticket是一个随机生成的字符串，存进数据库后，将其发给客户端，让其凭此访问服务器*/
        loginticket.setStatus(0);
        loginticket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        System.out.println(loginticket.getExpired());
        loginticketMapper.insertTicket(loginticket);

        map.put("ticket", loginticket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginticketMapper.updateStatus(ticket, 1);
    }
    public Loginticket findLoginTicket(String ticket) {
        return loginticketMapper.selectByTicket(ticket);
    }
}