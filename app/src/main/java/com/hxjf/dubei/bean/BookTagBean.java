package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/21.
 */

public class BookTagBean {

    /**
     * responseCode : 1
     * responseData : [{"id":"e43fbb9e8b2d40579b13e055efd8f720","name":"经管","code":"1"},{"id":"c7e79fa4a4ed47e8aee490afb3a04904","name":"营销","code":"2"},{"id":"de091646a7594ef79ffe9beae5f6f42e","name":"互联网","code":"3"},{"id":"ac2ca5e6e72f47dc88ba70445985ff63","name":"职场","code":"4"},{"id":"af0aa52989c7491fa6724a0c08d1336c","name":"传记","code":"5"},{"id":"c7fa406bf96844de82ecc47072a32278","name":"励志","code":"6"},{"id":"2a04d79ee71a487e8356766390bfb2ca","name":"哲学","code":"7"},{"id":"0532a8d769f94f8ba2ab6bc02e3a9224","name":"心理学","code":"8"},{"id":"2c8cab9626b7474583881bf970df24f5","name":"历史","code":"9"},{"id":"514a1c6c6b73488eb2752faa0cd7c9ca","name":"金融","code":"10"},{"id":"bdebc0e1144f420cb84ad703212a4fa5","name":"小说","code":"11"},{"id":"a2eda4f91c0b481bb1ef57ba973cbbd4","name":"其他","code":"12"}]
     */

    private int responseCode;
    /**
     * id : e43fbb9e8b2d40579b13e055efd8f720
     * name : 经管
     * code : 1
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
        private String name;
        private String code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
    }
}
