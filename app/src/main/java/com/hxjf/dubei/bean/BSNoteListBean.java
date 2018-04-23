package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/25.
 */

public class BSNoteListBean {

    /**
     * responseCode : 1
     * responseMsg : 部分笔记工厂数据异常，请重试
     * responseData : [{"id":"59c385800cf277518c123e54","factoryId":"59b8d8840cf2ce4ab13ce617","readTime":0,"readStatus":1,"statusValue":"未读","noteFactoryInfo":{"id":"59b8d8840cf2ce4ab13ce617","title":"吴晓波：算法时代，你可能已掉入信息陷阱","coverId":"e6a292bb6d4a46b59d982f7f285e937e","wordNumber":1376,"contentAbout":"信息时代，你受到的困扰有哪些？信息时代，会产生哪两种效应？有什么方法可以解决信息陷阱？","coverPath":"/noteFactory/201709/13150435fzit.jpg"}},{"id":"59c387dd0cf277518c123e5d","factoryId":"599f88cb211c5c6e15cdc995","readTime":1300,"readStatus":3,"statusValue":"已阅完","noteFactoryInfo":{"id":"599f88cb211c5c6e15cdc995","title":"你这么努力，为什么比不过贪玩的人？","coverId":"1dd1df69c7714837bcf89d19693036ab","wordNumber":3535,"contentAbout":"曼弗雷德在《正念领导力》里谈到\u201c事实上，玩耍是一种可贵的学习形式，是创造性生产的源泉。只工作不玩耍，人们就无法发挥最佳潜力。\u201d所有辛勤的职场人，请在繁忙的工作之余歇一歇，为自己预留一点放松和玩耍的时间","coverPath":"/note/bijigongchang.png"}},{"id":"59e81bbe0cf213598f56c66b","factoryId":"59e746a10cf290012cfdff4e","readTime":0,"readStatus":1,"statusValue":"未读","noteFactoryInfo":{"id":"59e746a10cf290012cfdff4e","title":"奥斯卡影帝卡梅隆：失败是你的选项，但畏惧一定不是！","coverId":"041d7079762f47258ae6a94108ea81c9","wordNumber":3016,"contentAbout":"这是一篇TED演讲，讲者是《阿凡达》的导演詹姆斯·卡梅隆（James Cameron）。在这个演讲里，卡梅隆回顾了自己从电影学院毕业后走上导演道路的故事。卡梅隆最后总结了一句话：\u201c失败是你其中一个选项，但畏惧不是。\u201dcuriosity imagination and respect,卡梅隆告诉你，永远不要给自己设限。","coverPath":"/noteFactory/201710/18201841q27o.jpg"}}]
     */

    private int responseCode;
    private String responseMsg;
    /**
     * id : 59c385800cf277518c123e54
     * factoryId : 59b8d8840cf2ce4ab13ce617
     * readTime : 0
     * readStatus : 1
     * statusValue : 未读
     * noteFactoryInfo : {"id":"59b8d8840cf2ce4ab13ce617","title":"吴晓波：算法时代，你可能已掉入信息陷阱","coverId":"e6a292bb6d4a46b59d982f7f285e937e","wordNumber":1376,"contentAbout":"信息时代，你受到的困扰有哪些？信息时代，会产生哪两种效应？有什么方法可以解决信息陷阱？","coverPath":"/noteFactory/201709/13150435fzit.jpg"}
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
        private String factoryId;
        private int readTime;
        private int readStatus;
        private String statusValue;
        /**
         * id : 59b8d8840cf2ce4ab13ce617
         * title : 吴晓波：算法时代，你可能已掉入信息陷阱
         * coverId : e6a292bb6d4a46b59d982f7f285e937e
         * wordNumber : 1376
         * contentAbout : 信息时代，你受到的困扰有哪些？信息时代，会产生哪两种效应？有什么方法可以解决信息陷阱？
         * coverPath : /noteFactory/201709/13150435fzit.jpg
         */

        private NoteFactoryInfoBean noteFactoryInfo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFactoryId() {
            return factoryId;
        }

        public void setFactoryId(String factoryId) {
            this.factoryId = factoryId;
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

        public NoteFactoryInfoBean getNoteFactoryInfo() {
            return noteFactoryInfo;
        }

        public void setNoteFactoryInfo(NoteFactoryInfoBean noteFactoryInfo) {
            this.noteFactoryInfo = noteFactoryInfo;
        }

        public static class NoteFactoryInfoBean {
            private String id;
            private String title;
            private String coverId;
            private int wordNumber;
            private String contentAbout;
            private String coverPath;
            private int status;

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

            public int getWordNumber() {
                return wordNumber;
            }

            public void setWordNumber(int wordNumber) {
                this.wordNumber = wordNumber;
            }

            public String getContentAbout() {
                return contentAbout;
            }

            public void setContentAbout(String contentAbout) {
                this.contentAbout = contentAbout;
            }

            public String getCoverPath() {
                return coverPath;
            }

            public void setCoverPath(String coverPath) {
                this.coverPath = coverPath;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
