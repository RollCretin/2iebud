package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/20.
 */

public class TagBean {

    /**
     * responseCode : 1
     * responseData : [{"tagName":"互联网"},{"tagName":"哲学"},{"tagName":"营养学"},{"tagName":"美学"},{"tagName":"文艺青年"},{"tagName":"时间管理"},{"tagName":"历史"},{"tagName":"大数据"},{"tagName":"资深阅读爱好者"},{"tagName":"社会学"},{"tagName":"理财"},{"tagName":"运营"},{"tagName":"设计"},{"tagName":"产品汪"},{"tagName":"代码喵"}]
     */

    private int responseCode;
    /**
     * tagName : 互联网
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
        private String tagName;

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }
    }
}
