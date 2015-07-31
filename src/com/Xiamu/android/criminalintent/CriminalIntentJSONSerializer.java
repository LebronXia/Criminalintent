package com.Xiamu.android.criminalintent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class CriminalIntentJSONSerializer {
	private Context mContext;
	private String mFilename;
	private static final String TAG = "CriminalIntentJSONSerializer";
	
	@SuppressWarnings("resource")
	public ArrayList<Crime> loadCrimes() throws IOException,JSONException{
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		try {
			InputStream in =null;
			if(Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)){
				in = new FileInputStream(new File(Environment.
						getExternalStorageDirectory().getAbsolutePath(),
						mFilename));
			}else{
				in = mContext.openFileInput(mFilename);
			}
			
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				jsonString.append(line);
			}
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
			.nextValue();
			for(int i = 0; i < array.length(); i++){
				crimes.add(new Crime(array.getJSONObject(i)));
			}
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}finally{
			if(reader != null)
			reader.close();
		}
		
		return crimes;
	}
	/*
	//从文件中加载crime记录
	public ArrayList<Crime> loadCrimes() throws IOException,JSONException{
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		try {
			//打开和读文件到到一个缓冲区
			InputStream in = mContext.openFileInput(mFilename);
			//InputStreamReader 是字节流通向字符流的桥梁
			//字节流转换为字符流放到Buffered缓冲区
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				//append 方法始终将这些字符添加到缓冲区的末端；而 insert 方法则在指定的点添加字符。
				jsonString.append(line);
			}
			//json文本解析类JSONTokener  
			//此时还未读取任何json文本，直接读取就是一个JSONObject对象。
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
					.nextValue();
			//从JSONArray数组取出crime对象放到crimes中
			for(int i = 0; i < array.length(); i++){
				crimes.add(new Crime(array.getJSONObject(i)));
			}
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}finally{
			if(reader != null)
				reader.close();
		}
		
		return crimes;
		
	}
	*/
	public CriminalIntentJSONSerializer(Context c, String f) {
		super();
		// TODO Auto-generated constructor stub
		mContext = c;
		mFilename =f;
	}
	
	//保存用户输入的内容到文件
	@SuppressWarnings("resource")
	public void saveCrimes(ArrayList<Crime> crimes)
				throws JSONException,IOException{
		JSONArray array = new JSONArray();
		//格式化对象放入数组
		for(Crime c : crimes)
			array.put(c.toJSON());	
		
		Writer writer = null;
		try {
			OutputStream out = null;
			//在使用外部存储之前，你必须要先检查外部存储的当前状态，以判断是否可用。
			//Strig MEDIA_MOUNTED存储媒体已经挂载，并且挂载点可读/写。
			//获得当前外部储存媒体的状态。
			if(Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)){
//				File sdCard = Environment.getExternalStorageDirectory();
//				// 获取外部存储设备（SD卡）的路径
//				Log.i(TAG, sdCard.getAbsolutePath());
//				// 查看LogCat,获取的sd卡的绝对路径为 /storage/sdcard
//				sdCard = new File(sdCard, "/MyFiles");
//				sdCard.mkdirs();// 创建MyFiles目录(可创建多级目录)
//				sdCard = new File(sdCard, mFilename);
//				out = new FileOutputStream(sdCard);	
				out = new FileOutputStream(new File(Environment.
						//获取外部存储媒体目录，路径
						getExternalStorageDirectory().getAbsolutePath(),mFilename));
				Log.i(TAG, Environment.getExternalStorageDirectory()
						.getAbsolutePath());
			}else{
				out = mContext.openFileOutput(mFilename,Context.MODE_PRIVATE);
			}
				writer = new OutputStreamWriter(out);

				writer.write(array.toString());

		}finally {
				// TODO: handle exception
				if(writer != null)
					writer.close();
			}
		
			



	
	

	//打开文件并写入数据
	//Writer writer = null;
	//try{
		 /* 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的， 
         * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的 
         *   public abstract FileOutputStream openFileOutput(String name, int mode) 
         *   throws FileNotFoundException; 
         * openFileOutput(String name, int mode); 
         * 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名 
         *          该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt 
         * 第二个参数，代表文件的操作模式 
         *          MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖 
         *          MODE_APPEND  私有   重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件 
         *          MODE_WORLD_READABLE 公用  可读 
         *          MODE_WORLD_WRITEABLE 公用 可读写 
         *  */  
		//打开一个文件,获得OutputStream对象
//		OutputStream out  = mContext.
//				openFileOutput(mFilename,Context.MODE_PRIVATE);
//		writer = new OutputStreamWriter(out);
//		//调用OutputStreamWriter的写入方法写入数据
//		writer.write(array.toString());	
//	}finally {
//		if(writer != null )
//		writer.close();
//	}
  }	
	


}
