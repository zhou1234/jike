package com.jike.shanglv.Common;

import java.util.List;

import com.jike.shanglv.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ListViewAdapter extends BaseAdapter {

	private List<String> list;
	private Context context;

	private Bitmap[] bitmap;

	public ListViewAdapter(Context context, List<String> list,
			ListView listView, Bitmap[] bitmap) {
		this.context = context;
		this.list = list;
		this.bitmap = bitmap;
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
		View view = convertView;
		Holder holder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.view, arg2,
					false);
			holder = new Holder(view);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		ImageView imageView = holder.getImageView();

		imageView.setImageBitmap(bitmap[postition % list.size()]);

		return view;
	}

	class Holder {
		View view;
		ImageView imageView;

		public Holder(View view) {
			this.view = view;
		}

		public ImageView getImageView() {
			if (imageView == null) {

				BitmapUtil.releaseImageViewResouce(imageView);
				imageView = (ImageView) view.findViewById(R.id.imageView1);
			}
			return imageView;
		}

	}

}
