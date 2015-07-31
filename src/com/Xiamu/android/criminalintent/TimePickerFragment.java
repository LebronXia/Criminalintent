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
	//�������ݸ�TimePickerFragment��������arguement bundle��
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
		//CrimeFragment֪ͨ�����ٺ�ͷ�������
		getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mDate = (Date) getArguments().getSerializable(EXTRA_TIME);
		
		//�õ�Сʱ������
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		//��ʱ��XML�ļ�����view��ͼ
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_time,null);
		
		TimePicker timePicker = (TimePicker) v.findViewById(R.id.dialog_time_timePicker);
		//��ʼ��timePicker
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		//���ü��������û��ı�ʱ���date�����õ�����
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
