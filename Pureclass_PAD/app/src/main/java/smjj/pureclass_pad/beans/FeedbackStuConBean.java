package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/20.
 */

public class FeedbackStuConBean {


    /**
     * Tables : {"Table":{"Rows":[{"ClassID":"1000458240","SpeakID":"3ba7fd6f-849c-400b-b9bc-10ad990757d8","StudentNo":"SU0038602","Title":"入门测","ECount":5,"CurrCount":3,"ErrorCount":7},{"ClassID":"1000458240","SpeakID":"3ba7fd6f-849c-400b-b9bc-10ad990757d8","StudentNo":"SU0038602","Title":"出门考","ECount":5,"CurrCount":2,"ErrorCount":8}]}}
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
         * Table : {"Rows":[{"ClassID":"1000458240","SpeakID":"3ba7fd6f-849c-400b-b9bc-10ad990757d8","StudentNo":"SU0038602","Title":"入门测","ECount":5,"CurrCount":3,"ErrorCount":7},{"ClassID":"1000458240","SpeakID":"3ba7fd6f-849c-400b-b9bc-10ad990757d8","StudentNo":"SU0038602","Title":"出门考","ECount":5,"CurrCount":2,"ErrorCount":8}]}
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
                 * ClassID : 1000458240
                 * SpeakID : 3ba7fd6f-849c-400b-b9bc-10ad990757d8
                 * StudentNo : SU0038602
                 * Title : 入门测
                 * ECount : 5
                 * CurrCount : 3
                 * ErrorCount : 7
                 */

                private String ClassID;
                private String SpeakID;
                private String StudentNo;
                private String Title;
                private int ECount;
                private int CurrCount;
                private int ErrorCount;

                public String getClassID() {
                    return ClassID;
                }

                public void setClassID(String ClassID) {
                    this.ClassID = ClassID;
                }

                public String getSpeakID() {
                    return SpeakID;
                }

                public void setSpeakID(String SpeakID) {
                    this.SpeakID = SpeakID;
                }

                public String getStudentNo() {
                    return StudentNo;
                }

                public void setStudentNo(String StudentNo) {
                    this.StudentNo = StudentNo;
                }

                public String getTitle() {
                    return Title;
                }

                public void setTitle(String Title) {
                    this.Title = Title;
                }

                public int getECount() {
                    return ECount;
                }

                public void setECount(int ECount) {
                    this.ECount = ECount;
                }

                public int getCurrCount() {
                    return CurrCount;
                }

                public void setCurrCount(int CurrCount) {
                    this.CurrCount = CurrCount;
                }

                public int getErrorCount() {
                    return ErrorCount;
                }

                public void setErrorCount(int ErrorCount) {
                    this.ErrorCount = ErrorCount;
                }
            }
        }
    }
}
