package com.incomm.vms.fileprocess.cache;

import com.incomm.vms.fileprocess.model.FileAggregateSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class FileAggregateSummaryStore {
    private static Logger LOGGER = LoggerFactory.getLogger(FileAggregateSummaryStore.class);
    private static ConcurrentHashMap<String, FileAggregateSummary> summaryStore;
    static {
        summaryStore = new ConcurrentHashMap<>();
    }

    public static void updateProducedRecordCount(String correlationId, String fileName, int totalRecordCount) {
        LOGGER.info("Total message count:{} for file with correlationId:{} filename:{}", totalRecordCount,
                correlationId, fileName );
        summaryStore.computeIfPresent(correlationId, (k, v) -> {
            v.setCompletionTime(getSystemTime());
            v.setFileName(fileName);
            v.setTotalProducedRecordCount(totalRecordCount);
            return v;
        });

        summaryStore.computeIfAbsent(correlationId, v -> {
            FileAggregateSummary summary = new FileAggregateSummary();
            summary.setListOfPanCodes(new ArrayList<>());
            summary.setListOfDeletePanCodes(new ArrayList<>());
            summary.setFileName(fileName);
            summary.setCompletionTime(getSystemTime());
            summary.setTotalProducedRecordCount(totalRecordCount);
            return summary;
        });
    }

    private static long getSystemTime() {
        return System.currentTimeMillis();
    }

    public static void updateConsumedFailedRecordCount(String correlationId, String fileName) {
        summaryStore.computeIfPresent(correlationId, (k, v) -> {
            v.setCompletionTime(getSystemTime());
            int consumedCount = v.getTotalConsumedRecordCount();
            v.setTotalConsumedRecordCount(consumedCount + 1);
            return v;
        });

        summaryStore.computeIfAbsent(correlationId, v -> {
            FileAggregateSummary summary = new FileAggregateSummary();
            summary.setListOfDeletePanCodes(new ArrayList<>());
            summary.setListOfPanCodes(new ArrayList<>());
            summary.setCompletionTime(getSystemTime());
            summary.setFileName(fileName);
            summary.setTotalConsumedRecordCount(1);
            return summary;
        });
    }

    public static void updateConsumedRecordCount(String correlationId, String panCode, Boolean addToDeleteList, String fileName) {
        summaryStore.computeIfPresent(correlationId, (k, v) -> {
            int consumedCount = v.getTotalConsumedRecordCount();
            v.setTotalConsumedRecordCount(consumedCount + 1);
            v.setCompletionTime(getSystemTime());
            v.setPanCode(panCode);
            if (addToDeleteList) {
                v.setDeletePanCode(panCode);
            }
            return v;
        });

        summaryStore.computeIfAbsent(correlationId, v -> {
            FileAggregateSummary summary = new FileAggregateSummary();
            summary.setListOfDeletePanCodes(new ArrayList<>());
            summary.setListOfPanCodes(new ArrayList<>());
            summary.setCompletionTime(getSystemTime());
            summary.setFileName(fileName);
            summary.setTotalConsumedRecordCount(1);
            summary.setPanCode(panCode);
            if (addToDeleteList) {
                summary.setDeletePanCode(panCode);
            }
            return summary;
        });
    }

    public static FileAggregateSummary getSummaryStore(String correlationId) {
        return summaryStore.get(correlationId);
    }

    public static ConcurrentHashMap<String, FileAggregateSummary> getAllSummaryStore() {
        return summaryStore;
    }

    public static void evictCache(String correlationId) {
        summaryStore.remove(correlationId);
    }
}