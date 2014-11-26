package com.jike.shanglv.SeclectCity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jike.shanglv.MainActivity;
import com.jike.shanglv.MyApplication;
import com.jike.shanglv.Common.DateUtil;
import com.jike.shanglv.SeclectCity.SideBar.OnTouchingLetterChangedListener;
import com.jike.shanglv.R;

public class TrainCityActivity extends Activity {
	
	private ImageButton back_imgbtn, home_imgbtn;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private TrainSortAdapter adapter;
	private ClearEditText mClearEditText;
	private Context context;
	
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<TrainCityModel> SourceDateList;
	
	/**
	 * ����ƴ��������ListView�����������
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select_activity);
		context=this;
		initViews();
		((MyApplication)getApplication()).addActivity(this);
	}

	private void initViews() {
		back_imgbtn = (ImageButton) findViewById(R.id.back_imgbtn);
		home_imgbtn = (ImageButton) findViewById(R.id.home_imgbtn);
		back_imgbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		home_imgbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, MainActivity.class));
			}
		});
		
		//ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//�����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���

				//Toast.makeText(getApplication(), ((ContactModel)adapter.getItem(position)).getCityName(), Toast.LENGTH_SHORT).show();
				
				//���س������Ƽ�������
				setResult(
						0,
						getIntent().putExtra("pickedCity",
								((TrainCityModel)adapter.getItem(position)).getName()+
								"#"+
								((TrainCityModel)adapter.getItem(position)).getCode()));
				finish();
			}
		});
		
		SourceDateList = getTrainCityModel() ;
				//filledData(getResources().getStringArray(R.array.date));
		
		// ����a-z��������Դ����
		//Collections.sort(SourceDateList, pinyinComparator);
		sortCities();
		adapter = new TrainSortAdapter(context, SourceDateList);
		sortListView.setAdapter(adapter);
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//�������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	/*
	 * �Գ����б��������
	 */
	private void sortCities() {
		Comparator<TrainCityModel> comparator = new Comparator<TrainCityModel>() {
			@Override
			public int compare(TrainCityModel s1, TrainCityModel s2) {
				if (s1.ishot.compareTo(s2.ishot)!=0) {
					return s2.ishot.compareTo(s1.ishot);
				}
			    else if (s1.pinyin.compareTo(s2.pinyin) != 0) {
					return s1.pinyin.compareTo(s2.pinyin);
				} else {
					return s1.name.compareTo(s2.name);
				}
			}
		};
		Collections.sort(SourceDateList, comparator);
	}
	
	private ArrayList<TrainCityModel> getTrainCityModel() {
		ArrayList<TrainCityModel> names = new ArrayList<TrainCityModel>();
		try {
			String jsonStr = getJson("traincity");
			JSONArray array = new JSONArray(jsonStr);
			int len = array.length();
			Map<String, String> map;
			List<Map<String, String>> data;
			for (int i = 0; i < len; i++) {
				JSONObject object = array.getJSONObject(i);
				TrainCityModel acm = new TrainCityModel();
				acm.name = object.getString("name");
				acm.suoxie = object.getString("suoxie");
				acm.pinyin = object.getString("pinyin");
//				acm.ishot = object.getString("ishot");
				acm.code=object.getString("code");
				acm.ishot="0";
				acm.abcd = acm.pinyin.substring(0, 1).toUpperCase();
				
				if (acm.ishot.equals("1")) {
					acm.abcd="����";
				}
				names.add(acm);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return names;
	}

	/**
	 * ��ȡ�����ļ���JSON�ַ���
	 * 
	 * @param fileName
	 * @return
	 */
	private String getJson(String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					getAssets().open(fileName), "GB2312"));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	/**
	 * ΪListView�������
	 * @param date
	 * @return
	 */
	private List<TrainCityModel> filledData(String [] date){
		List<TrainCityModel> mSortList = new ArrayList<TrainCityModel>();
		
		for(int i=0; i<date.length; i++){
			TrainCityModel TrainCityModel = new TrainCityModel();
			TrainCityModel.setName(date[i]);
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
//			String ishot=
			
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]")){
				TrainCityModel.setAbcd(sortString.toUpperCase());
			}else{
				TrainCityModel.setAbcd("#");
			}
			mSortList.add(TrainCityModel);
		}
		return mSortList;
	}
	
	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<TrainCityModel> filterDateList = new ArrayList<TrainCityModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(TrainCityModel TrainCityModel : SourceDateList){
				String name = TrainCityModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(TrainCityModel);
				}
				
				if(name.indexOf(filterStr.toString()) != -1 || TrainCityModel.getAbcd().startsWith(filterStr.toString())){
					filterDateList.add(TrainCityModel);
				}
				if(name.indexOf(filterStr.toString()) != -1 || TrainCityModel.getPinyin().startsWith(filterStr.toString())){
					filterDateList.add(TrainCityModel);
				}
			}
		}
		filterDateList=DateUtil.removeDuplicateWithOrder(filterDateList);
		// ����a-z��������
		sortCities();
		adapter.updateListView(filterDateList);
	}
}
