package com.pingwei.common_library.banner.bo;

/**
 * Created by chandlerbing on 2017/3/15.
 */

public class BaseBannerItemInfo {

    private String name;
    private String detail;
    //点击图片后的跳转地址，非必须
    private String href;

    private String pubDate;
    //唯一性标示
    private String id;

    private String plusInfo1;
    private String plusInfo2;
    private String plusInfo3;

    public BaseBannerItemInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlusInfo1() {
        return plusInfo1;
    }

    public void setPlusInfo1(String plusInfo1) {
        this.plusInfo1 = plusInfo1;
    }

    public String getPlusInfo2() {
        return plusInfo2;
    }

    public void setPlusInfo2(String plusInfo2) {
        this.plusInfo2 = plusInfo2;
    }

    public String getPlusInfo3() {
        return plusInfo3;
    }

    public void setPlusInfo3(String plusInfo3) {
        this.plusInfo3 = plusInfo3;
    }
}
