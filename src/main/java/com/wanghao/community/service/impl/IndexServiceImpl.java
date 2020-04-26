package com.wanghao.community.service.impl;

import com.wanghao.community.dao.DiscussPostMapper;
import com.wanghao.community.entity.DiscussPost;
import com.wanghao.community.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<DiscussPost> findDisscussPosts(int userId, int offest, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offest, limit);
    }

    @Override
    public int findDisscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
