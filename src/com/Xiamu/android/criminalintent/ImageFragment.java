package com.Xiamu.android.criminalintent;

import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
	public static final String EXTRA_IMAGE_PATH = 
			"com.xiamu.android.criminalintent.image_path";
	public static final String EXTRA_IMAGE_ORIENTATION = 
			"com.xiamu.android.criminalintent.image_orientation";
	public static ImageFragment newInstance(String imagePath,int 
			orientation){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
		
		args.putInt(EXTRA_IMAGE_ORIENTATION, orientation);
		ImageFragment fragment = new ImageFragment();
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return fragment;
	}
	private ImageView mImageView;
	//ʹ�ø���onCreatView�ķ���������
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mImageView = new ImageView(getActivity());
		String path = (String) getArguments().getSerializable(EXTRA_IMAGE_PATH);
		int orientation = (int)getArguments().getInt(EXTRA_IMAGE_ORIENTATION);
		BitmapDrawable image = PictureUtils.getScaleDrawable(getActivity(), path);
		//�ж��Ƿ�Ϊ����
		if(orientation == Configuration.ORIENTATION_PORTRAIT){
			image = PictureUtils.getPortraitDrawable(mImageView, image);
		}
		mImageView.setImageDrawable(image);
		return mImageView;		
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		PictureUtils.cleanImageview(mImageView);
	}
	
}
