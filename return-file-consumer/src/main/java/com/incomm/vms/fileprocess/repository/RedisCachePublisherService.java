package com.incomm.vms.fileprocess.repository;

import com.incomm.vms.fileprocess.redis.CachePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisCachePublisherService implements CachePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisCachePublisherService() {
    }

    public RedisCachePublisherService(final RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(final String key, final String message) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, message);
    }

    public String retrieve(final String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        return (String) values.get(key);
    }

}
