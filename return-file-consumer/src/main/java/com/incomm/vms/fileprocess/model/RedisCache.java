package com.incomm.vms.fileprocess.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import static com.incomm.vms.fileprocess.config.Constants.AGGREGATE_SUMMARY;

@RedisHash(AGGREGATE_SUMMARY)
@Entity
@Table(name = AGGREGATE_SUMMARY)
@Access(value= AccessType.FIELD)
public class RedisCache implements Serializable {
    @javax.persistence.Id
    @Indexed
    @Column(name = "ID")
    private Long id;
    @Column(name = "SUMMARY_STORE")
    private ConcurrentHashMap<String, FileAggregateSummary> summaryStore;

    public RedisCache(Long id, ConcurrentHashMap<String, FileAggregateSummary> summaryStore) {
        this.id = id;
        this.summaryStore = summaryStore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConcurrentHashMap<String, FileAggregateSummary> getSummaryStore() {
        return summaryStore;
    }

    public void setSummaryStore(ConcurrentHashMap<String, FileAggregateSummary> summaryStore) {
        this.summaryStore = summaryStore;
    }
}
