package com.shx.base.adapter.base;

/**
 * 跟view绑定的item
 */
public class ShxItem {

    public static final int ITEM_NA = -1;
    private int viewType = ITEM_NA; // view的类型
    private Object obj;
    private String s1;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
