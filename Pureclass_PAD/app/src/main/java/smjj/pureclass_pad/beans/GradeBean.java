package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/8/22.
 */

public class GradeBean {


    private List<RowsBean> Rows;

    public List<RowsBean> getRows() {
        return Rows;
    }

    public void setRows(List<RowsBean> Rows) {
        this.Rows = Rows;
    }

    public static class RowsBean {
        /**
         * YearID : 初一
         */

        private String YearID;

        public String getYearID() {
            return YearID;
        }

        public void setYearID(String YearID) {
            this.YearID = YearID;
        }
    }
}
