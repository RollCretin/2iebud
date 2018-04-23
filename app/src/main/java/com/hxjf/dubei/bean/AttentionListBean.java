package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class AttentionListBean {

    /**
     * responseCode : 1
     * responseMsg :
     * responseData : [{"id":"xxx","userId":"344d44b31b7a4ae6b8bf87b1104cf09f","attentionUserId":"056aeac09d5d4f9bb1a8b638b062a023","attentionUserName":"廖崇涛","attentionUserHeadPath":"/wechat/201801/16174140CDGCKB.png"}]
     */

    private int responseCode;
    private String responseMsg;
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
        /**
         * id : xxx
         * userId : 344d44b31b7a4ae6b8bf87b1104cf09f
         * attentionUserId : 056aeac09d5d4f9bb1a8b638b062a023
         * attentionUserName : 廖崇涛
         * attentionUserHeadPath : /wechat/201801/16174140CDGCKB.png
         */

        private String id;
        private String userId;
        private String attentionUserId;
        private String attentionUserName;
        private String attentionUserHeadPath;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAttentionUserId() {
            return attentionUserId;
        }

        public void setAttentionUserId(String attentionUserId) {
            this.attentionUserId = attentionUserId;
        }

        public String getAttentionUserName() {
            return attentionUserName;
        }

        public void setAttentionUserName(String attentionUserName) {
            this.attentionUserName = attentionUserName;
        }

        public String getAttentionUserHeadPath() {
            return attentionUserHeadPath;
        }

        public void setAttentionUserHeadPath(String attentionUserHeadPath) {
            this.attentionUserHeadPath = attentionUserHeadPath;
        }
    }
}
