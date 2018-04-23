package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/11/3.
 */

public class ChangDuParamBean {

    /**
     * responseCode : 1
     * responseData : [{"name":"一年(365天)","code":"4","value":"198","type":"vipType"}]
     */

    private int responseCode;
    /**
     * name : 一年(365天)
     * code : 4
     * value : 198
     * type : vipType
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
        private String name;
        private String code;
        private String value;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
