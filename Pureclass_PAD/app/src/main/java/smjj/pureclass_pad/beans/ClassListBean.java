package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/7/12.
 */

public class ClassListBean {


    /**
     * Tables : {"Table":{"Rows":[{"F0001":"1000370759","F0009":"暑假某班","F0006":"小学","F0007":"二年级","F0008":"语文","F0010":"班课","F0011":"2017-07-12"}]}}
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
         * Table : {"Rows":[{"F0001":"1000370759","F0009":"暑假某班","F0006":"小学","F0007":"二年级","F0008":"语文","F0010":"班课","F0011":"2017-07-12"}]}
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
                 * F0001 : 1000370759
                 * F0009 : 暑假某班
                 * F0006 : 小学
                 * F0007 : 二年级
                 * F0008 : 语文
                 * F0010 : 班课
                 * F0011 : 2017-07-12
                 */

                private String F0001;
                private String F0009;
                private String F0006;
                private String F0007;
                private String F0008;
                private String F0010;
                private String F0011;

                public String getF0001() {
                    return F0001;
                }

                public void setF0001(String F0001) {
                    this.F0001 = F0001;
                }

                public String getF0009() {
                    return F0009;
                }

                public void setF0009(String F0009) {
                    this.F0009 = F0009;
                }

                public String getF0006() {
                    return F0006;
                }

                public void setF0006(String F0006) {
                    this.F0006 = F0006;
                }

                public String getF0007() {
                    return F0007;
                }

                public void setF0007(String F0007) {
                    this.F0007 = F0007;
                }

                public String getF0008() {
                    return F0008;
                }

                public void setF0008(String F0008) {
                    this.F0008 = F0008;
                }

                public String getF0010() {
                    return F0010;
                }

                public void setF0010(String F0010) {
                    this.F0010 = F0010;
                }

                public String getF0011() {
                    return F0011;
                }

                public void setF0011(String F0011) {
                    this.F0011 = F0011;
                }
            }
        }
    }
}
