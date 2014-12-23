package com.jike.shanglv.Common;

import java.util.List;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TimeTaskScroll extends TimerTask {

	private ListView listView;
	private int cont = 0;
	private ListViewAdapter listAdapter;

	public TimeTaskScroll(Context context, ListView listView, List<String> list) {
		this.listView = listView;
		listAdapter = new ListViewAdapter(context, list,this.listView);   
		this.listView.setAdapter(listAdapter);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			View listItem = listAdapter.getView(cont, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			int list_child_item_height = listItem.getMeasuredHeight();
			listView.smoothScrollBy(list_child_item_height+1, 1000);
			// listView.smoothScrollToPosition(cont);
			cont++;
			if (cont == 3) {
				cont = 0;
			}
		};
	};

	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		handler.sendMessage(msg);
	}

}
