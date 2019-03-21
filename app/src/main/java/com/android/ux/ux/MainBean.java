package com.android.ux.ux;

import java.util.ArrayList;
import java.util.List;

public class MainBean {
    public List<CategoryBean> all = new ArrayList<>();

    public static class CategoryBean {
        public String category_name = "";
        public List<ActivityBean> category_list = new ArrayList<>();

        public CategoryBean() {}
    }

    public static class ActivityBean {
        public String name = "";
        public String activity = "";

        public ActivityBean() {}
        public ActivityBean(String name, String activity) {
            this.name = name;
            this.activity = activity;
        }
    }
}
