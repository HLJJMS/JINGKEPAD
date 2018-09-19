package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/9.
 */

public class SpeachKonwBean {


    /**
     * Tables : {"Table":{"Rows":[{"ID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678","Chaptername":"正数和负数的概念"}]}}
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
         * Table : {"Rows":[{"ID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678","Chaptername":"正数和负数的概念"}]}
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
                 * ID : db9e99c6-d8cb-4a37-a016-4585fd1c9678
                 * Chaptername : 正数和负数的概念
                 */

                private String ID;
                private String Chaptername;

                public String getID() {
                    return ID;
                }

                public void setID(String ID) {
                    this.ID = ID;
                }

                public String getChaptername() {
                    return Chaptername;
                }

                public void setChaptername(String Chaptername) {
                    this.Chaptername = Chaptername;
                }
            }
        }
    }
}
