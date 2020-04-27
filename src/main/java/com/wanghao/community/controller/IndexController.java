package com.wanghao.community.controller;

import com.wanghao.community.entity.DiscussPost;
import com.wanghao.community.entity.Page;
import com.wanghao.community.entity.User;
import com.wanghao.community.service.IndexService;
import com.wanghao.community.service.UserService;
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
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index(Model model, Page page){
        page.setRows(indexService.findDisscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = indexService.findDisscussPosts(0, page.getOffset(), page.getLimit());
        ArrayList<Map<String,Object>> discussPosts = new ArrayList<>();
        if(list!=null){
            for(DiscussPost post:list){
                HashMap<String, Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
}
