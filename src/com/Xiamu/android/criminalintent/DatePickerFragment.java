package com.Xiamu.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {

	public static final String EXTRA_DATE =
			"com.xiamu.android.criminalintent.date";
	private Date mDate;
	//利用单例模式将记录日期保存在DatePickerFragment的argument bundle中，  方便DatePickerFragment可以直接获取
	public static DatePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}
	private void sendResult(int resultCode){
		//获取目标fragment
		if(getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		//将日期数据作为extra附加到intent上
		i.putExtra(EXTRA_DATE, mDate);
		//调用CrimeFragment.onActivityResult()方法，实现数据回传
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
		
		//创建一个Calendar对象,然后用Date对象来来进行配置年、月、日
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		//创建日期视图
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		
		//将Date的值显示在calendar上
		DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
		//初始化DatePicker对象
		datePicker.init(year, month, day, new OnDateChangedListener() {
			
			public void onDateChanged(DatePicker view, int year, int month,
					int day) {
				// TODO Auto-generated method stub
				//将canlendar上的年、月、日变成Date的
				mDate = new GregorianCalendar(year, month, day).getTime();
				
				//更新Arguement里的mDate变量
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		
		//创建AlertDialog.Builder类的对象可以设置属性
		return new AlertDialog.Builder(getActivity())
			.setView(v)
			.setTitle(R.string.date_picker_title)
			.setPositiveButton(android.R.string.ok, 
					//实现一个DialogInterface.OnClickListener()监听接口
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							sendResult(Activity.RESULT_OK);
						}
					})
			.create();
		
	}

}
