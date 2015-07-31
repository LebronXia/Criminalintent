package com.Xiamu.android.criminalintent;
import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;


public class Crime {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	private static final String JSON_PHOTO = "photo";
	private static final String JSON_SUSPECT = "suspect";
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private Photo mPhoto;
	private String mSuspect;	//嫌疑人姓名
	private boolean mSolved;
	public Crime(){
		//生成唯一标识符
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	//创建一个接受JSONObject对象的构造方法，解析
	public Crime(JSONObject json) throws JSONException{
		//fromString(String name) 方法被用作toString()方法中所述创建的字符串标准来表示一个UUID
		mId = UUID.fromString(json.getString(JSON_ID));
		if(json.has(JSON_TITLE)){
			mTitle = json.getString(JSON_TITLE);
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
		if(json.has(JSON_PHOTO))
				mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
		if(json.has(JSON_SUSPECT))
			mSuspect = json.getString(JSON_SUSPECT);
	}
	//将数据转变为JSON文件的JSONObject对象数据
	public JSONObject toJSON() throws JSONException{
		//创建JSON对象
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		if(mPhoto != null)
			json.put(JSON_PHOTO, mPhoto.toJSON());
		json.put(JSON_SUSPECT, mSuspect);
		return json;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		mTitle = title;
	}
	public UUID getId() {
		return mId;
	}
	public boolean isSolved() {
		return mSolved;
	}
	public void setSolved(boolean solved) {
		mSolved = solved;
	}
	public Date getDate() {
		
		return mDate;
	}
	public void setDate(Date date) {
		mDate = date;
	}
	@Override
	public String toString() {
		return mTitle;
	}

	public Photo getPhoto() {
		return mPhoto;
	}

	public void setPhoto(Photo P) {
		this.mPhoto = P;
	}

	public String getSuspect() {
		return mSuspect;
	}

	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}
	
	public boolean daletePhoto(String filename){
		File file = new File(filename);
		setPhoto(null);
		return file.delete();
	}
	
	
}
