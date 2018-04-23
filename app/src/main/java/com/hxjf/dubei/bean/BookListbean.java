package com.hxjf.dubei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/24.
 */

public class BookListbean {

    /**
     * responseCode : 1
     * responseData : {"content":[{"title":"互联网","summary":"关于金融的书","cover":"/image/book/201707171234.png","shareTotal":4,"bookCount":5,"id":"59688860d4c6eae9b057814f","praiseCount":3},{"title":"金融","summary":"关于金融的书","cover":"/image/book/201707171234.png","shareTotal":0,"bookCount":4,"id":"59688872d4c6eae9b0578150","praiseCount":0},{"title":"IT","summary":"关于IT的书","cover":"/image/book/201707171234.png","shareTotal":0,"bookCount":4,"id":"5968887bd4c6eae9b0578151","praiseCount":0},{"title":"季节","summary":"关于季节的书","cover":"/image/book/201707171234.png","shareTotal":0,"bookCount":4,"id":"59688884d4c6eae9b0578152","praiseCount":0},{"title":"股灾","summary":"关于股灾的书","cover":"/image/book/201707171234.png","shareTotal":0,"bookCount":4,"id":"5968888dd4c6eae9b0578153","praiseCount":0}],"totalElements":5,"totalPages":1,"last":true,"numberOfElements":5,"size":10,"number":0,"first":true}
     */

    private int responseCode;
    /**
     * content : [{"title":"互联网","summary":"关于金融的书","cover":"/image/book/201707171234.png","shareTotal":4,"bookCount":5,"id":"59688860d4c6eae9b057814f","praiseCount":3},{"title":"金融","summary":"关于金融的书","cover":"/image/book/201707171234.png","shareTotal":0,"bookCount":4,"id":"59688872d4c6eae9b0578150","praiseCount":0},{"title":"IT","summary":"关于IT的书","cover":"/image/book/201707171234.png","shareTotal":0,"bookCount":4,"id":"5968887bd4c6eae9b0578151","praiseCount":0},{"title":"季节","summary":"关于季节的书","cover":"/image/book/201707171234.png","shareTotal":0,"bookCount":4,"id":"59688884d4c6eae9b0578152","praiseCount":0},{"title":"股灾","summary":"关于股灾的书","cover":"/image/book/201707171234.png","shareTotal":0,"bookCount":4,"id":"5968888dd4c6eae9b0578153","praiseCount":0}]
     * totalElements : 5
     * totalPages : 1
     * last : true
     * numberOfElements : 5
     * size : 10
     * number : 0
     * first : true
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
        private int totalElements;
        private int totalPages;
        private boolean last;
        private int numberOfElements;
        private int size;
        private int number;
        private boolean first;
        /**
         * title : 互联网
         * summary : 关于金融的书
         * cover : /image/book/201707171234.png
         * shareTotal : 4
         * bookCount : 5
         * id : 59688860d4c6eae9b057814f
         * praiseCount : 3
         * "ownerId": "04021d4f7e3b480c878f4ae94638ce8c",
         "ownerName": "白月初",
         "ownerHeadPath": "/head/default/byc.png"
         */

        private List<ContentBean> content;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean implements Serializable {
            private static final long serialVersionUID = -807353761666154634L;
            private String title;
            private String summary;
            private String cover;
            private int shareTotal;
            private int bookCount;
            private String id;
            private int praiseCount;
            private String ownerId;
            private String ownerName;
            private String ownerHeadPath;


            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getShareTotal() {
                return shareTotal;
            }

            public void setShareTotal(int shareTotal) {
                this.shareTotal = shareTotal;
            }

            public int getBookCount() {
                return bookCount;
            }

            public void setBookCount(int bookCount) {
                this.bookCount = bookCount;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getPraiseCount() {
                return praiseCount;
            }

            public void setPraiseCount(int praiseCount) {
                this.praiseCount = praiseCount;
            }

            public String getOwnerId() {
                return ownerId;
            }

            public void setOwnerId(String ownerId) {
                this.ownerId = ownerId;
            }

            public String getOwnerName() {
                return ownerName;
            }

            public void setOwnerName(String ownerName) {
                this.ownerName = ownerName;
            }

            public String getOwnerHeadPath() {
                return ownerHeadPath;
            }

            public void setOwnerHeadPath(String ownerHeadPath) {
                this.ownerHeadPath = ownerHeadPath;
            }
        }
    }
}
