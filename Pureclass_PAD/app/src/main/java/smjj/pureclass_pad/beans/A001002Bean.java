package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2018/9/13.
 */

public class A001002Bean {

    /**
     * Tables : {"Table":{"Rows":[{"ID":1,"CompanyCode":"C00015","MenuName":"入门测","MenuNumber":1,"TypeCode":"0","TypeName":"试题"},{"ID":2,"CompanyCode":"C00015","MenuName":"深化应用","MenuNumber":1,"TypeCode":"1","TypeName":"课件"}]}}
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
         * Table : {"Rows":[{"ID":1,"CompanyCode":"C00015","MenuName":"入门测","MenuNumber":1,"TypeCode":"0","TypeName":"试题"},{"ID":2,"CompanyCode":"C00015","MenuName":"深化应用","MenuNumber":1,"TypeCode":"1","TypeName":"课件"}]}
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
                 * ID : 1
                 * CompanyCode : C00015
                 * MenuName : 入门测
                 * MenuNumber : 1
                 * TypeCode : 0
                 * TypeName : 试题
                 */

                private int ID;
                private String CompanyCode;
                private String MenuName;
                private int MenuNumber;
                private String TypeCode;
                private String TypeName;

                public int getID() {
                    return ID;
                }

                public void setID(int ID) {
                    this.ID = ID;
                }

                public String getCompanyCode() {
                    return CompanyCode;
                }

                public void setCompanyCode(String CompanyCode) {
                    this.CompanyCode = CompanyCode;
                }

                public String getMenuName() {
                    return MenuName;
                }

                public void setMenuName(String MenuName) {
                    this.MenuName = MenuName;
                }

                public int getMenuNumber() {
                    return MenuNumber;
                }

                public void setMenuNumber(int MenuNumber) {
                    this.MenuNumber = MenuNumber;
                }

                public String getTypeCode() {
                    return TypeCode;
                }

                public void setTypeCode(String TypeCode) {
                    this.TypeCode = TypeCode;
                }

                public String getTypeName() {
                    return TypeName;
                }

                public void setTypeName(String TypeName) {
                    this.TypeName = TypeName;
                }
            }
        }
    }
}
