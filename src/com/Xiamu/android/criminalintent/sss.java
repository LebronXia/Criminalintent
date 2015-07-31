package com.Xiamu.android.criminalintent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class sss extends DialogFragment {

	private ArrayList<View> Views= new ArrayList<View>();
	public static final String EXTRA_DATE =
			"com.xiamu.android.criminalintent.date";
	private Date mDate;
	//���õ���ģʽ����¼���ڱ�����DatePickerFragment��argument bundle�У�  ����DatePickerFragment����ֱ�ӻ�ȡ
	public static DatePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}
	private void sendResult(int resultCode){
		//��ȡĿ��fragment
		if(getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		//������������Ϊextra���ӵ�intent��
		i.putExtra(EXTRA_DATE, mDate);
		//����CrimeFragment.onActivityResult()������ʵ�����ݻش�
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View timeView = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		final View dateView = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		final CharSequence[] items = {"�޸�ʱ��","�޸�����"};
		mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
		
		//����һ��Calendar����,Ȼ����Date�����������������ꡢ�¡���
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		
		Views.add(timeView);
		Views.add(dateView);
		
		//��Date��ֵ��ʾ��calendar��
				DatePicker datePicker = (DatePicker) dateView.findViewById(R.id.dialog_date_datePicker);
		//��ʼ��DatePicker����
				datePicker.init(year, month, day, new OnDateChangedListener() {
					
					public void onDateChanged(DatePicker view, int year, int month,
							int day) {
						// TODO Auto-generated method stub
						//��canlendar�ϵ��ꡢ�¡��ձ��Date��
						mDate = new GregorianCalendar(year, month, day).getTime();
						
						//����Arguement���mDate����
						getArguments().putSerializable(EXTRA_DATE, mDate);
					}
				});
				TimePicker timePicker = (TimePicker) timeView.findViewById(R.id.dialog_time_timePicker);
				timePicker.setCurrentHour(hour);
				timePicker.setCurrentMinute(minute);
				timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
					
					public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
						// TODO Auto-generated method stub
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(mDate);
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE,minute);
						mDate = calendar.getTime();
						getArguments().putSerializable(EXTRA_DATE, mDate);
					}
				});
				
				return new AlertDialog.Builder(getActivity())
		.setItems(items,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				if(items[item].equals("�޸�ʱ��")){
					new AlertDialog.Builder(getActivity())
					.setView(timeView)
					 .setTitle(R.string.time_picker_title) 
					 	.setPositiveButton("ȷ��", null)
					 	.show();
				}
				else{
					new AlertDialog.Builder(getActivity())
					.setView(dateView)
					 .setTitle(R.string.date_picker_title) 
					 	.setPositiveButton("ȷ��", null)
					 	.show();
				}
			}
		} )
//		.setPositiveButton(android.R.string.ok, 
//				new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//
//					}
//				})
		.create();
			
		
	}
	
}
