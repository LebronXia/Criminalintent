package com.Xiamu.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;
//�޸�crimes��¼������ݸ��¶���ҪcrimeLab�ദ��
public class CrimeLab {
	
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	
	private ArrayList<Crime> mCrimes;
	private CriminalIntentJSONSerializer mSerializer;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	//ʹ��Context�������������������activity����ȡ��Ŀ��Դ������Ӧ�õ�˽�д洢�ռ������
	public CrimeLab(Context appContext) {
		super();
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
		mSerializer  = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
		
		try {
			mCrimes = mSerializer.loadCrimes();
		} catch (Exception e) {
			// TODO: handle exception
			mCrimes = new ArrayList<Crime>();
			Log.e(TAG, "Error loading crimes:", e);
		}
		
//		//�������б�����������100��Crime����
//		for(int i = 0; i < 100; i++){
//			Crime c = new Crime();
//			c.setTitle("Crime #" + i);
//			c.setSolved(i % 2 == 0);
//			mCrimes.add(c);
//		}
	}
	
	public static CrimeLab get(Context c){
		if(sCrimeLab == null){
			//application context�����Ӧ�õ�ȫ����context��
			//ֻҪ��Ӧ�ò���ĵ�������Ӧ����application context
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	//���������б�
	public ArrayList<Crime> getCrimes(){
		return mCrimes;
				
	}
	//���ش���ָ��ID��Crime���� 
	public Crime getCrime(UUID id){
		for(Crime c : mCrimes){
			if(c.getId().equals(id))
			return c;
		}
		return null;
	}
	public void addCrime(Crime c){
		mCrimes.add(c);
	}
	public void deleteCrime(Crime c){
		mCrimes.remove(c);
	}
	public boolean saveCrimes(){
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "crimes save to file");
			return true;
		} catch ( Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Error saving crimes" , e);
			return false;
		}
	}
	
	
}
