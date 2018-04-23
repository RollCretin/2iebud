package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/25.
 */

public class BSChaiduListBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseData : [{"id":"596c240a0d1f16bfcf4a9fd9","bookId":"testbook","progress":0,"readTime":0,"readStatus":1,"statusValue":"未读","challengeFlag":0,"flagValue":"未在挑战中","bookInfo":{"name":"小王子1","normalPath":"/image/book/201707171234.png"}}]
     */

    private int responseCode;
    private String responseMsg;
    /**
     * id : 596c240a0d1f16bfcf4a9fd9
     * bookId : testbook
     * progress : 0
     * readTime : 0
     * readStatus : 1
     * statusValue : 未读
     * challengeFlag : 0
     * flagValue : 未在挑战中
     * bookInfo : {"name":"小王子1","normalPath":"/image/book/201707171234.png"}
     */

    private List<ResponseDataBean> responseData;

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

    public List<ResponseDataBean> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseDataBean> responseData) {
        this.responseData = responseData;
    }

    public static class ResponseDataBean {
        private String id;
        private String bookId;
        private int progress;
        private int readTime;
        private int readStatus;
        private String statusValue;
        private int challengeFlag;
        private String flagValue;
        /**
         * name : 小王子1
         * normalPath : /image/book/201707171234.png
         */

        private BookInfoBean bookInfo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getReadTime() {
            return readTime;
        }

        public void setReadTime(int readTime) {
            this.readTime = readTime;
        }

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public String getStatusValue() {
            return statusValue;
        }

        public void setStatusValue(String statusValue) {
            this.statusValue = statusValue;
        }

        public int getChallengeFlag() {
            return challengeFlag;
        }

        public void setChallengeFlag(int challengeFlag) {
            this.challengeFlag = challengeFlag;
        }

        public String getFlagValue() {
            return flagValue;
        }

        public void setFlagValue(String flagValue) {
            this.flagValue = flagValue;
        }

        public BookInfoBean getBookInfo() {
            return bookInfo;
        }

        public void setBookInfo(BookInfoBean bookInfo) {
            this.bookInfo = bookInfo;
        }

        public static class BookInfoBean {
            private String name;
            private String normalPath;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNormalPath() {
                return normalPath;
            }

            public void setNormalPath(String normalPath) {
                this.normalPath = normalPath;
            }
        }
    }
}
