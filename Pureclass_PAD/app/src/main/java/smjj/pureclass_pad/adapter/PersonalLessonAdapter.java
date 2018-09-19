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
import smjj.pureclass_pad.eventbus.EventBusFrist;

/**
 * Created by wlm on 2018/9/13.
 */

public class PersonalLessonAdapter extends RecyclerView.Adapter<PersonalLessonAdapter.viewHolder> {
    private List<A001002Bean.TablesBean.TableBean.RowsBean> bean = new ArrayList<A001002Bean.TablesBean.TableBean.RowsBean>();
    int index=0;
    boolean frist = true;
    RecyclerView recyclerView;

    public void setData(List<A001002Bean.TablesBean.TableBean.RowsBean> bean, RecyclerView recyclerView) {
        this.bean.addAll(bean);
        this.recyclerView = recyclerView;
        notifyDataSetChanged();

    }

    @Override
    public PersonalLessonAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PersonalLessonAdapter.viewHolder holder, final int position) {
        holder.radioButton.setText(bean.get(position).getMenuName());
        if(frist==true&&position==0){
            holder.radioButton.setChecked(true);
        }
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
        viewHolder view = (viewHolder) recyclerView.findViewHolderForLayoutPosition(p);
        view.radioButton.setChecked(true);
        viewHolder indexView = (viewHolder) recyclerView.findViewHolderForLayoutPosition(index);
        indexView.radioButton.setChecked(false);
        notifyItemChanged(index);
        sendMessage(p);
    }

    @Override
    public int getItemCount() {
        return bean.size();
    }
    public List<A001002Bean.TablesBean.TableBean.RowsBean> returnBean(){
        return bean;
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        public RadioButton radioButton;
        public viewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radio_for_recycler);

        }
    }
    private void sendMessage(int p){
        EventBus.getDefault().post(new EventBusFrist(String.valueOf(bean.get(p).getMenuNumber())));
    }

}
