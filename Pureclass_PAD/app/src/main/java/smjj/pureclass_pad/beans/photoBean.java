package smjj.pureclass_pad.beans;

import java.util.ArrayList;
import java.util.List;

import smjj.pureclass_pad.R;

/**
 * Created by wlm on 2018/9/17.
 */

public class photoBean {
    private List<Integer> photo = new ArrayList<>();

    public photoBean() {
        this.photo.add(R.drawable.rumence);
        this.photo.add(R.drawable.qingjingdaoru);
        this.photo.add(R.drawable.xuexixinzhi);
        this.photo.add(R.drawable.shenhuayingyong);
        this.photo.add(R.drawable.gonggulianxi);
        this.photo.add(R.drawable.ketangxiaoji);
        this.photo.add(R.drawable.chumenkao);

    }

    public List<Integer> getPhoto() {
        return photo;
    }
}
