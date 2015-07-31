package com.Xiamu.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;
//修改crimes记录后的数据更新都需要crimeLab类处理
public class CrimeLab {
	
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	
	private ArrayList<Crime> mCrimes;
	private CriminalIntentJSONSerializer mSerializer;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	//使用Context参数，单例课完成启动activity、获取项目资源、查找应用的私有存储空间等任务
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
		
//		//往数组列表中批量存入100个Crime对象
//		for(int i = 0; i < 100; i++){
//			Crime c = new Crime();
//			c.setTitle("Crime #" + i);
//			c.setSolved(i % 2 == 0);
//			mCrimes.add(c);
//		}
	}
	
	public static CrimeLab get(Context c){
		if(sCrimeLab == null){
			//application context是针对应用的全局性context。
			//只要是应用层面的单例，就应该用application context
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	//返回数组列表
	public ArrayList<Crime> getCrimes(){
		return mCrimes;
				
	}
	//返回带有指定ID的Crime对象 
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
