package com.guheng.duoduo.Object;

import java.io.Serializable;

public class EntityItem implements Serializable {
    private static final String TAG = "EntityItem";
    private String mDate; // 日期
    private String mSummarized; // 主题

    public EntityItem(String date, String summarized) {
        this.mDate = date;
        this.mSummarized = summarized;
    }

    public String getDate() {
        return this.mDate;
    }

    public String getSummarized() {
        return this.mSummarized;
    }
}
