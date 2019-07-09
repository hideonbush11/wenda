package com.example.advance.repository;


import com.example.advance.model.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface QuestionRepository extends ElasticsearchRepository<Question,Long>{
}
