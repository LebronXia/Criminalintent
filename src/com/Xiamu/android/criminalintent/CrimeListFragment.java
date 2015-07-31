package com.Xiamu.android.criminalintent;

import java.security.PublicKey;
import java.util.ArrayList;

import com.Xiamu.android.criminalintent.R.id;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {

	private ArrayList<Crime> mCrimes;
	private boolean mSubtitleVisible;
	private Callbacks mCallbacks;
	private static final String TAG = "CrimeListFragment";

	/**
	 * ����activity�ص��ӿ�
	 */
	
	public interface Callbacks{
		void onCrimeSelected(Crime crime);
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//֪ͨFragmentManager������onCreateOptionsMenu��ѡ��˵��Ļص�
		setHasOptionsMenu(true);
		//�����й�activity,ʹ��fragment��������activity�������
		getActivity().setTitle(R.string.crimes_title);
		//��ȡ�洢��CrimeLab�е�crime�б�
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		//����adapt���ƶ���
//		ArrayAdapter<Crime> adapter = 
//				new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1,
//						mCrimes);
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		//ListFragment���������ListView����adapter
		setListAdapter(adapter);
		
		//���´���Activityʱ������Fragment���Ա�ָ�
		setRetainInstance(true);
		mSubtitleVisible = false;
	}
	
	@TargetApi(11) 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = super.onCreateView(inflater, container, savedInstanceState);
		//���ݱ���mSubtitleVisible��ֵȷ���Ƿ�Ҫ�����ӱ���
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(mSubtitleVisible){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		//�Ǽ�listview��ͼ��ʵ�ֵ�������б�����ܵ��������Ĳ˵�
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			//�����ϰ汾
			registerForContextMenu(listView);
		}else{
			//�������Ĳ������ڸ߰汾��
			//CHOICE_MODE_MULTIPLE_MODAL ���Ƕ�ѡģʽ
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			//��ͼ��ѡ�л���ʱ�ᴥ����
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode mode) {
					// TODO Auto-generated method stub
					
				}
				
				//Android������Ӧ�ö���ϤContext Menu�ˣ�
				//Context Menu�������ڲ�����֮�ϵ���ͼ��ActionMode����ʱռ����ActionBar��λ�á��������ActionMode��ʵ�ַ�����
				
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					//����ģʽmode����������Ĳ�������������
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					// TODO Auto-generated method stub
					switch (item.getItemId()) {
					case R.id.menu_item_delete_crime:
						CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
						CrimeLab crimeLab = CrimeLab.get(getActivity());
						for(int i = adapter.getCount() - 1; i >= 0; i-- ){
							if(getListView().isItemChecked(i)){
								crimeLab.deleteCrime(adapter.getItem(i));
							}
						}
						//׼�����ٲ���ģʽ
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default:
						return false;
					}
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		
		return v;
	}
	//listFragment��Ĭ��ʵ�ַ�������һ��ȫ��ListView����
	//Listview��Ҫ��ʾ��ͼ����ʱ��������adapterչ���Ự��ͨ
	//���������¹����б�ʱ��Listview����adapter��getView()�����������ȡҪ��ʾ����ͼ

	@Override
	//��Ӧ�û����б���ĵ���¼�
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		//����������listFragment�б���ͼ�ϵ�adapter.
//		Crime c =(Crime) getListAdapter().getItem(position);
		//��ȡ�б�ǰposition��Crime����
		Crime c = (Crime) getListAdapter().getItem(position);
		//Log.d(TAG, c.getTitle() + "was clicked");
		//����CrimePagerActivity
//		Intent i = new Intent(getActivity(),CrimePagerActivity.class);
//		i.putExtra(CrimeFragment.EXTRA_CRIME_ID,c.getId());
//		startActivity(i);
		mCallbacks.onCrimeSelected(c);
	}
	//����һ��֪�������Crime���󽻻���adapter
	private class CrimeAdapter extends ArrayAdapter<Crime>{
		//���췽��
		public CrimeAdapter(ArrayList<Crime> crimes){
		//public ArrayAdapter(Context context, int textViewResourceId, T[] object)
			super(getActivity(),0,crimes);
		}
		public View getView(int position, View converView, ViewGroup parent){
			if(converView == null){
				converView = getActivity().getLayoutInflater().
						inflate(R.layout.list_item_crime, null);//����fragment����ͼ
			}
			//�������¶����Ǹ��ö���Ӧ����Adapter��getItem(int)������ȡ��ǰposition��Crime����
			Crime c =getItem(position);
						
			//������ͼ���󷵻ظ�listCiew
			TextView titleTextView = 
					(TextView) converView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			TextView dateTextView = 
					(TextView)converView.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText(DateFormat.getDateFormat(getActivity()).format(c.getDate()) + " " + DateFormat.getTimeFormat(getActivity()).format(c.getDate()));
			//mTimeButton.setText(DateFormat.getDateFormat(getActivity()).format(mCrime.getDate())););
			CheckBox solvedCheckBox = 
					(CheckBox) converView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			
			return converView;
		
		}
	}
	//һ����˵��Ҫ��֤fragment��ͼ�õ�ˢ�£���onResume()�����ڸ��´������ȫ��
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	//����ѡ��˵�
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		//�鿴�ӱ���״̬���Ա�֤�˵��������֮ƥ��
		MenuItem showMenuItem = menu.findItem(R.id.menu_item_show_subtitle);
		if(mSubtitleVisible && showMenuItem != null){
			showMenuItem.setTitle(R.string.hide_subtitle);
		}
	}
	//�û����ѡ��˵��еĲ˵����ǣ�fragment���յ�onOptionsItemSelected()�Ļص�����
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case R.id.menu_item_crime:
				//����һ���µ�Crimeʵ��
				Crime crime = new Crime();
				//��ӵ�CrimeLab��
				CrimeLab.get(getActivity()).addCrime(crime);
				//����id
//				Intent i = new Intent(getActivity(), CrimePagerActivity.class);
//				i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//				startActivityForResult(i, 0);
				//һ����ɲ˵���ʱ�䴦��Ӧ����trueֵ�Ա�������ɲ˵���ѡ�����ȫ������
				((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
				mCallbacks.onCrimeSelected(crime);
				return true;
				//����С����
			case R.id.menu_item_show_subtitle:
				//�����������û����ʾ�ӱ��⣬��Ӧ������ʾ�ӱ��⣬ͬʱ�л��˵������hide subtitle
				if(getActivity().getActionBar().getSubtitle() == null){
					getActivity().getActionBar().setSubtitle(R.string.subtitle);
					mSubtitleVisible = true;
					item.setTitle(R.string.hide_subtitle);
					//����ӱ����Ѿ���ʾ����Ӧ������Ϊnullֵ��ͬʱ���˵�������л���show subtitle
				}else {
					getActivity().getActionBar().setSubtitle(null);
					mSubtitleVisible = false;
					item.setTitle(R.string.show_subtitle);
				}
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
		
		}	
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
		//�ص���ȡ�������Ϣ
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//������ʾ�����Ĳ˵�������ͼ���������е�λ�á�
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
		Crime crime = adapter.getItem(position);
		
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).deleteCrime(crime);
			//notifyDataSetChanged����ͨ��һ���ⲿ�ķ���������������������ݸı�ʱ��Ҫǿ�Ƶ���getView��ˢ��ÿ��Item�����ݡ�
			adapter.notifyDataSetChanged();
			return true;

		}
		return super.onContextItemSelected(item);
	}
	public void updateUI(){
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
}
