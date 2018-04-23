package com.hxjf.dubei.bean;

/**
 * Created by Chen_Zhang on 2017/10/20.
 */

public class WithdrawBean {

    /**
     * responseCode : 1
     * responseMsg : 您的申请已提交，请耐心等待处理!
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
