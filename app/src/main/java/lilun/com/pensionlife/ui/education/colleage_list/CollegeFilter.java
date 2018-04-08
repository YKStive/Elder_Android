package lilun.com.pensionlife.ui.education.colleage_list;

import java.util.List;

/**
 * 大学filter
 */
public class CollegeFilter {

    public CollegeFilter(List<String> ids) {
        where = new WhereBean();
        where.setId(new WhereBean.IdBean());
        where.getId().setInq(ids);
    }

    /**
     * where : {"id":{"inq":[]}}
     */


    private WhereBean where = new WhereBean();

    public WhereBean getWhere() {
        return where;
    }


    public static class WhereBean {
        public WhereBean setId(IdBean id) {
            this.id = id;
            return this;
        }

        /**
         * id : {"inq":[]}
         */

        private IdBean id = new IdBean();
        private int visible = 0;

        public int getVisible() {
            return visible;
        }

        public WhereBean setVisible(int visible) {
            this.visible = visible;
            return this;
        }

        public IdBean getId() {
            return id;
        }


        public static class IdBean {
            private List<String> inq;


            public void setInq(List<String> inq) {
                this.inq = inq;
            }
        }
    }
}
