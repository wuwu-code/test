package com.jinken.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.util.*;

@SpringBootTest
class RedisDemoApplicationTests {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 操作rediskey
     */
    @Test
    void testKeys() {
        //查看redis有多少key，实际开发慎用
        Set<String> keys = stringRedisTemplate.keys("*");
        System.out.println("keys = " + keys);

        //设置key的过期时间
        stringRedisTemplate.expire("19876850907", Duration.ofSeconds(60));

        //删除key
        stringRedisTemplate.delete("zhangsan_friends");

        //修改key的名称
        stringRedisTemplate.rename("lisi_friends","wangwu_friends");
    }

    //String类型操作
    @Test
    void testString(){
        //获取操作String数据类型的对象
        ValueOperations<String, String> vos = stringRedisTemplate.opsForValue();

        //设置值
        vos.set("username","郭郭郭");
        //设置值并设置超时时间(验证码)
        vos.set("19876850907","1324",Duration.ofSeconds(60));

        //递增，每次递增第 （点赞，评论数，回复数等等）
        vos.increment("star");
        //累加指定步长
        vos.increment("star",10);
        //递减(取消赞)
        vos.decrement("star");

        //获取指定的key的值
        String username = vos.get("username");
        System.out.println("username = " + username);

        //获取多个key的值
        List<String> values = vos.multiGet(Arrays.asList("username","star"));
        System.out.println("value = " + values);

        //如果没有key，创建数据并设置过期时间，如果有，不管
        vos.setIfAbsent("19876850908","1234",Duration.ofSeconds(60));


    }

    //List 数据类型操作
    @Test
    void testList(){
        //获取操作List集合的对象
        ListOperations<String, String> los = stringRedisTemplate.opsForList();

        //从左边添加数据
        los.leftPush("names","郑吊毛");
        los.leftPushAll("names","张三","李四","张三","小明","tom");

        //获取指定索引的数据 0 第一个 -1 最后一个 从头取到尾
        List<String> names = los.range("names", 0, -1);
        System.out.println("names = " + names);

        //从右向左添加数据
        los.rightPush("music","晴天");
        los.rightPushAll("music","鸡你太美","泰裤辣","心太软");

        //从左边删除指定数量的数据
        los.leftPop("music",1);

        //从右边删除指定数量的数据
        los.rightPop("music",2);

        //获取List指定索引位置的数据（从0开始）
        String music = los.index("music", 0);
        System.out.println("music = " + music);


    }
    //Hash类型
    @Test
    void testHash(){
        //获取操作hash数据类型的对象
        HashOperations<String, Object, Object> hos = stringRedisTemplate.opsForHash();

        //存储数据
        hos.put("userinfo1","name","张三");
        hos.put("userinfo1","age","18");
        hos.put("userinfo1","sid","20210901123");
        hos.put("userinfo1","gender","男");
        hos.put("userinfo1","phone","13512341234");

        //查询数据
        //查询所有数据
        Map<Object, Object> userinfo1 = hos.entries("userinfo1");
        System.out.println("userinfo1 = " + userinfo1);
        //查询单个字段数据
        String name = (String) hos.get("userinfo1", "name");
        System.out.println("name = " + name);
        //删除字段
        hos.delete("userinfo1","age","gender");

        //获取所有的字段
        List<Object> keys = hos.values("userinfo1");
        System.out.println("keys = " + keys);

    }
    //set集合
    @Test
    void testSet(){
        //获取操作set数据的对象
        SetOperations<String, String> sos = stringRedisTemplate.opsForSet();
        //存储数据
        sos.add("languages1","java","php","c","c++","java","c","go");
        //
        sos.add("languages2","javascript","php","c","java","py");

        //查询所有数据
        Set<String> languages1 = sos.members("languages1");
        System.out.println("languages1 = " + languages1);

        //删除set集合指定的元素
        //sos.remove("languages1","java");

        //随机删除并返回一个数据
        //String pop = sos.pop("languages2");
        //System.out.println("pop = " + pop);
        //随机删除并返回多个数据
        //List<String> pops = sos.pop("languages2", 3);
        //System.out.println("pops = " + pops);

        //取两个集合的交集
        Set<String> intersect = sos.intersect("languages1", "languages1");
        System.out.println("intersect = " + intersect);


        //全集
        Set<String> union = sos.union(Arrays.asList("languages1", "languages2"));
        System.out.println("union = " + union);

        //差集
        Set<String> difference = sos.difference(Arrays.asList("languages1", "languages3"));
        System.out.println("difference = " + difference);

    }
    //SortedSet ：有序不可重复
    @Test
    void testZset(){
        //获取操作SortedSet的对象
        ZSetOperations<String, String> zos = stringRedisTemplate.opsForZSet();

        //存储数据
        zos.add("music","晴天",10.0);
        zos.add("music","泰裤辣",1.1);
        zos.add("music","丑八怪",9.0);
        zos.add("music","心太软",7.0);
        zos.add("music","七里香",4.5);

        //获取指定范围数据(升序)
        Set<String> music = zos.range("music", 0, -1);
        System.out.println("music = " + music);
        //获取指定范围数据(降序)
        Set<String> desc = zos.reverseRange("music", 0, -1);
        System.out.println("desc = " + desc);

        //按照分值范围获取数据
        Set<String> music1 = zos.rangeByScore("music", 5.0, 10.0);
        System.out.println("music1 = " + music1);
        //删除
        zos.remove("music","泰裤辣");



    }

}
