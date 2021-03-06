package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/5.
 */

public class HotBookBean {

    /**
     * responseCode : 1
     * responseData : {"pageNum":1,"pageSize":10,"size":2,"orderBy":"create_time DESC","startRow":1,"endRow":2,"total":2,"pages":1,"list":[{"id":"0a05e786e0a943c0993813cf93643584","createTime":"2017-08-18","name":"\u201c分水岭\u201d大时代","author":"廖崇涛","doubanScore":8.5,"coverPath":"/book/201707171234.png"},{"id":"356dea623d634cd0aecd790492f53468","createTime":"2017-08-18","name":"钓愚","author":"穆先念","doubanScore":8.5,"coverPath":"/book/201707171234.png"}],"firstPage":1,"prePage":0,"nextPage":0,"lastPage":1,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1]}
     */

    private int responseCode;
    /**
     * pageNum : 1
     * pageSize : 10
     * size : 2
     * orderBy : create_time DESC
     * startRow : 1
     * endRow : 2
     * total : 2
     * pages : 1
     * list : [{"id":"0a05e786e0a943c0993813cf93643584","createTime":"2017-08-18","name":"\u201c分水岭\u201d大时代","author":"廖崇涛","doubanScore":8.5,"coverPath":"/book/201707171234.png"},{"id":"356dea623d634cd0aecd790492f53468","createTime":"2017-08-18","name":"钓愚","author":"穆先念","doubanScore":8.5,"coverPath":"/book/201707171234.png"}]
     * firstPage : 1
     * prePage : 0
     * nextPage : 0
     * lastPage : 1
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
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
        private int pageNum;
        private int pageSize;
        private int size;
        private String orderBy;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private int firstPage;
        private int prePage;
        private int nextPage;
        private int lastPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        /**
         * id : 0a05e786e0a943c0993813cf93643584
         * createTime : 2017-08-18
         * name : “分水岭”大时代
         * author : 廖崇涛
         * doubanScore : 8.5
         * coverPath : /book/201707171234.png
         * "ownerId": "b55e17347e1f4b52b1ade8302c1a3ba4",
         "ownerName": "雨天的尾巴",
         "ownerHeadPath": "/head/default/ytdwb.png",
         */

        private List<ListBean> list;
        private List<Integer> navigatepageNums;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean {
            private String id;
            private String createTime;
            private String name;
            private String author;
            private double doubanScore;
            private String coverPath;
            private String ownerId;
            private String ownerName;
            private String ownerHeadPath;

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

            public double getDoubanScore() {
                return doubanScore;
            }

            public void setDoubanScore(double doubanScore) {
                this.doubanScore = doubanScore;
            }

            public String getCoverPath() {
                return coverPath;
            }

            public void setCoverPath(String coverPath) {
                this.coverPath = coverPath;
            }
        }
    }
}
