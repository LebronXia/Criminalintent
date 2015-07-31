package com.Xiamu.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {

	//得到一个按比例缩小的图片资源
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaleDrawable(Activity a, String path){
		Display display = a.getWindowManager().getDefaultDisplay();		
		float destWidth = display.getWidth();
		float destHeight = display.getHeight();
		
		//读取图片的大小
		BitmapFactory.Options options = new BitmapFactory.Options();
		//设为true，不会返回bitmap，只会返回宽和高
		options.inJustDecodeBounds = true;
		//转换为bitmap
		BitmapFactory.decodeFile(path,options);
		
		//返回的图片的宽和高
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		//根据最小比例来设置
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
	//得到正确方向的图片
	public static BitmapDrawable getPortraitDrawable(ImageView iView, BitmapDrawable origImage){
		//设置旋转的值
		/*
		 * 利用Matrix构建缩放矩阵：
                     Matrix matrix = new Matrix(); 
                     matrix.postScale(scale, scale);
       		四、计算起始剪裁的位置x，y和剪裁宽度width,高度height；
       		五、调用createBitmap方法，在原图片的基础上剪裁新的图片并缩放相应的比例。
		 * */
		Matrix m = new Matrix();
		m.setRotate(90);
		Bitmap br = Bitmap.createBitmap(origImage.getBitmap(),0,0,
				origImage.getIntrinsicWidth(),origImage.getIntrinsicHeight(),m,true);
		origImage = new BitmapDrawable(iView.getResources(),br);
		return origImage;
	}
	//清理图片资源
	public static void cleanImageview(ImageView imageview){
		if(!(imageview.getDrawable() instanceof BitmapDrawable))
			return;
		BitmapDrawable b = (BitmapDrawable) imageview.getDrawable();
		//回收bitmap资源
		b.getBitmap().recycle();
		imageview.setImageBitmap(null);
	}
}
