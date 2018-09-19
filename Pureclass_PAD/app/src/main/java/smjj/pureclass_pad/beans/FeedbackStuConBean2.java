package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/26.
 */

public class FeedbackStuConBean2 {


    /**
     * Tables : {"Table":{"Rows":[{"ClassID":"1000458255","SpeakID":"3ba7fd6f-849c-400b-b9bc-10ad990757d8","StudentNo":"SU0038606","Title":"入门测","ECount":5,"CurrCount":4,"ErrorCount":1},{"ClassID":"1000458255","SpeakID":"3ba7fd6f-849c-400b-b9bc-10ad990757d8","StudentNo":"SU0038606","Title":"出门考","ECount":5,"CurrCount":2,"ErrorCount":3}]},"Table1":{"Rows":[{"SecendID":"5943f990-8788-4a9d-9810-e9e75da922d9","classId":"1000458255","StudentNo":"SU0038606","ExamID":"19405d56-79ff-4e77-98ad-381a30188f78","Title":"方一浩_课后作业_作业20170923","Chaptername":"正数和负数的意义","qidcount":3}]},"Table2":{"Rows":[{"classId":"1000458254","StudentNo":"SU0038606","Title":"方一浩_课后作业_作业20170922","Chaptername":"正数和负数的概念","ECount":2,"CurrCount":0,"ErrorCount":2}]}}
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
         * Table : {"Rows":[{"ClassID":"1000458255","SpeakID":"3ba7fd6f-849c-400b-b9bc-10ad990757d8","StudentNo":"SU0038606","Title":"入门测","ECount":5,"CurrCount":4,"ErrorCount":1},{"ClassID":"1000458255","SpeakID":"3ba7fd6f-849c-400b-b9bc-10ad990757d8","StudentNo":"SU0038606","Title":"出门考","ECount":5,"CurrCount":2,"ErrorCount":3}]}
         * Table1 : {"Rows":[{"SecendID":"5943f990-8788-4a9d-9810-e9e75da922d9","classId":"1000458255","StudentNo":"SU0038606","ExamID":"19405d56-79ff-4e77-98ad-381a30188f78","Title":"方一浩_课后作业_作业20170923","Chaptername":"正数和负数的意义","qidcount":3}]}
         * Table2 : {"Rows":[{"classId":"1000458254","StudentNo":"SU0038606","Title":"方一浩_课后作业_作业20170922","Chaptername":"正数和负数的概念","ECount":2,"CurrCount":0,"ErrorCount":2}]}
         */

        private TableBean Table;
        private Table1Bean Table1;
        private Table2Bean Table2;

        public TableBean getTable() {
            return Table;
        }

        public void setTable(TableBean Table) {
            this.Table = Table;
        }

        public Table1Bean getTable1() {
            return Table1;
        }

        public void setTable1(Table1Bean Table1) {
            this.Table1 = Table1;
        }

        public Table2Bean getTable2() {
            return Table2;
        }

        public void setTable2(Table2Bean Table2) {
            this.Table2 = Table2;
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
                 * ClassID : 1000458255
                 * SpeakID : 3ba7fd6f-849c-400b-b9bc-10ad990757d8
                 * StudentNo : SU0038606
                 * Title : 入门测
                 * ECount : 5
                 * CurrCount : 4
                 * ErrorCount : 1
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

        public static class Table1Bean {
            private List<RowsBeanX> Rows;

            public List<RowsBeanX> getRows() {
                return Rows;
            }

            public void setRows(List<RowsBeanX> Rows) {
                this.Rows = Rows;
            }

            public static class RowsBeanX {
                /**
                 * SecendID : 5943f990-8788-4a9d-9810-e9e75da922d9
                 * classId : 1000458255
                 * StudentNo : SU0038606
                 * ExamID : 19405d56-79ff-4e77-98ad-381a30188f78
                 * Title : 方一浩_课后作业_作业20170923
                 * Chaptername : 正数和负数的意义
                 * qidcount : 3
                 */

                private String SecendID;
                private String classId;
                private String StudentNo;
                private String ExamID;
                private String Title;
                private String Chaptername;
                private int qidcount;

                public String getSecendID() {
                    return SecendID;
                }

                public void setSecendID(String SecendID) {
                    this.SecendID = SecendID;
                }

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

                public String getExamID() {
                    return ExamID;
                }

                public void setExamID(String ExamID) {
                    this.ExamID = ExamID;
                }

                public String getTitle() {
                    return Title;
                }

                public void setTitle(String Title) {
                    this.Title = Title;
                }

                public String getChaptername() {
                    return Chaptername;
                }

                public void setChaptername(String Chaptername) {
                    this.Chaptername = Chaptername;
                }

                public int getQidcount() {
                    return qidcount;
                }

                public void setQidcount(int qidcount) {
                    this.qidcount = qidcount;
                }
            }
        }

        public static class Table2Bean {
            private List<RowsBeanXX> Rows;

            public List<RowsBeanXX> getRows() {
                return Rows;
            }

            public void setRows(List<RowsBeanXX> Rows) {
                this.Rows = Rows;
            }

            public static class RowsBeanXX {
                /**
                 * classId : 1000458254
                 * StudentNo : SU0038606
                 * Title : 方一浩_课后作业_作业20170922
                 * Chaptername : 正数和负数的概念
                 * ECount : 2
                 * CurrCount : 0
                 * ErrorCount : 2
                 */

                private String classId;
                private String StudentNo;
                private String Title;
                private String Chaptername;
                private int ECount;
                private int CurrCount;
                private int ErrorCount;

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

                public String getTitle() {
                    return Title;
                }

                public void setTitle(String Title) {
                    this.Title = Title;
                }

                public String getChaptername() {
                    return Chaptername;
                }

                public void setChaptername(String Chaptername) {
                    this.Chaptername = Chaptername;
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
