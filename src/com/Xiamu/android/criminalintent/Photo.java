package com.Xiamu.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
	private static final String JSON_FILENAME = "filename";
	private static final String JSON_ORIENTATION = "orientation";
	
	private String mFilename;
	private int mOrientation;
	
	public Photo(String filename){
		mFilename = filename;
	}
	public Photo(String filename, int orientation) {
		super();
		mFilename = filename;
		mOrientation = orientation;
	}
	//代表现有文件在磁盘上创建一个照片
	//在保存以及加载photo类型的数据时，会使用它
	public Photo(JSONObject json) throws JSONException{
		mFilename = json.getString(JSON_FILENAME);
		mOrientation =json.getInt(JSON_ORIENTATION);
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_FILENAME, mFilename);
		json.put(JSON_ORIENTATION, mOrientation);
		return json;		
	}
	public int getOrientation() {
		return mOrientation;
	}
	public void setOrientation(int orientation) {
		mOrientation = orientation;
	}
	public String getFilename(){
		return mFilename;
	}
}
