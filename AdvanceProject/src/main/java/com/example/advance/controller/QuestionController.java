package com.example.advance.controller;

import com.example.advance.model.*;
import com.example.advance.service.*;
import com.example.advance.utils.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    FollowService followService;
    @Autowired
    LikeService likeService;
    //
//    @Autowired
//    EventProducer eventProducer;
//
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        // 拿到qid对应的question
        Question question = questionService.selectQuestionById(qid);
        model.addAttribute("question", question);
        // question对应的评论、用户信息
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
//        List<ViewObject> followUsers = new ArrayList<>();
        // 获取关注的用户信息
//        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
//        for (Integer userId : users) {
//            ViewObject vo = new ViewObject();
//            User u = userService.getUser(userId);
//            if (u == null) {
//                continue;
//            }
//            vo.set("name", u.getName());
//            vo.set("headUrl", u.getHead_url());
//            vo.set("id", u.getId());
//            followUsers.add(vo);
//        }
//        model.addAttribute("followUsers", followUsers);
//        if (hostHolder.getUser() != null) {
//            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
//        } else {
//            model.addAttribute("followed", false);
//        }
        return "detail";
    }

    @RequestMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(String title, String content) {
        try {
            // 创建Question对象
            Question question = new Question();
            question.setContent(content);
            question.setComment_count(0);
//            question.setCreated_date(new Date());
            // 获取当前时间
            Date nowdate=new Date();
            //转换时间格式
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date a = Timestamp.valueOf(simpleDate.format(nowdate));
            question.setCreated_date(Timestamp.valueOf(simpleDate.format(nowdate)));

            question.setTitle(title);
            if (hostHolder.getUser() == null) {
                return ForumUtil.getJsonString(999); // 前端收到999会执行登陆跳转
            } else {
                question.setUser_id(hostHolder.getUser().getId());
            }
            // Question对象入库
            if (questionService.addQuestion(question) > 0) {
//                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
//                        .setActorId(question.getUserId()).setEntityId(question.getId())
//                .setExt("title", question.getTitle()).setExt("content", question.getContent()));
                return ForumUtil.getJsonString(0);
            }
        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }
        return ForumUtil.getJsonString(1, "失败");
    }
}
