package com.pingwei.common_library.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 只有一种类型的recyclerview adapter
 * Created by chandlerbing on 2016/10/14.
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter {

    private int layoutId;

    private List<T> lst = new ArrayList<T>();

    private OnItemClickListener listener;

    public RecyclerViewAdapter(int layoutId) {
        this.layoutId = layoutId;
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置数据
     * @param data
     */
    public void refreshData(List<T> data) {
            lst.clear();
        if(data != null) {
            lst.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 追加数据
     * @param data
     */
    public void addData(List<T> data) {
        if(data != null) {
            lst.addAll(data);
            notifyDataSetChanged();

        }
    }




    /**
     * 获取item
     * @param position
     * @return
     */
    public T getItem(int position) {
        return lst.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new VHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final T item = lst.get(position);
        //避免插入、删除造成的position混乱
  //      final int pos = holder.getLayoutPosition();
        //设置监听
        if(listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClicked(holder.itemView,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.itemLongClicked(holder.itemView,position);
                    return true;
                }
            });
        }


        onBindData((VHolder) holder,position,item);
    }

    /**
     * 数据和view绑定
     * @param holder
     * @param position
     * @param item
     */
    public abstract void onBindData(VHolder holder,int position,T item);


    @Override
    public int getItemCount() {
        return lst.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        public SparseArray<View> childrenViews;
        public View parentView;

        public VHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            childrenViews = new SparseArray<View>();
        }

        /**
         * 通过viewId获取convertview内的各个组件
         * @param viewId
         * @param <T>
         * @return
         */
        public <T extends View> T getView(int viewId) {
            View view = childrenViews.get(viewId);
            if(view == null) {
                view = parentView.findViewById(viewId);
                childrenViews.put(viewId,view);
            }
            return (T)view;
        }


    }

    public interface OnItemClickListener {
        public void itemClicked(View v, int position);
        public void itemLongClicked(View v, int position);
    }
}
