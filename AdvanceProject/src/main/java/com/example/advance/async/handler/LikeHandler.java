package com.example.advance.async.handler;

import com.example.advance.async.EventHandler;
import com.example.advance.async.EventModel;
import com.example.advance.async.EventType;
import com.example.advance.model.Message;
import com.example.advance.model.User;
import com.example.advance.service.MessageService;
import com.example.advance.service.UserService;
import com.example.advance.utils.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        // 给被赞的人发message
        int fromId = ForumUtil.SYSTEMCONTROLLER_USERID;
        int toId = model.getEntityOwnerId();
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setCreatedDate(new Date());
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        User user = userService.getUser(model.getActorId());
        message.setContent("用户'" + user.getName() + "'赞了你的问题,http://127.0.0.1:8080/question/" + model.getExts("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE); // 只关注LIKE的事件
    }
}
