package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/7/13.
 */

public class SpeachBean {


    /**
     * Tables : {"Table":{"Rows":[{"ID":18,"SpeakID":"a0da5004-125c-4c1b-b98a-c0aebb41c336","SpeakName":"正负数","SeasonDate":"秋","StudySection":"初中","Grade":"初一","Subject":"数学","SecendID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678,5943f990-8788-4a9d-9810-e9e75da922d9","Status":1,"Flag":true}]}}
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
         * Table : {"Rows":[{"ID":18,"SpeakID":"a0da5004-125c-4c1b-b98a-c0aebb41c336","SpeakName":"正负数","SeasonDate":"秋","StudySection":"初中","Grade":"初一","Subject":"数学","SecendID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678,5943f990-8788-4a9d-9810-e9e75da922d9","Status":1,"Flag":true}]}
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
                 * ID : 18
                 * SpeakID : a0da5004-125c-4c1b-b98a-c0aebb41c336
                 * SpeakName : 正负数
                 * SeasonDate : 秋
                 * StudySection : 初中
                 * Grade : 初一
                 * Subject : 数学
                 * SecendID : db9e99c6-d8cb-4a37-a016-4585fd1c9678,5943f990-8788-4a9d-9810-e9e75da922d9
                 * Status : 1
                 * Flag : true
                 */

                private int ID;
                private String SpeakID;
                private String SpeakName;
                private String SeasonDate;
                private String StudySection;
                private String Grade;
                private String Subject;
                private String SecendID;

                private int Status;
                private boolean Flag;

                public int getID() {
                    return ID;
                }

                public void setID(int ID) {
                    this.ID = ID;
                }

                public String getSpeakID() {
                    return SpeakID;
                }

                public void setSpeakID(String SpeakID) {
                    this.SpeakID = SpeakID;
                }

                public String getSpeakName() {
                    return SpeakName;
                }

                public void setSpeakName(String SpeakName) {
                    this.SpeakName = SpeakName;
                }

                public String getSeasonDate() {
                    return SeasonDate;
                }

                public void setSeasonDate(String SeasonDate) {
                    this.SeasonDate = SeasonDate;
                }

                public String getStudySection() {
                    return StudySection;
                }

                public void setStudySection(String StudySection) {
                    this.StudySection = StudySection;
                }

                public String getGrade() {
                    return Grade;
                }

                public void setGrade(String Grade) {
                    this.Grade = Grade;
                }

                public String getSubject() {
                    return Subject;
                }

                public void setSubject(String Subject) {
                    this.Subject = Subject;
                }

                public String getSecendID() {
                    return SecendID;
                }

                public void setSecendID(String SecendID) {
                    this.SecendID = SecendID;
                }

                public int getStatus() {
                    return Status;
                }

                public void setStatus(int Status) {
                    this.Status = Status;
                }

                public boolean isFlag() {
                    return Flag;
                }

                public void setFlag(boolean Flag) {
                    this.Flag = Flag;
                }
            }
        }
    }
}
