package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/19.
 */

public class TeacherRLBean {

    /**
     * Tables : {"Table":{"Rows":[{"ID":2,"ClassID":"1000458239","ReflectContent":"教学反思","TeacherNo":"US0005664","TeacherName":"谷老师","Reserve1":null,"Reserve2":null,"Reserve3":null,"Status":0,"AddUser":"US0005664","AddTime":"2017-09-19 18:24:08","Remarks":null}]}}
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
         * Table : {"Rows":[{"ID":2,"ClassID":"1000458239","ReflectContent":"教学反思","TeacherNo":"US0005664","TeacherName":"谷老师","Reserve1":null,"Reserve2":null,"Reserve3":null,"Status":0,"AddUser":"US0005664","AddTime":"2017-09-19 18:24:08","Remarks":null}]}
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
                 * ID : 2
                 * ClassID : 1000458239
                 * ReflectContent : 教学反思
                 * TeacherNo : US0005664
                 * TeacherName : 谷老师
                 * Reserve1 : null
                 * Reserve2 : null
                 * Reserve3 : null
                 * Status : 0
                 * AddUser : US0005664
                 * AddTime : 2017-09-19 18:24:08
                 * Remarks : null
                 */

                private int ID;
                private String ClassID;
                private String ReflectContent;
                private String TeacherNo;
                private String TeacherName;
                private Object Reserve1;
                private Object Reserve2;
                private Object Reserve3;
                private int Status;
                private String AddUser;
                private String AddTime;
                private Object Remarks;

                public int getID() {
                    return ID;
                }

                public void setID(int ID) {
                    this.ID = ID;
                }

                public String getClassID() {
                    return ClassID;
                }

                public void setClassID(String ClassID) {
                    this.ClassID = ClassID;
                }

                public String getReflectContent() {
                    return ReflectContent;
                }

                public void setReflectContent(String ReflectContent) {
                    this.ReflectContent = ReflectContent;
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

                public Object getReserve1() {
                    return Reserve1;
                }

                public void setReserve1(Object Reserve1) {
                    this.Reserve1 = Reserve1;
                }

                public Object getReserve2() {
                    return Reserve2;
                }

                public void setReserve2(Object Reserve2) {
                    this.Reserve2 = Reserve2;
                }

                public Object getReserve3() {
                    return Reserve3;
                }

                public void setReserve3(Object Reserve3) {
                    this.Reserve3 = Reserve3;
                }

                public int getStatus() {
                    return Status;
                }

                public void setStatus(int Status) {
                    this.Status = Status;
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

                public Object getRemarks() {
                    return Remarks;
                }

                public void setRemarks(Object Remarks) {
                    this.Remarks = Remarks;
                }
            }
        }
    }
}
