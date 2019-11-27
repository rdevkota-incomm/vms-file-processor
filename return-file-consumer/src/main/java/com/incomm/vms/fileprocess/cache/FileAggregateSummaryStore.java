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

    public static void upsertProducedRecordCount(String uuid, int totalRecordCount) {
        LOGGER.info("Total message count:{} for file with uuid:{}", totalRecordCount, uuid );
        summaryStore.computeIfPresent(uuid, (k, v) -> {
            v.setTotalProducedRecordCount(totalRecordCount);
            return v;
        });

        summaryStore.computeIfAbsent(uuid, v -> {
            FileAggregateSummary summary = new FileAggregateSummary();
            summary.setListOfPanCodes(new ArrayList<>());
            summary.setListOfDeletePanCodes(new ArrayList<>());

            summary.setTotalProducedRecordCount(totalRecordCount);
            return summary;
        });
    }

    public static void upsertConsumedFailedRecord(String uuid) {
        summaryStore.computeIfPresent(uuid, (k, v) -> {
            int consumedCount = v.getTotalConsumedRecordCount();
            v.setTotalConsumedRecordCount(consumedCount + 1);
            return v;
        });

        summaryStore.computeIfAbsent(uuid, v -> {
            FileAggregateSummary summary = new FileAggregateSummary();
            summary.setListOfDeletePanCodes(new ArrayList<>());
            summary.setListOfPanCodes(new ArrayList<>());
            summary.setTotalConsumedRecordCount(1);
            return summary;
        });
    }
    public static void upsertConsumedRecord(String uuid, String panCode, Boolean addToDeleteList) {
        summaryStore.computeIfPresent(uuid, (k, v) -> {
            int consumedCount = v.getTotalConsumedRecordCount();
            v.setTotalConsumedRecordCount(consumedCount + 1);
            v.setPanCode(panCode);
            if (addToDeleteList) {
                v.setDeletePanCode(panCode);
            }
            return v;
        });

        summaryStore.computeIfAbsent(uuid, v -> {
            FileAggregateSummary summary = new FileAggregateSummary();
            summary.setListOfDeletePanCodes(new ArrayList<>());
            summary.setListOfPanCodes(new ArrayList<>());
            summary.setTotalConsumedRecordCount(1);
            summary.setPanCode(panCode);
            if (addToDeleteList) {
                summary.setDeletePanCode(panCode);
            }
            return summary;
        });
    }

    public static FileAggregateSummary getSummaryStore(String uuid) {
        return summaryStore.get(uuid);
    }

    public static ConcurrentHashMap getAllSummaryStore() {
        return summaryStore;
    }

    public static void evictCache(String uuid) {
        summaryStore.remove(uuid);
    }
}