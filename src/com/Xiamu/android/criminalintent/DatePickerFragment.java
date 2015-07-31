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
		mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
		
		//����һ��Calendar����,Ȼ����Date�����������������ꡢ�¡���
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		//����������ͼ
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		
		//��Date��ֵ��ʾ��calendar��
		DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
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
		
		//����AlertDialog.Builder��Ķ��������������
		return new AlertDialog.Builder(getActivity())
			.setView(v)
			.setTitle(R.string.date_picker_title)
			.setPositiveButton(android.R.string.ok, 
					//ʵ��һ��DialogInterface.OnClickListener()�����ӿ�
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
