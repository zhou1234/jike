package com.jike.shanglv.Common;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * ������������
 * 
 * @author Administrator
 * 
 */
public class CommonFunc {

	/**
	 * �ж��ֻ���ʽ�Ƿ���ȷ
	 * 
	 * @param String
	 *            mobiles
	 * @return boolean
	 */
	public static boolean isMobileNO(String mobiles) {
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher m = p.matcher(mobiles);
//		return m.matches();
		 Pattern p = null;  
	        Matcher m = null;  
	        boolean b = false;   
	        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // ��֤�ֻ���  
	        m = p.matcher(mobiles);  
	        b = m.matches();   
	        return b;  
	}
	
	 /** 
     * �绰������֤ 
     *  
     * @param  str 
     * @return ��֤ͨ������true 
     */  
    public static boolean isPhone(String str) {   
        Pattern p1 = null,p2 = null;  
        Matcher m = null;  
        boolean b = false;    
        p1 = Pattern.compile("^[0][1-9]{2,3}[0-9]{5,10}$");  // ��֤�����ŵ�  
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // ��֤û�����ŵ�  
        if(str.length() >9)  
        {   m = p1.matcher(str);  
            b = m.matches();    
        }else{  
            m = p2.matcher(str);  
            b = m.matches();   
        }    
        return b;  
    }  

	/**
	 * �ж�email��ʽ�Ƿ���ȷ
	 * 
	 * @param String
	 *            email
	 * @return boolean
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * �ж��û�����ʽ�Ƿ���ȷ��6-12λ�����ֻ���ĸ��_��ɣ�
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean isValidUserName(String username) {
		String str = "^[0-9a-zA-Z_]{6,12}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(username);
		return m.matches();
	}

	/**
	 * �ж������ʽ�Ƿ���ȷ��6-20λ�����ֻ���ĸ��ɣ�
	 * 
	 * @param String
	 *            password
	 * @return boolean
	 */
	public static boolean isValidPassword(String password) {
		String str = "^[0-9a-zA-Z]{6,20}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(password);
		return m.matches();
	}
	/**
	 * �������󵥣�����������Ӣ�Ŀ�ͷ��ֻ����Ӣ���ַ���б��,
	 * @param input
	 * @return
	 */
	public static Boolean isEnglishName(String input){
		if (!input.contains("/")) {
			return false;
		}
		String str = "^[a-zA-Z][a-z A-Z/]*$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(input);
		return m.matches();
	}
	
	/**������ʽ������֤
	 * */
    public static boolean isNumber(String str)
    {
        java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("[0-9.]*");
        java.util.regex.Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
             return false;
        }
        else
        {
             return true;
        }
    }

	/**
	 * ��ȡMD5����
	 */
	public static String MD5(String password) {
		MessageDigest md;
		try {
			// ����һ��MD5���ܼ���ժҪ
			md = MessageDigest.getInstance("MD5");
			// ����md5����
			md.update(password.getBytes());
			// digest()���ȷ������md5 hashֵ������ֵΪ8Ϊ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�
			// BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ
			String pwd = new BigInteger(1, md.digest()).toString(16);
			if (pwd.length()<32) {
				return getMD5Str(password);
			}
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}
	
	/** 
     * MD5 ���� 
     */  
    private static String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
  
            messageDigest.reset();  
  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
            System.out.println("NoSuchAlgorithmException caught!");  
            System.exit(-1);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    }  

	public static String getPhoneNumber(Context context) {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String numberString="";
		try {//û�е绰�����ֻ�  �����쳣   ����""
			numberString =mTelephonyMgr.getLine1Number().replace("+86", "");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return numberString;
	}

	/**
	 * ȥ����ͬԪ�صķ���
	 * 
	 * @param al
	 * @return
	 */
	public static ArrayList<Object> singleElement(ArrayList<Object> al) {
		ArrayList<Object> arrayList = new ArrayList<Object>();
		Iterator<Object> it = al.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			// �����������Ԫ��,����ӽ���,contains() �����ײ���õ��� Person �� equals() ����
			if (!arrayList.contains(obj))
				arrayList.add(obj);
		}
		// �����µ�û���ظ�Ԫ�ص�ArrayList���϶���
		return arrayList;
	}

	/**
	 * ��֤�����ַ����Ƿ���YYYY-MM-DD��ʽ
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDataFormat(String str) {
		boolean flag = false;
		// String
		// regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
		String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern pattern1 = Pattern.compile(regxStr);
		Matcher isNo = pattern1.matcher(str);
		if (isNo.matches()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * ���ܣ��ж��ַ����Ƿ�Ϊ����
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

}
