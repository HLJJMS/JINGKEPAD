package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/8/22.
 */

public class KnowledgeBean {


    /**
     * Tables : {"Table":{"Rows":[{"xueke":"英语","YearID":"小一,小二,小三","Chaptername":"词汇","SubID":2426,"ID":"ef0892fb-fe0e-4c62-9c41-3f273adc15e8"},{"xueke":"英语","YearID":"小一,小二,小三","Chaptername":"字母","SubID":2430,"ID":"27a26f54-f59c-40ab-9ec9-44e10993cadd"},{"xueke":"英语","YearID":"小一,小二,小三","Chaptername":"话题","SubID":2447,"ID":"17aecd86-59aa-4e37-8b19-5c6392d05e6e"}]}}
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
         * Table : {"Rows":[{"xueke":"英语","YearID":"小一,小二,小三","Chaptername":"词汇","SubID":2426,"ID":"ef0892fb-fe0e-4c62-9c41-3f273adc15e8"},{"xueke":"英语","YearID":"小一,小二,小三","Chaptername":"字母","SubID":2430,"ID":"27a26f54-f59c-40ab-9ec9-44e10993cadd"},{"xueke":"英语","YearID":"小一,小二,小三","Chaptername":"话题","SubID":2447,"ID":"17aecd86-59aa-4e37-8b19-5c6392d05e6e"}]}
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
                 * xueke : 英语
                 * YearID : 小一,小二,小三
                 * Chaptername : 词汇
                 * SubID : 2426
                 * ID : ef0892fb-fe0e-4c62-9c41-3f273adc15e8
                 */

                private String xueke;
                private String YearID;
                private String Chaptername;
                private int SubID;
                private String ID;

                public String getXueke() {
                    return xueke;
                }

                public void setXueke(String xueke) {
                    this.xueke = xueke;
                }

                public String getYearID() {
                    return YearID;
                }

                public void setYearID(String YearID) {
                    this.YearID = YearID;
                }

                public String getChaptername() {
                    return Chaptername;
                }

                public void setChaptername(String Chaptername) {
                    this.Chaptername = Chaptername;
                }

                public int getSubID() {
                    return SubID;
                }

                public void setSubID(int SubID) {
                    this.SubID = SubID;
                }

                public String getID() {
                    return ID;
                }

                public void setID(String ID) {
                    this.ID = ID;
                }
            }
        }
    }
}
