package com.example.advance.async;

import java.util.List;

public interface EventHandler {
    // 处理event的方法
    void doHandle(EventModel model);
    // 哪些event类型被关注； 即注册
    List<EventType> getSupportEventTypes();
}