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
	 * 创建activity回调接口
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
		//通知FragmentManager，接收onCreateOptionsMenu的选项菜单的回调
		setHasOptionsMenu(true);
		//返回托管activity,使得fragment处理更多的activity相关事务
		getActivity().setTitle(R.string.crimes_title);
		//获取存储在CrimeLab中的crime列表
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		//创建adapt控制对象
//		ArrayAdapter<Crime> adapter = 
//				new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1,
//						mCrimes);
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		//ListFragment管理的内置ListView设置adapter
		setListAdapter(adapter);
		
		//重新创建Activity时不销毁Fragment，以便恢复
		setRetainInstance(true);
		mSubtitleVisible = false;
	}
	
	@TargetApi(11) 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = super.onCreateView(inflater, container, savedInstanceState);
		//根据变量mSubtitleVisible的值确定是否要设置子标题
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(mSubtitleVisible){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		//登记listview视图，实现点击任意列表项，都能弹出上下文菜单
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			//区分老版本
			registerForContextMenu(listView);
		}else{
			//用上下文操作栏在高版本上
			//CHOICE_MODE_MULTIPLE_MODAL 都是多选模式
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			//视图在选中或撤销时会触发它
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
				
				//Android开发者应该都熟悉Context Menu了，
				//Context Menu是悬浮在操作项之上的视图。ActionMode是临时占据了ActionBar的位置。下面给出ActionMode的实现方法。
				
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					//操作模式mode负责对上下文操作栏进行配置
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
						//准备销毁操作模式
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
	//listFragment会默认实现方法生成一个全屏ListView布局
	//Listview需要显示视图对象时，会与其adapter展开会话沟通
	//在我们上下滚动列表时，Listview调用adapter的getView()方法，按需获取要显示的视图

	@Override
	//响应用户对列表项的点击事件
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		//返回设置在listFragment列表视图上的adapter.
//		Crime c =(Crime) getListAdapter().getItem(position);
		//获取列表当前position的Crime对象
		Crime c = (Crime) getListAdapter().getItem(position);
		//Log.d(TAG, c.getTitle() + "was clicked");
		//开启CrimePagerActivity
//		Intent i = new Intent(getActivity(),CrimePagerActivity.class);
//		i.putExtra(CrimeFragment.EXTRA_CRIME_ID,c.getId());
//		startActivity(i);
		mCallbacks.onCrimeSelected(c);
	}
	//创建一个知道如何与Crime对象交互的adapter
	private class CrimeAdapter extends ArrayAdapter<Crime>{
		//构造方法
		public CrimeAdapter(ArrayList<Crime> crimes){
		//public ArrayAdapter(Context context, int textViewResourceId, T[] object)
			super(getActivity(),0,crimes);
		}
		public View getView(int position, View converView, ViewGroup parent){
			if(converView == null){
				converView = getActivity().getLayoutInflater().
						inflate(R.layout.list_item_crime, null);//产生fragment的视图
			}
			//无论是新对象还是复用对象都应调用Adapter的getItem(int)方法获取当前position的Crime对象；
			Crime c =getItem(position);
						
			//最后把视图对象返回给listCiew
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
	//一般来说，要保证fragment视图得到刷新，在onResume()方法内更新代码是最安全的
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	//创建选项菜单
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		//查看子标题状态，以保证菜单项标题与之匹配
		MenuItem showMenuItem = menu.findItem(R.id.menu_item_show_subtitle);
		if(mSubtitleVisible && showMenuItem != null){
			showMenuItem.setTitle(R.string.hide_subtitle);
		}
	}
	//用户点击选项菜单中的菜单项是，fragment会收到onOptionsItemSelected()的回调请求
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case R.id.menu_item_crime:
				//创建一个新的Crime实例
				Crime crime = new Crime();
				//添加到CrimeLab中
				CrimeLab.get(getActivity()).addCrime(crime);
				//传送id
//				Intent i = new Intent(getActivity(), CrimePagerActivity.class);
//				i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//				startActivityForResult(i, 0);
				//一旦完成菜单项时间处理，应返回true值以表明已完成菜单项选择处理的全部任务
				((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
				mCallbacks.onCrimeSelected(crime);
				return true;
				//设置小标题
			case R.id.menu_item_show_subtitle:
				//如果操作栏上没有显示子标题，则应设置显示子标题，同时切换菜单项标题hide subtitle
				if(getActivity().getActionBar().getSubtitle() == null){
					getActivity().getActionBar().setSubtitle(R.string.subtitle);
					mSubtitleVisible = true;
					item.setTitle(R.string.hide_subtitle);
					//如果子标题已经显示，则应设置其为null值，同时将菜单项标题切换回show subtitle
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
		//回调获取对象的信息
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//用于显示上下文菜单的子视图在适配器中的位置。
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
		Crime crime = adapter.getItem(position);
		
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).deleteCrime(crime);
			//notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容。
			adapter.notifyDataSetChanged();
			return true;

		}
		return super.onContextItemSelected(item);
	}
	public void updateUI(){
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
}
