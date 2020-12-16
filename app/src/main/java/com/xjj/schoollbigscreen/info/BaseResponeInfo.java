package com.xjj.schoollbigscreen.info;

/**
 * FileName: BaseRespone
 * Author: Target
 * Date: 2020/10/22 3:33 PM
 */
public class BaseResponeInfo {


    /**
     * bizData : {"createDateAsDate":1603351870864,"lastModDate":1603351870859,"lastModDateAsDate":1603351870859,"updateType":0}
     * rtnCode : 0000000
     * ts : 1603351870864
     */


    private String rtnCode;
    private String data;
    private long ts;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

}
