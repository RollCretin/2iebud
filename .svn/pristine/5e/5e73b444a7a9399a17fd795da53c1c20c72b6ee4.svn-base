package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/5.
 */

public class HomeChallengeDetailBean {

    /**
     * responseCode : 1
     * responseData : {"id":"5981475b0cf2d7a9f287629c","bookId":"testbook909090909090","startTime":"20170807","endTime":"20170816","total":1,"challengeStatus":1,"challengeStatusValue":"即将开始","createUid":"9c18e50767ec4ba2b419570c4109abcf","challengeUserList":[{"id":"5981475b0cf2d7a9f287629d","challengeId":"5981475b0cf2d7a9f287629c","userId":"9c18e50767ec4ba2b419570c4109abcf","progress":0,"readTime":0,"result":1,"participantUser":{"id":"9c18e50767ec4ba2b419570c4109abcf","nickName":"蛋糕","normalPath":"/image/head/201708/04173436ef2m.jpg","hasBookTag":false},"praiseFlag":false}],"bookInfo":{"id":"testbook909090909090","name":"小王子909090909090","author":"[法] 安托万\u2022德\u2022圣埃克苏佩里","type":1,"normalPath":"/image/book/201707171234.png"}}
     */

    private int responseCode;
    /**
     * id : 5981475b0cf2d7a9f287629c
     * bookId : testbook909090909090
     * startTime : 20170807
     * endTime : 20170816
     * total : 1
     * challengeStatus : 1
     * challengeStatusValue : 即将开始
     * createUid : 9c18e50767ec4ba2b419570c4109abcf
     * challengeUserList : [{"id":"5981475b0cf2d7a9f287629d","challengeId":"5981475b0cf2d7a9f287629c","userId":"9c18e50767ec4ba2b419570c4109abcf","progress":0,"readTime":0,"result":1,"participantUser":{"id":"9c18e50767ec4ba2b419570c4109abcf","nickName":"蛋糕","normalPath":"/image/head/201708/04173436ef2m.jpg","hasBookTag":false},"praiseFlag":false}]
     * bookInfo : {"id":"testbook909090909090","name":"小王子909090909090","author":"[法] 安托万\u2022德\u2022圣埃克苏佩里","type":1,"normalPath":"/image/book/201707171234.png"}
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
        private String bookId;
        private String startTime;
        private String endTime;
        private int total;
        private int challengeStatus;
        private String challengeStatusValue;
        private String createUid;
        /**
         * id : testbook909090909090
         * name : 小王子909090909090
         * author : [法] 安托万•德•圣埃克苏佩里
         * type : 1
         * normalPath : /image/book/201707171234.png
         */

        private BookInfoBean bookInfo;
        /**
         * id : 5981475b0cf2d7a9f287629d
         * challengeId : 5981475b0cf2d7a9f287629c
         * userId : 9c18e50767ec4ba2b419570c4109abcf
         * progress : 0
         * readTime : 0
         * result : 1
         * participantUser : {"id":"9c18e50767ec4ba2b419570c4109abcf","nickName":"蛋糕","normalPath":"/image/head/201708/04173436ef2m.jpg","hasBookTag":false}
         * praiseFlag : false
         */

        private List<ChallengeUserListBean> challengeUserList;

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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getChallengeStatus() {
            return challengeStatus;
        }

        public void setChallengeStatus(int challengeStatus) {
            this.challengeStatus = challengeStatus;
        }

        public String getChallengeStatusValue() {
            return challengeStatusValue;
        }

        public void setChallengeStatusValue(String challengeStatusValue) {
            this.challengeStatusValue = challengeStatusValue;
        }

        public String getCreateUid() {
            return createUid;
        }

        public void setCreateUid(String createUid) {
            this.createUid = createUid;
        }

        public BookInfoBean getBookInfo() {
            return bookInfo;
        }

        public void setBookInfo(BookInfoBean bookInfo) {
            this.bookInfo = bookInfo;
        }

        public List<ChallengeUserListBean> getChallengeUserList() {
            return challengeUserList;
        }

        public void setChallengeUserList(List<ChallengeUserListBean> challengeUserList) {
            this.challengeUserList = challengeUserList;
        }

        public static class BookInfoBean {
            private String id;
            private String name;
            private String author;
            private int type;
            private String normalPath;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getNormalPath() {
                return normalPath;
            }

            public void setNormalPath(String normalPath) {
                this.normalPath = normalPath;
            }
        }

        public static class ChallengeUserListBean {
            private String id;
            private String challengeId;
            private String userId;
            private int progress;
            private int readTime;
            private int result;
            /**
             * id : 9c18e50767ec4ba2b419570c4109abcf
             * nickName : 蛋糕
             * normalPath : /image/head/201708/04173436ef2m.jpg
             * hasBookTag : false
             */

            private ParticipantUserBean participantUser;
            private boolean praiseFlag;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getChallengeId() {
                return challengeId;
            }

            public void setChallengeId(String challengeId) {
                this.challengeId = challengeId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
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

            public int getResult() {
                return result;
            }

            public void setResult(int result) {
                this.result = result;
            }

            public ParticipantUserBean getParticipantUser() {
                return participantUser;
            }

            public void setParticipantUser(ParticipantUserBean participantUser) {
                this.participantUser = participantUser;
            }

            public boolean isPraiseFlag() {
                return praiseFlag;
            }

            public void setPraiseFlag(boolean praiseFlag) {
                this.praiseFlag = praiseFlag;
            }

            public static class ParticipantUserBean {
                private String id;
                private String nickName;
                private String normalPath;
                private boolean hasBookTag;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
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
            }
        }
    }
}
