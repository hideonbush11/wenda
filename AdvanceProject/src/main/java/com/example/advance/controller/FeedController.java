package com.example.advance.controller;

import com.example.advance.model.EntityType;
import com.example.advance.model.Feed;
import com.example.advance.model.HostHolder;
import com.example.advance.service.FeedService;
import com.example.advance.service.FollowService;
import com.example.advance.utils.JedisAdapter;
import com.example.advance.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(value = "/pushfeeds")
    public String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<>();
        // 测试布局
        if (true) {
            return "feeds";
        }
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(value = "/pullfeeds")
    public String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            // 关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
