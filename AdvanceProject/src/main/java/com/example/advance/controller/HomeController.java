package com.example.advance.controller;

import com.example.advance.model.*;
import com.example.advance.service.CommentService;
import com.example.advance.service.FollowService;
import com.example.advance.service.QuestionService;
import com.example.advance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/user/{userId}")
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        // 显示关注和被关注列表
        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), userId, EntityType.ENTITY_USER));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "profile";
    }

    @RequestMapping(value = {"/", "/index"})
    public String home(Model model) {
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.selectLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            //问题关注的数量
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vo.set("user", userService.getUser(question.getUser_id()));
            vos.add(vo);
        }
        return vos;
    }
}
