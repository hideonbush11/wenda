package com.example.advance.controller;

import com.example.advance.async.EventModel;
import com.example.advance.async.EventProducer;
import com.example.advance.async.EventType;
import com.example.advance.model.Comment;
import com.example.advance.model.EntityType;
import com.example.advance.model.HostHolder;
import com.example.advance.service.CommentService;
import com.example.advance.service.QuestionService;
import com.example.advance.utils.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = {"/addComment"})
    public String addComment(Integer questionId, String content) {
        try {
            // 增加question的评论
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(ForumUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);
            // 更新question的评论数
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);
            // 推送异步事件
            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId()).setEntityId(questionId));
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }
}
