package com.hxjf.dubei.bean;

import java.io.Serializable;

/**
 * Created by Chen_Zhang on 2017/7/18.
 */

public class BindBean implements Serializable {

    @Override
    public String toString() {
        return "BindBean{" +
                "responseCode=" + responseCode +
                ", responseMsg='" + responseMsg + '\'' +
                ", responseData=" + responseData +
                '}';
    }

    private static final long serialVersionUID = -807353761888154634L;
    /**
     * responseCode : 0
     * responseMsg : 登录成功
     * responseData : {"id":"20b1f32101164558aef827d862091856","telephone":"13111111111","nickName":"齐天大圣","sex":"1","sexValue":"男","intro":"谜一样的男人","tag":"大咖、互联网","readDuration":3600,"imageInfo":{"normalPath":"头像路径"}}
     */

    private int responseCode;
    private String responseMsg;
    /**
     * id : 20b1f32101164558aef827d862091856
     * telephone : 13111111111
     * nickName : 齐天大圣
     * sex : 1
     * sexValue : 男
     * intro : 谜一样的男人
     * tag : 大咖、互联网
     * readDuration : 3600
     * imageInfo : {"normalPath":"头像路径"}
     */

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
        private String id;
        private String telephone;
        private String nickName;
        private String sex;
        private String sexValue;
        private String intro;
        private String tag;
        private int readDuration;
        /**
         * normalPath : 头像路径
         */

        private ImageInfoBean imageInfo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSexValue() {
            return sexValue;
        }

        public void setSexValue(String sexValue) {
            this.sexValue = sexValue;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getReadDuration() {
            return readDuration;
        }

        public void setReadDuration(int readDuration) {
            this.readDuration = readDuration;
        }

        public ImageInfoBean getImageInfo() {
            return imageInfo;
        }

        public void setImageInfo(ImageInfoBean imageInfo) {
            this.imageInfo = imageInfo;
        }

        public static class ImageInfoBean {
            private String normalPath;

            public String getNormalPath() {
                return normalPath;
            }

            public void setNormalPath(String normalPath) {
                this.normalPath = normalPath;
            }
        }
    }
}
