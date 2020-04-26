package com.wanghao.community;

import com.wanghao.community.dao.DiscussPostMapper;
import com.wanghao.community.dao.UserMapper;
import com.wanghao.community.entity.DiscussPost;
import com.wanghao.community.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    void contextLoads() {
        User user = userMapper.selectById(11);
        System.out.println(user);
        User user1 = userMapper.selectByName("liubei");
        System.out.println(user1);
        User user3 = userMapper.selectByEmail("nowcoder1@sina.com");
        System.out.println(user3);
        User user2 = new User();
        user2.setUsername("测试");
        user2.setPassword("1234");
        user2.setCreateTime(new Date());
        user2.setStatus(1);
        int i = userMapper.insertUser(user2);
        System.out.println(user2);
        userMapper.updateHeader(150,"123");
        userMapper.updatePassword(150,"123456");
        userMapper.updateStatus(150,0);
    }


    @Test
    void testPostMapper(){
        int i = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(i);
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost post:discussPosts){
            System.out.println(post);
        }
    }

}
