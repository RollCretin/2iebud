package com.hxjf.dubei.bean;

/**
 * Created by Chen_Zhang on 2017/10/24.
 */

public class CheckVersionBean {

    /**
     * responseCode : 1
     * responseData : {"id":"11111","createTime":"2017-10-24","update":"Yes","newVersion":"1.0.1","apkFileUrl":"http://test1.17dubei.com/down/app-release_10_jiagu_sign.apk","updateLog":"1、修复bug若干 2、优化细节","targetSize":"18183086","newMd5":"bcaab1494be5441f8ba798b963a5d82f","constraint":false,"iosVersion":false}
     */

    private int responseCode;
    /**
     * id : 11111
     * createTime : 2017-10-24
     * update : Yes
     * newVersion : 1.0.1
     * apkFileUrl : http://test1.17dubei.com/down/app-release_10_jiagu_sign.apk
     * updateLog : 1、修复bug若干 2、优化细节
     * targetSize : 18183086
     * newMd5 : bcaab1494be5441f8ba798b963a5d82f
     * constraint : false
     * iosVersion : false
     * switchFlag : false
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
        private String id;
        private String createTime;
        private String update;
        private String newVersion;
        private String apkFileUrl;
        private String updateLog;
        private String targetSize;
        private String newMd5;
        private boolean constraint;
        private boolean iosVersion;
        private boolean switchFlag;

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

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        public String getNewVersion() {
            return newVersion;
        }

        public void setNewVersion(String newVersion) {
            this.newVersion = newVersion;
        }

        public String getApkFileUrl() {
            return apkFileUrl;
        }

        public void setApkFileUrl(String apkFileUrl) {
            this.apkFileUrl = apkFileUrl;
        }

        public String getUpdateLog() {
            return updateLog;
        }

        public void setUpdateLog(String updateLog) {
            this.updateLog = updateLog;
        }

        public String getTargetSize() {
            return targetSize;
        }

        public void setTargetSize(String targetSize) {
            this.targetSize = targetSize;
        }

        public String getNewMd5() {
            return newMd5;
        }

        public void setNewMd5(String newMd5) {
            this.newMd5 = newMd5;
        }

        public boolean isConstraint() {
            return constraint;
        }

        public void setConstraint(boolean constraint) {
            this.constraint = constraint;
        }

        public boolean isIosVersion() {
            return iosVersion;
        }

        public void setIosVersion(boolean iosVersion) {
            this.iosVersion = iosVersion;
        }

        public boolean isSwitchFlag() {
            return switchFlag;
        }

        public void setSwitchFlag(boolean switchFlag) {
            this.switchFlag = switchFlag;
        }
    }
}
