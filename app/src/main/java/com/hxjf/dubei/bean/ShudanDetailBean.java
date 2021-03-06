package com.hxjf.dubei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/30.
 */

public class ShudanDetailBean implements Serializable {

    private static final long serialVersionUID = -6478204679271650187L;
    /**
     * responseCode : 1
     * responseData : {"title":"测试002","summary":"<p>002<\/p>","cover":"","shareTotal":2,"bookCount":1,"id":"59f000a0fa07b31e7d06002e","collected":false,"praiseCount":0,"collectCount":1,"books":[{"name":"旅行与读书","cover":"/book/201710/2310531350ty.jpg","author":"詹宏志","id":"20f6d59639334f09818966a2e5235688","contentAbout":"《旅行与读书》是一本没有图片的旅行游记，不提供可以按图索骥的旅行指南，却带有丰富的想象与画面感。永不止步的前行者詹宏志，总是在阅读，始终在路上。他的旅行多由读书而起，十个推理小说般的精彩故事，犹如一场百科知识的脑内冲浪与日常哲思的马拉松。被一本托斯卡纳食谱指南引路的摊牌考验；因尽信书而惊险万分的瑞士登山之旅；在印度无力招架念诵着鲁拜集的高明地毯商人；在南非草丛中充满生命体验的萨伐旅；在灾难过后的巴厘岛矛盾复杂的旅人心境\u2026\u2026十场梦幻旅，别样新人生。","path":"/book/0200251603.epub","type":1,"wordNumber":195314,"doubanScore":7.8,"paid":false,"canFreeRead":false,"inBookShelf":true}]}
     */

    private int responseCode;
    /**
     * title : 测试002
     * summary : <p>002</p>
     * cover :
     * shareTotal : 2
     * bookCount : 1
     * id : 59f000a0fa07b31e7d06002e
     * collected : false
     * praiseCount : 0
     * collectCount : 1
     * "ownerId": "04021d4f7e3b480c878f4ae94638ce8c",
     "ownerName": "白月初",
     "ownerHeadPath": "/head/default/byc.png"
     * books : [{"name":"旅行与读书","cover":"/book/201710/2310531350ty.jpg","author":"詹宏志","id":"20f6d59639334f09818966a2e5235688","contentAbout":"《旅行与读书》是一本没有图片的旅行游记，不提供可以按图索骥的旅行指南，却带有丰富的想象与画面感。永不止步的前行者詹宏志，总是在阅读，始终在路上。他的旅行多由读书而起，十个推理小说般的精彩故事，犹如一场百科知识的脑内冲浪与日常哲思的马拉松。被一本托斯卡纳食谱指南引路的摊牌考验；因尽信书而惊险万分的瑞士登山之旅；在印度无力招架念诵着鲁拜集的高明地毯商人；在南非草丛中充满生命体验的萨伐旅；在灾难过后的巴厘岛矛盾复杂的旅人心境\u2026\u2026十场梦幻旅，别样新人生。","path":"/book/0200251603.epub","type":1,"wordNumber":195314,"doubanScore":7.8,"paid":false,"canFreeRead":false,"inBookShelf":true}]
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

    public static class ResponseDataBean implements Serializable{
        private static final long serialVersionUID = -9059334590492560217L;
        private String title;
        private String summary;
        private String cover;
        private int shareTotal;
        private int bookCount;
        private String id;
        private boolean collected;
        private int praiseCount;
        private int collectCount;
        private String ownerId;
        private String ownerName;
        private String ownerHeadPath;
        /**
         * name : 旅行与读书
         * cover : /book/201710/2310531350ty.jpg
         * author : 詹宏志
         * id : 20f6d59639334f09818966a2e5235688
         * contentAbout : 《旅行与读书》是一本没有图片的旅行游记，不提供可以按图索骥的旅行指南，却带有丰富的想象与画面感。永不止步的前行者詹宏志，总是在阅读，始终在路上。他的旅行多由读书而起，十个推理小说般的精彩故事，犹如一场百科知识的脑内冲浪与日常哲思的马拉松。被一本托斯卡纳食谱指南引路的摊牌考验；因尽信书而惊险万分的瑞士登山之旅；在印度无力招架念诵着鲁拜集的高明地毯商人；在南非草丛中充满生命体验的萨伐旅；在灾难过后的巴厘岛矛盾复杂的旅人心境……十场梦幻旅，别样新人生。
         * path : /book/0200251603.epub
         * type : 1
         * wordNumber : 195314
         * doubanScore : 7.8
         * paid : false
         * canFreeRead : false
         * inBookShelf : true
         */

        private List<BooksBean> books;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getShareTotal() {
            return shareTotal;
        }

        public void setShareTotal(int shareTotal) {
            this.shareTotal = shareTotal;
        }

        public int getBookCount() {
            return bookCount;
        }

        public void setBookCount(int bookCount) {
            this.bookCount = bookCount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }

        public int getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(int praiseCount) {
            this.praiseCount = praiseCount;
        }

        public int getCollectCount() {
            return collectCount;
        }

        public void setCollectCount(int collectCount) {
            this.collectCount = collectCount;
        }

        public List<BooksBean> getBooks() {
            return books;
        }

        public void setBooks(List<BooksBean> books) {
            this.books = books;
        }

        public static class BooksBean implements Serializable{
            private static final long serialVersionUID = -1566002982013435609L;
            private String name;
            private String cover;
            private String author;
            private String id;
            private String contentAbout;
            private String path;
            private int type;
            private int wordNumber;
            private double doubanScore;
            private boolean paid;
            private boolean canFreeRead;
            private boolean inBookShelf;
            private int status;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getWordNumber() {
                return wordNumber;
            }

            public void setWordNumber(int wordNumber) {
                this.wordNumber = wordNumber;
            }

            public double getDoubanScore() {
                return doubanScore;
            }

            public void setDoubanScore(double doubanScore) {
                this.doubanScore = doubanScore;
            }

            public boolean isPaid() {
                return paid;
            }

            public void setPaid(boolean paid) {
                this.paid = paid;
            }

            public boolean isCanFreeRead() {
                return canFreeRead;
            }

            public void setCanFreeRead(boolean canFreeRead) {
                this.canFreeRead = canFreeRead;
            }

            public boolean isInBookShelf() {
                return inBookShelf;
            }

            public void setInBookShelf(boolean inBookShelf) {
                this.inBookShelf = inBookShelf;
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
