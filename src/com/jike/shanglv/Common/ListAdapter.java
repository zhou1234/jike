package com.jike.shanglv.Common;

import java.util.List;

import com.jike.shanglv.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	private List<String> list;
	private LayoutInflater mInflater;
	private Context context;
	private int cont = 0;
	private int[] image = { R.drawable.ad_four, R.drawable.banner,
			R.drawable.banner1 };

	public ListAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int arg0) {

		return list.get(arg0 % list.size());
	}

	@Override
	public long getItemId(int arg0) {
		return arg0 % list.size();
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int postition, View convertView, ViewGroup arg2) {
		View view = LayoutInflater.from(context).inflate(R.layout.view, arg2,
				false);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		if (cont == 3) {
			cont = 0;
		}
		imageView.setImageResource(image[cont]);
		cont++;
		return view;
	}
}
