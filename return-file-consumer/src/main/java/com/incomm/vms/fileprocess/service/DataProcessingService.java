package com.incomm.vms.fileprocess.service;

import com.incomm.vms.fileprocess.model.FileProcessReasonMaster;
import com.incomm.vms.fileprocess.model.LineItemDetail;
import com.incomm.vms.fileprocess.model.ReturnFileDTO;
import com.incomm.vms.fileprocess.repository.CardIssuanceStatusRepository;
import com.incomm.vms.fileprocess.repository.DeleteCardRepository;
import com.incomm.vms.fileprocess.repository.FileProcessReasonRepository;
import com.incomm.vms.fileprocess.repository.LineItemDetailRepository;
import com.incomm.vms.fileprocess.repository.OrderDetailRepository;
import com.incomm.vms.fileprocess.repository.OrderLineItemRepository;
import com.incomm.vms.fileprocess.repository.ReturnFileDataRepository;
import com.incomm.vms.fileprocess.repository.UploadDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.incomm.vms.fileprocess.config.Constants.FILE_NAME;
import static com.incomm.vms.fileprocess.config.Constants.RECORD_NUMBER;

@Service
public class DataProcessingService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DataProcessingService.class);
    @Autowired
    private ErrorProcessingService errorProcessingService;
    @Autowired
    private FileAggregationService fileAggregationService;
    @Autowired
    private UploadDetailRepository uploadDetailRepository;
    @Autowired
    private LineItemDetailRepository lineItemDetailRepository;
    @Autowired
    private FileProcessReasonRepository fileProcessReasonRepository;
    @Autowired
    private CardIssuanceStatusRepository cardIssuanceStatusRepository;
    @Autowired
    private ReturnFileDataRepository returnFileDataRepository;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private DeleteCardRepository deleteCardRepository;

    @Value("${vms.instance-code}")
    private String instanceCode;

    public void processRecords(ReturnFileDTO returnFileRecord) {
        Map<String, String> messageHeaders = returnFileRecord.getHeaders();
        String fileName = messageHeaders.get(FILE_NAME);
        LOGGER.info("File with name: {} ", fileName);
        LineItemDetail lineItemDetail = null;
        try {
            lineItemDetail = lineItemDetailRepository.findLineItem(instanceCode, returnFileRecord.getSerialNumber());
        } catch (BadSqlGrammarException e) {
            LOGGER.error("Bad syntax error for lineItemDetail query {}", e.getSql(), e);
        }
        if (lineItemDetail == null) {
            errorProcessingService.processNoSerilNumberFoundError(returnFileRecord, fileName);
            fileAggregationService.addConsumerCountForFailedRecord(messageHeaders);
        } else {
            String panCode = lineItemDetail.getPanCode();
            boolean deletePanCode = false;
            FileProcessReasonMaster fileProcessReason = fileProcessReasonRepository.findByRejectReason(returnFileRecord.getRejectReason());
            lineItemDetailRepository.update(returnFileRecord.getSerialNumber(), panCode, fileProcessReason);

            if ("Y".equalsIgnoreCase(fileProcessReason.getSuccessFailureFlag())) {
                cardIssuanceStatusRepository.update(instanceCode, panCode);
            } else if (!"Y".equalsIgnoreCase(fileProcessReason.getSuccessFailureFlag()) &&
                    !lineItemDetail.getPartnerId().equalsIgnoreCase("Replace_Partner_ID")) {
                deletePanCode = true;
                LOGGER.info("Received message from delete card : {} ", panCode);
            }

            returnFileDataRepository.save(instanceCode, fileName, messageHeaders.get(RECORD_NUMBER), returnFileRecord,
                    lineItemDetail);
            fileAggregationService.saveConsumedDetail(panCode, deletePanCode, messageHeaders);
         }
        LOGGER.info("Done processing");
    }
}
