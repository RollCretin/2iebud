package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/21.
 */

public class hotWordBean {

    /**
     * responseCode : 1
     * responseData : [{"id":"太公要略"},{"id":"兰亭序"},{"id":"小王子"},{"id":"哈哈啥"},{"id":"战"},{"id":"哈哈"},{"id":"太公要"},{"id":"发现"},{"id":"黑天鹅"},{"id":"王子"}]
     */

    private int responseCode;
    /**
     * id : 太公要略
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
