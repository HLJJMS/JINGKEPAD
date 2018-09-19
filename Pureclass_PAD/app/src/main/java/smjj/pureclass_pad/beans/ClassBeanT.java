package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/12/7.
 */

public class ClassBeanT {


    /**
     * Tables : {"Table":{"Rows":[{"classNo":"SU0038606","className":"方一浩","courseNo":"C00001","subjects":"数学","classType":"一对一","gradeno":"SG00007","gradetype":"初一","schoolNo":"A000043001001","schgegin":"公主坟校区","teacherNo":"US0005665","teacherName":"习思民","Stnames":"方一浩"}]}}
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
         * Table : {"Rows":[{"classNo":"SU0038606","className":"方一浩","courseNo":"C00001","subjects":"数学","classType":"一对一","gradeno":"SG00007","gradetype":"初一","schoolNo":"A000043001001","schgegin":"公主坟校区","teacherNo":"US0005665","teacherName":"习思民","Stnames":"方一浩"}]}
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
                 * classNo : SU0038606
                 * className : 方一浩
                 * courseNo : C00001
                 * subjects : 数学
                 * classType : 一对一
                 * gradeno : SG00007
                 * gradetype : 初一
                 * schoolNo : A000043001001
                 * schgegin : 公主坟校区
                 * teacherNo : US0005665
                 * teacherName : 习思民
                 * Stnames : 方一浩
                 * MaterialNo : 教材编号
                 * MaterialName : 教材名
                 * Number:本班级下所有的学生
                 */

                private String classNo;
                private String className;
                private String courseNo;
                private String subjects;
                private String classType;
                private String gradeno;
                private String gradetype;
                private String schoolNo;
                private String schgegin;
                private String teacherNo;
                private String teacherName;
                private String Stnames;
                private String TchType;

                public String getTchType() {
                    return TchType;
                }

                public void setTchType(String tchType) {
                    TchType = tchType;
                }

                public int getNumber() {
                    return Number;
                }

                public void setNumber(int number) {
                    Number = number;
                }

                private String MaterialNo;
                private String MaterialName;
                private int Number;

                public String getMaterialName() {
                    return MaterialName;
                }

                public void setMaterialName(String materialName) {
                    MaterialName = materialName;
                }

                public String getMaterialNo() {
                    return MaterialNo;
                }

                public void setMaterialNo(String materialNo) {
                    MaterialNo = materialNo;
                }

                public String getClassNo() {
                    return classNo;
                }

                public void setClassNo(String classNo) {
                    this.classNo = classNo;
                }

                public String getClassName() {
                    return className;
                }

                public void setClassName(String className) {
                    this.className = className;
                }

                public String getCourseNo() {
                    return courseNo;
                }

                public void setCourseNo(String courseNo) {
                    this.courseNo = courseNo;
                }

                public String getSubjects() {
                    return subjects;
                }

                public void setSubjects(String subjects) {
                    this.subjects = subjects;
                }

                public String getClassType() {
                    return classType;
                }

                public void setClassType(String classType) {
                    this.classType = classType;
                }

                public String getGradeno() {
                    return gradeno;
                }

                public void setGradeno(String gradeno) {
                    this.gradeno = gradeno;
                }

                public String getGradetype() {
                    return gradetype;
                }

                public void setGradetype(String gradetype) {
                    this.gradetype = gradetype;
                }

                public String getSchoolNo() {
                    return schoolNo;
                }

                public void setSchoolNo(String schoolNo) {
                    this.schoolNo = schoolNo;
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

                public String getStnames() {
                    return Stnames;
                }

                public void setStnames(String Stnames) {
                    this.Stnames = Stnames;
                }
            }
        }
    }
}
