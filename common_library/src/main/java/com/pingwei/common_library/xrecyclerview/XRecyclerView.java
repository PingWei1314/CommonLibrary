package com.pingwei.common_library.xrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 *根据类型添加,并不是真的header tooter
 * 思路:首先在init方法中初始化header和footer
 *
 * 在onscrollstatechanged方法中判断并执行上拉加载更多操作
 * 在ontouchevent动作中判断并执行下拉刷新操作
 *
 */
public class XRecyclerView extends RecyclerView {

    //正在上滑加载更多操作
    private boolean isLoadingData = false;
    private boolean isNoMore = false;
    //下拉刷新进度动画类型,progressstyle中的一个
    private int mRefreshProgressStyle = ProgressStyle.SysProgress;
    //上滑更多进度动画类型
    private int mLoadingMoreProgressStyle = ProgressStyle.SysProgress;
    //header集合
    private ArrayList<View> mHeaderViews = new ArrayList<>();

    //内部包装adapter
    private WrapAdapter mWrapAdapter;
    //上次触摸的y位置
    private float mLastY = -1;
    //下拉比率，即最多 下拉刷新组件高度为屏幕高度的1／3
    private static final float DRAG_RATE = 3;
    //下拉上滑监听
    private LoadingListener mLoadingListener;
    //带有箭头的头部view
    private ArrowRefreshHeader mRefreshHeader;
    //是否支持下拉刷新
    private boolean pullRefreshEnabled = true;
    //是否支持上滑更多
    private boolean loadingMoreEnabled = true;
    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    private static final int TYPE_REFRESH_HEADER = 10000;//设置一个很大的数字,尽可能避免和用户的adapter冲突
    private static final int TYPE_FOOTER = 10001;
    //初始化header index
    private static final int HEADER_INIT_INDEX = 10002;
    //每个header必须有不同的type,不然滚动的时候顺序会变化
    private static List<Integer> sHeaderTypes = new ArrayList<>();
    private int mPageCount = 0;
    //adapter没有数据的时候显示,类似于listView的emptyView
    private View mEmptyView;
    private View mFootView;
    //连接外部adapter和内部wrapadapter的observer
    private final AdapterDataObserver mDataObserver = new DataObserver();
    //  默认为展开状态
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 首先调用该方法, 初始化header和footer
     */
    private void init() {
        if (pullRefreshEnabled) {
            //初始化箭头头部view
            mRefreshHeader = new ArrowRefreshHeader(getContext());
            //设置头部view进度动画类型
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }
        //初始化底部控件 设置进度动画类型
        LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        mFootView = footView;
        mFootView.setVisibility(GONE);
    }

    /**
     * 对外提供的添加header的方法，可以添加多个header
     * sheadertypes中的数字减去header_inint_index对应mheaderviews的下标
     * @param view
     */
    public void addHeaderView(View view) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(view);
    }

    //根据header的ViewType判断是哪个header
    private View getHeaderViewByType(int itemType) {
        if(!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    //判断一个type是否为传统的头部部分，对应的type值为sheadertypes里面的值
    private boolean isHeaderType(int itemViewType) {
        return  mHeaderViews.size() > 0 &&  sHeaderTypes.contains(itemViewType);
    }

    //判断外部adapte中的type是否是XRecyclerView保留的itemViewType
    //即 下拉刷新view 、footer view、 外部设置的header
    private boolean isReservedItemViewType(int itemViewType) {
        if(itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_FOOTER || sHeaderTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置header， header只有一个，与上滑更多的footer冲突
     * @param view
     */
    public void setFootView(final View view) {
        mFootView = view;
    }

    /**
     * 上滑更多结束
     */
    public void loadMoreComplete() {
        isLoadingData = false;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    /**
     * 上滑加载更多没有了
     * @param noMore
     */
    public void setNoMore(boolean noMore){
        isLoadingData = false;
        isNoMore = noMore;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setState(isNoMore ? LoadingMoreFooter.STATE_NOMORE:LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void reset(){
        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    /**
     * 下拉刷新结束
     */
    public void refreshComplete() {

        mRefreshHeader.refreshComplete();
        setNoMore(false);
    }

    public void setRefreshHeader(ArrowRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled) {
            if (mFootView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter)mFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
            }
        }
    }

    /**
     * 设置header进度动画类型
     * @param style
     */
    public void setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }

    /**
     * 设置footer进度动画类型
     * @param style
     */
    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setProgressStyle(style);
        }
    }

    /**
     * 设置header箭头图片,箭头必须是向下的
     * @param resId
     */
    public void setArrowImageView(int resId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resId);
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 改写adapter，将adapter进行包装
     * @param adapter
     */
    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        //其他的一些数据改变的方法也通过dataobserver交给wrapadapter
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    /**
     * 监听scroll状态改变
     * 滑动到最底部（滑动状态由滑动变为idle），并且没有正在下拉刷新，并且没有正在加载更多，并且有加载更多的能力，
     * 并且数量要多于一屏，
     *
     *  然后触发加载更多
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //闲置状态，并且未在上滑更多状态
        if (state == RecyclerView.SCROLL_STATE_IDLE
                && mLoadingListener != null
                && !isLoadingData
                && loadingMoreEnabled) {
            //对于三种布局获取最后一个可见的position
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
                //线性布局下 获取最后一个可见position（除去里footer）
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            //getchildcount:当前窗口可见的childview的数量
            //getitemcount:adapter中的getitemcount
            //itemcount > childcount:表示数量要多于一屏才会触发添加更多
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isNoMore
                    //正常状态或手指释放状态
                    && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true;
                if (mFootView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    mFootView.setVisibility(View.VISIBLE);
                }
                mLoadingListener.onLoadMore();
            }
        }
    }

    /**
     * 判断并执行下拉刷新动作
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //action_down的时候还会符合条件
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //deltay有正负：向下滑为正（下拉刷新），向上滑为负（上拉更多）：
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                //在最上方 并且有下拉刷新能力  并且处于展开（默认为展开）
                if (isOnTop()
                        && pullRefreshEnabled
                        && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    //下拉刷新组件高度最大到达屏幕的 1／DRAG_RATE
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0
                            && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                //ACTION_UP时，再将mlasty初始化
                mLastY = -1; // reset
                if (isOnTop()
                        && pullRefreshEnabled
                        && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    //如果是从release_to_refresh到refreshing，则触发下拉刷新动作
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 是否下拉刷新组件在窗口最上面
     * @return
     */
    private boolean isOnTop() {
        //可见时，是attachedtowindow的，不可见时，是unattachedtowindow的
        if (mRefreshHeader.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将外部adapter的数据改变通过该观察者作用到wrapperadapter中
     * 作用在外部的adapter中
     */
    private class DataObserver extends AdapterDataObserver {
        /**
         * adapter的notifydatasetchanged会最终调用onchanged方法，该方法最终调用
         * wrapperadapter的notifydatasetchanged方法
         *
         * 数据集改变
         * 对于数据集为空做了处理，显示空view
         */
        @Override
        public void onChanged() {
            //从recyclerview中获取adapter即wrapperadapter
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (pullRefreshEnabled) {
                    emptyCount++;
                }
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                //没有计算外部设置的header，如果外部设置里header则不会显示空view
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    XRecyclerView.this.setVisibility(View.GONE);
                    //显示空view
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    XRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        /**
         * 批量插入
         * @param positionStart
         * @param itemCount
         */
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        /**
         * 批量改变
         * @param positionStart
         * @param itemCount
         */
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        /**
         * 批量删除
         * @param positionStart
         * @param itemCount
         */
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        /**
         * 移动item
         * @param fromPosition
         * @param toPosition
         * @param itemCount
         */
        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);

        }
    };

    /**
     * 对自定义的adapter进行封装
     */
    public class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter adapter;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        /**
         * 根据位置判断是否是外部设置的头部部分
         * @param position
         * @return
         */
        public boolean isHeader(int position) {
            return position >= 1 && position < mHeaderViews.size() + 1;
        }

        /**
         * 是否是tooter
         * @param position
         * @return
         */
        public boolean isFooter(int position) {
            if(loadingMoreEnabled) {
                return position == getItemCount() - 1;
            }else {
                return false;
            }
        }

        /**
         * 根据位置判断是否是下拉刷新部分
         * @param position
         * @return
         */
        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        /**
         * 获取外部设置的头部数据
         * @return
         */
        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //下拉刷新部分
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder(mRefreshHeader);
                //外面设置的头部部分
            } else if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));

                //上滑加载更多部分
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootView);
            }
            //其他的外部数据部分
            return adapter.onCreateViewHolder(parent, viewType);
        }

        /**
         * 下拉刷新部分、头部部分、footer部分 都不绑定数据
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //如果是下拉刷新部分或者是外部设置的头部部分，不绑定数据
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            //获取在外部设置的adapter的数据部分的position（减去下拉刷新数量 减去外部设置的头部数量）
            int adjPosition = position - (getHeadersCount() + 1);
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                //避免是footer的情况（两个数据相等）
                if (adjPosition < adapterCount) {
                    //调用外部的绑定数据
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            //如果允许上滑加载更多
            if(loadingMoreEnabled) {
                if (adapter != null) {
                    //下拉刷新、footer 、设置的头部部分、数据部分
                    return getHeadersCount() + adapter.getItemCount() + 2;
                } else {
                    return getHeadersCount() + 2;
                }
                //不允许上滑加载更多
            }else {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount() + 1;
                } else {
                    return getHeadersCount() + 1;
                }
            }
        }

        /**
         * adapter中的方法  获取类型
         * 外部adapter中的类型要小于10000
         * @param position
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            //外部adapter的数据部分position
            int adjPosition = position - (getHeadersCount() + 1);
            //是否使用了内部保留的类型
            if(isReservedItemViewType(adapter.getItemViewType(adjPosition))) {
                throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 " );
            }
            //是否是下拉刷新部分
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            //是否是外部设置的头部部分，类型大于等于10002
            if (isHeader(position)) {
                position = position - 1;
                return sHeaderTypes.get(position);
            }
            //是否是footer部分
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }

            //外部设置的其他类型
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            //是外部设置的数据类型（除去下拉刷新、外部设置的头部）
            if (adapter != null && position >= getHeadersCount() + 1) {
                int adjPosition = position - (getHeadersCount() + 1);
                //除去footer
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        /**
         *
         * recyclerview成功setadapter（在setadapter方法中调用）后调用
         * 因此setlayoutmanager必须在setadapter前调用
         *
         *  对gridlayoutmanager进行处理，使特殊的item占据一行
         *  spancount:列数
         *  spansize：每一个item的跨度，跨越了多少个span

         * @param recyclerView
         */
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            //如果是网格布局
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                //设置每个item的跨度
                //下拉刷新、上拉更多、外部设置的头部 都会横跨一行
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        /**
         * 方法就是用来当你的列表项滑出可见窗口之外的时候，需要重写此方法进行相应的一些操作。
         * @param recyclerView
         */
        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        /**
         * 当列表项出现到可视界面的时候调用
         *
            对于瀑布流布局进行设置，如果是下拉刷新、上滑更多、设置的头部 则设置充满一行
         瀑布流布局没有方法一开始就动态设置列数，所以在每次item可见时设置
         对于所有的布局，
         * @param holder
         */
        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            //holer.itemview就是从构造方法里传入的view
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            StaggeredGridLayoutManager m;

            //如果是下拉刷新、上滑更多、设置的头部  则设置fullspan
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) ||isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        /**
         * 注销观察者
         * @param observer
         */
        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        /**
         * Adapter是RecyclerView的数据供应方，当Adapter和RecyclerView绑定的时候其实就是一个相互引用的过程。
         * 绑定过程中RecyclerView注册成为Adapter的观察者，
         * 一旦Adapter中的数据发生改变就通过调用Adapter的notifyXX方法告知RecyclerView去刷新视图
         * 绑定观察者
         * @param observer
         */
        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 设置下拉上滑监听
     * @param listener
     */
    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    /**
     * 下拉上滑监听
     */
    public interface LoadingListener {
        /**
         * 下拉刷新监听
         */
        void onRefresh();

        /**
         * 上滑加载更多监听
         */
        void onLoadMore();
    }

    /**
     * 开始刷新,对外使用的方法，内部不使用
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && pullRefreshEnabled && mLoadingListener != null) {
            //设置为正在刷新
            mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
            //设置下拉刷新view高度
            Log.e("setrefresh高度：",mRefreshHeader.getVisibleHeight() + "");
            mRefreshHeader.onMove(mRefreshHeader.getMeasuredHeight());
            //下拉刷新回调
            mLoadingListener.onRefresh();
        }
    }

    /**
     * 获取layout属性,在onresume之前获取的都为空，在onresume之后调用
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if(p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout)p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if(child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout)child;
                    break;
                }
            }
            if(appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {

                    }
                });
            }
        }
    }
}