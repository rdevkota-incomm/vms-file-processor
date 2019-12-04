package com.incomm.vms.fileprocess.redis;

public interface CachePublisher {
    void publish(final String key, final String message);
}