package smjj.pureclass_pad.common;


/**
 * Created by wlm on 2017/6/6.
 */

public class Constants {

    //服务器地址
    public static final String WEB_SERVER_URL_OFFICIAL = "http://erp.iwenxin.net/webservice/FineClass.asmx";
    public static final String WEB_SERVER_URL_TESt = "http://testerp.iwenxin.net/webservice/FineClass.asmx";

    //判断是不是正式环境true代表正式环境，flas代表测试环境
    public static boolean IS_OFFICIAL = true;

//     正式环境
//    public static final String WEB_SERVER_URL = "http://erp.iwenxin.net/webservice/FineClass.asmx";

    //    测试环境
    public static final String WEB_SERVER_URL = "http://testerp.iwenxin.net/webservice/FineClass.asmx";

//    public static final String WEB_SERVER_URL = "http://47.92.81.135:8001/webservice/FineClass.asmx";
//    public static String WEB_SERVER_URL = WEB_SERVER_URL_OFFICIAL;

    public static final String URL = "http://testerp.iwenxin.net/";

    //查找错误信息的方法
    public static final String ErrorInfoMethodName = "TEST";

    //key
    public static final String key = "QspZCjj7iEGrPWQUg4eqyw";

    //登陆接口方法名
    public static final String LogMethodName = "A001001";

    //排课列表方法名（内部教师）
    public static final String SchedulingMethodName = "A002001";
//    public static final String SchedulingMethodName = "TeacherLogins";

    //排课列表获取学生方法名（内部教师）
    public static final String SchedulingGetStudentMethodName = "A002002";

    //添加学生信息方法名
    public static final String AddStudentInfoMethodName = "A002003";

    //获取省份城市方法名
    public static final String GetCityMethodName = "A002004";

    //获取区域方法名
    public static final String GetAreaMethodName = "A002005";

    //获取排课编号方法名
    public static final String GetClassNoMethodName = "A002006";

    //添加班级信息方法名
    public static final String AddClassInfoMethodName = "A002007";

    //外部教师获取学生列表方法名
    public static final String GetStrdentListMethodName = "A002008";

    //外部教师获取班级列表方法名
    public static final String GetClassListMethodName = "A002009";

    //检查学生账号是否已经存在方法名
    public static final String CheckLoginNameMethodName = "A002010";

    //获取第几讲方法名
    public static final String GetSpeachMethodName = "A002011";

    //获取知识点方法名
    public static final String GetKnowledgeMethodName = "A002012";

    //添加排课信息方法名
    public static final String AddSchedulingMethodName = "A002013";


    //排课列表方法名（外部教师）
    public static final String SchedulingMethodName1 = "A002014";

    //排课列表获取学生方法名（外部教师）
    public static final String SchedulingGetStudentMethodName1 = "A002015";

    //链接排课和讲
    public static final String ContentIDAndSpeach = "A002016";

    //获取讲义
    public static final String GetSpeach = "A002017";

    //外部教师排课获取校区
    public static final String GetSchoolListMN = "A002018";


    //上课学生签到接口
    public static final String GoClassSignMethodName = "A003001";

    //上课学生分组接口
    public static final String GroupMethodName = "A003002";

    //查看学生是否签到
    public static final String GroupStudentMethodName = "A003003";

    //查看学生是否签到
    public static final String CheckSignMethodName = "A003004";

    //查看学生是否分组
    public static final String CheckGroupMethodName = "A003005";

    //修改组名
    public static final String GroupNameAmendMethodName = "A003006";


    //获取题目入门测、出门考
    public static final String GetExercisesMethodName = "A003007";

    //发布题目入门测、出门考
    public static final String ReleaseExercisesMethodName = "A003008";

    //检查是否发布入门测、出门考
    public static final String CheckedReleaseMethodName = "A003009";

    //入门测、出门考个人排名
    public static final String PersonageRankingMethodName = "A003010";

    //入门测、出门考小组排名
    public static final String GroupRankingMethodName = "A003011";

    //个人积分榜
    public static final String PersonageScoreMethodName = "A003012";

    //小组积分榜
    public static final String GroupScoreMethodName = "A003013";

    //获取课堂小结
    public static final String GetConclusionMN = "A003014";

    //保存课堂小结
    public static final String SaveConclusionMN = "A003015";

    //获取情境导入，学习新知接口
    public static final String GetGoClassUrlMN = "A003016";

    //获取选择一级知识点
    public static final String GetKnowledge1MN = "A004001";

    //获取选择子级知识点
    public static final String GetKnowledge2MN = "A004002";

    //获取选择知识点的年级
    public static final String GetSelectGrandeMN = "A004003";

    //获取布置作业排课列表
    public static final String GetSetWorkScdListMN = "A004004";

    //布置作业加入试题篮
    public static final String AddBasketMN = "A004005";

    //获取试题篮列表
    public static final String GetBasketListMN = "A004006";

    //删除试题篮试题
    public static final String DeleteTestBskMN = "A004007";

    //获取课后布置作业学生的接口
    public static final String GetStuSetWorkMN = "A004008";

    //获取单个学生所做的题目
    public static final String GetStuWorkExeMN = "A004009";

    //题目挑错
    public static final String SaveMisInfoMN = "A004011";
    //横向的recyclerview
    public static final String A001002 = "A001002";
    //是否有教学反思
    public static final String HaveTechRlMN = "A004012";

    //获取教学反思列表
    public static final String TechRlListMN = "A004013";

    //课堂反馈列表
    public static final String FeedBackListMN = "A004016";

    //提交课堂反馈
    public static final String SubmitFeedBackInfoMN = "A004017";

    //查询课堂反馈
    public static final String GetFeedBackInfoMN = "A004018";

    //查询课堂反馈学生
    public static final String GetFeedBackStuMN = "A004019";

    //添加教学反思信息
    public static final String AddTeachRLMN = "A004020";

    //查询教学反思信息
    public static final String GetTeachRLMN = "A004021";

    //获取学生入门测、出门考答题情况（课堂反馈）
    public static final String GetStuFeedbackMN = "A004022";

    //获取学生本次作业情况（课堂反馈）
    public static final String GetStuFeedbackMN1 = "A004023";

    //获取学情分析排课列表（课堂反馈）
    public static final String GetAnalysisScdMN = "A004024";

    //获取学情分析学生成绩、排名
    public static final String GetAnalysisStuScoreMN = "A004025";

    //获取学情分析学生知识点掌握情况
    public static final String GetAnalyStuCondMN = "A004026";

    //获取学情分析学生答题展示
    public static final String GetAnalyStuCondMN1 = "A004027";

    //获取入门测、粗门考错误知识点（课堂反馈）
    public static final String GetErroeKPMN = "A004028";

    //获取上次作业错误知识点（课堂反馈）
    public static final String GetErroeKPMN1 = "A004030";

    //获取学生作业情况（课堂反馈）
    public static final String GetStuFeedbackMN2 = "A004032";

    //获取学生课堂反馈内容
    public static final String GetStuFbContentMN = "A004033";

    //保存学情分析内容
    public static final String SaveAnalyMN = "A004034";

    //查询学情分析内容
    public static final String GetAnalyContentMN = "A004035";

    //测评根据知识点获取题目
    public static final String GetAprExeMN = "A005001";

    //测评添加试题篮
    public static final String AddAprBskMN = "A005002";

    //测评获取试题篮列题目
    public static final String GetAprBskMN = "A005003";

    //测评删除试题篮试题
    public static final String DeleteAprBskMN = "A005004";

    //测评生成试题
    public static final String CreatTestMN = "A005005";

    //测评获取教师名下进90天的学生
    public static final String GetAprStuMN = "A005006";

    //测评发布试题
    public static final String AprReleaseTestMN = "A005007";

    //二期获取ppt、课件、说课、授课视屏
    public static final String GetSpeachDetailMN2 = "B002001";

    //二期备课上课获取班级接口(shijian)
    public static final String GetClassMN2 = "B002002";

    //二期获取未备课的排课列表shijian
    public static final String GetNoPreSduMN2 = "B002003";

    //二期获取课讲名
    public static final String GetSpeachMN2 = "B002012";

    //二期确认备课
    public static final String SurePreClassMN2 = "B002005";

    //二期提取资料包
    public static final String GetGramNameMN2 = "B002010";

    //二期个性化备课加入试题篮
    public static final String PersonalAddBskMN2 = "B002006";

    //二期个性化备课获取试题篮试题
    public static final String GetBskExeMN2 = "B002007";

    //删除个性化备课试题篮试题
    public static final String DelePersonalBskMN2 = "B002008";

    //二期创建资料包
    public static final String CreateDataGramMN2 = "B002009";

    //二期获取资料包中的试题
    public static final String GetDataGramExercMN2 = "B002011";

    //二期获取教材
    public static final String GetCourseMN2 = "B002013";

    //二期教材
    public static final String SureCourseMN2 = "B002014";

    //二期上课获取班级信息
    public static final String GetGoClassMN2 = "B003001";

    //二期上课获取已备课的讲
    public static final String GetSpeachClassMN2 = "B003002";

    //二期上课获取入门测、出门考、深化应用、巩固练习试题
    public static final String GetClassExercMN2 = "B003003";

    //二期上课删除入门测、出门考、深化应用、巩固练习试题（试题篮中的）
    public static final String DelClassExercMN2 = "B003004";

    //二期确认上课
    public static final String SureGoClassMN2 = "B003005";

    //二期获取课后班级
    public static final String GetClassAfterMN2 = "B004001";

    //二期获取课后布置作业接口
    public static final String SetWorkCAMN2 = "B004002";

    //二期获取课后教学反思接口
    public static final String GetCAReflecMN2 = "B004003";

    //二期获取课后课堂反馈接口
    public static final String GetCAFeedbackcMN2 = "B004004";

    //二期获取课后学情分析接口
    public static final String GetCAStuAnaMN2 = "B004005";

    //查询答案解析已发布的试题
    public static final String QueryAnswerMN2 = "C001001";

    //查询答案解析已发布的试题
    public static final String QueryAnswerDetailsMN2 = "C001002";

    //双师模式下检查学生签到接口
    public static final String CheckSignTotalMN2 = "C001003";

    //双师模式下停止答题
    public static final String StopAnswerMN2 = "C001004";

    //双师模式发布试题
    public static final String ReleaseTestMN2 = "C001005";

    //双师模式检查是否上课
    public static final String CheckGoClassMN2 = "C001006";

    //双师模式课后获取班级
    public static final String GetClassCAFMN2 = "C004001";

    //双师模式上课默认签到
    public static final String ClassSignMN2 = "C004003";

    //答题器开始答题
    public static final String LCllidkerStartMN2 = "C100080";

    //答题器停止答题
    public static final String LCllidkerStopMN2 = "C100081";

    //双师模式获取各校区签到人数
    public static final String GetSignStuMN2 = "C100083";

    //双师答题器模式确认上课并发布上课试题
    public static final String SignAndRelMN2 = "C100084";

    //双师答题器模式获取临时答题详情
    public static final String GetIQDetailsMN2 = "C100085";

    //双师答题器模式临时答题教师选择正确选项
    public static final String IQTechSelectMN2 = "C100086";

    //双师答题器模式获取学生排名
    public static final String IQGetRankMN2 = "C100087";

    //双师答题器模式抢答教师选择正确选项
    public static final String ResponderSelectMN2 = "C100088";

    //双师答题器模式抢答排行榜
    public static final String ResponderRankMN2 = "C100089";

    //双师答题器模式红包排行榜
    public static final String RedPacketRankMN2 = "C100090";
}
