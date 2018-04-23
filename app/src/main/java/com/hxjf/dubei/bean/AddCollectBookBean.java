package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2018/1/2.
 */

public class AddCollectBookBean {

    /**
     * responseCode : 1
     * responseMsg : 添加藏书成功
     * responseData : {"id":"5a4afdc00cf27e601e0f3662","createTime":"2018-01-02 11:34:24","ownerType":0,"status":1,"location":[115.2,22.1],"isbn":"9787121269394","name":"Android开发艺术探索","author":"任玉刚","doubanScore":"8.2","tag":"1","publisher":"电子工业出版社"}
     */

    private int responseCode;
    private String responseMsg;
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
        /**
         * id : 5a4afdc00cf27e601e0f3662
         * createTime : 2018-01-02 11:34:24
         * ownerType : 0
         * status : 1
         * location : [115.2,22.1]
         * isbn : 9787121269394
         * name : Android开发艺术探索
         * author : 任玉刚
         * doubanScore : 8.2
         * tag : 1
         * publisher : 电子工业出版社
         */

        private String id;
        private String createTime;
        private int ownerType;
        private int status;
        private String isbn;
        private String name;
        private String author;
        private String doubanScore;
        private String tag;
        private String publisher;
        private List<Double> location;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getOwnerType() {
            return ownerType;
        }

        public void setOwnerType(int ownerType) {
            this.ownerType = ownerType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDoubanScore() {
            return doubanScore;
        }

        public void setDoubanScore(String doubanScore) {
            this.doubanScore = doubanScore;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }
    }
}
