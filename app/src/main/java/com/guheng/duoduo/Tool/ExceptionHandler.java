package com.guheng.duoduo.Tool;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.guheng.duoduo.Interface.IExceptionHandler;
import com.guheng.duoduo.MainActivity;
import com.mic.etoast2.Toast;
import com.robin.lazy.logger.LazyLogger;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler; // 系统默认的异常处理器
    private AppCompatActivity mAppCompatActivity;
    private IExceptionHandler mIExceptionHandler;

    public ExceptionHandler(AppCompatActivity appCompatActivity, IExceptionHandler iExceptionHandler) {
        this.mAppCompatActivity = appCompatActivity;
        this.mIExceptionHandler = iExceptionHandler;

        // 获取系统默认的异常处理器
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置自定义的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        LazyLogger.e(throwable, "程序异常退出！");

        if (!handleException(throwable) && mDefaultUncaughtExceptionHandler != null) {
            // 如果用户没有处理，则让系统默认的异常处理器来处理
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        } else {
            try {
                Thread.sleep(1000);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (this.mIExceptionHandler != null) {
                this.mIExceptionHandler.exit();
            }

            // 恢复系统默认的UncaughtException处理器
            Thread.setDefaultUncaughtExceptionHandler(mDefaultUncaughtExceptionHandler);

            this.mAppCompatActivity.finish();
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param throwable
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable throwable) {
        final AppCompatActivity appCompatActivity = this.mAppCompatActivity;
        // 显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(appCompatActivity, "很抱歉，程序出现异常，即将退出！", android.widget.Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        return true;
    }
}
