package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/22.
 */

public class AnalyStuScoreBean {


    /**
     * Tables : {"Table":{"Rows":[{"classId":"1000458239","StudentNo":"SU0038604","score":0,"ranking":3,"Averagescore":6}]}}
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
         * Table : {"Rows":[{"classId":"1000458239","StudentNo":"SU0038604","score":0,"ranking":3,"Averagescore":6}]}
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
                 * classId : 1000458239
                 * StudentNo : SU0038604
                 * score : 0
                 * ranking : 3
                 * Averagescore : 6
                 */

                private String classId;
                private String StudentNo;
                private double score;
                private int ranking;
                private double Averagescore;

                public String getClassId() {
                    return classId;
                }

                public void setClassId(String classId) {
                    this.classId = classId;
                }

                public String getStudentNo() {
                    return StudentNo;
                }

                public void setStudentNo(String StudentNo) {
                    this.StudentNo = StudentNo;
                }

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public int getRanking() {
                    return ranking;
                }

                public void setRanking(int ranking) {
                    this.ranking = ranking;
                }

                public double getAveragescore() {
                    return Averagescore;
                }

                public void setAveragescore(double Averagescore) {
                    this.Averagescore = Averagescore;
                }
            }
        }
    }
}
