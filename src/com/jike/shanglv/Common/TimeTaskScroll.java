package com.jike.shanglv.Common;

import java.util.List;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.ListView;

public class TimeTaskScroll extends TimerTask {
	
	private ListView listView;
	
	public TimeTaskScroll(Context context, ListView listView, List<String> list){
		this.listView = listView;
		listView.setAdapter(new ListAdapter(context, list));
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			listView.smoothScrollBy(listView.getHeight()+1, 1000);
		};
	};

	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		handler.sendMessage(msg);
	}

}
