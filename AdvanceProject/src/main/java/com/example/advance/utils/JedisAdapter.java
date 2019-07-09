package com.example.advance.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.advance.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/9");
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    // 集合（sets）里面有多少数目 （点赞数）
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
        }
        return null;
    }

    // list中的每一个object对应一个操作的返回值
    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            tx.discard();
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException ioe) {
                }
            }
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();

        jedis.set("hello", "world");
        jedis.setex("hello2", 60, "mygod");
        jedis.set("se", "100");
        jedis.incrBy("se", 5);
        jedis.decrBy("se", 2);
        System.out.println(jedis.get("se"));

        // list
        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        System.out.println("4" +  jedis.lrange(listName, 0, 12));
        System.out.println("4" +  jedis.lrange(listName, 0, 3));
        System.out.println("5" +  jedis.llen(listName));
        System.out.println("6" +  jedis.lpop(listName));
        System.out.println("7" +  jedis.lrange(listName, 2, 6));
        System.out.println("8" +  jedis.lindex(listName, 2));
        System.out.println("9" +  jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "xx"));
        System.out.println("9" +  jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));


        // hash 实现菲关系型（非结构化）的关键
        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "15124141");
        System.out.println("12" + jedis.hget(userKey, "name"));
        System.out.println("13" + jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        System.out.println("14" + jedis.hgetAll(userKey));
        System.out.println("15"+ jedis.hexists(userKey, "email"));;
        System.out.println("17" + jedis.hkeys(userKey));
        System.out.println("17" + jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "zju");
        jedis.hsetnx(userKey, "name", "yxy");
        System.out.println("19" + jedis.hgetAll(userKey));

        // set 去重，求交， 求不同
        String likeKey1 = "commentLike1";
        String like2 = "commentLike2";
        for (int i = 0; i < 10; ++i) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(like2, String.valueOf(i * i));
        }
        System.out.println("20" + jedis.smembers(likeKey1)); // 求成员
        System.out.println("21" + jedis.smembers(like2));

        System.out.println("22" + jedis.sunion(likeKey1, like2));  // 求并，大家都有
        System.out.println("24"+jedis.sinter(likeKey1, like2));  // 共同好友（共同关注），求交
        System.out.println("23" + jedis.sdiff(likeKey1, like2)); //
        System.out.println("24" + jedis.sismember(likeKey1, "12")); // 查12在不在里面
        jedis.srem(likeKey1, "5"); // 删除
        jedis.smove(likeKey1, like2, "25"); // 移动
        jedis.scard(likeKey1); // 集合有多少元素，用来求人数
        System.out.println(jedis.srandmember(likeKey1, 2)); // 随机取两个元素

        // sorted sets
        String rankKey ="rankKey";
        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 60, "Ben"); // 给权重，sorted sets
        System.out.println("31" + jedis.zcount(rankKey, 20, 40)); // 查找在20~40分的个数
        jedis.zincrby(rankKey, 2, "Ben"); // 给Ben加2分
        System.out.println("32" + jedis.zscore(rankKey, "Ben")); // 查找ben的分数
        System.out.println("33" + jedis.zrange(rankKey, 1, 3)); // 按分数输出前3名
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "60", "100")) {
            System.out.println("37" + tuple.getElement() + ":" + String.valueOf(tuple.getScore())); // 输出60到100分的人以及他的分数
        }

        System.out.println("38" + jedis.zrank(rankKey, "Ben")); // 查找某个人的排名
        System.out.println("39" + jedis.zrevrank(rankKey, "Ben")); // 倒数

        JedisPool pool = new JedisPool();  // 默认有8个核，可以开8个线程
        // 使用连接池可以多线程访问redis
        for (int i = 0; i < 100; i++) {
            Jedis j = pool.getResource(); // 获取一个连接，用来访问redis
            System.out.println("45" + j.get("Ben"));
            j.close(); // 用完还回去， 否则被独占了
        }

        User user = new User();
        user.setName("xx");
        user.setPassword("ppp");
        user.setHead_url("a.png");
        user.setSalt("salt");
        user.setId(1);
        System.out.println("46" + JSONObject.toJSONString(user));;
        jedis.set("user1", JSONObject.toJSONString(user));

        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value, User.class);
        System.out.println("47" + user2);
        int k = 2;
    }
}