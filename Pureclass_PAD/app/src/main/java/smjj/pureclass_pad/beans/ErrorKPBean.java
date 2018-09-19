package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/26.
 * 课堂反馈错误知识点实体类
 */

public class ErrorKPBean {


    /**
     * Tables : {"Table":{"Rows":[{"SecendID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678","Chaptername":"正数和负数的概念","Title":"入门测"}]}}
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
         * Table : {"Rows":[{"SecendID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678","Chaptername":"正数和负数的概念","Title":"入门测"}]}
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
                 * SecendID : db9e99c6-d8cb-4a37-a016-4585fd1c9678
                 * Chaptername : 正数和负数的概念
                 * Title : 入门测
                 */

                private String SecendID;
                private String Chaptername;
                private String Title;

                public String getSecendID() {
                    return SecendID;
                }

                public void setSecendID(String SecendID) {
                    this.SecendID = SecendID;
                }

                public String getChaptername() {
                    return Chaptername;
                }

                public void setChaptername(String Chaptername) {
                    this.Chaptername = Chaptername;
                }

                public String getTitle() {
                    return Title;
                }

                public void setTitle(String Title) {
                    this.Title = Title;
                }
            }
        }
    }
}
