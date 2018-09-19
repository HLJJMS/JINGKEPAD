package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/5.
 */

public class StuWorkAnswerBean {


    /**
     * Tables : {"Table":{"Rows":[{"Stem":"www","Choses":"rrr","QusAnswer":"qqqq","Correct":"B","Analysis":"ddddd"}]}}
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
         * Table : {"Rows":[{"Stem":"www","Choses":"rrr","QusAnswer":"qqqq","Correct":"B","Analysis":"ddddd"}]}
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
                 * Stem : www
                 * Choses : rrr
                 * QusAnswer : qqqq
                 * Correct : B
                 * Analysis : ddddd
                 */

                private String Stem;
                private String Choses;
                private String QusAnswer;
                private String Correct;
                private String Analysis;

                public String getStem() {
                    return Stem;
                }

                public void setStem(String Stem) {
                    this.Stem = Stem;
                }

                public String getChoses() {
                    return Choses;
                }

                public void setChoses(String Choses) {
                    this.Choses = Choses;
                }

                public String getQusAnswer() {
                    return QusAnswer;
                }

                public void setQusAnswer(String QusAnswer) {
                    this.QusAnswer = QusAnswer;
                }

                public String getCorrect() {
                    return Correct;
                }

                public void setCorrect(String Correct) {
                    this.Correct = Correct;
                }

                public String getAnalysis() {
                    return Analysis;
                }

                public void setAnalysis(String Analysis) {
                    this.Analysis = Analysis;
                }
            }
        }
    }
}
