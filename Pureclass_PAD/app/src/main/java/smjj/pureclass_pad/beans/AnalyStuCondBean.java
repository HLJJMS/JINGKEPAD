package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/25.
 */

public class AnalyStuCondBean {


    /**
     * Tables : {"Table":{"Rows":[{"classId":"1000458254","StudentNo":"SU0038606","SecendID":"5943f990-8788-4a9d-9810-e9e75da922d9","Chaptername":"正数和负数的意义","zcount":14,"scount":6,"Percentage":42},{"classId":"1000458254","StudentNo":"SU0038606","SecendID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678","Chaptername":"正数和负数的概念","zcount":6,"scount":4,"Percentage":66}]}}
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
         * Table : {"Rows":[{"classId":"1000458254","StudentNo":"SU0038606","SecendID":"5943f990-8788-4a9d-9810-e9e75da922d9","Chaptername":"正数和负数的意义","zcount":14,"scount":6,"Percentage":42},{"classId":"1000458254","StudentNo":"SU0038606","SecendID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678","Chaptername":"正数和负数的概念","zcount":6,"scount":4,"Percentage":66}]}
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
                 * classId : 1000458254
                 * StudentNo : SU0038606
                 * SecendID : 5943f990-8788-4a9d-9810-e9e75da922d9
                 * Chaptername : 正数和负数的意义
                 * zcount : 14
                 * scount : 6
                 * Percentage : 42
                 */

                private String classId;
                private String StudentNo;
                private String SecendID;
                private String Chaptername;
                private int zcount;
                private int scount;
                private int Percentage;

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

                public int getZcount() {
                    return zcount;
                }

                public void setZcount(int zcount) {
                    this.zcount = zcount;
                }

                public int getScount() {
                    return scount;
                }

                public void setScount(int scount) {
                    this.scount = scount;
                }

                public int getPercentage() {
                    return Percentage;
                }

                public void setPercentage(int Percentage) {
                    this.Percentage = Percentage;
                }
            }
        }
    }
}
