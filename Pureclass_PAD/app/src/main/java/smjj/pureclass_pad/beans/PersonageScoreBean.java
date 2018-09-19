package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/8/15.
 */

public class PersonageScoreBean {


    /**
     * Tables : {"Table":{"Rows":[{"F0011":"张玉","F0006":2,"F0007":2,"F0009":"天津武清香江广场校"},{"F0011":"张小芹","F0006":2,"F0007":2,"F0009":"天津武清香江广场校"},{"F0011":"阳欧","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"徐震","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"白鹏","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"黄睿","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"朱文帅","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"冷欣妍","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"郭芙清","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"}]}}
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
         * Table : {"Rows":[{"F0011":"张玉","F0006":2,"F0007":2,"F0009":"天津武清香江广场校"},{"F0011":"张小芹","F0006":2,"F0007":2,"F0009":"天津武清香江广场校"},{"F0011":"阳欧","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"徐震","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"白鹏","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"黄睿","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"朱文帅","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"冷欣妍","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"},{"F0011":"郭芙清","F0006":null,"F0007":null,"F0009":"天津武清香江广场校"}]}
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
                 * F0011 : 张玉
                 * F0006 : 2
                 * F0007 : 2
                 * F0009 : 天津武清香江广场校
                 */

                private String F0011;
                private int F0006;
                private int F0007;
                private String F0009;

                public String getF0011() {
                    return F0011;
                }

                public void setF0011(String F0011) {
                    this.F0011 = F0011;
                }

                public int getF0006() {
                    return F0006;
                }

                public void setF0006(int F0006) {
                    this.F0006 = F0006;
                }

                public int getF0007() {
                    return F0007;
                }

                public void setF0007(int F0007) {
                    this.F0007 = F0007;
                }

                public String getF0009() {
                    return F0009;
                }

                public void setF0009(String F0009) {
                    this.F0009 = F0009;
                }
            }
        }
    }
}
