package com.mykids.learning.data;

import java.util.List;

public class Category {
    public final String id;
    public final String title;
    public final String icon;
    public final int colorRes;
    public final int colorSoftRes;
    public final List<Object> items;
    public final List<List<Object>> stages;

    public Category(String id, String title, String icon, int colorRes, int colorSoftRes,
                     List<Object> items, List<List<Object>> stages) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.colorRes = colorRes;
        this.colorSoftRes = colorSoftRes;
        this.items = items;
        this.stages = stages;
    }
}
