package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * 外部教师排课列表实体类
 * Created by wlm on 2017/6/23.
 */

public class SchedulingTableBean {


    private TablesBean Tables;

    public TablesBean getTables() {
        return Tables;
    }

    public void setTables(TablesBean Tables) {
        this.Tables = Tables;
    }

    public static class TablesBean {

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

                private int ID;
                private String classId;
                private String ClassNo;
                private String classname;
                private String subjects;
                private String gratename;
                private String timeStar;
                private String timeEnd;
                private String weekly;
                private String schgegin;
                private String teacherNo;
                private String teacherName;
                private String classType;
                private String remark;
                private String intState;
                private String confirmDate;
                private String auditorDate;
                private String confirmPerson;
                private String auditorPerson;
                private String Gname;
                private String Bookid;
                private String classhours;
                private String schoolNo;

                public String getClasshours() {
                    return classhours;
                }

                public void setClasshours(String classhours) {
                    this.classhours = classhours;
                }

                public String getSchoolNo() {
                    return schoolNo;
                }

                public void setSchoolNo(String schoolNo) {
                    this.schoolNo = schoolNo;
                }

                public String getGradeNo() {
                    return gradeNo;
                }

                public void setGradeNo(String gradeNo) {
                    this.gradeNo = gradeNo;
                }

                private String gradeNo;

                public int getID() {
                    return ID;
                }

                public void setID(int ID) {
                    this.ID = ID;
                }

                public String getBookid() {
                    return Bookid;
                }

                public void setBookid(String bookid) {
                    Bookid = bookid;
                }

                public String getGname() {

                    return Gname;
                }

                public void setGname(String gname) {
                    Gname = gname;
                }

                public String getClassId() {
                    return classId;
                }

                public void setClassId(String classId) {
                    this.classId = classId;
                }

                public String getClassNo() {
                    return ClassNo;
                }

                public void setClassNo(String ClassNo) {
                    this.ClassNo = ClassNo;
                }

                public String getClassname() {
                    return classname;
                }

                public void setClassname(String classname) {
                    this.classname = classname;
                }

                public String getSubjects() {
                    return subjects;
                }

                public void setSubjects(String subjects) {
                    this.subjects = subjects;
                }

                public String getGratename() {
                    return gratename;
                }

                public void setGratename(String gratename) {
                    this.gratename = gratename;
                }

                public String getTimeStar() {
                    return timeStar;
                }

                public void setTimeStar(String timeStar) {
                    this.timeStar = timeStar;
                }

                public String getTimeEnd() {
                    return timeEnd;
                }

                public void setTimeEnd(String timeEnd) {
                    this.timeEnd = timeEnd;
                }

                public String getWeekly() {
                    return weekly;
                }

                public void setWeekly(String weekly) {
                    this.weekly = weekly;
                }

                public String getSchgegin() {
                    return schgegin;
                }

                public void setSchgegin(String schgegin) {
                    this.schgegin = schgegin;
                }

                public String getTeacherNo() {
                    return teacherNo;
                }

                public void setTeacherNo(String teacherNo) {
                    this.teacherNo = teacherNo;
                }

                public String getTeacherName() {
                    return teacherName;
                }

                public void setTeacherName(String teacherName) {
                    this.teacherName = teacherName;
                }

                public String getClassType() {
                    return classType;
                }

                public void setClassType(String classType) {
                    this.classType = classType;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getIntState() {
                    return intState;
                }

                public void setIntState(String intState) {
                    this.intState = intState;
                }

                public String getConfirmDate() {
                    return confirmDate;
                }

                public void setConfirmDate(String confirmDate) {
                    this.confirmDate = confirmDate;
                }

                public String getAuditorDate() {
                    return auditorDate;
                }

                public void setAuditorDate(String auditorDate) {
                    this.auditorDate = auditorDate;
                }

                public String getConfirmPerson() {
                    return confirmPerson;
                }

                public void setConfirmPerson(String confirmPerson) {
                    this.confirmPerson = confirmPerson;
                }

                public String getAuditorPerson() {
                    return auditorPerson;
                }

                public void setAuditorPerson(String auditorPerson) {
                    this.auditorPerson = auditorPerson;
                }
            }
        }
    }
}
