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
	
	//������볣��
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
	//��ʾ��ť�ϵ���Ϣ��������װ��һ�����е�updateDate������
//	public void uptimeTime(){
//		mTimeButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate()))+ " " + DateFormat.getTimeFormat(getActivity()).format(date);
//	}
	public void updateDate(){
		mDateButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate()) + " " + DateFormat.getTimeFormat(getActivity()).format(mCrime.getDate()));
		//mTimeButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate()));
		//mTimeButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate()) + " " + DateFormat.getTimeFormat(getActivity()).format(mCrime.getDate()));
		//mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy",mCrime.getDate()));
	}
	
	//���ز�ƴ��������ªϰ�����ı���Ϣ
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
		//��ӦonOptionsItemSelected()�Ļص�����
		setHasOptionsMenu(true);
	}
	//��ӦӦ��ͼ��˵���
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			//��ȡԪ�ؾ�������֣��鿴���Ƿ�ָ���˸�activity
			if(NavUtils.getParentActivityName(getActivity()) != null){
				//��������activity���棬����Ӧ��.CrimeListActivity����
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
		//�жϽ�������ж�
		if(resultCode != Activity.RESULT_OK) return;
		//����ChoiceFragment���������
		if(requestCode == REQUEST_CHOICE){
			//��Extraȡ��choice��ѡ��
			int choice = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOICE,0);
			
			if(choice == 0) return;
			FragmentManager fm = getActivity().getSupportFragmentManager();
			//choiceΪѡ������,��ʾDatePickerFragment
			if(choice == ChoiceDialogFragment.CHOICE_DATE){
				DatePickerFragment dialog  = DatePickerFragment.newInstance(mCrime.getDate());
				
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			//choiceΪѡ��ʱ��,��ʾTimePickerFragment
			}else if(choice == ChoiceDialogFragment.CHOICE_TIME){
				TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
				
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
				dialog.show(fm,DIALOG_TIME);
			}
		}
		//����DatePickerFragment���������
		if(requestCode == REQUEST_DATE){
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			mCallbacks.onCrimeUpdated(mCrime);
			updateDate();
		}
		//����TimePickerFragment���������
		if(requestCode == REQUEST_TIME){
			Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(time);
			updateDate();
		}else if(requestCode == REQUEST_PHOTO){
			
			if(mCrime.getPhoto() != null){
				mCrime.daletePhoto(getActivity().getFileStreamPath(
						mCrime.getPhoto().getFilename()).getAbsolutePath());
			}
			//����һ���µ�photoʵ��������ӵ�crime��
			String filename = data
					.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			int orientation = data
					.getIntExtra(CrimeCameraFragment.EXTRA_PHOTO_ORIENTATION, 0);
			if(filename != null){
				//Log.i(TAG, "filename:" + filename);
				Photo p = new Photo(filename,orientation);
				mCrime.setPhoto(p);
				mCallbacks.onCrimeUpdated(mCrime);
				//��CrimeCameraActivty���غ�Ҳ������ʾ���ĵ�ͼƬ
				showPhoto();
				//Log.i(TAG, "Crime:" + mCrime.getTitle() + "has a photo");
			}			
		} else if(requestCode == REQUEST_CONTACT){
			Uri contactUri = data.getData();
			//�ƶ���Ҫ�ҵ����ص�����
			String[] queryFields = new String[]{
					ContactsContract.Contacts.DISPLAY_NAME
			};
			//ִ����Ĳ�ѯ
			//�г�����
			Cursor c = getActivity().getContentResolver()
					.query(contactUri, queryFields, null, null, null);
			//������Ƿ�õ���һ�����
			if(c.getCount() == 0){
				c.close();
				return;
			}
			//ȡ�����ĵ�һ��ֵ�����������˵�����
			c.moveToFirst();
			String suspect = c.getString(0);
			mCrime.setSuspect(suspect);
			mCallbacks.onCrimeUpdated(mCrime);
			mSuspectButton.setText(suspect);
			c.close();
			
		}
		
	}


	//����fragment��ͼ�Ĳ��֣�Ȼ�����ɵ�View���ظ��й�activity
	//Ҳ������EditText�������Ӧ�û�����ĵط�
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		
		UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
		//�ڶ��������Ǹ���ͼ�������������Ǹ�֪�����������Ƿ����ɵ���ͼ��Ӹ�����ͼ
		View v = inflater.inflate(R.layout.fragment_crime, container, false);
		//�ж�android�豸�İ汾�Ŵ���
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(NavUtils.getParentActivityName(getActivity()) != null){
				//actionBar�����ʾ��Ļ�����ı�����,��ʾ�����ͼ��
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);				
			}
		}
		
		//��ExditText�����Ӽ���������
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		//����text��ͼ
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				mCrime.setTitle(s.toString());//���ָı�ʱ������ģ�Ͳ�����	
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
		
//		//�����û����ʱ�䰴ť�ļ�����
//		mTimeButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				FragmentManager fm = getActivity()
//						.getSupportFragmentManager();
//				TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
//				//���ý���Ŀ�������������ϵ��Ŀ��fragmentʹ�ø��������֪ͨ����һ��fragment�ڷ��ػ�ȡ����
//				dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
//				dialog.show(fm, DIALOG_TIME);
//			}
//		});
		
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Dialogmentʵ��Ҳ�����й�activity��fragmentManager�����
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				//����һ���洢Date��Fragment
				//DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				ChoiceDialogFragment dialog = new ChoiceDialogFragment();
				//����Ŀ��Fragment������������Ŀ��Fragment��ʹ�ø��������֪ͨ����һ��fragment�ڷ���������Ϣ
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_CHOICE);
				//DIALOG_DATE����ʶ����ڶ������Dialogment
				dialog.show(fm,DIALOG_CHOICE);
			}
		});
		
		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		//����checkbox��ͼ
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
		//������ڲ���������豸�����հ�ťӦ�ý���
		PackageManager pm = getActivity().getPackageManager();
		//���������
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
				//ǰ�������
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
				//�汾��С��9
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
				//ÿ�ζ���ʾactivityѡ����
				i.createChooser(i, getString(R.string.send_report));
				startActivity(i);
			}
		});
		
		//����ϵ��Ӧ����ѡ��������
		mSuspectButton = (Button) v.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//����ϵ�����ݿ��л�ȡĳ����ϵ��
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
	//�����ź��ͼƬ���ø�imageView��ͼ
	private void showPhoto(){
		Photo p = mCrime.getPhoto();
		BitmapDrawable b =null;
		if(p != null){
			String path = getActivity()
					.getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaleDrawable(getActivity(), path);
			if(p.getOrientation() ==
					Configuration.ORIENTATION_PORTRAIT){
				Log.i("CrimeFragment", "������������������������������");
				b = PictureUtils.getPortraitDrawable(mPhotoView, b);
			}
		}
		mPhotoView.setImageDrawable(b);
	}
	
	
	public static CrimeFragment newInstance(UUID crimeId){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		
		CrimeFragment fragment = new CrimeFragment();
		//����argument bunndle ��fragment
		fragment.setArguments(args);
		
		return fragment;
	}
	
	//��Onpause�б���Ӧ������
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
