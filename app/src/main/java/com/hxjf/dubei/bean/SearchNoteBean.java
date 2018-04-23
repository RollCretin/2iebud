package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/28.
 */

public class SearchNoteBean {

    /**
     * responseCode : 1
     * responseData : {"content":[{"id":"5979acf671fd4394ff6d8bdc","title":"xxxxxx","coverId":"xxxxxxxx","lecturer":"xxxx","coverPath":"xxxxx","tag":1,"tagValue":"大咖讲座","classify":1,"classifyValue":"互联网","contentAbout":"xxxxx"}],"totalElements":0,"totalPages":0,"last":true,"numberOfElements":0,"size":10,"number":0,"first":true}
     */

    private int responseCode;
    /**
     * content : [{"id":"5979acf671fd4394ff6d8bdc","title":"xxxxxx","coverId":"xxxxxxxx","lecturer":"xxxx","coverPath":"xxxxx","tag":1,"tagValue":"大咖讲座","classify":1,"classifyValue":"互联网","contentAbout":"xxxxx"}]
     * totalElements : 0
     * totalPages : 0
     * last : true
     * numberOfElements : 0
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
         * id : 5979acf671fd4394ff6d8bdc
         * title : xxxxxx
         * coverId : xxxxxxxx
         * lecturer : xxxx
         * coverPath : xxxxx
         * tag : 1
         * tagValue : 大咖讲座
         * classify : 1
         * classifyValue : 互联网
         * contentAbout : xxxxx
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

        public static class ContentBean {
            private String id;
            private String title;
            private String coverId;
            private String lecturer;
            private String coverPath;
            private int tag;
            private String tagValue;
            private int classify;
            private String classifyValue;
            private String contentAbout;

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

            public String getCoverId() {
                return coverId;
            }

            public void setCoverId(String coverId) {
                this.coverId = coverId;
            }

            public String getLecturer() {
                return lecturer;
            }

            public void setLecturer(String lecturer) {
                this.lecturer = lecturer;
            }

            public String getCoverPath() {
                return coverPath;
            }

            public void setCoverPath(String coverPath) {
                this.coverPath = coverPath;
            }

            public int getTag() {
                return tag;
            }

            public void setTag(int tag) {
                this.tag = tag;
            }

            public String getTagValue() {
                return tagValue;
            }

            public void setTagValue(String tagValue) {
                this.tagValue = tagValue;
            }

            public int getClassify() {
                return classify;
            }

            public void setClassify(int classify) {
                this.classify = classify;
            }

            public String getClassifyValue() {
                return classifyValue;
            }

            public void setClassifyValue(String classifyValue) {
                this.classifyValue = classifyValue;
            }

            public String getContentAbout() {
                return contentAbout;
            }

            public void setContentAbout(String contentAbout) {
                this.contentAbout = contentAbout;
            }
        }
    }
}
