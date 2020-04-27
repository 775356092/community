package com.wanghao.community;

import com.wanghao.community.dao.LoginTicketMapper;
import com.wanghao.community.entity.LoginTicket;
import com.wanghao.community.util.CommunityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoginTicketTest {

    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void test(){
        LoginTicket loginTicket1 = loginTicketMapper.selectByTicket("3752721a5d194e8c93494e3e25ab2fe1");
        System.out.println(loginTicket1);
        loginTicketMapper.updateStatus("3752721a5d194e8c93494e3e25ab2fe1",1);
    }
}
