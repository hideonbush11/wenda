package com.example.advance;

import com.example.advance.dao.UserDAO;
import com.example.advance.mapper.QuestionDAO;
import com.example.advance.model.EntityType;
import com.example.advance.model.Question;
import com.example.advance.model.User;
import com.example.advance.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    FollowService followService;

    @Test
    public void initDatabase() {
        Random random = new Random();

        for(int i=0; i<11; ++i){
            User user = new User();
            user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d",i+1));
            user.setPassword("XX");
            user.setSalt("");
            userDAO.addUser(user);

            //互相关注的测试数据的生成
            for (int j = 0; j< i; ++j){
                followService.follow(j+1, EntityType.ENTITY_USER, i+1);
            }

            Question question = new Question();
            question.setComment_count(i+1);
            Date date = new Date();
            date.setTime(date.getTime() + 60*60*1000*i);
            question.setCreated_date(date);
            question.setUser_id(i+1);
            question.setTitle(String.format("Title%d",i+1));
            question.setContent(String.format("Content %d",i+1));
            questionDAO.addQuestion(question);
        }
    }
}
