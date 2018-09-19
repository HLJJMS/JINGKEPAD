package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/7/12.
 */

public class CityInfoBean {


    /**
     * Tables : {"Table":{"Rows":[{"RegionID":9,"RegjionName":"北京市","ShortName":"北京 ","AreaCode":"010","JianPin":"BJ ","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":28,"RegjionName":"河北省","ShortName":"河北","AreaCode":"","JianPin":"HB","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":40,"RegjionName":"江西省","ShortName":"江西","AreaCode":"","JianPin":"JX","ParentID":2,"Flag":true,"LevelID":2},{"RegionID":52,"RegjionName":"广东省","ShortName":"广东","AreaCode":"","JianPin":"GD","ParentID":4,"Flag":true,"LevelID":2},{"RegionID":74,"RegjionName":"吉林省","ShortName":"吉林","AreaCode":"","JianPin":"JL","ParentID":8,"Flag":true,"LevelID":2},{"RegionID":85,"RegjionName":"西藏","ShortName":"西藏","AreaCode":"","JianPin":"XC","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":89,"RegjionName":"上海市","ShortName":"上海 ","AreaCode":"021","JianPin":"SH ","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":109,"RegjionName":"浙江省","ShortName":"浙江","AreaCode":"","JianPin":"ZJ","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":121,"RegjionName":"山西省","ShortName":"山西","AreaCode":"","JianPin":"SX","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":132,"RegjionName":"四川省","ShortName":"四川","AreaCode":"","JianPin":"SC","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":156,"RegjionName":"重庆市","ShortName":"重庆 ","AreaCode":"023","JianPin":"ZQ ","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":197,"RegjionName":"广西省","ShortName":"广西","AreaCode":"","JianPin":"GX","ParentID":4,"Flag":true,"LevelID":2},{"RegionID":208,"RegjionName":"天津市","ShortName":"天津 ","AreaCode":"022","JianPin":"TJ ","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":227,"RegjionName":"辽宁省","ShortName":"辽宁","AreaCode":"","JianPin":"LN","ParentID":8,"Flag":true,"LevelID":2},{"RegionID":242,"RegjionName":"甘肃省","ShortName":"甘肃","AreaCode":"","JianPin":"GS","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":254,"RegjionName":"湖南省","ShortName":"湖南","AreaCode":"","JianPin":"HN","ParentID":2,"Flag":true,"LevelID":2},{"RegionID":269,"RegjionName":"贵州省","ShortName":"贵州","AreaCode":"","JianPin":"GZ","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":279,"RegjionName":"湖北省","ShortName":"湖北","AreaCode":"","JianPin":"HB","ParentID":2,"Flag":true,"LevelID":2},{"RegionID":294,"RegjionName":"山东省","ShortName":"山东","AreaCode":"","JianPin":"SD","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":305,"RegjionName":"河南省","ShortName":"河南","AreaCode":"","JianPin":"HN","ParentID":2,"Flag":true,"LevelID":2},{"RegionID":323,"RegjionName":"陕西省","ShortName":"陕西","AreaCode":"","JianPin":"SX","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":334,"RegjionName":"香港","ShortName":"香港","AreaCode":"","JianPin":"XG","ParentID":7,"Flag":true,"LevelID":2},{"RegionID":356,"RegjionName":"江苏省","ShortName":"江苏","AreaCode":"","JianPin":"JS","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":370,"RegjionName":"黑龙江","ShortName":"黑龙江","AreaCode":"","JianPin":"HLJ","ParentID":8,"Flag":true,"LevelID":2},{"RegionID":381,"RegjionName":"云南省","ShortName":"云南","AreaCode":"","JianPin":"YN","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":399,"RegjionName":"青海省","ShortName":"青海","AreaCode":"","JianPin":"QH","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":407,"RegjionName":"澳门","ShortName":"澳门","AreaCode":"","JianPin":"AM","ParentID":7,"Flag":true,"LevelID":2},{"RegionID":412,"RegjionName":"内蒙古","ShortName":"内蒙古","AreaCode":"","JianPin":"NMG","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":425,"RegjionName":"福建省","ShortName":"福建","AreaCode":"","JianPin":"FJ","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":436,"RegjionName":"安徽省","ShortName":"安徽省","AreaCode":"","JianPin":"AHS","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":453,"RegjionName":"宁夏","ShortName":"宁夏","AreaCode":"","JianPin":"NX","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":458,"RegjionName":"海南省","ShortName":"海南","AreaCode":"","JianPin":"HN","ParentID":4,"Flag":true,"LevelID":2},{"RegionID":462,"RegjionName":"新疆","ShortName":"新疆","AreaCode":"","JianPin":"XJ","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":481,"RegjionName":"台湾","ShortName":"台湾","AreaCode":"886","JianPin":"TW","ParentID":7,"Flag":true,"LevelID":2}]}}
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
         * Table : {"Rows":[{"RegionID":9,"RegjionName":"北京市","ShortName":"北京 ","AreaCode":"010","JianPin":"BJ ","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":28,"RegjionName":"河北省","ShortName":"河北","AreaCode":"","JianPin":"HB","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":40,"RegjionName":"江西省","ShortName":"江西","AreaCode":"","JianPin":"JX","ParentID":2,"Flag":true,"LevelID":2},{"RegionID":52,"RegjionName":"广东省","ShortName":"广东","AreaCode":"","JianPin":"GD","ParentID":4,"Flag":true,"LevelID":2},{"RegionID":74,"RegjionName":"吉林省","ShortName":"吉林","AreaCode":"","JianPin":"JL","ParentID":8,"Flag":true,"LevelID":2},{"RegionID":85,"RegjionName":"西藏","ShortName":"西藏","AreaCode":"","JianPin":"XC","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":89,"RegjionName":"上海市","ShortName":"上海 ","AreaCode":"021","JianPin":"SH ","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":109,"RegjionName":"浙江省","ShortName":"浙江","AreaCode":"","JianPin":"ZJ","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":121,"RegjionName":"山西省","ShortName":"山西","AreaCode":"","JianPin":"SX","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":132,"RegjionName":"四川省","ShortName":"四川","AreaCode":"","JianPin":"SC","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":156,"RegjionName":"重庆市","ShortName":"重庆 ","AreaCode":"023","JianPin":"ZQ ","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":197,"RegjionName":"广西省","ShortName":"广西","AreaCode":"","JianPin":"GX","ParentID":4,"Flag":true,"LevelID":2},{"RegionID":208,"RegjionName":"天津市","ShortName":"天津 ","AreaCode":"022","JianPin":"TJ ","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":227,"RegjionName":"辽宁省","ShortName":"辽宁","AreaCode":"","JianPin":"LN","ParentID":8,"Flag":true,"LevelID":2},{"RegionID":242,"RegjionName":"甘肃省","ShortName":"甘肃","AreaCode":"","JianPin":"GS","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":254,"RegjionName":"湖南省","ShortName":"湖南","AreaCode":"","JianPin":"HN","ParentID":2,"Flag":true,"LevelID":2},{"RegionID":269,"RegjionName":"贵州省","ShortName":"贵州","AreaCode":"","JianPin":"GZ","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":279,"RegjionName":"湖北省","ShortName":"湖北","AreaCode":"","JianPin":"HB","ParentID":2,"Flag":true,"LevelID":2},{"RegionID":294,"RegjionName":"山东省","ShortName":"山东","AreaCode":"","JianPin":"SD","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":305,"RegjionName":"河南省","ShortName":"河南","AreaCode":"","JianPin":"HN","ParentID":2,"Flag":true,"LevelID":2},{"RegionID":323,"RegjionName":"陕西省","ShortName":"陕西","AreaCode":"","JianPin":"SX","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":334,"RegjionName":"香港","ShortName":"香港","AreaCode":"","JianPin":"XG","ParentID":7,"Flag":true,"LevelID":2},{"RegionID":356,"RegjionName":"江苏省","ShortName":"江苏","AreaCode":"","JianPin":"JS","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":370,"RegjionName":"黑龙江","ShortName":"黑龙江","AreaCode":"","JianPin":"HLJ","ParentID":8,"Flag":true,"LevelID":2},{"RegionID":381,"RegjionName":"云南省","ShortName":"云南","AreaCode":"","JianPin":"YN","ParentID":6,"Flag":true,"LevelID":2},{"RegionID":399,"RegjionName":"青海省","ShortName":"青海","AreaCode":"","JianPin":"QH","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":407,"RegjionName":"澳门","ShortName":"澳门","AreaCode":"","JianPin":"AM","ParentID":7,"Flag":true,"LevelID":2},{"RegionID":412,"RegjionName":"内蒙古","ShortName":"内蒙古","AreaCode":"","JianPin":"NMG","ParentID":1,"Flag":true,"LevelID":2},{"RegionID":425,"RegjionName":"福建省","ShortName":"福建","AreaCode":"","JianPin":"FJ","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":436,"RegjionName":"安徽省","ShortName":"安徽省","AreaCode":"","JianPin":"AHS","ParentID":3,"Flag":true,"LevelID":2},{"RegionID":453,"RegjionName":"宁夏","ShortName":"宁夏","AreaCode":"","JianPin":"NX","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":458,"RegjionName":"海南省","ShortName":"海南","AreaCode":"","JianPin":"HN","ParentID":4,"Flag":true,"LevelID":2},{"RegionID":462,"RegjionName":"新疆","ShortName":"新疆","AreaCode":"","JianPin":"XJ","ParentID":5,"Flag":true,"LevelID":2},{"RegionID":481,"RegjionName":"台湾","ShortName":"台湾","AreaCode":"886","JianPin":"TW","ParentID":7,"Flag":true,"LevelID":2}]}
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
                 * RegionID : 9
                 * RegjionName : 北京市
                 * ShortName : 北京
                 * AreaCode : 010
                 * JianPin : BJ
                 * ParentID : 1
                 * Flag : true
                 * LevelID : 2
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
