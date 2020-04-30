package com.wanghao.community.service.impl;

import com.wanghao.community.dao.LoginTicketMapper;
import com.wanghao.community.dao.UserMapper;
import com.wanghao.community.entity.LoginTicket;
import com.wanghao.community.entity.User;
import com.wanghao.community.service.UserService;
import com.wanghao.community.util.CommunityConstant;
import com.wanghao.community.util.CommunityUtil;
import com.wanghao.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.awt.im.InputContext;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService, CommunityConstant {

    //项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //服务器域名
    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Override
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public Map<String, Object> register(User user) {
        HashMap<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        User u = userMapper.selectByName(user.getUsername());
        //用户名已存在
        if (u != null) {
            map.put("usernameMsg", "该用户名已被注册!");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        //邮箱已存在
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.MD5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:80/community/activation/${userId}/${activationCode}
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendEmail(user.getEmail(), "激活账号", content);

        return map;
    }

    @Override
    public int activation(int userId, String key) {
        User user = userMapper.selectById(userId);
        //已经激活了
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getStatus() == 0 && key.equals(user.getActivationCode())) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        User user = userMapper.selectByName(username);

        if (user == null) {
            map.put("usernameMsg", "您输入的账号不存在!");
            return map;
        }
        if (!user.getPassword().equals(CommunityUtil.MD5(password + user.getSalt()))) {
            map.put("passwordMsg", "您输入的密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicker(loginTicket);

        map.put("ticket",loginTicket.getTicket());

        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket,1);
    }

    @Override
    public LoginTicket findByTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    @Override
    public int updateHeaderUrl(int userId, String headerUrl) {
        return userMapper.updateHeader(userId,headerUrl);
    }

    @Override
    public Map<String, Object> updatePassword(int userId, String password,String newPassword,String repeat) {
        HashMap<String, Object> map = new HashMap<>();
        //原始密码为空
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","原始密码不能为空！");
            return map;
        }
        //新密码为空
        if(StringUtils.isBlank(newPassword)){
            map.put("newPasswordMsg","新密码不能为空！");
            return map;
        }
        //确认密码为空
        if(StringUtils.isBlank(repeat)){
            map.put("newPasswordMsg","确认密码不能为空！");
            return map;
        }
        //两次密码输入不一致
        if(!newPassword.equals(repeat)){
            map.put("newPasswordMsg","两次密码输入不一致请重新输入！");
            return map;
        }

        User user = userMapper.selectById(userId);
        String oldPassword = user.getPassword();
        String salt = user.getSalt();
        String newPsd = CommunityUtil.MD5(password+salt);
        //原密码不一致
        if(!newPsd.equals(oldPassword)){
            map.put("passwordMsg","原始密码错误请重新输入！");
            return map;
        }

        //更新密码
        userMapper.updatePassword(userId,CommunityUtil.MD5(newPassword+salt));
        return map;
    }
}
