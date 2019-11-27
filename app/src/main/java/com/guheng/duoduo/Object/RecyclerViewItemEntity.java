package com.guheng.duoduo.Object;

import java.text.SimpleDateFormat;
import java.util.Locale;

;

public class RecyclerViewItemEntity {
    private static final String TAG = "RecyclerViewItemEntity";
    private long mTimeMillis; // 毫秒
    private String mSummarized; // 主题

    public RecyclerViewItemEntity(long timeMillis, String summarized) {
        this.mTimeMillis = timeMillis;
        this.mSummarized = summarized;
    }

    public long getTimeMillis() {
        return this.mTimeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.mTimeMillis = timeMillis;
    }

    public String getYearMonthDayHourMinuteSecondString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return formatter.format(this.mTimeMillis);
    }

    public String getSummarized() {
        return this.mSummarized;
    }

    public void setSummarized(String summarized) {
        this.mSummarized = summarized;
    }
}
