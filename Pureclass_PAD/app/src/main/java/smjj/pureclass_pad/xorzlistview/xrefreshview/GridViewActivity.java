package smjj.pureclass_pad.xorzlistview.xrefreshview;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import smjj.pureclass_pad.R;


public class GridViewActivity extends Activity {
	private GridView gv;
	private List<String> str_name = new ArrayList<String>();
	private XRefreshView outView;
	private ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gridview);
		for (int i = 0; i < 50; i++) {
			str_name.add("数据" + i);
		}
		gv = (GridView) findViewById(R.id.gv);
		outView = (XRefreshView) findViewById(R.id.custom_view);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, str_name);
		gv.setAdapter(adapter);
		outView.setPinnedTime(1000);
		outView.setAutoLoadMore(true);
//		outView.setCustomHeaderView(new CustomHeader(this));
//		outView.setCustomHeaderView(new XRefreshViewHeader(this));
		outView.setMoveForHorizontal(true);
		outView.setCustomFooterView(new CustomerFooter(this));
		outView.setPinnedContent(true);
		outView.setPullLoadEnable(false);//禁止上拉加载更多
		outView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
			@Override
			public void onRefresh() {

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						outView.stopRefresh();
					}
				}, 2000);
			}

			@Override
			public void onLoadMore(boolean isSlience) {
				final List<String> addlist = new ArrayList<String>();
				for (int i = 0; i < 20; i++) {
					addlist.add("数据" + (i + str_name.size()));
				}

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (str_name.size() <= 90) {
							if (Build.VERSION.SDK_INT >= 11) {
								str_name.addAll(addlist);
								adapter.addAll(addlist);
							}
							outView.stopLoadMore();
						} else {
							outView.setLoadComplete(true);
						}
					}
				}, 2000);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		outView.startRefresh();
	}


}
