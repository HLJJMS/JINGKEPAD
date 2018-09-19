package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/8/2.
 */

public class InductionTestExercisesBean {


    /**
     * Tables : {"Table":{"Rows":[{"ContentID":2308,"ID":"34c4a6ca-7407-4095-81d8-45ff07317029","Stem":"","Choses":"","Correct":"A","Analysis":"","ErrInfo":"","Flag":1,"JkType":1,"SecendID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678","ZhiSD":"db9e99c6-d8cb-4a37-a016-4585fd1c9678,"}]}}
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
         * Table : {"Rows":[{"ContentID":2308,"ID":"34c4a6ca-7407-4095-81d8-45ff07317029","Stem":"","Choses":"","Correct":"A","Analysis":"","ErrInfo":"","Flag":1,"JkType":1,"SecendID":"db9e99c6-d8cb-4a37-a016-4585fd1c9678","ZhiSD":"db9e99c6-d8cb-4a37-a016-4585fd1c9678,"}]}
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
                 * ContentID : 2308
                 * ID : 34c4a6ca-7407-4095-81d8-45ff07317029
                 * Stem :
                 * Choses :
                 * Correct : A
                 * Analysis :
                 * ErrInfo :
                 * Flag : 1
                 * JkType : 1
                 * SecendID : db9e99c6-d8cb-4a37-a016-4585fd1c9678
                 * ZhiSD : db9e99c6-d8cb-4a37-a016-4585fd1c9678,
                 */

                private int ContentID;
                private String ID;
                private String Stem;
                private String Choses;
                private String Correct;
                private String Analysis;
                private String ErrInfo;
                private int Flag;
                private int JkType;
                private String SecendID;
                private String ZhiSD;

                //接口c001001返回
                private String CitemNo;//练习题答案解析有该字段
                private String TeacherNo;//练习题答案解析有该字段
                private String QID;//练习题答案解析有该字段 题ID
                private int qcount;//练习题答案解析有该字段 答题renshu8
                private int qsum;//练习题答案解析有该字段  答对人数
                private float Corrects;//练习题答案解析有该字段  正确率

                public String getCitemNo() {
                    return CitemNo;
                }

                public void setCitemNo(String citemNo) {
                    CitemNo = citemNo;
                }

                public String getTeacherNo() {
                    return TeacherNo;
                }

                public void setTeacherNo(String teacherNo) {
                    TeacherNo = teacherNo;
                }

                public String getQID() {
                    return QID;
                }

                public void setQID(String QID) {
                    this.QID = QID;
                }

                public int getQcount() {
                    return qcount;
                }

                public void setQcount(int qcount) {
                    this.qcount = qcount;
                }

                public int getQsum() {
                    return qsum;
                }

                public void setQsum(int qsum) {
                    this.qsum = qsum;
                }

                public float getCorrects() {
                    return Corrects;
                }

                public void setCorrects(float corrects) {
                    Corrects = corrects;
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

                public String getCorrect() {
                    return Correct;
                }

                public void setCorrect(String Correct) {
                    this.Correct = Correct;
                }

                public String getAnalysis() {
                    return Analysis;
                }

                public void setAnalysis(String Analysis) {
                    this.Analysis = Analysis;
                }

                public String getErrInfo() {
                    return ErrInfo;
                }

                public void setErrInfo(String ErrInfo) {
                    this.ErrInfo = ErrInfo;
                }

                public int getFlag() {
                    return Flag;
                }

                public void setFlag(int Flag) {
                    this.Flag = Flag;
                }

                public int getJkType() {
                    return JkType;
                }

                public void setJkType(int JkType) {
                    this.JkType = JkType;
                }

                public String getSecendID() {
                    return SecendID;
                }

                public void setSecendID(String SecendID) {
                    this.SecendID = SecendID;
                }

                public String getZhiSD() {
                    return ZhiSD;
                }

                public void setZhiSD(String ZhiSD) {
                    this.ZhiSD = ZhiSD;
                }
            }
        }
    }
}
