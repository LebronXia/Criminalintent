package com.Xiamu.android.criminalintent;

import java.util.Calendar;
import java.util.Date;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME =
			"com.xiamu.android,criminalintent.time";
	//传送数据给TimePickerFragment，保存在arguement bundle中
	private Date mDate;
	public static TimePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, date);
		
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}
	private void sendResule(int resultCode){
		if(getTargetFragment() == null)
			return;
		Intent i = new Intent();
		i.putExtra(EXTRA_TIME, mDate);
		//CrimeFragment通知它销毁后就返回数据
		getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mDate = (Date) getArguments().getSerializable(EXTRA_TIME);
		
		//得到小时，分钟
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		//将时间XML文件赋给view视图
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_time,null);
		
		TimePicker timePicker = (TimePicker) v.findViewById(R.id.dialog_time_timePicker);
		//初始化timePicker
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		//设置监听器，用户改变时间后，date对象会得到更新
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				mDate = cal.getTime();
				getArguments().putSerializable(EXTRA_TIME, mDate);
			}
		});
		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.time_picker_title)
		.setPositiveButton(
				android.R.string.ok,
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sendResule(Activity.RESULT_OK);
					}
				})
		.create();  
		
	}
	
}
