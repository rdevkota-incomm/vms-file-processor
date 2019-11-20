package com.incomm.vms.fileprocess.cache;

import com.incomm.vms.fileprocess.model.FileAggregateSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileAggregateSummaryStore {
    private static Logger LOGGER = LoggerFactory.getLogger(FileAggregateSummaryStore.class);

    private static ConcurrentHashMap<String, FileAggregateSummary> summaryStore;

    static {
        summaryStore = new ConcurrentHashMap<>();
    }

    public static void upsertProducedRecordCount(String uuid, int totalRecordCount) {
        summaryStore.computeIfPresent(uuid, (k, v) -> {
            v.setTotalProducedRecordCount(totalRecordCount);
            return v;
        });

        summaryStore.computeIfAbsent(uuid, v -> {
            FileAggregateSummary summary = new FileAggregateSummary();
            List<String> panCodeList = new ArrayList<>();
            summary.setTotalProducedRecordCount(totalRecordCount);
            summary.setListOfPanCodes(panCodeList);
            summary.setListOfDeletePanCodes(panCodeList);
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

}
