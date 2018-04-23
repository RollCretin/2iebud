package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/12/20.
 */

public class ActivityListBean {

    /**
     * responseCode : 1
     * responseData : {"content":[{"id":"5a2e4a7b0cf2425620a7ba5b","title":"读书交流会2","tag":"123456","banner":"/activity/201712/11170603ae90.jpg","ownerName":"111","beginTime":"123456","endTime":"123546"},{"id":"5a2e2bc30cf2f34dbee62991","title":"读书交流会","tag":"11","banner":"/activity/201712/11145458pzx2.jpg","ownerName":"111","beginTime":"20171212","endTime":"20171215"}],"totalElements":2,"last":true,"totalPages":1,"numberOfElements":2,"first":true,"size":10,"number":0}
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
         * content : [{"id":"5a2e4a7b0cf2425620a7ba5b","title":"读书交流会2","tag":"123456","banner":"/activity/201712/11170603ae90.jpg","ownerName":"111","beginTime":"123456","endTime":"123546"},{"id":"5a2e2bc30cf2f34dbee62991","title":"读书交流会","tag":"11","banner":"/activity/201712/11145458pzx2.jpg","ownerName":"111","beginTime":"20171212","endTime":"20171215"}]
         * totalElements : 2
         * last : true
         * totalPages : 1
         * numberOfElements : 2
         * first : true
         * size : 10
         * number : 0
         */

        private int totalElements;
        private boolean last;
        private int totalPages;
        private int numberOfElements;
        private boolean first;
        private int size;
        private int number;
        private List<ContentBean> content;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
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

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean {
            /**
             * id : 5a2e4a7b0cf2425620a7ba5b
             * title : 读书交流会2
             * tag : 123456
             * banner : /activity/201712/11170603ae90.jpg
             * ownerName : 111
             * beginTime : 123456
             * endTime : 123546
             * "time": "2018/1/20-2018/1/22"
             */

            private String id;
            private String title;
            private String tag;
            private String banner;
            private String ownerName;
            private String beginTime;
            private String endTime;
            private String time;

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

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getBanner() {
                return banner;
            }

            public void setBanner(String banner) {
                this.banner = banner;
            }

            public String getOwnerName() {
                return ownerName;
            }

            public void setOwnerName(String ownerName) {
                this.ownerName = ownerName;
            }

            public String getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(String beginTime) {
                this.beginTime = beginTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
