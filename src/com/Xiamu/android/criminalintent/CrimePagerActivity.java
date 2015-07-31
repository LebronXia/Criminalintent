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
		//�Դ��뷽ʽ����ViewPager��ͼ
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		//ViewPager��ҪPagerAdapter������ʾ��ͼ
		mCrimes = CrimeLab.get(this).getCrimes();
		//�����ص�fragment��Ӹ�activity����ҪFragmentManager��ԭ������
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
				//����ָ��λ�õ�fragment
				return CrimeFragment.newInstance(crime.getId());
			}
		});
		//���ü����� ������ǰViewPager��ǰ��ʾҳ���״̬�仯
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			//��ʾ��ǰ�ĸ�ҳ�汻ѡ��
			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				Crime crime = mCrimes.get(pos);
				if(crime.getTitle() != null){
					setTitle(crime.getTitle());
				}
				
			}
			
			//��֪���ǵ�ǰҳ����������Ϊ״̬
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			//��֪����ҳ�潫�Ử������
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		UUID crimeId = (UUID) getIntent().
				getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for(int i = 0; i < mCrimes.size(); i++){
			//Crimeʵ����mId��intent extra ��crimeId��ƥ�䣬�򽫵�ǰҪ��ʾ���б�������Ϊcrime�������е�����λ��
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
