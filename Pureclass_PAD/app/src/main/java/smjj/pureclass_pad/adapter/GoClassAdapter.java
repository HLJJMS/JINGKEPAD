package smjj.pureclass_pad.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.beans.A001002Bean;
import smjj.pureclass_pad.beans.photoBean;

/**
 * Created by wlm on 2018/9/17.
 */

public class GoClassAdapter extends RecyclerView.Adapter<GoClassAdapter.viewHolder> {
    private List<A001002Bean.TablesBean.TableBean.RowsBean> listBean = new ArrayList<A001002Bean.TablesBean.TableBean.RowsBean>();
    private photoBean photoBean = new photoBean();
    private GoClassActivity activity;
    private WeakReference<GoClassActivity> reference;
    public GoClassAdapter(GoClassActivity activity) {
        reference = new WeakReference<>(activity);
        this.activity = (GoClassActivity)reference.get();
    }

    @Override
    public GoClassAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_go_class_end, parent, false);
        return new GoClassAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoClassAdapter.viewHolder holder, final int position) {
        holder.txt.setText(listBean.get(position).getMenuName());
        holder.imageView.setImageResource(photoBean.getPhoto().get(position));
        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            activity.getTypeCode(listBean.get(position).getTypeCode());
                activity.getTypeName(listBean.get(position).getMenuName(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }
    public void setData(List<A001002Bean.TablesBean.TableBean.RowsBean> bean){
        listBean.clear();
        listBean.addAll(bean);
        notifyDataSetChanged();
    }
    public class viewHolder extends RecyclerView.ViewHolder{
        TextView txt;
        ImageView imageView;
        public viewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.photo) ;
            txt = (TextView)itemView.findViewById(R.id.txt);
        }
    }
}
