package com.guheng.duoduo.Tool;

import android.os.Environment;

import java.io.File;

public class Paths {
    // SD卡根路径
    public static final String mSDCardPath = Environment.getExternalStorageDirectory().toString();
    // 家路径
    public static final String mHomePath = "DuoDuo";
    // 日志路径
    public static final String mLogPath = Paths.mHomePath + File.separator + "Logs";
    // 数据路径
    public static final String mDataPath = Paths.mHomePath + File.separator + "Data";
    // 数据文件路径
    public static final String mDataFile = Paths.mDataPath + File.separator + "Data.ddd";
}
