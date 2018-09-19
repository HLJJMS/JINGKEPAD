package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2018/6/6.
 */
//校区签到实体类
public class SchoolSignBean {


    /**
     * Tables : {"Table":{"Rows":[{"ClassName":"暑佳分数学小四提高班02","number":9,"SNumber":4,"rate":"44.44%","ClassNo":"天津武清香江广场校"},{"ClassNo":"天津武清香江广场校","number":9,"SNumber":4,"rate":"44.44%"}]}}
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
         * Table : {"Rows":[{"ClassName":"暑佳分数学小四提高班02","number":9,"SNumber":4,"rate":"44.44%","ClassNo":"天津武清香江广场校"},{"ClassNo":"天津武清香江广场校","number":9,"SNumber":4,"rate":"44.44%"}]}
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
                 * ClassName : 暑佳分数学小四提高班02
                 * number : 9
                 * SNumber : 4
                 * rate : 44.44%
                 * ClassNo : 天津武清香江广场校
                 */

                private String ClassName;
                private int number;
                private int SNumber;
                private String rate;
                private String ClassNo;

                public String getClassName() {
                    return ClassName;
                }

                public void setClassName(String ClassName) {
                    this.ClassName = ClassName;
                }

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
                }

                public int getSNumber() {
                    return SNumber;
                }

                public void setSNumber(int SNumber) {
                    this.SNumber = SNumber;
                }

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public String getClassNo() {
                    return ClassNo;
                }

                public void setClassNo(String ClassNo) {
                    this.ClassNo = ClassNo;
                }
            }
        }
    }
}
