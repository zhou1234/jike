package com.jike.shanglv;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jike.shanglv.Common.BitmapUtil;
import com.jike.shanglv.Enums.SPkeys;
import com.umeng.analytics.MobclickAgent;

public class GuideActivity extends Activity implements OnPageChangeListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;

	// 底部小点图片
	private ImageView[] dots;
	private Context mContext;

	// 记录当前选中位置
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityguide);
		mContext = this;
		// 初始化页面
		initViews();
		((MyApplication) getApplication()).addActivity(this);
		// 初始化底部小点
		initDots();
	}

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();

		View view1 = inflater.inflate(R.layout.what_new_one, null);
		ImageView imageView1 = (ImageView) view1.findViewById(R.id.img1);
		BitmapUtil.releaseImageViewResouce(imageView1);
		imageView1.setImageBitmap(BitmapUtil.readBitMap(mContext,
				R.drawable.guide_01));
		
		View view2 = inflater.inflate(R.layout.what_new_two, null);
		ImageView imageView2 = (ImageView) view2.findViewById(R.id.img2);
		BitmapUtil.releaseImageViewResouce(imageView2);
		imageView2.setImageBitmap(BitmapUtil.readBitMap(mContext,
				R.drawable.guide_02));
		
		View view3 = inflater.inflate(R.layout.what_new_three, null);
		ImageView imageView3 = (ImageView) view3.findViewById(R.id.img3);
		BitmapUtil.releaseImageViewResouce(imageView3);
		imageView3.setImageBitmap(BitmapUtil.readBitMap(mContext,
				R.drawable.guide_03));
		
		View view4 = inflater.inflate(R.layout.what_new_four, null);
		ImageView imageView4 = (ImageView) view4.findViewById(R.id.img4);
		BitmapUtil.releaseImageViewResouce(imageView4);
		imageView4.setImageBitmap(BitmapUtil.readBitMap(mContext,
				R.drawable.guide_04));
		
		// 初始化引导图片列表
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views, this);

		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		setCurrentDot(arg0);
	}

	public class ViewPagerAdapter extends PagerAdapter {
		// 界面列表
		private List<View> views;
		private Activity activity;

		private static final String SHAREDPREFERENCES_NAME = "first_pref";

		public ViewPagerAdapter(List<View> views, Activity activity) {
			this.views = views;
			this.activity = activity;
		}

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		// 获得当前界面数
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		// 初始化arg1位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			if (arg1 == views.size() - 1) {
				ImageView mStartWeiboImageButton = (ImageView) arg0
						.findViewById(R.id.iv_start);
				mStartWeiboImageButton
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 设置已经引导
								setGuided();
								goHome();
							}
						});
			}
			return views.get(arg1);
		}

		// private void goHome() {
		// // 跳转
		// Intent intent = new Intent(activity, MainActivity.class);
		// activity.startActivity(intent);
		// activity.finish();
		// }
		private void goHome() {
			Intent intent = null;
			// if(sp.getString(SPkeys.utype.getString(), "0").equals(
			// "2")){
			// intent = new Intent(GuideActivity.this, MainActivity.class);
			// }else if(sp.getString(SPkeys.utype.getString(), "0").equals(
			// "1")){
			// intent = new Intent(GuideActivity.this, ActivityBMenu.class);
			// }
			intent = new Intent(GuideActivity.this, MainActivityN.class);
			GuideActivity.this.startActivity(intent);
			GuideActivity.this.finish();
		}

		/**
		 * 
		 * method desc：设置已经引导过了，下次启动不用再次引导
		 */
		private void setGuided() {
			SharedPreferences preferences = getSharedPreferences(
					SPkeys.SPNAME.getString(), MODE_PRIVATE);
			try {
				preferences
						.edit()
						.putBoolean(
								SPkeys.isFirstIn.getString()
										+ mContext
												.getPackageManager()
												.getPackageInfo(
														mContext.getPackageName(),
														0).versionCode, true)
						.commit();
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("GuideActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("GuideActivity");
		MobclickAgent.onPause(this);

	}
}
