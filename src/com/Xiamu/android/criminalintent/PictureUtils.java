package com.Xiamu.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {

	//�õ�һ����������С��ͼƬ��Դ
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaleDrawable(Activity a, String path){
		Display display = a.getWindowManager().getDefaultDisplay();		
		float destWidth = display.getWidth();
		float destHeight = display.getHeight();
		
		//��ȡͼƬ�Ĵ�С
		BitmapFactory.Options options = new BitmapFactory.Options();
		//��Ϊtrue�����᷵��bitmap��ֻ�᷵�ؿ�͸�
		options.inJustDecodeBounds = true;
		//ת��Ϊbitmap
		BitmapFactory.decodeFile(path,options);
		
		//���ص�ͼƬ�Ŀ�͸�
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		//������С����������
		int inSampleSize = 1;
		if(srcHeight > destHeight || srcWidth > destWidth){
			if(srcWidth > srcHeight){
				inSampleSize = Math.round(srcHeight / destHeight);
			}else{
				inSampleSize = Math.round(srcWidth / destWidth);
			}
		}
		
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return new BitmapDrawable(a.getResources(), bitmap);		
	}
	//�õ���ȷ�����ͼƬ
	public static BitmapDrawable getPortraitDrawable(ImageView iView, BitmapDrawable origImage){
		//������ת��ֵ
		/*
		 * ����Matrix�������ž���
                     Matrix matrix = new Matrix(); 
                     matrix.postScale(scale, scale);
       		�ġ�������ʼ���õ�λ��x��y�ͼ��ÿ��width,�߶�height��
       		�塢����createBitmap��������ԭͼƬ�Ļ����ϼ����µ�ͼƬ��������Ӧ�ı�����
		 * */
		Matrix m = new Matrix();
		m.setRotate(90);
		Bitmap br = Bitmap.createBitmap(origImage.getBitmap(),0,0,
				origImage.getIntrinsicWidth(),origImage.getIntrinsicHeight(),m,true);
		origImage = new BitmapDrawable(iView.getResources(),br);
		return origImage;
	}
	//����ͼƬ��Դ
	public static void cleanImageview(ImageView imageview){
		if(!(imageview.getDrawable() instanceof BitmapDrawable))
			return;
		BitmapDrawable b = (BitmapDrawable) imageview.getDrawable();
		//����bitmap��Դ
		b.getBitmap().recycle();
		imageview.setImageBitmap(null);
	}
}
