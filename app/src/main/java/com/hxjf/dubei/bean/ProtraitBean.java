package com.hxjf.dubei.bean;

/**
 * Created by Chen_Zhang on 2017/7/21.
 */

public class ProtraitBean {

    /**
     * responseCode : 1
     * responseMsg : 上传成功
     * responseData : {"picPath":"返回头像的路径"}
     */

    private int responseCode;
    private String responseMsg;
    /**
     * picPath : 返回头像的路径
     */

    private ResponseDataBean responseData;

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

    public ResponseDataBean getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseDataBean responseData) {
        this.responseData = responseData;
    }

    public static class ResponseDataBean {
        private String picPath;

        public String getPicPath() {
            return picPath;
        }

        public void setPicPath(String picPath) {
            this.picPath = picPath;
        }
    }
}
