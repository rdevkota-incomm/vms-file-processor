package com.incomm.vms.fileprocess.service;

import com.incomm.vms.fileprocess.cache.FileAggregateSummaryStore;
import com.incomm.vms.fileprocess.model.FileAggregateSummary;
import com.incomm.vms.fileprocess.model.OrderDetailAggregate;
import com.incomm.vms.fileprocess.model.ReturnFileAggregateDTO;
import com.incomm.vms.fileprocess.repository.DeleteCardRepository;
import com.incomm.vms.fileprocess.repository.OrderAggregateRepository;
import com.incomm.vms.fileprocess.repository.OrderLineItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.incomm.vms.fileprocess.config.Constants.UU_ID;

@Service
public class FileAggregationService {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileAggregationService.class);

    @Autowired
    private DeleteCardRepository deleteCardRepository;
    @Autowired
    private OrderAggregateRepository orderAggregateRepository;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    public void saveTotalProducedCount(ReturnFileAggregateDTO fileAggregateDTO) {
        String uuid = fileAggregateDTO.getUuid();
        LOGGER.debug("Saving aggregate DTO {}", fileAggregateDTO);
        FileAggregateSummaryStore.upsertProducedRecordCount(uuid, fileAggregateDTO.getTotalRecordCount());
        if (isConsumtionComplete(uuid)) {
            aggregateSummary(uuid);
        }
    }

    public void saveConsumedDetail(String panCode, Boolean deleteRequired, Map<String, String> headers) {
        String uuid = headers.get(UU_ID);
        LOGGER.debug("Saving consumer DTO with header {}", headers);
        FileAggregateSummaryStore.upsertConsumedRecord(uuid, panCode, deleteRequired);
        if (isConsumtionComplete(uuid)) {
            aggregateSummary(uuid);
        }
    }

    public void addConsumerCountForFailedRecord(Map<String, String> headers) {
        String uuid = headers.get(UU_ID);
        LOGGER.debug("Saving consumer DTO with header {}", headers);
        FileAggregateSummaryStore.upsertConsumedFailedRecord(uuid);
        if (isConsumtionComplete(uuid)) {
            aggregateSummary(uuid);
        }
    }

    private void aggregateSummary(String uuid) {
        FileAggregateSummary summary = FileAggregateSummaryStore.getSummaryStore(uuid);
        LOGGER.info("Will update product detail .. ");
        // get aggregate info
        if (!summary.getListOfPanCodes().isEmpty()) {
            LOGGER.info("Getting Pan codes ");
            List<OrderDetailAggregate> aggregateList = orderAggregateRepository.getLineItemSummary(summary.getListOfPanCodes());

            aggregateList.stream().forEach(x -> updateOrderLineItems(x));
        }

        // delete records
        if (!summary.getListOfDeletePanCodes().isEmpty()) {
            LOGGER.info("Deleting Pan codes ");
            deleteCardRepository.delete(summary.getListOfDeletePanCodes());
        }
    }

    private void updateOrderLineItems(OrderDetailAggregate orderDetailAggregate) {
        orderLineItemRepository.update(orderDetailAggregate);
    }

    private boolean isConsumtionComplete(String uuid) {
        FileAggregateSummary summary = FileAggregateSummaryStore.getSummaryStore(uuid);
        LOGGER.info("summary.getTotalConsumedRecordCount(): {}", summary.getTotalConsumedRecordCount());
        LOGGER.info("summary.getTotalProducedRecordCount(): {}", summary.getTotalProducedRecordCount());
        return summary.getTotalConsumedRecordCount() >= summary.getTotalProducedRecordCount();
    }
}