package com.wanghao.community.service;

import com.wanghao.community.entity.DiscussPost;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IndexService {

    List<DiscussPost> findDisscussPosts(int userId, int offest, int limit);

    int findDisscussPostRows(int userId);
}
