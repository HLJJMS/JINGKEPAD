package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/27.
 */

public class AnalyContentBean {


    /**
     * Tables : {"Table":{"Rows":[{"ClassID":"1000458254","AnalysisContent":"继续努力，加油学习。"}]}}
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
         * Table : {"Rows":[{"ClassID":"1000458254","AnalysisContent":"继续努力，加油学习。"}]}
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
                 * ClassID : 1000458254
                 * AnalysisContent : 继续努力，加油学习。
                 */

                private String ClassID;
                private String AnalysisContent;

                public String getClassID() {
                    return ClassID;
                }

                public void setClassID(String ClassID) {
                    this.ClassID = ClassID;
                }

                public String getAnalysisContent() {
                    return AnalysisContent;
                }

                public void setAnalysisContent(String AnalysisContent) {
                    this.AnalysisContent = AnalysisContent;
                }
            }
        }
    }
}
