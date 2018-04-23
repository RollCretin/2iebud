package com.hxjf.dubei.bean;

/**
 * Created by Chen_Zhang on 2017/8/2.
 */

public class ChallengeNumBean {

    /**
     * responseCode : 1
     * responseData : {"count":5}
     */

    private int responseCode;
    /**
     * count : 5
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
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
