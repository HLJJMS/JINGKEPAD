package smjj.pureclass_pad.video.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.video.widget.VolumnView;


public class VolumnManager {
	private Toast t;
	private VolumnView vv_content;

	private Context context;

	public VolumnManager(Context context) {
		this.context = context;
	}

	public void show(float progress) {
		if (t == null) {
			t = new Toast(context);
			View layout = LayoutInflater.from(context).inflate(R.layout.volumn_controller, null);
			vv_content = (VolumnView) layout.findViewById(R.id.vv_content);
			t.setView(layout);
			t.setGravity(Gravity.BOTTOM, 0, 100);
			t.setDuration(Toast.LENGTH_SHORT);
		}
		vv_content.setProgress(progress);
		t.show();
	}
}
