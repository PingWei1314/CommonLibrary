package com.pingwei.common_library.utils;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.pingwei.common_library.R;

public class MyDialog extends Dialog {
	private boolean lock = false;
	
	
	public boolean isLock() {
		return lock;
	}

	public void lock() {
		this.lock = true;
	}
	public void unlock() {
		this.lock = false;
	}
	

	@Override
	public void show() {
		lock();
		super.show();
		
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		unlock();
	}
	@Override
	public void dismiss() {
		unlock();
		super.dismiss();
	}
	public static MyDialog getInstance(Context context){
		return new MyDialog(context);
	}
	
	public MyDialog(Context context) {
		super(context, R.style.noalphadialog);
		this.setContentView(R.layout.util_dialog);
		this.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
