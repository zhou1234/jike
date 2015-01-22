package com.jike.shanglv.Common;

import java.util.List;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
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

	public TimeTaskScroll(Context context, ListView listView,
			List<String> list, Bitmap[] bitmap) {
		this.listView = listView;
		listAdapter = new ListViewAdapter(context, list, this.listView, bitmap);
		this.listView.setAdapter(listAdapter);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			listView.smoothScrollBy(listView.getHeight() + 1, 1000);
			listView.setSelectionFromTop(cont, 0);
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
