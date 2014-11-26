package com.jike.shanglv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.jike.shanglv.Models.HotelDetail;
import com.jike.shanglv.NetAndJson.JSONHelper;


public class ActivityHotelLocation extends Activity {

	public static final String TITLE = "activity_title";
	MapView mMapView = null;
	BaiduMap mBaidumap = null;
	double hotelY, hotelX, myX, myY;
	LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_hotel_location);
			((MyApplication) getApplication()).addActivity(this);

			mMapView = (MapView) findViewById(R.id.bmapView);
			mBaidumap = mMapView.getMap();

			((ImageButton) findViewById(R.id.back_imgbtn))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							finish();
						}
					});

			mLocationClient = new LocationClient(this.getApplicationContext());
			mLocationClient.registerLocationListener(new MyLocationListener());
			InitLocation();
			mLocationClient.start();
			mLocationClient.requestLocation();

			HotelDetail maphotel = null;
			Marker marker = null;
			BitmapDescriptor mIconMaker;
			OverlayOptions overlayOptions = null;
			Bundle bundle = this.getIntent().getExtras();
			if (bundle != null)
				if (bundle.containsKey("hotel"))
					try {
						maphotel = JSONHelper.parseObject(
								bundle.getString("hotel"), HotelDetail.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
			LatLng latLng = new LatLng(Double.parseDouble(maphotel.getY()),
					Double.parseDouble(maphotel.getX()));
			hotelY = Double.parseDouble(maphotel.getY());
			hotelX = Double.parseDouble(maphotel.getX());
			View view = getMapView(maphotel.getHotelname(),
					maphotel.getMin_price());
			mIconMaker = BitmapDescriptorFactory.fromView(view);
			// ͼ��
			overlayOptions = new MarkerOptions().position(latLng)
					.icon(mIconMaker).zIndex(5);
			marker = (Marker) (mBaidumap.addOverlay(overlayOptions));

			// �趨���ĵ�����
			MapStatus mMapStatus = new MapStatus.Builder().target(latLng)
					.zoom(16).build();
			// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mMapStatus);
			mBaidumap.setMapStatus(mMapStatusUpdate);

			mBaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(final Marker marker) {
					// startNavi();
					return true;
				}
			});

			((ImageButton) findViewById(R.id.navi_imgbtn))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							startNavi();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public View getMapView(String hotelName, String hotelPrice) {
		LayoutInflater inflater = LayoutInflater
				.from(ActivityHotelLocation.this);
		View convertView = inflater.inflate(R.layout.hotel_map_icon, null);
		TextView hotel_name_tv = (TextView) convertView
				.findViewById(R.id.hotel_name_tv);
		TextView hotelPrice_tv = (TextView) convertView
				.findViewById(R.id.price_tv);
		hotel_name_tv.setText(hotelName);
		hotelPrice_tv.setText("��" + hotelPrice);
		return convertView;
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		// option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ:�߾���
		option.setCoorType("gcj02");// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		int span = 1000;
		option.setScanSpan(span);// ���÷���λ����ļ��ʱ��Ϊ1000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {// Receive Location
			myX = location.getLatitude();
			myY = location.getLongitude();
		}
	}

	/**
	 * ��ʼ����
	 * 
	 * @param view
	 */
	public void startNavi() {
		LatLng pt1 = new LatLng(myX, myY);// (hotelY+0.01,hotelX+0.01);
		Log.e("myY-myX", myY + "---" + myX);
		LatLng pt2 = new LatLng(hotelY, hotelX);
		// ���� ��������
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName = "�����￪ʼ";
		para.endPoint = pt2;
		para.endName = "���������";

		try {
			BaiduMapNavigation.openBaiduMapNavi(para, this);
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("����δ��װ�ٶȵ�ͼapp��app�汾���ͣ����ȷ�ϰ�װ��");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							BaiduMapNavigation
									.getLatestBaiduMapApp(ActivityHotelLocation.this);
						}
					});
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}
}
