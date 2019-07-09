package com.example.advance.controller;

import com.example.advance.model.EntityType;
import com.example.advance.model.Question;
import com.example.advance.model.ViewObject;
import com.example.advance.service.FollowService;
import com.example.advance.service.QuestionService;
import com.example.advance.service.SearchService;
import com.example.advance.service.UserService;
import com.example.advance.utils.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    SearchService searchService;
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    // 保存数据库所有question到es
    @RequestMapping(value = "/save")
    @ResponseBody
    public String save() {
        List<Question> questions = questionService.selectLatestQuestions(0, 0, 500); // 500是按数据量写的
        searchService.save(questions);
        return ForumUtil.getJsonString(0);
    }

    @RequestMapping(value = "/search")
    public String search(Model model, @RequestParam("q") String keyword, @RequestParam(value = "p", defaultValue = "1") int p) {
        try {
            int offset = (p - 1) * 10;
            List<Question> questionList = searchService.testSearch(keyword, offset, 10);
            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                ViewObject vo = new ViewObject();
                vo.set("question", question);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUser(question.getUser_id()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            logger.error("搜索失败" + e.getMessage());
        }
        return "result";
    }
}