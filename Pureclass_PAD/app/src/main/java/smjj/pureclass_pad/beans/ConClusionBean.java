package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/8/18.
 */

public class ConClusionBean {


    private List<RowsBean> Rows;

    public List<RowsBean> getRows() {
        return Rows;
    }

    public void setRows(List<RowsBean> Rows) {
        this.Rows = Rows;
    }

    public static class RowsBean {
        /**
         * ClassID : EC1000000001
         * SpeakID : 4901
         * SpeakName : 第一讲一笔画
         * SummaryContent : 课堂小结内容1111111
         * TeacherNo : US0000276
         * TeacherName : 周霞
         * AddUser : 周霞
         * AddTime : 2017-08-18
         */

        private String ClassID;
        private String SpeakID;
        private String SpeakName;
        private String SummaryContent;
        private String TeacherNo;
        private String TeacherName;
        private String AddUser;
        private String AddTime;

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

        public String getSpeakName() {
            return SpeakName;
        }

        public void setSpeakName(String SpeakName) {
            this.SpeakName = SpeakName;
        }

        public String getSummaryContent() {
            return SummaryContent;
        }

        public void setSummaryContent(String SummaryContent) {
            this.SummaryContent = SummaryContent;
        }

        public String getTeacherNo() {
            return TeacherNo;
        }

        public void setTeacherNo(String TeacherNo) {
            this.TeacherNo = TeacherNo;
        }

        public String getTeacherName() {
            return TeacherName;
        }

        public void setTeacherName(String TeacherName) {
            this.TeacherName = TeacherName;
        }

        public String getAddUser() {
            return AddUser;
        }

        public void setAddUser(String AddUser) {
            this.AddUser = AddUser;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
        }
    }
}
