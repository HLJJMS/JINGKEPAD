package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2018/7/13.
 */

public class RedPacketRankBean {


    /**
     * Tables : {"Table":{"Rows":[{"F0008":"A0000143001","F0009":"天津武清香江广场校","F0010":"SU0038674","F0011":"苏艺伟","F0012":20},{"F0008":"A0000143001","F0009":"天津武清香江广场校","F0010":"SU0038679","F0011":"刘宗","F0012":21},{"F0008":"A0000143001","F0009":"天津武清香江广场校","F0010":"SU0038676","F0011":"刘旭东","F0012":27},{"F0008":"A0000143001","F0009":"天津武清香江广场校","F0010":"SU0038675","F0011":"张玲君","F0012":29}]}}
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
         * Table : {"Rows":[{"F0008":"A0000143001","F0009":"天津武清香江广场校","F0010":"SU0038674","F0011":"苏艺伟","F0012":20},{"F0008":"A0000143001","F0009":"天津武清香江广场校","F0010":"SU0038679","F0011":"刘宗","F0012":21},{"F0008":"A0000143001","F0009":"天津武清香江广场校","F0010":"SU0038676","F0011":"刘旭东","F0012":27},{"F0008":"A0000143001","F0009":"天津武清香江广场校","F0010":"SU0038675","F0011":"张玲君","F0012":29}]}
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
                 * F0008 : A0000143001
                 * F0009 : 天津武清香江广场校
                 * F0010 : SU0038674
                 * F0011 : 苏艺伟
                 * F0012 : 20
                 */

                private String F0008;
                private String F0009;
                private String F0010;
                private String F0011;
                private int F0012;

                public String getF0008() {
                    return F0008;
                }

                public void setF0008(String F0008) {
                    this.F0008 = F0008;
                }

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

                public String getF0011() {
                    return F0011;
                }

                public void setF0011(String F0011) {
                    this.F0011 = F0011;
                }

                public int getF0012() {
                    return F0012;
                }

                public void setF0012(int F0012) {
                    this.F0012 = F0012;
                }
            }
        }
    }
}
