package com.hxjf.dubei.bean;

/**
 * Created by Chen_Zhang on 2017/7/18.
 */

public class SMSBean {

    /**
     * responseCode : 1
     * responseMsg : 短信已发送，请注意查收
     */

    private int responseCode;
    private String responseMsg;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
