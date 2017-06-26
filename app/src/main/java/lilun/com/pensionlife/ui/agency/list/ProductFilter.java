package lilun.com.pensionlife.ui.agency.list;

import java.util.List;

/**
 * 机构产品的筛选条件
 *
 * @author yk
 *         create at 2017/4/17 14:25
 *         email : yk_developer@163.com
 */
public class ProductFilter {
//    /**
//     * where : {"title":{"like":""},"areaIds":{"inq":["",""]},"price":{"between":[100,300]},"score":4}
//     */
//
//    private WhereBean where;
//
//    public WhereBean getWhere() {
//        return where;
//    }
//
//    public void setWhere(WhereBean where) {
//        this.where = where;
//    }
//
//    public static class WhereBean {
//        /**
//         * title : {"like":""}
//         * areaIds : {"inq":["",""]}
//         * price : {"between":[100,300]}
//         * score : 4
//         */
//
//        private TitleBean title;
//        private AreaIdsBean areaIds;
//        private PriceBean price;
//        private int score;
//
//        public TitleBean getTitle() {
//            return title;
//        }
//
//        public void setTitle(TitleBean title) {
//            this.title = title;
//        }
//
//        public AreaIdsBean getAreaIds() {
//            return areaIds;
//        }
//
//        public void setAreaIds(AreaIdsBean areaIds) {
//            this.areaIds = areaIds;
//        }
//
//        public PriceBean getPrice() {
//            return price;
//        }
//
//        public void setPrice(PriceBean price) {
//            this.price = price;
//        }
//
//        public int getScore() {
//            return score;
//        }
//
//        public void setScore(int score) {
//            this.score = score;
//        }
//
//        public static class TitleBean {
//            /**
//             * like :
//             */
//
//            private String like;
//
//            public String getLike() {
//                return like;
//            }
//
//            public void setLike(String like) {
//                this.like = like;
//            }
//        }
//
//        public static class AreaIdsBean {
//            private List<String> inq;
//
//            public List<String> getInq() {
//                return inq;
//            }
//
//            public void setInq(List<String> inq) {
//                this.inq = inq;
//            }
//        }
//
//        public static class PriceBean {
//            private List<Integer> between;
//
//            public List<Integer> getBetween() {
//                return between;
//            }
//
//            public void setBetween(List<Integer> between) {
//                this.between = between;
//            }
//        }
//    }

    public ProductFilter() {
        where = new WhereBean();
    }

    /**
     * {where : {"title":{"like":""},"areaIds":{"inq":["",""]},"price":{"between":[100,300]},"score":4}}
     */


    public WhereBean where;

    public WhereBean getWhere() {
        return where;
    }

    public void setWhere(WhereBean where) {
        this.where = where;
    }

    public static class WhereBean {
        /**
         * title : {"like":""}
         * areasList :
         * price : {"between":[100,300]}
         * score : 4
         */

        private TitleBean title;
        private int visible = 0;

        public int getVisible() {
            return visible;
        }

        public WhereBean setVisible(int visible) {
            this.visible = visible;
            return this;
        }

//        private String areasList;
        public PriceBean price;
        private String categoryId;
        private Integer score;
        private String creatorId;
        private String organizationId;
        private AreaIdsBean areaIds = new AreaIdsBean();
//        private String areaIds = "{\"inq\":" + User.spliceId("") + "}";
//        private ArrayList<String> areaIds = new ArrayList<>();
//        public WhereBean addArea(String categoryId) {
//            areaIds.add(categoryId);
//            return this;
//        }


        public AreaIdsBean getAreaIds() {
            return areaIds;
        }

        public void setAreaIds(AreaIdsBean areaIds) {
            this.areaIds = areaIds;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public WhereBean setCategoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public WhereBean setCreatorId(String creatorId) {
            this.creatorId = creatorId;
            return this;
        }

        public String getOrganizationId() {
            return organizationId;
        }

        public WhereBean setOrganizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public TitleBean getTitle() {
            return title;
        }

        public void setTitle(TitleBean title) {
            this.title = title;
        }
//
//        public String getAreasList() {
//            return areasList;
//        }
//
//        public void setAreasList(String areasList) {
//            this.areasList = areasList;
//        }

        public PriceBean getPrice() {
            return price;
        }

        public void setPrice(PriceBean price) {
            this.price = price;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public static class TitleBean {
            /**
             * like :
             */

            private String like;

            public String getLike() {
                return like;
            }

            public void setLike(String like) {
                this.like = like;
            }
        }

        public static class AreaIdsBean {
            private List<String> inq;

            public List<String> getInq() {
                return inq;
            }

            public void setInq(List<String> inq) {
                this.inq = inq;
            }
        }

        public static class PriceBean {
            private List<Integer> between;

            public List<Integer> getBetween() {
                return between;
            }

            public void setBetween(List<Integer> between) {
                this.between = between;
            }
        }
    }


    //

}


