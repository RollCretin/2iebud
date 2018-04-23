package com.hxjf.dubei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/25.
 */

public class BSBookListBean implements Serializable {

    private static final long serialVersionUID = -7647762475133657553L;
    /**
     * responseCode : 1
     * responseData : [{"id":"5a016cdf0cf24525003515b0","bookId":"20f6d59639334f09818966a2e5235688","progress":0,"readTime":9,"readStatus":2,"statusValue":"阅读中","challengeFlag":0,"flagValue":"未在挑战中","bookShelfFlag":1,"bookInfo":{"id":"20f6d59639334f09818966a2e5235688","name":"旅行与读书","author":"詹宏志","wordNumber":195314,"contentAbout":"《旅行与读书》是一本没有图片的旅行游记，不提供可以按图索骥的旅行指南，却带有丰富的想象与画面感。永不止步的前行者詹宏志，总是在阅读，始终在路上。他的旅行多由读书而起，十个推理小说般的精彩故事，犹如一场百科知识的脑内冲浪与日常哲思的马拉松。被一本托斯卡纳食谱指南引路的摊牌考验；因尽信书而惊险万分的瑞士登山之旅；在印度无力招架念诵着鲁拜集的高明地毯商人；在南非草丛中充满生命体验的萨伐旅；在灾难过后的巴厘岛矛盾复杂的旅人心境\u2026\u2026十场梦幻旅，别样新人生。","path":"/book/0200251603.epub","coverPath":"/book/201710/2310531350ty.jpg","status":1},"canFreeRead":true},{"id":"5a0176990cf2b2bfe6a27a8e","bookId":"aad02637eeca4836b5d3d812270084c8","progress":0,"readTime":0,"readStatus":1,"statusValue":"未读","challengeFlag":0,"flagValue":"未在挑战中","bookShelfFlag":1,"bookInfo":{"id":"aad02637eeca4836b5d3d812270084c8","name":"六西格玛管理法（珍藏版）","author":"[美] 潘迪，[美] 纽曼，[美]卡瓦纳","wordNumber":214084,"contentAbout":"在整个企业流程中，六西格玛是指每百万次机会中有多少缺陷或失误，包括产品本身以及产品生产的流程、运输、系统故障、不可抗力等。六西格玛管理要求企业在整个流程每百万次机会中的缺陷少于3.4次。\r\n\r\n麦肯锡公司的调查研究表明，如果一个3西格玛企业组织其所有资源改进过程，大约每年可以提高一个西格玛水平，即每年可以获得以下收益：利润率增加20%；产能提高12%～18%；雇员减少12%；资本投入减少10%～30%，而且直至提升到4.8西格玛水平，企业均无须大的资本投入，当达到4.8西格玛水平时，再提高一个西格玛则需要增加投入，但此时产品的竞争力已大幅提高，市场占有率极高，给企业带来的利润将远远大于此时的投入。\r\n\r\n本书是国内引进的第一本全面介绍六西格玛管理法的经典图书，自2001年出版以来已畅销十多年。本书第2版加入了这十几年来诸多公司实施六西格玛的经验，并以此证明了书中绝大多数核心准则和工具的适用性。","path":"/book/8678095332.epub","coverPath":"/book/201710/2217314356si.jpg","status":1},"canFreeRead":false}]
     */

    private int responseCode;
    /**
     * id : 5a016cdf0cf24525003515b0
     * bookId : 20f6d59639334f09818966a2e5235688
     * progress : 0
     * readTime : 9
     * readStatus : 2
     * statusValue : 阅读中
     * challengeFlag : 0
     * flagValue : 未在挑战中
     * bookShelfFlag : 1
     * bookInfo : {"id":"20f6d59639334f09818966a2e5235688","name":"旅行与读书","author":"詹宏志","wordNumber":195314,"contentAbout":"《旅行与读书》是一本没有图片的旅行游记，不提供可以按图索骥的旅行指南，却带有丰富的想象与画面感。永不止步的前行者詹宏志，总是在阅读，始终在路上。他的旅行多由读书而起，十个推理小说般的精彩故事，犹如一场百科知识的脑内冲浪与日常哲思的马拉松。被一本托斯卡纳食谱指南引路的摊牌考验；因尽信书而惊险万分的瑞士登山之旅；在印度无力招架念诵着鲁拜集的高明地毯商人；在南非草丛中充满生命体验的萨伐旅；在灾难过后的巴厘岛矛盾复杂的旅人心境\u2026\u2026十场梦幻旅，别样新人生。","path":"/book/0200251603.epub","coverPath":"/book/201710/2310531350ty.jpg","status":1}
     * canFreeRead : true
     */

    private List<ResponseDataBean> responseData;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<ResponseDataBean> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseDataBean> responseData) {
        this.responseData = responseData;
    }

    public static class ResponseDataBean implements Serializable{
        private static final long serialVersionUID = 8085220033597976341L;
        private String id;
        private String bookId;
        private int progress;
        private int readTime;
        private int readStatus;
        private String statusValue;
        private int challengeFlag;
        private String flagValue;
        private int bookShelfFlag;
        /**
         * id : 20f6d59639334f09818966a2e5235688
         * name : 旅行与读书
         * author : 詹宏志
         * wordNumber : 195314
         * contentAbout : 《旅行与读书》是一本没有图片的旅行游记，不提供可以按图索骥的旅行指南，却带有丰富的想象与画面感。永不止步的前行者詹宏志，总是在阅读，始终在路上。他的旅行多由读书而起，十个推理小说般的精彩故事，犹如一场百科知识的脑内冲浪与日常哲思的马拉松。被一本托斯卡纳食谱指南引路的摊牌考验；因尽信书而惊险万分的瑞士登山之旅；在印度无力招架念诵着鲁拜集的高明地毯商人；在南非草丛中充满生命体验的萨伐旅；在灾难过后的巴厘岛矛盾复杂的旅人心境……十场梦幻旅，别样新人生。
         * path : /book/0200251603.epub
         * coverPath : /book/201710/2310531350ty.jpg
         * status : 1
         */

        private BookInfoBean bookInfo;
        private boolean canFreeRead;

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

        public int getChallengeFlag() {
            return challengeFlag;
        }

        public void setChallengeFlag(int challengeFlag) {
            this.challengeFlag = challengeFlag;
        }

        public String getFlagValue() {
            return flagValue;
        }

        public void setFlagValue(String flagValue) {
            this.flagValue = flagValue;
        }

        public int getBookShelfFlag() {
            return bookShelfFlag;
        }

        public void setBookShelfFlag(int bookShelfFlag) {
            this.bookShelfFlag = bookShelfFlag;
        }

        public BookInfoBean getBookInfo() {
            return bookInfo;
        }

        public void setBookInfo(BookInfoBean bookInfo) {
            this.bookInfo = bookInfo;
        }

        public boolean isCanFreeRead() {
            return canFreeRead;
        }

        public void setCanFreeRead(boolean canFreeRead) {
            this.canFreeRead = canFreeRead;
        }

        public static class BookInfoBean implements Serializable {
            private static final long serialVersionUID = -8296140622089550684L;
            private String id;
            private String name;
            private String author;
            private int wordNumber;
            private String contentAbout;
            private String path;
            private String coverPath;
            private int status;

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

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
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
