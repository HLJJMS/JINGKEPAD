package smjj.pureclass_pad.eventbus;

import java.util.List;

import smjj.pureclass_pad.beans.A001002Bean;

/**
 * Created by wlm on 2018/9/14.
 */

public class EventBusTwo {
   private   List<A001002Bean.TablesBean.TableBean.RowsBean>bean;
   public  EventBusTwo( List<A001002Bean.TablesBean.TableBean.RowsBean> bean){
       this.bean = bean;
   }

    public List <A001002Bean.TablesBean.TableBean.RowsBean> getBean() {
        return bean;
    }

    public void setBean( List<A001002Bean.TablesBean.TableBean.RowsBean> bean) {
        this.bean = bean;
    }
}
