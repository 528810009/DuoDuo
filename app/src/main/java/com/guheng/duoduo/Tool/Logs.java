package com.guheng.duoduo.Tool;

import com.robin.lazy.logger.LazyLogger;
import com.robin.lazy.logger.Log4JTool;
import com.robin.lazy.logger.LogLevel;
import com.robin.lazy.logger.PrinterType;

import org.apache.log4j.Level;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Logs {
    private String mLogPath;

    public Logs(String logPath) {
        this.mLogPath = logPath;

        String logName = getLogName();

        LazyLogger.init(/* PrinterType.FORMATTED */PrinterType.ORDINARY) // 打印类型
                .methodCount(3) // default 2
                .hideThreadInfo() // default shown
                .logLevel(LogLevel.ALL) // default LogLevel.ALL(设置全局日志等级)
                .methodOffset(2) // default 0
                .logTool(/* new AndroidLogTool() *//*new SLF4JTool()*/ // Log4j中的Level与本框架的LogLevel是分开设置的(Level只用来设置log4j的日志等级)
                        new Log4JTool(Level.ALL, this.mLogPath, logName));

        // 删除过期的日志文件
        clearOldLogFiles();
    }

    // 获取日志文件名称
    private String getLogName() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
    }

    // 清理过期的日志
    private void clearOldLogFiles() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

        final long MINUTE_FACTOR = 1000 * 60;
        final long HOUR_FACTOR = 60 * MINUTE_FACTOR;
        final long DAY_FACTOR = 24 * HOUR_FACTOR;

        final long DAY_THRESHOLD = 30; // 天
        final long HOUR_THRESHOLD = 0; // 小时
        final long MINUTE_THRESHOLD = 0; // 分钟

        try {
            String strNowDate = new SimpleDateFormat("yyyyMMddHHmmss",
                    Locale.getDefault()).format(new Date(System.currentTimeMillis()));

            Date dateNow = dateFormat.parse(strNowDate);

            // 获取日志文件列表
            ArrayList<File> files = new ArrayList<>();
            traverseFindFiles(this.mLogPath, new String[]{"LOG"}, files);

            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                String strLogName = file.getName();
                String strDateOld = strLogName.substring(0, strLogName.lastIndexOf(".log"));

                Date dateOld = new Date(System.currentTimeMillis());
                try {
                    dateOld = dateFormat.parse(strDateOld);
                } catch (Exception exception) {
                    LazyLogger.w("删除过期日志文件(忽略文件)：" + file.getAbsolutePath() + " " + exception);
                }

                long dateDifference = dateNow.getTime() - dateOld.getTime();

                long daysDifference = dateDifference / DAY_FACTOR;
                long hoursDifference = (dateDifference - daysDifference * DAY_FACTOR) / HOUR_FACTOR;
                long minutesDifference = (dateDifference - daysDifference * DAY_FACTOR - hoursDifference * HOUR_FACTOR) / MINUTE_FACTOR;

                if (daysDifference < DAY_THRESHOLD) {
                    continue; // 非过期日志文件
                } else if (daysDifference == DAY_THRESHOLD) {
                    if (hoursDifference < HOUR_THRESHOLD) {
                        continue; // 非过期日志文件
                    } else if (hoursDifference == HOUR_THRESHOLD) {
                        if (minutesDifference < MINUTE_THRESHOLD) {
                            continue; // 非过期日志文件
                        } else if (minutesDifference == MINUTE_THRESHOLD) {
                            continue; // 非过期日志文件
                        } else {
                            // 过期日志文件
                        }
                    } else {
                        // 过期日志文件
                    }
                } else {
                    // 过期日志文件
                }

                LazyLogger.i(
                        "删除过期日志文件(超过" + DAY_THRESHOLD + "天" + HOUR_THRESHOLD + "小时" + MINUTE_THRESHOLD + "分钟)：" + file.getAbsolutePath());

                file.delete();
            }
        } catch (Exception exception) {
            LazyLogger.e("删除过期日志文件错误！" + " " + exception);
        }
    }

    // 查找指定目录下指定后缀的所有文件
    public static void traverseFindFiles(String path, String[] fileSuffixes, ArrayList<File> files) {
        File dir = new File(path);
        if (!dir.exists()) {
            return; // 路径不存在
        }

        File[] subFiles = dir.listFiles();
        if (subFiles == null || subFiles.length == 0) {
            return; // 空文件夹
        }

        for (File subFile : subFiles) {
            if (subFile.isFile()) {
                String subFileName = subFile.getName();
                String subFileSuffix = subFileName.substring(subFileName.lastIndexOf(".") + 1);
                for (String fileSuffix : fileSuffixes) {
                    if (fileSuffix.equalsIgnoreCase(subFileSuffix)) {
                        files.add(subFile);
                    }
                }
            } else {
                traverseFindFiles(subFile.getAbsolutePath(), fileSuffixes, files);
            }
        }
    }
}
