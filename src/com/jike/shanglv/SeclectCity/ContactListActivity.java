package com.jike.shanglv.SeclectCity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
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
import com.jike.shanglv.SeclectCity.SideBarContact.OnTouchingLetterChangedListener;
import com.jike.shanglv.R;

public class ContactListActivity extends Activity {
	
	public static final int SELECTED_CONTACT_FINISH = 5;
	
	private ImageButton back_imgbtn, home_imgbtn;
	private ListView sortListView;
	private SideBarContact sideBar;
	private TextView dialog;
	private ContactListSortAdapter adapter;
	private ClearEditText mClearEditText;
	private Context context;
	
	public String name_select;
	public String number_select;
	public ArrayList<ContactListModel> Contact_List_Display = new ArrayList<ContactListModel>();
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Phone.SORT_KEY_PRIMARY };
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	private static final int PHONES_NUMBER_INDEX = 1;
	private static final int PHONES_PINYIN_INDEX = 2;
	
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<ContactListModel> SourceDateList;
	
	/**
	 * ����ƴ��������ListView�����������
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_select_activity);
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
		sideBar = (SideBarContact) findViewById(R.id.sidrbar);
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
				
				String numberString=((ContactListModel)adapter.getItem(position)).getnumber();
				setResult(0,getIntent().putExtra("pickedPhoneNum",numberString));
				finish();
			}
		});
		
		getPhoneContacts();
		SourceDateList = Contact_List_Display;
				//filledData(getResources().getStringArray(R.array.date));
		
		// ����a-z��������Դ����
		//Collections.sort(SourceDateList, pinyinComparator);
		sortCities();
		adapter = new ContactListSortAdapter(context, SourceDateList);
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
	 * ���б��������
	 */
	private void sortCities() {
		Comparator<ContactListModel> comparator = new Comparator<ContactListModel>() {
			@Override
			public int compare(ContactListModel s1, ContactListModel s2) {
				return s1.pinyin.compareTo(s2.pinyin);
			}
		};
		Collections.sort(SourceDateList, comparator);
	}
	
	private void getPhoneContacts() {
		ContentResolver resolver = context.getContentResolver();
		// ��ȡ�ֻ���ϵ��
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, "sort_key asc");

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				// �õ��ֻ�����
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// �õ���ϵ������
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);
				String contactPinyin = phoneCursor
						.getString(PHONES_PINYIN_INDEX);
				ContactListModel c = new ContactListModel(contactName, contactPinyin,
						phoneNumber);
				c.setShortchar(contactPinyin.substring(0, 1).toUpperCase());
				Contact_List_Display.add(c);
			}
			phoneCursor.close();
		}
	}
	
	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<ContactListModel> filterDateList = new ArrayList<ContactListModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(ContactListModel Contact_List : SourceDateList){
				String name = Contact_List.getname();
				String num=Contact_List.getnumber();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(Contact_List);
				}
				if(num.indexOf(filterStr.toString()) != -1 ||num.startsWith(filterStr.toString())){
					filterDateList.add(Contact_List);
				}
			}
		}
		filterDateList=DateUtil.removeDuplicateWithOrder(filterDateList);
		// ����a-z��������
		sortCities();
		adapter.updateListView(filterDateList);
	}

}
