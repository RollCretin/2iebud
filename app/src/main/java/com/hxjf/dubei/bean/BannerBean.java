package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/18.
 */

public class BannerBean {

    /**
     * responseCode : 1
     * responseData : [{"id":"53ccc5cda81d41b781ae0de9772b9d73","url":"http://test1.17dubei.com/getChangCard.html","normalPath":"/banner/getChangDuCard.png"},{"id":"5ff8322ee43646929402a7bd7fbb1a3f","normalPath":"/banner/challenge.png"},{"id":"adf0a05681c84271837f1655e52eb5e4","normalPath":"/banner/divideBook.png"}]
     */

    private int responseCode;
    /**
     * id : 53ccc5cda81d41b781ae0de9772b9d73
     * url : http://test1.17dubei.com/getChangCard.html
     * normalPath : /banner/getChangDuCard.png
     */

    private List<ResponseDataBean> responseData;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<ResponseDataBean> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseDataBean> responseData) {
        this.responseData = responseData;
    }

    public static class ResponseDataBean {
        private String id;
        private String url;
        private String normalPath;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getNormalPath() {
            return normalPath;
        }

        public void setNormalPath(String normalPath) {
            this.normalPath = normalPath;
        }
    }
}
