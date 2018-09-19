package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2018/7/13.
 */

public class ResponderRankBean {


    /**
     * Tables : {"Table":{"Rows":[{"F0009":"SU0038674","F0010":"苏艺伟","F0011":0,"F0012":"A","F0014":"A0000143001","F0015":"天津武清香江广场校"},{"F0009":"SU0038675","F0010":"张玲君","F0011":1,"F0012":"C","F0014":"A0000143001","F0015":"天津武清香江广场校"},{"F0009":"SU0038676","F0010":"刘旭东","F0011":1,"F0012":"B","F0014":"A0000143001","F0015":"天津武清香江广场校"},{"F0009":"SU0038679","F0010":"刘宗","F0011":2,"F0012":"D","F0014":"A0000143001","F0015":"天津武清香江广场校"}]}}
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
         * Table : {"Rows":[{"F0009":"SU0038674","F0010":"苏艺伟","F0011":0,"F0012":"A","F0014":"A0000143001","F0015":"天津武清香江广场校"},{"F0009":"SU0038675","F0010":"张玲君","F0011":1,"F0012":"C","F0014":"A0000143001","F0015":"天津武清香江广场校"},{"F0009":"SU0038676","F0010":"刘旭东","F0011":1,"F0012":"B","F0014":"A0000143001","F0015":"天津武清香江广场校"},{"F0009":"SU0038679","F0010":"刘宗","F0011":2,"F0012":"D","F0014":"A0000143001","F0015":"天津武清香江广场校"}]}
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
                 * F0009 : SU0038674
                 * F0010 : 苏艺伟
                 * F0011 : 0
                 * F0012 : A
                 * F0014 : A0000143001
                 * F0015 : 天津武清香江广场校
                 */

                private String F0009;
                private String F0010;
                private int F0011;
                private String F0012;
                private String F0014;
                private String F0015;

                public String getF0009() {
                    return F0009;
                }

                public void setF0009(String F0009) {
                    this.F0009 = F0009;
                }

                public String getF0010() {
                    return F0010;
                }

                public void setF0010(String F0010) {
                    this.F0010 = F0010;
                }

                public int getF0011() {
                    return F0011;
                }

                public void setF0011(int F0011) {
                    this.F0011 = F0011;
                }

                public String getF0012() {
                    return F0012;
                }

                public void setF0012(String F0012) {
                    this.F0012 = F0012;
                }

                public String getF0014() {
                    return F0014;
                }

                public void setF0014(String F0014) {
                    this.F0014 = F0014;
                }

                public String getF0015() {
                    return F0015;
                }

                public void setF0015(String F0015) {
                    this.F0015 = F0015;
                }
            }
        }
    }
}
