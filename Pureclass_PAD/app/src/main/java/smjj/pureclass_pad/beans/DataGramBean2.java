package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/12/11.
 */

public class DataGramBean2 {


    /**
     * Tables : {"Table":{"Rows":[{"PackageNo":"bf1656ab-fc6b-4f6a-ba83-b0e9aa40611f","PackageName":"2"}]}}
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
         * Table : {"Rows":[{"PackageNo":"bf1656ab-fc6b-4f6a-ba83-b0e9aa40611f","PackageName":"2"}]}
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
                 * PackageNo : bf1656ab-fc6b-4f6a-ba83-b0e9aa40611f
                 * PackageName : 2
                 */

                private String PackageNo;
                private String PackageName;

                public String getPackageNo() {
                    return PackageNo;
                }

                public void setPackageNo(String PackageNo) {
                    this.PackageNo = PackageNo;
                }

                public String getPackageName() {
                    return PackageName;
                }

                public void setPackageName(String PackageName) {
                    this.PackageName = PackageName;
                }
            }
        }
    }
}
