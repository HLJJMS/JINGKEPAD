package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/8/23.
 */

public class TestBasketBean {


    /**
     * Tables : {"Table":{"Rows":[{"BasketID":1180,"ContentID":4417,"ID":"16cfbe2c-98b6-4ed6-99ca-c1aa7b2c845e","Subject":"语文","Grade":"初一","Flag":1,"LessonID":"EC1000000001","Stem":"的一项是","Choses":"春水》是冰心在印度诗人泰戈尔《飞鸟集》的影响下写成的。"}]}}
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
         * Table : {"Rows":[{"BasketID":1180,"ContentID":4417,"ID":"16cfbe2c-98b6-4ed6-99ca-c1aa7b2c845e","Subject":"语文","Grade":"初一","Flag":1,"LessonID":"EC1000000001","Stem":"的一项是","Choses":"春水》是冰心在印度诗人泰戈尔《飞鸟集》的影响下写成的。"}]}
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
                 * BasketID : 1180
                 * ContentID : 4417
                 * ID : 16cfbe2c-98b6-4ed6-99ca-c1aa7b2c845e
                 * Subject : 语文
                 * Grade : 初一
                 * Flag : 1
                 * LessonID : EC1000000001
                 * Stem : 的一项是
                 * Choses : 春水》是冰心在印度诗人泰戈尔《飞鸟集》的影响下写成的。
                 */

                private int BasketID;
                private int ContentID;
                private String ID;
                private String Subject;
                private String Grade;
                private int Flag;
                private String LessonID;
                private String Stem;
                private String Choses;
                private String TitleType;

                public String getTitleType() {
                    return TitleType;
                }

                public void setTitleType(String titleType) {
                    TitleType = titleType;
                }

                public int getBasketID() {
                    return BasketID;
                }

                public void setBasketID(int BasketID) {
                    this.BasketID = BasketID;
                }

                public int getContentID() {
                    return ContentID;
                }

                public void setContentID(int ContentID) {
                    this.ContentID = ContentID;
                }

                public String getID() {
                    return ID;
                }

                public void setID(String ID) {
                    this.ID = ID;
                }

                public String getSubject() {
                    return Subject;
                }

                public void setSubject(String Subject) {
                    this.Subject = Subject;
                }

                public String getGrade() {
                    return Grade;
                }

                public void setGrade(String Grade) {
                    this.Grade = Grade;
                }

                public int getFlag() {
                    return Flag;
                }

                public void setFlag(int Flag) {
                    this.Flag = Flag;
                }

                public String getLessonID() {
                    return LessonID;
                }

                public void setLessonID(String LessonID) {
                    this.LessonID = LessonID;
                }

                public String getStem() {
                    return Stem;
                }

                public void setStem(String Stem) {
                    this.Stem = Stem;
                }

                public String getChoses() {
                    return Choses;
                }

                public void setChoses(String Choses) {
                    this.Choses = Choses;
                }
            }
        }
    }
}
