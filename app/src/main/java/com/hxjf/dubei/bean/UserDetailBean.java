package com.hxjf.dubei.bean;

import java.io.Serializable;

/**
 * Created by Chen_Zhang on 2017/8/13.
 */

public class UserDetailBean implements Serializable {

    private static final long serialVersionUID = 1155535219576509186L;
    /**
     * responseCode : 1
     * responseMsg:"登录成功"
     * responseData : {"id":"c81de91e0c1e42caac1e639c0c898b46","telephone":"13083338339","nickName":"smaile","sex":2,"sexValue":"女","tag":"资深阅读爱好者、代码喵、你好","intro":"melding","readDuration":18867,"vipStatus":1,"vipStatusValue":"未开通","vipOverTime":"2018-01-01","password":"ce17bbf926c716c63835258ecc766d5a","normalPath":"/wechat/201711/02180111DVKJLH.png","hasBookTag":true,"msgNum":0,"accountMoney":0}
     */

    private int responseCode;
    /**
     * id : c81de91e0c1e42caac1e639c0c898b46
     * telephone : 13083338339
     * nickName : smaile
     * sex : 2
     * sexValue : 女
     * tag : 资深阅读爱好者、代码喵、你好
     * intro : melding
     * readDuration : 18867
     * vipStatus : 1
     * vipStatusValue : 未开通
     * vipOverTime : 2018-01-01
     *
     * "giveReadCoin": 0,
     * password : ce17bbf926c716c63835258ecc766d5a
     * normalPath : /wechat/201711/02180111DVKJLH.png
     * hasBookTag : true
     * msgNum : 0
     * accountMoney : 0
     * attentionFlag
     */
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

    public static class ResponseDataBean implements Serializable {
        private static final long serialVersionUID = 1520629609385280799L;
        private String id;
        private String telephone;
        private String nickName;
        private int sex;
        private String sexValue;
        private String tag;
        private String intro;
        private int readDuration;
        private int vipStatus;
        private String vipStatusValue;
        private String vipOverTime;
        private int giveReadCoin;
        private String password;
        private String normalPath;
        private boolean hasBookTag;
        private int msgNum;
        private int attentionCount;
        private double accountMoney;
        private boolean attentionFlag;

        public boolean isAttentionFlag() {
            return attentionFlag;
        }


        public void setAttentionFlag(boolean attentionFlag) {
            this.attentionFlag = attentionFlag;
        }

        public int getAttentionCount() {
            return attentionCount;
        }

        public void setAttentionCount(int attentionCount) {
            this.attentionCount = attentionCount;
        }
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSexValue() {
            return sexValue;
        }

        public void setSexValue(String sexValue) {
            this.sexValue = sexValue;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getReadDuration() {
            return readDuration;
        }

        public void setReadDuration(int readDuration) {
            this.readDuration = readDuration;
        }

        public int getVipStatus() {
            return vipStatus;
        }

        public void setVipStatus(int vipStatus) {
            this.vipStatus = vipStatus;
        }

        public String getVipStatusValue() {
            return vipStatusValue;
        }

        public void setVipStatusValue(String vipStatusValue) {
            this.vipStatusValue = vipStatusValue;
        }

        public String getVipOverTime() {
            return vipOverTime;
        }

        public void setVipOverTime(String vipOverTime) {
            this.vipOverTime = vipOverTime;
        }

        public int getGiveReadCoin() {
            return giveReadCoin;
        }

        public void setGiveReadCoin(int giveReadCoin) {
            this.giveReadCoin = giveReadCoin;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNormalPath() {
            return normalPath;
        }

        public void setNormalPath(String normalPath) {
            this.normalPath = normalPath;
        }

        public boolean isHasBookTag() {
            return hasBookTag;
        }

        public void setHasBookTag(boolean hasBookTag) {
            this.hasBookTag = hasBookTag;
        }

        public int getMsgNum() {
            return msgNum;
        }

        public void setMsgNum(int msgNum) {
            this.msgNum = msgNum;
        }

        public double getAccountMoney() {
            return accountMoney;
        }

        public void setAccountMoney(double accountMoney) {
            this.accountMoney = accountMoney;
        }
    }
}
