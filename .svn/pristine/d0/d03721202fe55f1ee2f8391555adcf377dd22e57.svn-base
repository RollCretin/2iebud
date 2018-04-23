package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/22.
 */

public class BookClassifyBean {

    /**
     * responseCode : 1
     * responseData : {"menuInfo":[{"text":"1","value":"经管","childInfo":[]},{"text":"2","value":"营销","childInfo":[]},{"text":"3","value":"互联网","childInfo":[]},{"text":"4","value":"职场","childInfo":[]},{"text":"5","value":"传记","childInfo":[]},{"text":"6","value":"励志","childInfo":[]},{"text":"7","value":"哲学","childInfo":[]},{"text":"8","value":"心理学","childInfo":[]},{"text":"9","value":"历史","childInfo":[]},{"text":"10","value":"金融","childInfo":[]},{"text":"11","value":"小说","childInfo":[]},{"text":"12","value":"其他","childInfo":[]}]}
     */

    private int responseCode;
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
        /**
         * text : 1
         * value : 经管
         * childInfo : []
         */

        private List<MenuInfoBean> menuInfo;

        public List<MenuInfoBean> getMenuInfo() {
            return menuInfo;
        }

        public void setMenuInfo(List<MenuInfoBean> menuInfo) {
            this.menuInfo = menuInfo;
        }

        public static class MenuInfoBean {
            private String text;
            private String value;
            private List<?> childInfo;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public List<?> getChildInfo() {
                return childInfo;
            }

            public void setChildInfo(List<?> childInfo) {
                this.childInfo = childInfo;
            }
        }
    }
}
