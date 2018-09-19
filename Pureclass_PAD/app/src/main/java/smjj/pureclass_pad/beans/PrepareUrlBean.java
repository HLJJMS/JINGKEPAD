package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/9.
 */

public class PrepareUrlBean {


    /**
     * Tables : {"Table":{"Rows":[{"BodyLink":"http://47.92.81.135:8001/Lecture/LectureUpLoad/20170908170634534_1029261.pdf"}]}}
     */

    private TablesBean Tables;

    public TablesBean getTables() {
        return Tables;
    }

    public void setTables(TablesBean Tables) {
        this.Tables = Tables;
    }

    public static class TablesBean {
        /**
         * Table : {"Rows":[{"BodyLink":"http://47.92.81.135:8001/Lecture/LectureUpLoad/20170908170634534_1029261.pdf"}]}
         */

        private TableBean Table;

        public TableBean getTable() {
            return Table;
        }

        public void setTable(TableBean Table) {
            this.Table = Table;
        }

        public static class TableBean {
            private List<RowsBean> Rows;

            public List<RowsBean> getRows() {
                return Rows;
            }

            public void setRows(List<RowsBean> Rows) {
                this.Rows = Rows;
            }

            public static class RowsBean {
                /**
                 * BodyLink : http://47.92.81.135:8001/Lecture/LectureUpLoad/20170908170634534_1029261.pdf
                 */

                private String BodyLink;

                public String getBodyLink() {
                    return BodyLink;
                }

                public void setBodyLink(String BodyLink) {
                    this.BodyLink = BodyLink;
                }
            }
        }
    }
}
