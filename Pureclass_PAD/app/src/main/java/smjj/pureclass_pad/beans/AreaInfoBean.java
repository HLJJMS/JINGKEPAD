package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/7/12.
 */

public class AreaInfoBean {


    /**
     * Tables : {"Table":{"Rows":[{"RegionID":75,"RegjionName":"长春市","ShortName":"长春 ","AreaCode":"0431","JianPin":"CC ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":76,"RegjionName":"吉林市","ShortName":"吉林 ","AreaCode":"0432","JianPin":"JL ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":77,"RegjionName":"延吉市","ShortName":"延吉 ","AreaCode":"0433","JianPin":"YJ ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":78,"RegjionName":"四平市","ShortName":"四平 ","AreaCode":"0434","JianPin":"SP ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":79,"RegjionName":"通化市","ShortName":"通化 ","AreaCode":"0435","JianPin":"TH ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":80,"RegjionName":"白城市","ShortName":"白城 ","AreaCode":"0436","JianPin":"BC ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":81,"RegjionName":"辽源市","ShortName":"辽源 ","AreaCode":"0437","JianPin":"LY ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":82,"RegjionName":"松原市","ShortName":"松原 ","AreaCode":"0438","JianPin":"SY ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":83,"RegjionName":"浑江市","ShortName":"浑江 ","AreaCode":"0439","JianPin":"HJ ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":84,"RegjionName":"珲春市","ShortName":"珲春 ","AreaCode":"0440","JianPin":"ZC ","ParentID":74,"Flag":true,"LevelID":3}]}}
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
         * Table : {"Rows":[{"RegionID":75,"RegjionName":"长春市","ShortName":"长春 ","AreaCode":"0431","JianPin":"CC ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":76,"RegjionName":"吉林市","ShortName":"吉林 ","AreaCode":"0432","JianPin":"JL ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":77,"RegjionName":"延吉市","ShortName":"延吉 ","AreaCode":"0433","JianPin":"YJ ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":78,"RegjionName":"四平市","ShortName":"四平 ","AreaCode":"0434","JianPin":"SP ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":79,"RegjionName":"通化市","ShortName":"通化 ","AreaCode":"0435","JianPin":"TH ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":80,"RegjionName":"白城市","ShortName":"白城 ","AreaCode":"0436","JianPin":"BC ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":81,"RegjionName":"辽源市","ShortName":"辽源 ","AreaCode":"0437","JianPin":"LY ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":82,"RegjionName":"松原市","ShortName":"松原 ","AreaCode":"0438","JianPin":"SY ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":83,"RegjionName":"浑江市","ShortName":"浑江 ","AreaCode":"0439","JianPin":"HJ ","ParentID":74,"Flag":true,"LevelID":3},{"RegionID":84,"RegjionName":"珲春市","ShortName":"珲春 ","AreaCode":"0440","JianPin":"ZC ","ParentID":74,"Flag":true,"LevelID":3}]}
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
                 * RegionID : 75
                 * RegjionName : 长春市
                 * ShortName : 长春
                 * AreaCode : 0431
                 * JianPin : CC
                 * ParentID : 74
                 * Flag : true
                 * LevelID : 3
                 */

                private int RegionID;
                private String RegjionName;
                private String ShortName;
                private String AreaCode;
                private String JianPin;
                private int ParentID;
                private boolean Flag;
                private int LevelID;

                public int getRegionID() {
                    return RegionID;
                }

                public void setRegionID(int RegionID) {
                    this.RegionID = RegionID;
                }

                public String getRegjionName() {
                    return RegjionName;
                }

                public void setRegjionName(String RegjionName) {
                    this.RegjionName = RegjionName;
                }

                public String getShortName() {
                    return ShortName;
                }

                public void setShortName(String ShortName) {
                    this.ShortName = ShortName;
                }

                public String getAreaCode() {
                    return AreaCode;
                }

                public void setAreaCode(String AreaCode) {
                    this.AreaCode = AreaCode;
                }

                public String getJianPin() {
                    return JianPin;
                }

                public void setJianPin(String JianPin) {
                    this.JianPin = JianPin;
                }

                public int getParentID() {
                    return ParentID;
                }

                public void setParentID(int ParentID) {
                    this.ParentID = ParentID;
                }

                public boolean isFlag() {
                    return Flag;
                }

                public void setFlag(boolean Flag) {
                    this.Flag = Flag;
                }

                public int getLevelID() {
                    return LevelID;
                }

                public void setLevelID(int LevelID) {
                    this.LevelID = LevelID;
                }
            }
        }
    }
}
