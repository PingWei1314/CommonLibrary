package com.pingwei.common_library.banner.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.RequestManager;
import com.pingwei.common_library.R;
import com.pingwei.common_library.banner.BannerClickCallback;
import com.pingwei.common_library.banner.bo.BaseBannerItemInfo;
import com.pingwei.common_library.banner.bo.ResourceBannerItemInfo;
import com.pingwei.common_library.banner.bo.UrlBannerItemInfo;

/**
 * 一个imgeview界面
 * Created by huanghaibin
 * on 16-5-23.
 */
public class BannerItemView extends RelativeLayout implements View.OnClickListener {

    private BaseBannerItemInfo banner;
    private ImageView iv_banner;
    private BannerClickCallback mCallback;


    public BannerItemView(Context context) {
        super(context,null);
        init(context);

    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_banner_item,this,true);
        iv_banner = (ImageView) findViewById(R.id.iv_banner);
        setOnClickListener(this);

    }

    /**
     * 网络获取图片 或者加载本地图片
     * @param manager
     * @param banner
     */
    public void initData(RequestManager manager, BaseBannerItemInfo banner, BannerClickCallback callback) {
        this.banner = banner;
        this.mCallback = callback;
        if(banner instanceof ResourceBannerItemInfo) {
            ResourceBannerItemInfo item = (ResourceBannerItemInfo) this.banner;
            manager.load(item.getResId()).into(iv_banner);

        }else if(banner instanceof UrlBannerItemInfo) {
            UrlBannerItemInfo item = (UrlBannerItemInfo) this.banner;
            manager.load(item.getImgeUrl()).into(iv_banner);
        }
    }

    /**
     * 点击后 的监听，后续动作应该弄到外部
     * @param v
     */
    @Override
    public void onClick(View v) {

        if(mCallback != null) {
            mCallback.onClicked(banner);
        }
    }

    /**
     * 获取banner的标题
     * @return
     */
    private String getTitle() {
        return banner.getName();
    }

















}
