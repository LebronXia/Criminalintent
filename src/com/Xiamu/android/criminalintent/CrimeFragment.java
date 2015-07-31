package com.Xiamu.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import com.Xiamu.android.criminalintent.R.string;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


public class CrimeFragment extends Fragment {
	
	private static final String TAG = "CrimeFragment";
	public static final String EXTRA_CRIME_ID =
			"com.xiamu.android.criminalintent.crime_id";
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	private static final String DIALOG_CHOICE = "choice";
	private static final String DIALOG_IMAGE = "image";
	
	//请求代码常量
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	private static final int REQUEST_CHOICE = 2;
	private static final int REQUEST_PHOTO = 3;
	private static final int REQUEST_CONTACT = 4;
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	private Button mSuspectButton;
	private Button mCallButton;
	private Callbacks mCallbacks;
	
	public interface Callbacks{
		void onCrimeUpdated(Crime crime);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mCallbacks = (Callbacks) activity;
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mCallbacks = null;
	}
	//显示按钮上的信息，把它封装到一个公有的updateDate方法中
//	public void uptimeTime(){
//		mTimeButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate()))+ " " + DateFormat.getTimeFormat(getActivity()).format(date);
//	}
	public void updateDate(){
		mDateButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate()) + " " + DateFormat.getTimeFormat(getActivity()).format(mCrime.getDate()));
		//mTimeButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate()));
		//mTimeButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate()) + " " + DateFormat.getTimeFormat(getActivity()).format(mCrime.getDate()));
		//mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy",mCrime.getDate()));
	}
	
	//返回并拼接完整的陋习报告文本信息
	private String getCrimeReport(){
		String solvedString = null;
		if(mCrime.isSolved()){
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_unsolved);
		}
			
			String dateFormat = "EE, MMM dd";
			String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
		
			String suspect = mCrime.getSuspect();
			if(suspect == null){
				suspect = getString(R.string.crime_report_no_suspect);
			} else {
				suspect = getString(R.string.crime_report_suspect,suspect);
			}
			
			String report = getString(R.string.crime_report,
					mCrime.getTitle(), dateString, solvedString, suspect);
			
			return report;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCrime = new Crime();
		//响应onOptionsItemSelected()的回调请求
		setHasOptionsMenu(true);
	}
	//响应应用图标菜单项
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			//提取元素据里的名字，查看它是否指定了父activity
			if(NavUtils.getParentActivityName(getActivity()) != null){
				//导航至父activity界面，所对应的.CrimeListActivity界面
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}		
	}
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//判断结果代码行动
		if(resultCode != Activity.RESULT_OK) return;
		//接收ChoiceFragment的请求代码
		if(requestCode == REQUEST_CHOICE){
			//从Extra取出choice的选择
			int choice = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOICE,0);
			
			if(choice == 0) return;
			FragmentManager fm = getActivity().getSupportFragmentManager();
			//choice为选择日期,显示DatePickerFragment
			if(choice == ChoiceDialogFragment.CHOICE_DATE){
				DatePickerFragment dialog  = DatePickerFragment.newInstance(mCrime.getDate());
				
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			//choice为选择时间,显示TimePickerFragment
			}else if(choice == ChoiceDialogFragment.CHOICE_TIME){
				TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
				
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
				dialog.show(fm,DIALOG_TIME);
			}
		}
		//接收DatePickerFragment的请求代码
		if(requestCode == REQUEST_DATE){
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			mCallbacks.onCrimeUpdated(mCrime);
			updateDate();
		}
		//接收TimePickerFragment的请求代码
		if(requestCode == REQUEST_TIME){
			Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(time);
			updateDate();
		}else if(requestCode == REQUEST_PHOTO){
			
			if(mCrime.getPhoto() != null){
				mCrime.daletePhoto(getActivity().getFileStreamPath(
						mCrime.getPhoto().getFilename()).getAbsolutePath());
			}
			//创建一个新的photo实例对象，添加到crime中
			String filename = data
					.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			int orientation = data
					.getIntExtra(CrimeCameraFragment.EXTRA_PHOTO_ORIENTATION, 0);
			if(filename != null){
				//Log.i(TAG, "filename:" + filename);
				Photo p = new Photo(filename,orientation);
				mCrime.setPhoto(p);
				mCallbacks.onCrimeUpdated(mCrime);
				//从CrimeCameraActivty返回后，也可以显示所拍的图片
				showPhoto();
				//Log.i(TAG, "Crime:" + mCrime.getTitle() + "has a photo");
			}			
		} else if(requestCode == REQUEST_CONTACT){
			Uri contactUri = data.getData();
			//制定你要找到返回的数据
			String[] queryFields = new String[]{
					ContactsContract.Contacts.DISPLAY_NAME
			};
			//执行你的查询
			//列出条款
			Cursor c = getActivity().getContentResolver()
					.query(contactUri, queryFields, null, null, null);
			//检查你是否得到了一个结果
			if(c.getCount() == 0){
				c.close();
				return;
			}
			//取出来的第一个值就是你嫌疑人的名字
			c.moveToFirst();
			String suspect = c.getString(0);
			mCrime.setSuspect(suspect);
			mCallbacks.onCrimeUpdated(mCrime);
			mSuspectButton.setText(suspect);
			c.close();
			
		}
		
	}


	//生成fragment视图的布局，然后将生成的View返回给托管activity
	//也是生成EditText组件并响应用户输入的地方
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		
		UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
		//第二个参数是父视图，第三个参数是告知布局生成器是否将生成的视图添加给父视图
		View v = inflater.inflate(R.layout.fragment_crime, container, false);
		//判断android设备的版本号大于
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(NavUtils.getParentActivityName(getActivity()) != null){
				//actionBar替代显示屏幕顶部的标题栏,显示向左的图标
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);				
			}
		}
		
		//对ExditText组件添加监听器方法
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		//更新text视图
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				mCrime.setTitle(s.toString());//文字改变时，更新模型层数据	
				mCallbacks.onCrimeUpdated(mCrime);
				getActivity().setTitle(mCrime.getTitle());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		mDateButton = (Button) v.findViewById(R.id.crime_date);
		//mTimeButton = (Button) v.findViewById(R.id.crime_time);
		updateDate();
//		mDateButton.setText(DateFormat.getMediumDateFormat(getActivity())
//				.format(mCrime.getDate()));
		//mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy",mCrime.getDate()));
		
//		//定义用户点击时间按钮的监听器
//		mTimeButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				FragmentManager fm = getActivity()
//						.getSupportFragmentManager();
//				TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
//				//设置接收目标关联，建立联系，目标fragment使用该请求代码通知是哪一个fragment在返回获取他们
//				dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
//				dialog.show(fm, DIALOG_TIME);
//			}
//		});
		
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Dialogment实例也是由托管activity的fragmentManager管理的
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				//返回一个存储Date的Fragment
				//DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				ChoiceDialogFragment dialog = new ChoiceDialogFragment();
				//设置目标Fragment，建立关联，目标Fragment可使用该请求代码通知是哪一个fragment在返回数据信息
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_CHOICE);
				//DIALOG_DATE用来识别放在队列里的Dialogment
				dialog.show(fm,DIALOG_CHOICE);
			}
		});
		
		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		//更新checkbox视图
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mCrime.setSolved(isChecked);
				mCallbacks.onCrimeUpdated(mCrime);
			}
		});
		mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});
		
		mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Photo p = mCrime.getPhoto();
				if(p == null)
					return;
				
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				String path = getActivity()
						.getFileStreamPath(p.getFilename()).getAbsolutePath();
				ImageFragment.newInstance(path,
						mCrime.getPhoto().getOrientation())
							.show(fm, DIALOG_IMAGE);
				
			}
		});
		registerForContextMenu(mPhotoView);
		//如果对于不带相机的设备，拍照按钮应该禁用
		PackageManager pm = getActivity().getPackageManager();
		//后置摄像机
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
				//前置摄像机
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
				//版本号小于9
				Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD||
				//
				Camera.getNumberOfCameras() > 0;
				if(!hasACamera){
					mPhotoButton.setEnabled(false);
				}
				
		Button repButton = (Button) v.findViewById(R.id.crime_reporButton);
		repButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				i.putExtra(Intent.EXTRA_SUBJECT, 
						getString(R.string.crime_report_subject));
				//每次都显示activity选择器
				i.createChooser(i, getString(R.string.send_report));
				startActivity(i);
			}
		});
		
		//从联系人应用中选择嫌疑人
		mSuspectButton = (Button) v.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//从联系人数据库中获取某个联系人
				Intent i = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI); 
				startActivityForResult(i, REQUEST_CONTACT);
			}
		});
		mCallButton = (Button) v.findViewById(R.id.crime_callButton);
		mCallButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Uri uri = Uri.parse("tel:5551234");
				Intent it = new Intent(Intent.ACTION_DIAL,uri);
				startActivity(it);
			}
		});
		
		if(mCrime.getSuspect() != null)
			mSuspectButton.setText(mCrime.getSuspect());
		
		
					
		return v;
		
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(mCrime.getPhoto() != null
			&& mCrime.daletePhoto(getActivity().getFileStreamPath(
					mCrime.getPhoto().getFilename()).getAbsolutePath())){
			showPhoto();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	//将缩放后的图片设置给imageView视图
	private void showPhoto(){
		Photo p = mCrime.getPhoto();
		BitmapDrawable b =null;
		if(p != null){
			String path = getActivity()
					.getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaleDrawable(getActivity(), path);
			if(p.getOrientation() ==
					Configuration.ORIENTATION_PORTRAIT){
				Log.i("CrimeFragment", "触发这个额————————————");
				b = PictureUtils.getPortraitDrawable(mPhotoView, b);
			}
		}
		mPhotoView.setImageDrawable(b);
	}
	
	
	public static CrimeFragment newInstance(UUID crimeId){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		
		CrimeFragment fragment = new CrimeFragment();
		//附加argument bunndle 给fragment
		fragment.setArguments(args);
		
		return fragment;
	}
	
	//在Onpause中保存应用数据
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		showPhoto();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		PictureUtils.cleanImageview(mPhotoView);
	}
	
	

}
