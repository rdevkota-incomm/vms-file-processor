package com.incomm.vms.fileprocess.controller;

import com.incomm.vms.fileprocess.cache.FileAggregateSummaryStore;
import com.incomm.vms.fileprocess.model.RedisCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @GetMapping("/getall")
    @ResponseBody
    public ConcurrentHashMap getAllConsumtionDetails() {
        return FileAggregateSummaryStore.getAllSummaryStore();
    }

}
