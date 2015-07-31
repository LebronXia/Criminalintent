package com.Xiamu.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity 
		implements CrimeFragment.Callbacks{
	private ViewPager mViewPager;
	private ArrayList<Crime> mCrimes;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		//以代码方式创建ViewPager视图
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		//ViewPager需要PagerAdapter才能显示视图
		mCrimes = CrimeLab.get(this).getCrimes();
		//将返回的fragment添加给activity，需要FragmentManager的原因所在
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mCrimes.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				// TODO Auto-generated method stub
				Crime crime = mCrimes.get(pos);
				//返回指定位置的fragment
				return CrimeFragment.newInstance(crime.getId());
			}
		});
		//设置监听器 监听当前ViewPager当前显示页面的状态变化
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			//显示当前哪个页面被选中
			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				Crime crime = mCrimes.get(pos);
				if(crime.getTitle() != null){
					setTitle(crime.getTitle());
				}
				
			}
			
			//告知我们当前页面所处的行为状态
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			//告知我们页面将会滑向哪里
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		UUID crimeId = (UUID) getIntent().
				getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for(int i = 0; i < mCrimes.size(); i++){
			//Crime实例的mId与intent extra 的crimeId相匹配，则将当前要显示的列表项设置为crime在数组中的索引位置
			if(mCrimes.get(i).getId().equals(crimeId)){
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}

	@Override
	public void onCrimeUpdated(Crime crime) {
		// TODO Auto-generated method stub
		
	}

}
