package com.incomm.vms.fileprocess.controller;

import com.incomm.vms.fileprocess.cache.FileAggregateSummaryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @GetMapping("/getall")
    @ResponseBody
    public ConcurrentHashMap getAllConsumtionDetails() {
        return FileAggregateSummaryStore.getAllSummaryStore();
    }

    @GetMapping("/resetCache")
    public ConcurrentHashMap resetSummaryStore() {
        FileAggregateSummaryStore.getAllSummaryStore().clear();
        FileAggregateSummaryStore.syncCache();
        return FileAggregateSummaryStore.getAllSummaryStore();
    }

    @GetMapping("/stop")
    public String stop() {
        MessageListenerContainer listenerContainer =
                kafkaListenerEndpointRegistry.getListenerContainer("printer-awk-id");
        listenerContainer.stop();
        return "Stopped";
    }

    @GetMapping("/start")
    public void start() {
        MessageListenerContainer listenerContainer =
                kafkaListenerEndpointRegistry.getListenerContainer("printer-awk-id");
        listenerContainer.start();
    }

}
