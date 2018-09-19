package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/7/11.
 */
//排课列表点击获取学生实体类（内部老师）
public class StudentInternalBean {


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

                private String classid;
                private String StudentNo;
                private String UserName;
                private String classId;
                private String stuNo;
                private String stName;
                private String StudentName;
                private String Status;//签到状态
                private int number;

                public String getStatus() {
                    return Status;
                }

                public void setStatus(String status) {
                    Status = status;
                }

                private String classNo;

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
                }

                public String getClassNo() {
                    return classNo;
                }

                public void setClassNo(String classNo) {
                    this.classNo = classNo;
                }

                public int getSNumber() {
                    return SNumber;
                }

                public void setSNumber(int SNumber) {
                    this.SNumber = SNumber;
                }

                private int SNumber;

                public String getClassId() {
                    return classId;
                }

                public void setClassId(String classId) {
                    this.classId = classId;
                }

                public String getStuNo() {
                    return stuNo;
                }

                public void setStuNo(String stuNo) {
                    this.stuNo = stuNo;
                }

                public String getStName() {
                    return stName;
                }

                public void setStName(String stName) {
                    this.stName = stName;
                }

                public String getStudentName() {
                    return StudentName;
                }

                public void setStudentName(String studentName) {
                    StudentName = studentName;
                }


                public String getClassid() {
                    return classid;
                }

                public void setClassid(String classid) {
                    this.classid = classid;
                }

                public String getStudentNo() {
                    return StudentNo;
                }

                public void setStudentNo(String StudentNo) {
                    this.StudentNo = StudentNo;
                }

                public String getUserName() {
                    return UserName;
                }

                public void setUserName(String UserName) {
                    this.UserName = UserName;
                }
            }
        }
    }
}
