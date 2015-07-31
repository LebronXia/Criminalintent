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
	//���ļ��м���crime��¼
	public ArrayList<Crime> loadCrimes() throws IOException,JSONException{
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		try {
			//�򿪺Ͷ��ļ�����һ��������
			InputStream in = mContext.openFileInput(mFilename);
			//InputStreamReader ���ֽ���ͨ���ַ���������
			//�ֽ���ת��Ϊ�ַ����ŵ�Buffered������
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				//append ����ʼ�ս���Щ�ַ���ӵ���������ĩ�ˣ��� insert ��������ָ���ĵ�����ַ���
				jsonString.append(line);
			}
			//json�ı�������JSONTokener  
			//��ʱ��δ��ȡ�κ�json�ı���ֱ�Ӷ�ȡ����һ��JSONObject����
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
					.nextValue();
			//��JSONArray����ȡ��crime����ŵ�crimes��
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
	
	//�����û���������ݵ��ļ�
	@SuppressWarnings("resource")
	public void saveCrimes(ArrayList<Crime> crimes)
				throws JSONException,IOException{
		JSONArray array = new JSONArray();
		//��ʽ�������������
		for(Crime c : crimes)
			array.put(c.toJSON());	
		
		Writer writer = null;
		try {
			OutputStream out = null;
			//��ʹ���ⲿ�洢֮ǰ�������Ҫ�ȼ���ⲿ�洢�ĵ�ǰ״̬�����ж��Ƿ���á�
			//Strig MEDIA_MOUNTED�洢ý���Ѿ����أ����ҹ��ص�ɶ�/д��
			//��õ�ǰ�ⲿ����ý���״̬��
			if(Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)){
//				File sdCard = Environment.getExternalStorageDirectory();
//				// ��ȡ�ⲿ�洢�豸��SD������·��
//				Log.i(TAG, sdCard.getAbsolutePath());
//				// �鿴LogCat,��ȡ��sd���ľ���·��Ϊ /storage/sdcard
//				sdCard = new File(sdCard, "/MyFiles");
//				sdCard.mkdirs();// ����MyFilesĿ¼(�ɴ����༶Ŀ¼)
//				sdCard = new File(sdCard, mFilename);
//				out = new FileOutputStream(sdCard);	
				out = new FileOutputStream(new File(Environment.
						//��ȡ�ⲿ�洢ý��Ŀ¼��·��
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
		
			



	
	

	//���ļ���д������
	//Writer writer = null;
	//try{
		 /* �����û��ṩ���ļ������Լ��ļ���Ӧ��ģʽ����һ�������.�ļ�����ϵͳ��Ϊ�㴴��һ���ģ� 
         * ����Ϊʲô����ط�����FileNotFoundException�׳�����Ҳ�Ƚ����ơ���Context������������� 
         *   public abstract FileOutputStream openFileOutput(String name, int mode) 
         *   throws FileNotFoundException; 
         * openFileOutput(String name, int mode); 
         * ��һ�������������ļ����ƣ�ע��������ļ����Ʋ��ܰ����κε�/����/���ַָ�����ֻ�����ļ��� 
         *          ���ļ��ᱻ������/data/data/Ӧ������/files/chenzheng_java.txt 
         * �ڶ��������������ļ��Ĳ���ģʽ 
         *          MODE_PRIVATE ˽�У�ֻ�ܴ�������Ӧ�÷��ʣ� �ظ�д��ʱ���ļ����� 
         *          MODE_APPEND  ˽��   �ظ�д��ʱ�����ļ���ĩβ����׷�ӣ������Ǹ��ǵ�ԭ�����ļ� 
         *          MODE_WORLD_READABLE ����  �ɶ� 
         *          MODE_WORLD_WRITEABLE ���� �ɶ�д 
         *  */  
		//��һ���ļ�,���OutputStream����
//		OutputStream out  = mContext.
//				openFileOutput(mFilename,Context.MODE_PRIVATE);
//		writer = new OutputStreamWriter(out);
//		//����OutputStreamWriter��д�뷽��д������
//		writer.write(array.toString());	
//	}finally {
//		if(writer != null )
//		writer.close();
//	}
  }	
	


}
