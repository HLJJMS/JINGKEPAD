package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/2.
 */

public class StuSetWorkBean {

    private List<RowsBean> Rows;

    public List<RowsBean> getRows() {
        return Rows;
    }

    public void setRows(List<RowsBean> Rows) {
        this.Rows = Rows;
    }

    public static class RowsBean {
        /**
         * StudentName : 朱予涵
         * StudentNo : SU0025997
         */

        private String StudentName;
        private String StudentNo;

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String StudentName) {
            this.StudentName = StudentName;
        }

        public String getStudentNo() {
            return StudentNo;
        }

        public void setStudentNo(String StudentNo) {
            this.StudentNo = StudentNo;
        }
    }
}
