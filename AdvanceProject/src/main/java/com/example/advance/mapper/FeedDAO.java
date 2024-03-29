package com.example.advance.mapper;

import com.example.advance.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FeedDAO {
    String TABLE_NAME = "feed";
    String INSERT_FIELDS = "user_id, data, created_date, type";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{userId},#{data},#{createdDate},#{type})"})
    int addFeed(Feed feed);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id=#{id}"})
    Feed getFeedById(int id);

    List<Feed> selectUserFeeds(int maxId, List<Integer> userIds, int count);
}