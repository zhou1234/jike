package com.jike.shanglv.Common;

import java.io.InputStream;
import java.util.List;

import com.jike.shanglv.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ListViewAdapter extends BaseAdapter {

	private List<String> list;
	private Context context;
	private int[] image = { R.drawable.ad_four, R.drawable.banner,
			R.drawable.banner1 };

	public ListViewAdapter(Context context, List<String> list, ListView listView) {
		this.context = context;
		this.list = list;
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
		InputStream is = context.getResources().openRawResource(
				image[postition % list.size()]);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		//options.inSampleSize = 10; // width，hight设为原来的十分一
		Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
		
		imageView.setImageBitmap(bmp);
		
//		if (!bmp.isRecycled()) {
//			bmp.recycle(); // 回收图片所占的内存
//			System.gc(); // 提醒系统及时回收
//		}
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
				imageView = (ImageView) view.findViewById(R.id.imageView1);
			}
			return imageView;
		}

	}
}
