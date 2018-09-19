package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2018/6/11.
 */
//临时答题答题详情实体类
public class IQDetailsBean {


    /**
     * Tables : {"Table":{"Rows":[{"F0012":"A","Number":0},{"F0012":"B","Number":0},{"F0012":"C","Number":0},{"F0012":"D","Number":0},{"F0012":"not","Number":9}]}}
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
         * Table : {"Rows":[{"F0012":"A","Number":0},{"F0012":"B","Number":0},{"F0012":"C","Number":0},{"F0012":"D","Number":0},{"F0012":"not","Number":9}]}
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
                 * F0012 : A
                 * Number : 0
                 */

                private String F0012;
                private int Number;

                public String getF0012() {
                    return F0012;
                }

                public void setF0012(String F0012) {
                    this.F0012 = F0012;
                }

                public int getNumber() {
                    return Number;
                }

                public void setNumber(int Number) {
                    this.Number = Number;
                }
            }
        }
    }
}
