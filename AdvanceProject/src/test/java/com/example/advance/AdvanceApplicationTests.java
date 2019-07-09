package com.example.advance;

import com.example.advance.async.EventModel;
import com.example.advance.async.EventType;
import com.example.advance.model.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvanceApplicationTests {

    @Test
    public void contextLoads() {
        EventModel e = new EventModel(EventType.LIKE)
                .setActorId(0).setEntityId(1)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(2)
                .setExts("questionId", String.valueOf(3));
        System.out.println(e.getExts("questionId"));
    }

}
