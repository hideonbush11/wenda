package com.example.advance.controller;

import com.example.advance.async.EventModel;
import com.example.advance.async.EventProducer;
import com.example.advance.async.EventType;
import com.example.advance.model.Comment;
import com.example.advance.model.EntityType;
import com.example.advance.model.HostHolder;
import com.example.advance.service.CommentService;
import com.example.advance.service.LikeService;
import com.example.advance.utils.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = "/like")
    @ResponseBody
    public String like(Integer commentId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }
        // 获取点赞的那条评论
        Comment comment = commentService.getCommentById(commentId);
        // 异步队列发送私信给被赞人
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getEntityId())
                .setExts("questionId", String.valueOf(comment.getEntityId())));
        // 返回前端点赞数
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ForumUtil.getJsonString(0, String.valueOf(likeCount));
    }

    @RequestMapping(value = "/dislike")
    @ResponseBody
    public String dislike(Integer commentId) {
        if (hostHolder.getUser() == null) {
            return ForumUtil.getJsonString(999);
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ForumUtil.getJsonString(0, String.valueOf(likeCount));
    }
}
