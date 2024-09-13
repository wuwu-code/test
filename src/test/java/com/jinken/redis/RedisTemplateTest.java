package com.jinken.redis;

import com.jinken.redis.pojo.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTemplateTest {

    @Resource
    private RedisTemplate<String,User> redisTemplate;

    @Test
    void test1(){
        //获取操作字符串的对象
        ValueOperations<String, User> vos = redisTemplate.opsForValue();

        //创建用户对象
        User user = new User(1, "张三丰", 110, "123213213");

        //将对象存储到redis中
        vos.set("user1",user);


        User user1 = vos.get("user1");
        System.out.println("user1 = " + user1);

    }
}
