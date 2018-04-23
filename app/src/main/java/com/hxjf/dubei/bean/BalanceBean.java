package com.hxjf.dubei.bean;

/**
 * Created by Chen_Zhang on 2017/10/19.
 */

public class BalanceBean {

    /**
     * responseCode : 1
     * responseData : {"accountMoney":200.00}
     */

    private int responseCode;
    /**
     * accountMoney : 200.00
     */

    private ResponseDataBean responseData;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public ResponseDataBean getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseDataBean responseData) {
        this.responseData = responseData;
    }

    public static class ResponseDataBean {
        private double accountMoney;

        public double getAccountMoney() {
            return accountMoney;
        }

        public void setAccountMoney(double accountMoney) {
            this.accountMoney = accountMoney;
        }
    }
}
