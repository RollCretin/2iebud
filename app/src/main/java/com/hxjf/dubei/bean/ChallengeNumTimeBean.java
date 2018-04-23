package com.hxjf.dubei.bean;

/**
 * Created by Chen_Zhang on 2017/8/6.
 */

public class ChallengeNumTimeBean {

    /**
     * responseCode : 1
     * responseData : {"num":55,"theoryReadTime":2}
     */

    private int responseCode;
    /**
     * num : 55
     * theoryReadTime : 2
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
        private int num;
        private int theoryReadTime;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getTheoryReadTime() {
            return theoryReadTime;
        }

        public void setTheoryReadTime(int theoryReadTime) {
            this.theoryReadTime = theoryReadTime;
        }
    }
}
