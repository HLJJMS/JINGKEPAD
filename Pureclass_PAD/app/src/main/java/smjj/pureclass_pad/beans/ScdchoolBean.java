package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/28.
 */

public class ScdchoolBean {


    /**
     * Tables : {"Table":{"Rows":[{"DepartName":"华威学堂"}]}}
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
         * Table : {"Rows":[{"DepartName":"华威学堂"}]}
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
                 * DepartName : 华威学堂
                 */

                private String DepartName;

                public String getDepartName() {
                    return DepartName;
                }

                public void setDepartName(String DepartName) {
                    this.DepartName = DepartName;
                }
            }
        }
    }
}
