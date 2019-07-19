package com.shx.base.adapter.bean;

import java.util.ArrayList;

public class ListBean {

    public int currentPage;
    public int recordPage;
    public ArrayList<ItemData> list;

    public ListBean(int currentPage, int recordPage) {
        this.currentPage = currentPage;
        this.recordPage = recordPage;
    }

    public static class ItemData {
        //用于复杂列表区分样式
        public int type;
        public String name;
        public ArrayList<String> list;

        public ItemData(int type){
            this.type = type;
        }

        public ItemData(int type, String name) {
            this.type = type;
            this.name = name;
        }

    }
}
