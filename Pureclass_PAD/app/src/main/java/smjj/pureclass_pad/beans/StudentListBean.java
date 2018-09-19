package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/7/12.
 */

public class StudentListBean {


    /**
     * Tables : {"Table":{"Rows":[{"LoginName":"18738226907","UserCode":"SU25998","UserName":"赵锦涛","Sex":"男","XueDuan":"高中","ClassYear":"三年级","CSdate":"2017-07-12","XueXiaoName":"二中","sfShortName":"华北","csShortName":null}]}}
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
         * Table : {"Rows":[{"LoginName":"18738226907","UserCode":"SU25998","UserName":"赵锦涛","Sex":"男","XueDuan":"高中","ClassYear":"三年级","CSdate":"2017-07-12","XueXiaoName":"二中","sfShortName":"华北","csShortName":null}]}
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
                 * LoginName : 18738226907
                 * UserCode : SU25998
                 * UserName : 赵锦涛
                 * Sex : 男
                 * XueDuan : 高中
                 * ClassYear : 三年级
                 * CSdate : 2017-07-12
                 * XueXiaoName : 二中
                 * sfShortName : 华北
                 * csShortName : null
                 */

                private String LoginName;
                private String UserCode;
                private String UserName;
                private String Sex;
                private String XueDuan;
                private String ClassYear;
                private String CSdate;
                private String XueXiaoName;
                private String sfShortName;
                private String csShortName;

                public String getLoginName() {
                    return LoginName;
                }

                public void setLoginName(String LoginName) {
                    this.LoginName = LoginName;
                }

                public String getUserCode() {
                    return UserCode;
                }

                public void setUserCode(String UserCode) {
                    this.UserCode = UserCode;
                }

                public String getUserName() {
                    return UserName;
                }

                public void setUserName(String UserName) {
                    this.UserName = UserName;
                }

                public String getSex() {
                    return Sex;
                }

                public void setSex(String Sex) {
                    this.Sex = Sex;
                }

                public String getXueDuan() {
                    return XueDuan;
                }

                public void setXueDuan(String XueDuan) {
                    this.XueDuan = XueDuan;
                }

                public String getClassYear() {
                    return ClassYear;
                }

                public void setClassYear(String ClassYear) {
                    this.ClassYear = ClassYear;
                }

                public String getCSdate() {
                    return CSdate;
                }

                public void setCSdate(String CSdate) {
                    this.CSdate = CSdate;
                }

                public String getXueXiaoName() {
                    return XueXiaoName;
                }

                public void setXueXiaoName(String XueXiaoName) {
                    this.XueXiaoName = XueXiaoName;
                }

                public String getSfShortName() {
                    return sfShortName;
                }

                public void setSfShortName(String sfShortName) {
                    this.sfShortName = sfShortName;
                }

                public String getCsShortName() {
                    return csShortName;
                }

                public void setCsShortName(String csShortName) {
                    this.csShortName = csShortName;
                }
            }
        }
    }
}
