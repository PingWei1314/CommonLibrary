package com.pingwei.common_library.xrecyclerview;

/**
 * 下拉刷新的状态
 * Created by jianghejie on 15/11/22.
 */
interface BaseRefreshHeader {
	//正常
	int STATE_NORMAL = 0;
	//手指释放
	int STATE_RELEASE_TO_REFRESH = 1;
	//刷新中
	int STATE_REFRESHING = 2;
	//刷新完成
	int STATE_DONE = 3;

	/**
	 * 上下移动距离
	 * @param delta
     */
	void onMove(float delta);

	boolean releaseAction();

	void refreshComplete();

}