package com.incomm.vms.fileprocess.service;

import com.google.gson.Gson;
import com.incomm.vms.fileprocess.model.ReturnFileAggregateDTO;
import com.incomm.vms.fileprocess.model.ReturnFileDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

public class ConsumerService extends Thread {
    private static Logger LOGGER = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private DataProcessingService dataProcessingService;
    @Autowired
    private FileAggregationService fileAggregationService;

    @KafkaListener(topics = "${vms.printer-awk.topic}")
    public void consumeMessage(ConsumerRecord<?, ?> consumerRecord, Acknowledgment acknowledgment) {
        Gson gson = new Gson();
        String payload = consumerRecord.value().toString();;
        ReturnFileDTO returnFileDTO = gson.fromJson(payload, ReturnFileDTO.class);
        LOGGER.info("Received Message payload: {}", returnFileDTO.toString());
        dataProcessingService.processRecords(returnFileDTO);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "${vms.printer-awk-aggregate.topic}")
    public void consumeAggregateMessage(ConsumerRecord<?, ?> consumerRecord, Acknowledgment acknowledgment) {
        Gson gson = new Gson();
        String payload = consumerRecord.value().toString();
        ReturnFileAggregateDTO returnFileAggregateDTO = gson.fromJson(payload, ReturnFileAggregateDTO.class);
        LOGGER.info("Received Aggregate payload: {}", returnFileAggregateDTO.toString());
        fileAggregationService.saveTotalProducedCount(returnFileAggregateDTO);
        acknowledgment.acknowledge();
    }
}
