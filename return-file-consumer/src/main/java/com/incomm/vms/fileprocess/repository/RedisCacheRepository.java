package com.incomm.vms.fileprocess.repository;

import com.incomm.vms.fileprocess.model.RedisCache;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static com.incomm.vms.fileprocess.config.Constants.AGGREGATE_SUMMARY;

@Repository
public class RedisCacheRepository {

    private HashOperations hashOperations;
    private RedisTemplate<String, RedisCache> redisTemplate;

    public RedisCacheRepository(RedisTemplate<String, RedisCache> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void saveIntoCache(RedisCache aggregateSummaryMap) {
        hashOperations.put(AGGREGATE_SUMMARY, AGGREGATE_SUMMARY, aggregateSummaryMap);
    }

    public RedisCache getFromCache() {
        return (RedisCache) hashOperations.get(AGGREGATE_SUMMARY, AGGREGATE_SUMMARY);
    }
}
