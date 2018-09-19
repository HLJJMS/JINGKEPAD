package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2018/1/16.
 */

public class CheckSignTotalBean {


    /**
     * Tables : {"Table":{"Rows":[{"SchoolNo":"A000010","SchoolName":"新七中校区","number":23,"BranchNo":"B00000001","BranchName":"这是分课名称1","SNumber":1},{"SchoolNo":"A000016","SchoolName":"信美校区","number":12,"BranchNo":"B00000002","BranchName":"这是分课名称2","SNumber":0},{"SchoolNo":"A000019","SchoolName":"香河校区","number":15,"BranchNo":"B00000003","BranchName":"这是分课名称3","SNumber":0}]}}
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
         * Table : {"Rows":[{"SchoolNo":"A000010","SchoolName":"新七中校区","number":23,"BranchNo":"B00000001","BranchName":"这是分课名称1","SNumber":1},{"SchoolNo":"A000016","SchoolName":"信美校区","number":12,"BranchNo":"B00000002","BranchName":"这是分课名称2","SNumber":0},{"SchoolNo":"A000019","SchoolName":"香河校区","number":15,"BranchNo":"B00000003","BranchName":"这是分课名称3","SNumber":0}]}
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
                 * SchoolNo : A000010
                 * SchoolName : 新七中校区
                 * number : 23
                 * BranchNo : B00000001
                 * BranchName : 这是分课名称1
                 * SNumber : 1
                 */

                private String SchoolNo;
                private String SchoolName;
                private int number;
                private String BranchNo;
                private String BranchName;
                private String classNo;
                private int SNumber;
                private String studentNo;
                private String studentName;

                public String getSchoolNo() {
                    return SchoolNo;
                }

                public void setSchoolNo(String SchoolNo) {
                    this.SchoolNo = SchoolNo;
                }

                public String getSchoolName() {
                    return SchoolName;
                }

                public void setSchoolName(String SchoolName) {
                    this.SchoolName = SchoolName;
                }

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
                }

                public String getBranchNo() {
                    return BranchNo;
                }

                public void setBranchNo(String BranchNo) {
                    this.BranchNo = BranchNo;
                }

                public String getBranchName() {
                    return BranchName;
                }

                public void setBranchName(String BranchName) {
                    this.BranchName = BranchName;
                }

                public int getSNumber() {
                    return SNumber;
                }

                public void setSNumber(int SNumber) {
                    this.SNumber = SNumber;
                }

                public String getClassNo() {
                    return classNo;
                }

                public void setClassNo(String classNo) {
                    this.classNo = classNo;
                }
            }
        }
    }
}
