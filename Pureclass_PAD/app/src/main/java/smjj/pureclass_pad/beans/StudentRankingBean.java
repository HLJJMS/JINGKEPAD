package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/8/8.
 */

public class StudentRankingBean {


    /**
     * Tables : {"Table":{"Rows":[{"StudentName":"朱予涵","score":0,"CurrCount":0},{"StudentName":"徐紫千","score":0,"CurrCount":0},{"StudentName":"王彦婷","score":0,"CurrCount":0}]}}
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
         * Table : {"Rows":[{"StudentName":"朱予涵","score":0,"CurrCount":0},{"StudentName":"徐紫千","score":0,"CurrCount":0},{"StudentName":"王彦婷","score":0,"CurrCount":0}]}
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
                 * StudentName : 朱予涵
                 * score : 0
                 * CurrCount : 0
                 */

                private String StudentName;
                private int score;
                private int CurrCount;

                public String getStudentName() {
                    return StudentName;
                }

                public void setStudentName(String StudentName) {
                    this.StudentName = StudentName;
                }

                public int getScore() {
                    return score;
                }

                public void setScore(int score) {
                    this.score = score;
                }

                public int getCurrCount() {
                    return CurrCount;
                }

                public void setCurrCount(int CurrCount) {
                    this.CurrCount = CurrCount;
                }
            }
        }
    }
}
