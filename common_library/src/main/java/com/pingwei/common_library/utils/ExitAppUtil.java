package com.pingwei.common_library.utils;

import android.app.Activity;

import com.pingwei.common_library.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class ExitAppUtil {
	
	private List<BaseActivity> lst = new ArrayList<BaseActivity>();
	
	private static ExitAppUtil instance;
	
	private ExitAppUtil() {
		super();
	}
	
	public static ExitAppUtil getInstance() {
		
		if(instance == null) {
			instance = new ExitAppUtil();
		}
		return instance;
	}
	
	public void addActivity(BaseActivity activity) {
		
		lst.add(activity);
		
	}
	
	public void removeActivity(BaseActivity activity) {
		lst.remove(activity);
	}
	
	
	
	/**
	 * 将除clz之外的activity关闭
	 * @param clz
	 */
	public void removeMostExcept(Class<?> clz) {
		for(Activity a:lst) {
			if(clz.isInstance(a)) {
				continue;
			}else {
				a.finish();
			}
		}
	}

	/**
	 * 关闭activity，退出进程，退出程序。
	 */
	public void exit() {
		for(BaseActivity a:lst) {
			a.finish();
		}
		
		android.os.Process.killProcess(android.os.Process.myPid());
		//内存回收
		System.gc();
		System.exit(0);
	}

}
