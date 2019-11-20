package com.incomm.vms.fileprocess.model;

import com.google.gson.Gson;

import java.util.List;

public class FileAggregateSummary {

    private int totalProducedRecordCount;
    private int totalConsumedRecordCount;

    private List<String> listOfPanCodes;
    private List<String> listOfDeletePanCodes;

    public int getTotalProducedRecordCount() {
        return totalProducedRecordCount;
    }

    public void setTotalProducedRecordCount(int totalProducedRecordCount) {
        this.totalProducedRecordCount = totalProducedRecordCount;
    }

    public int getTotalConsumedRecordCount() {
        return totalConsumedRecordCount;
    }

    public void setTotalConsumedRecordCount(int totalConsumedRecordCount) {
        this.totalConsumedRecordCount = totalConsumedRecordCount;
    }

    public List<String> getListOfPanCodes() {
        return listOfPanCodes;
    }

    public void setListOfPanCodes(List<String> listOfPanCodes) {
        this.listOfPanCodes = listOfPanCodes;
    }

    public void setPanCode(String panCode) {
        this.listOfPanCodes.add(panCode);
    }

    public List<String> getListOfDeletePanCodes() {
        return listOfDeletePanCodes;
    }

    public void setListOfDeletePanCodes(List<String> listOfDeletePanCodes) {
        this.listOfDeletePanCodes = listOfDeletePanCodes;
    }

    public void setDeletePanCode(String panCode) {
        this.listOfDeletePanCodes.add(panCode);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
