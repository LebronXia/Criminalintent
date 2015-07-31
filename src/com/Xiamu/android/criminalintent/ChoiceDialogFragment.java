package com.Xiamu.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ChoiceDialogFragment extends DialogFragment {
	public static final String EXTRA_CHOICE = 
			"com.xiamu.android.criminalintent.choice";
	private int mChoice = 0;
	public static final int CHOICE_DATE = 1;
	public static final int CHOICE_TIME = 2;
	//·µ»ØÊý¾Ý
	private void sendResult(int resultCode){
		if(getTargetFragment() == null)
			return;
		Intent i = new Intent();
		i.putExtra(EXTRA_CHOICE, mChoice);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode,i);
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.choice_picker_title)
		.setPositiveButton(R.string.date_exdit, 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mChoice = CHOICE_DATE;
						sendResult(Activity.RESULT_OK);
					}
				})
				.setNegativeButton(R.string.time_exdit, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								mChoice = CHOICE_TIME;
								sendResult(Activity.RESULT_OK);
							}
						})
				.create();
	}
}
