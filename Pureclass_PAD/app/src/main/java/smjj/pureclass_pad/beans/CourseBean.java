package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/12/12.
 * 教材实体类
 */

public class CourseBean {


    /**
     * Tables : {"Table":{"Rows":[{"F0001":"SA00000041","F0002":"秋新数学三级二阶"}]}}
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
         * Table : {"Rows":[{"F0001":"SA00000041","F0002":"秋新数学三级二阶"}]}
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
                 * F0001 : SA00000041
                 * F0002 : 秋新数学三级二阶
                 */

                private String F0001;
                private String F0002;

                public String getF0001() {
                    return F0001;
                }

                public void setF0001(String F0001) {
                    this.F0001 = F0001;
                }

                public String getF0002() {
                    return F0002;
                }

                public void setF0002(String F0002) {
                    this.F0002 = F0002;
                }
            }
        }
    }
}
