package smjj.pureclass_pad.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.A001002Bean;
import smjj.pureclass_pad.eventbus.EventType;

/**
 * Created by wlm on 2018/9/14.
 */

public class LessonsBskAdapter extends RecyclerView.Adapter<LessonsBskAdapter.viewHolder> {
    private List<A001002Bean.TablesBean.TableBean.RowsBean> bean = new ArrayList<A001002Bean.TablesBean.TableBean.RowsBean>();
    int index=0;
    boolean frist = true;
    RecyclerView recyclerView;
    List<String> sumList=new ArrayList<String>();
    @Override
    public LessonsBskAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonsBskAdapter.viewHolder(view);
    }
    public void setData(List<A001002Bean.TablesBean.TableBean.RowsBean> bean,List<String> sumList, RecyclerView recyclerView) {
        this.bean.addAll(bean);
        this.recyclerView = recyclerView;
        this.sumList.addAll(sumList);
        notifyDataSetChanged();

    }
    public void setSumList(String type){
        for(int i = 0;i<sumList.size();i++){
            if(sumList.get(i).equals(type)){
                sumList.set(i,String.valueOf(Integer.valueOf(sumList.get(i))-1));
                notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onBindViewHolder(LessonsBskAdapter.viewHolder holder, final int position) {
        holder.radioButton.setText(bean.get(position).getMenuName()+sumList.get(position));
        if(frist==true&&position==0){
            holder.radioButton.setChecked(true);
        }
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(position);
            }
        });
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                frist=false;
                if (frist != true) {
                    Log.e("ok","ok");
                    doCheak(position);
                    index = position;
                }
            }
        });

    }

    private void doCheak(int p) {

        LessonsBskAdapter.viewHolder view = (LessonsBskAdapter.viewHolder) recyclerView.findViewHolderForLayoutPosition(p);
        view.radioButton.setChecked(true);
        LessonsBskAdapter.viewHolder indexView = (LessonsBskAdapter.viewHolder) recyclerView.findViewHolderForLayoutPosition(index);
        indexView.radioButton.setChecked(false);
        notifyItemChanged(index);

    }
    private void sendMessage(int p ){
        EventBus.getDefault().post(new EventType(String.valueOf(p)));
    }
    @Override
    public int getItemCount() {
        return bean.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public RadioButton radioButton;
        public viewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radio_for_recycler);

        }
    }
}
