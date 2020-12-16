package com.xjj.schoollbigscreen.info;

/**
 * FileName: ScreenInfo
 * Author: Target
 * Date: 2020/10/26 11:26 AM
 */
public class ScreenInfo {


    /**
     * code : 200
     * success : true
     * data : {"checkCode":"217785"}
     * msg : 操作成功
     */

    private int code;
    private boolean success;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * checkCode : 217785
         */

        private String checkCode;
        private String viewUrl;

        public String getViewUrl() {
            return viewUrl;
        }

        public void setViewUrl(String viewUrl) {
            this.viewUrl = viewUrl;
        }

        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }
    }
}
