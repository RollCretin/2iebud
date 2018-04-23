package com.hxjf.dubei.bean;

/**
 * Created by Chen_Zhang on 2017/12/20.
 */

public class ActivityDetailBean {

    /**
     * responseCode : 1
     * responseMsg
     * responseData : {"id":"5a2e4a7b0cf2425620a7ba5b","title":"读书交流会2","banner":"/activity/201712/11170603ae90.jpg","tag":"123456","beginTime":"123456","endTime":"123546","address":"广东省深圳市南山区田厦金牛国际中心","summary":"<p>1111<\/p>","ownerId":"59f3f8870cf231f3b321961e","ownerType":"1","ownerName":"111"}
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
         * id : 5a2e4a7b0cf2425620a7ba5b
         * title : 读书交流会2
         * banner : /activity/201712/11170603ae90.jpg
         * tag : 123456
         * beginTime : 123456
         * endTime : 123546
         * address : 广东省深圳市南山区田厦金牛国际中心
         * "intro": "测试活动介绍分享\r\n测试活动介绍分享\r\n测试活动介绍分享"
         * summary : <p>1111</p>
         * ownerId : 59f3f8870cf231f3b321961e
         * ownerType : 1
         * ownerName : 111
         * ownerHeadPhoto
         * shareUrl
         */

        private String id;
        private String title;
        private String banner;
        private String tag;
        private String beginTime;
        private String endTime;
        private String time;
        private String address;
        private String summary;
        private String intro;
        private String ownerId;
        private String ownerType;
        private String ownerName;
        private String ownerHeadPhoto;
        private String shareUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getOwnerType() {
            return ownerType;
        }

        public void setOwnerType(String ownerType) {
            this.ownerType = ownerType;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getOwnerHeadPhoto() {
            return ownerHeadPhoto;
        }

        public void setOwnerHeadPhoto(String ownerHeadPhoto) {
            this.ownerHeadPhoto = ownerHeadPhoto;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }
    }
}
