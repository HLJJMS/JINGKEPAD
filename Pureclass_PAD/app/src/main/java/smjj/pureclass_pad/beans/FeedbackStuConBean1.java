package smjj.pureclass_pad.beans;

import java.util.List;

/**
 * Created by wlm on 2017/9/21.
 */

public class FeedbackStuConBean1 {


    /**
     * Tables : {"Table":{"Rows":[{"classId":"1000458254","teachingRecords":"初一数学3-5人班161","StudentNo":"SU0038609","StudentName":"何四奇","StartTime":"2017-09-28","KnowledgeSituation":"5","KnowledgeComment":"知识点掌握不错","TaskSituation":"3","TaskComment":"上次作业完成不错","ThisTask":"本次作业有关知识点是正负数","Other":"希望继续努力"}]}}
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
         * Table : {"Rows":[{"classId":"1000458254","teachingRecords":"初一数学3-5人班161","StudentNo":"SU0038609","StudentName":"何四奇","StartTime":"2017-09-28","KnowledgeSituation":"5","KnowledgeComment":"知识点掌握不错","TaskSituation":"3","TaskComment":"上次作业完成不错","ThisTask":"本次作业有关知识点是正负数","Other":"希望继续努力"}]}
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
                 * classId : 1000458254
                 * teachingRecords : 初一数学3-5人班161
                 * StudentNo : SU0038609
                 * StudentName : 何四奇
                 * StartTime : 2017-09-28
                 * KnowledgeSituation : 5
                 * KnowledgeComment : 知识点掌握不错
                 * TaskSituation : 3
                 * TaskComment : 上次作业完成不错
                 * ThisTask : 本次作业有关知识点是正负数
                 * Other : 希望继续努力
                 */

                private String classId;
                private String teachingRecords;
                private String StudentNo;
                private String StudentName;
                private String StartTime;
                private String KnowledgeSituation;
                private String KnowledgeComment;
                private String TaskSituation;
                private String TaskComment;
                private String ThisTask;
                private String Other;

                public String getClassId() {
                    return classId;
                }

                public void setClassId(String classId) {
                    this.classId = classId;
                }

                public String getTeachingRecords() {
                    return teachingRecords;
                }

                public void setTeachingRecords(String teachingRecords) {
                    this.teachingRecords = teachingRecords;
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

                public String getStartTime() {
                    return StartTime;
                }

                public void setStartTime(String StartTime) {
                    this.StartTime = StartTime;
                }

                public String getKnowledgeSituation() {
                    return KnowledgeSituation;
                }

                public void setKnowledgeSituation(String KnowledgeSituation) {
                    this.KnowledgeSituation = KnowledgeSituation;
                }

                public String getKnowledgeComment() {
                    return KnowledgeComment;
                }

                public void setKnowledgeComment(String KnowledgeComment) {
                    this.KnowledgeComment = KnowledgeComment;
                }

                public String getTaskSituation() {
                    return TaskSituation;
                }

                public void setTaskSituation(String TaskSituation) {
                    this.TaskSituation = TaskSituation;
                }

                public String getTaskComment() {
                    return TaskComment;
                }

                public void setTaskComment(String TaskComment) {
                    this.TaskComment = TaskComment;
                }

                public String getThisTask() {
                    return ThisTask;
                }

                public void setThisTask(String ThisTask) {
                    this.ThisTask = ThisTask;
                }

                public String getOther() {
                    return Other;
                }

                public void setOther(String Other) {
                    this.Other = Other;
                }
            }
        }
    }
}
