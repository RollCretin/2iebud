package com.hxjf.dubei.bean;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/21.
 */

public class SearchBookBean {

    /**
     * responseCode : 1
     * responseData : {"pageNum":1,"pageSize":10,"size":1,"orderBy":"","startRow":1,"endRow":1,"total":1,"pages":1,"list":[{"id":"36239dc354c840109b3726e1a7647559","name":"罗马人的故事1","author":"穆先念","wordNumber":148796,"contentAbout":"自亚当·斯密以来，经济学的核心信条就是：自由市场制度就像一只\u201c看不见的手\u201d，但现在这只\u201c看不见的手\u201d已经变成了随时准备绊倒消费者的\u201c看不见的脚\u201d。\n两位诺奖得主乔治·阿克洛夫、罗伯特·席勒认为，市场在为我们带来福利的同时，也带来了灾难。普遍存在的人性弱点、信息不对称等让我们成为\u201c钓愚\u201d中的受骗者。\n作者历数从投行到政客，从汽车销售到房地产中介，从烟厂到酒厂，从食品厂到制药商，从信用卡到广告，无不存在\u201c自由的欺骗\u201d。他们以一个个生动的事例、详实的数据、幽默的阐述揭示了从日常生活到金融、政治等层面的富有想象力的钓愚手段，从另一个角度反思了金融危机爆发的内在原因：钓愚不仅让普通大众损失惨重，而且带来了前所未有的系统性风险，导致经济崩溃。\n作者进一步强调，如果政策制定者、经济学家和普通大众都能意识到钓愚的普遍存在，就能辨明和杜绝可能导致严重危机的欺骗问题，从制度创新、市场干预等方面实施更有效的监管，同时避免不必要的浪费，从而增强对经济危机的预测和把握，让\u201c看不见的手\u201d更好地为经济、为社会服务。\n这本书无疑将永远改变我们看待市场的方式，并改变我们在未来应对钓愚的选择。对于任何想要深入了解经济运转的人，这本书将提供最令人意想不到的新角度。而对于生活在当下的每个人来说，这本书更是不容错过","doubanScore":8.5,"coverPath":"/book/201707171234.png"}],"firstPage":1,"prePage":0,"nextPage":0,"lastPage":1,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1]}
     */

    private int responseCode;
    /**
     * pageNum : 1
     * pageSize : 10
     * size : 1
     * orderBy :
     * startRow : 1
     * endRow : 1
     * total : 1
     * pages : 1
     * list : [{"id":"36239dc354c840109b3726e1a7647559","name":"罗马人的故事1","author":"穆先念","wordNumber":148796,"contentAbout":"自亚当·斯密以来，经济学的核心信条就是：自由市场制度就像一只\u201c看不见的手\u201d，但现在这只\u201c看不见的手\u201d已经变成了随时准备绊倒消费者的\u201c看不见的脚\u201d。\n两位诺奖得主乔治·阿克洛夫、罗伯特·席勒认为，市场在为我们带来福利的同时，也带来了灾难。普遍存在的人性弱点、信息不对称等让我们成为\u201c钓愚\u201d中的受骗者。\n作者历数从投行到政客，从汽车销售到房地产中介，从烟厂到酒厂，从食品厂到制药商，从信用卡到广告，无不存在\u201c自由的欺骗\u201d。他们以一个个生动的事例、详实的数据、幽默的阐述揭示了从日常生活到金融、政治等层面的富有想象力的钓愚手段，从另一个角度反思了金融危机爆发的内在原因：钓愚不仅让普通大众损失惨重，而且带来了前所未有的系统性风险，导致经济崩溃。\n作者进一步强调，如果政策制定者、经济学家和普通大众都能意识到钓愚的普遍存在，就能辨明和杜绝可能导致严重危机的欺骗问题，从制度创新、市场干预等方面实施更有效的监管，同时避免不必要的浪费，从而增强对经济危机的预测和把握，让\u201c看不见的手\u201d更好地为经济、为社会服务。\n这本书无疑将永远改变我们看待市场的方式，并改变我们在未来应对钓愚的选择。对于任何想要深入了解经济运转的人，这本书将提供最令人意想不到的新角度。而对于生活在当下的每个人来说，这本书更是不容错过","doubanScore":8.5,"coverPath":"/book/201707171234.png"}]
     * firstPage : 1
     * prePage : 0
     * nextPage : 0
     * lastPage : 1
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
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
        private int pageNum;
        private int pageSize;
        private int size;
        private String orderBy;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private int firstPage;
        private int prePage;
        private int nextPage;
        private int lastPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        /**
         * id : 36239dc354c840109b3726e1a7647559
         * name : 罗马人的故事1
         * author : 穆先念
         * wordNumber : 148796
         * contentAbout : 自亚当·斯密以来，经济学的核心信条就是：自由市场制度就像一只“看不见的手”，但现在这只“看不见的手”已经变成了随时准备绊倒消费者的“看不见的脚”。
         两位诺奖得主乔治·阿克洛夫、罗伯特·席勒认为，市场在为我们带来福利的同时，也带来了灾难。普遍存在的人性弱点、信息不对称等让我们成为“钓愚”中的受骗者。
         作者历数从投行到政客，从汽车销售到房地产中介，从烟厂到酒厂，从食品厂到制药商，从信用卡到广告，无不存在“自由的欺骗”。他们以一个个生动的事例、详实的数据、幽默的阐述揭示了从日常生活到金融、政治等层面的富有想象力的钓愚手段，从另一个角度反思了金融危机爆发的内在原因：钓愚不仅让普通大众损失惨重，而且带来了前所未有的系统性风险，导致经济崩溃。
         作者进一步强调，如果政策制定者、经济学家和普通大众都能意识到钓愚的普遍存在，就能辨明和杜绝可能导致严重危机的欺骗问题，从制度创新、市场干预等方面实施更有效的监管，同时避免不必要的浪费，从而增强对经济危机的预测和把握，让“看不见的手”更好地为经济、为社会服务。
         这本书无疑将永远改变我们看待市场的方式，并改变我们在未来应对钓愚的选择。对于任何想要深入了解经济运转的人，这本书将提供最令人意想不到的新角度。而对于生活在当下的每个人来说，这本书更是不容错过
         * doubanScore : 8.5
         * coverPath : /book/201707171234.png
         */

        private List<ListBean> list;
        private List<Integer> navigatepageNums;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean {
            private String id;
            private String name;
            private String author;
            private int wordNumber;
            private String contentAbout;
            private double doubanScore;
            private String coverPath;
            private String ownerId;
            private String ownerName;
            private String ownerHeadPath;

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

            public double getDoubanScore() {
                return doubanScore;
            }

            public void setDoubanScore(double doubanScore) {
                this.doubanScore = doubanScore;
            }

            public String getCoverPath() {
                return coverPath;
            }

            public void setCoverPath(String coverPath) {
                this.coverPath = coverPath;
            }
        }
    }
}
