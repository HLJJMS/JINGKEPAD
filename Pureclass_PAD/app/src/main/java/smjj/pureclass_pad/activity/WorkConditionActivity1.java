package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.view.AutoHeightGridView;
import smjj.pureclass_pad.view.AutoHeightListView;
/**
 * 统测部分查看试题界面
 */
public class WorkConditionActivity1 extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_ranking_list1, ll_ranking_list;
    private Button sureBt;
    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;

    private Context context;
    private Handler mHandler;

    private SharedPreferences spConfig;

    private AutoHeightGridView gridView;
    private CommonAdapter gridViewAdapter;
    private List<String> gridViewList;
    private List<String> checkStudentList = new ArrayList<>();

    private AutoHeightListView listView;
    private List<String> listData;
    private CommonAdapter listAdapter;

    private AutoHeightListView listView1;
    private List<String> listData1;
    private CommonAdapter listAdapter1;

    //1代表课后查看作业   2代表测评查看试题
    private String enterMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_condition1);

        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;

        findView();


    }


    private void findView(){
        gridView = (AutoHeightGridView)findViewById(R.id.gridview);
        listView = (AutoHeightListView) findViewById(R.id.scheduling_lv);
        listView1 = (AutoHeightListView) findViewById(R.id.scheduling_lv1);
        ll_ranking_list = (LinearLayout) findViewById(R.id.ll_ranking_list);
        ll_ranking_list1 = (LinearLayout) findViewById(R.id.ll_ranking_list1);
        sureBt = (Button) findViewById(R.id.log_bt);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);

        enterMark = getIntent().getStringExtra("enterMark");

        initAdapter();

        if (enterMark.equals("1")){
            title_tv.setText("查看作业");
        }else if (enterMark.equals("2")){
            title_tv.setText("测评");
        }

        ll_ranking_list.setVisibility(View.VISIBLE);
        ll_ranking_list1.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

        sureBt.setOnClickListener(this);
        onBack.setOnClickListener(this);

    }


    private void initAdapter(){

        gridViewList = new ArrayList<>();
        gridViewList.add("王李明");
        gridViewList.add("方振东");
        gridViewList.add("牛强");
        gridViewList.add("夏花");

        gridViewAdapter = new CommonAdapter<String>(context, gridViewList, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final String rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setChecked(true);
                if (!checkStudentList.contains(rowsBean)){
                    checkStudentList.add(rowsBean);
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            checkStudentList.add(rowsBean);
                            checkBox.setTextColor(Color.WHITE);
                        }else {
                            checkStudentList.remove(rowsBean);
                            checkBox.setTextColor(Color.parseColor("#666666"));
                        }
                    }
                });

            }
        };


        listData = new ArrayList<>();
        listData.add("王李明");
        listData.add("方振东");
        listData.add("牛强");
        listData.add("夏花");

        final List<String> list = new ArrayList<>();
        list.add("100");
        list.add("90");
        list.add("80");
        list.add("70");

        final List<String> list1 = new ArrayList<>();
        list1.add("10");
        list1.add("9");
        list1.add("8");
        list1.add("7");

        listAdapter = new CommonAdapter<String>(context, listData, R.layout.item_module_ranking_lv) {
            @Override
            public void convert(ViewHolder holder, final String rowsBean) {
                int i = listData.indexOf(rowsBean);
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                if (i % 2 == 0) {
                    ll_item.setBackgroundColor(Color.parseColor("#d1ecd8"));
                } else {
                    ll_item.setBackgroundColor(Color.parseColor("#bae2c4"));
                }
                TextView rankingTextView = holder.getView(R.id.ranking_tv);
                ImageView rankingImageView = holder.getView(R.id.ranking_iv);
                if (i == 0){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.first);
                }else if (i ==1){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.second);
                }else if (i == 2){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.third);
                }else {
                    rankingImageView.setVisibility(View.GONE);
                    rankingTextView.setVisibility(View.VISIBLE);
                    rankingTextView.setText("" + (i + 1));
                }


                holder.setText(R.id.name_tv, listData.get(i));
                holder.setText(R.id.score_tv, list.get(i));
                holder.setText(R.id.percent_tv, list1.get(i));
            }
        };


        listData1 = new ArrayList<>();
        listData1.add("1.下列措施中，能使蒸发减慢的是（）");
        listData1.add("2.为架设一条输电线路，有粗细相同的铁线和铝线可供选择，下面叙述最合理的是（）");


        final List<String> list11 = new ArrayList<>();
        list11.add("A.给湿头发吹热风");
        list11.add("A.因铁线坚硬，应选铁线");

        final List<String> list12 = new ArrayList<>();
        list12.add("B.把盛有酒精的瓶口盖严");
        list12.add("B.因铝线易于架设，应选铝线");
        final List<String> list23 = new ArrayList<>();
        list23.add("C.给湿头发吹冷风");
        list23.add("C.因铁较便宜，应选铁线");
        final List<String> list34 = new ArrayList<>();
        list34.add("D.把盛有酒精的瓶口拧开");
        list34.add("D.因铝线电阻小，应选铝线");
        final List<String> list45 = new ArrayList<>();
        list45.add("B");
        list45.add("D");
        final List<String> list56 = new ArrayList<>();
        list56.add("酒精的饱和蒸气压比较低所以容易升华");
        list56.add("远距离输送电能，根据功率损失的表达式△P=I2R可知，要减少输电线上的功率损失，有两种方法：其一是减少输电线路的电阻，其二是减少输电电流，就本题而言应减少输电线路的电阻");

        listAdapter1 = new CommonAdapter<String>(context, listData1, R.layout.item_exercises_answer) {
            @Override
            public void convert(ViewHolder holder, final String rowsBean) {
                int i = listData1.indexOf(rowsBean);

                LinearLayout ll_answer5 = holder.getView(R.id.ll_exercises_answer5);
                LinearLayout ll_answer6 = holder.getView(R.id.ll_exercises_answer6);
                LinearLayout ll_answer7 = holder.getView(R.id.ll_exercises_answer7);
                ll_answer5.setVisibility(View.VISIBLE);
                ll_answer6.setVisibility(View.VISIBLE);
                ll_answer7.setVisibility(View.VISIBLE);


                holder.setText(R.id.tv_exercises_title, listData1.get(i));
                holder.setText(R.id.tv_exercises_answer1, list11.get(i));
                holder.setText(R.id.tv_exercises_answer2, list12.get(i));
                holder.setText(R.id.tv_exercises_answer3, list23.get(i));
                holder.setText(R.id.tv_exercises_answer4, list34.get(i));
                holder.setText(R.id.tv_exercises_answer5, list45.get(i));
                holder.setText(R.id.tv_exercises_answer6, list56.get(i));

            }
        };



        gridView.setAdapter(gridViewAdapter);
        listView.setAdapter(listAdapter);
        listView1.setAdapter(listAdapter1);



    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.onBack:
                finish();
                break;
            case R.id.log_bt:
                if (checkStudentList.size() <= 1){
                    ll_ranking_list.setVisibility(View.GONE);
                    ll_ranking_list1.setVisibility(View.VISIBLE);
                }else {
                    ll_ranking_list.setVisibility(View.VISIBLE);
                    ll_ranking_list1.setVisibility(View.GONE);
                }
                //请求数据刷新适配器
                break;
        }

    }
}
