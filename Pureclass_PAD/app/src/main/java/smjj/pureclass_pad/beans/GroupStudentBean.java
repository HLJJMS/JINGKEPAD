package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/7/28.
 */

public class GroupStudentBean {


    /**
     * Tables : {"Table":{"Rows":[{"GroupingID":1,"GroupingName":"第一组","StudentNo":"SU0010435","StudentName":"王彦婷"},{"GroupingID":1,"GroupingName":"第一组","StudentNo":"SU0025997","StudentName":"朱予涵"},{"GroupingID":2,"GroupingName":"第二组","StudentNo":"SU0016581","StudentName":"徐紫千"}]}}
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
         * Table : {"Rows":[{"GroupingID":1,"GroupingName":"第一组","StudentNo":"SU0010435","StudentName":"王彦婷"},{"GroupingID":1,"GroupingName":"第一组","StudentNo":"SU0025997","StudentName":"朱予涵"},{"GroupingID":2,"GroupingName":"第二组","StudentNo":"SU0016581","StudentName":"徐紫千"}]}
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
                 * GroupingID : 1
                 * GroupingName : 第一组
                 * StudentNo : SU0010435
                 * StudentName : 王彦婷
                 */

                private int GroupingID;
                private String GroupingName;
                private String StudentNo;
                private String StudentName;

                public int getGroupingID() {
                    return GroupingID;
                }

                public void setGroupingID(int GroupingID) {
                    this.GroupingID = GroupingID;
                }

                public String getGroupingName() {
                    return GroupingName;
                }

                public void setGroupingName(String GroupingName) {
                    this.GroupingName = GroupingName;
                }

                public String getStudentNo() {
                    return StudentNo;
                }

                public void setStudentNo(String StudentNo) {
                    this.StudentNo = StudentNo;
                }

                public String getStudentName() {
                    return StudentName;
                }

                public void setStudentName(String StudentName) {
                    this.StudentName = StudentName;
                }
            }
        }
    }
}
