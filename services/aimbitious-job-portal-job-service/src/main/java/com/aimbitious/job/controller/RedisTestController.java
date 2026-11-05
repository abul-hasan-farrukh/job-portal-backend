//
//package com.aimbitious.job.controller;
//
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/redis-test")
//public class RedisTestController {
//
//    private final StringRedisTemplate redisTemplate;
//
//    public RedisTestController(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    @GetMapping
//    public String testRedis() {
//
//        String key = "aimbitious:redis:test:" + UUID.randomUUID();
//        String value = "Redis is working!";
//
//        // Write
//        redisTemplate.opsForValue()
//                .set(key, value, 2, TimeUnit.MINUTES);
//
//        // Read
//        String retrievedValue =
//                redisTemplate.opsForValue().get(key);
//
//        // Delete
//        Boolean deleted =
//                redisTemplate.delete(key);
//
//        if (value.equals(retrievedValue)
//                && deleted) {
//            return "SUCCESS: Redis WRITE, READ and DELETE operations are working!";
//        }
//
//        return "FAILED: Redis test did not complete successfully.";
//    }
//}
