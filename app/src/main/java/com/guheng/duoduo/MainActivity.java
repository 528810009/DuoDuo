package com.guheng.duoduo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.guheng.duoduo.Adapter.RecyclerViewAdapter;
import com.guheng.duoduo.Interface.IExceptionHandler;
import com.guheng.duoduo.Object.EntityItem;
import com.guheng.duoduo.Object.EntityList;
import com.guheng.duoduo.Object.MsgData;
import com.guheng.duoduo.Tool.ExceptionHandler;
import com.guheng.duoduo.Tool.Logs;
import com.guheng.duoduo.Tool.Paths;
import com.guheng.duoduo.View.AddDlg;
import com.guheng.duoduo.View.EditDlg;
import com.mic.etoast2.Toast;
import com.robin.lazy.logger.LazyLogger;

import org.apache.log4j.chainsaw.Main;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /**
     * 需要进行检测的权限集合
     * Key：权限
     * Value：说明
     */
    private static final HashMap<String, String> mNeedPermissionsMap = new HashMap<String, String>() {
        {
            put(Manifest.permission.READ_EXTERNAL_STORAGE, "读取存储");
            put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入存储");
        }
    };

    /*
     * 消息类型码
     */
    public final static int MSG_CODE_ADD_BTN_OK = 2;
    public final static int MSG_CODE_EDIT_BTN_OK = 4;

    /*
     * 请求权限码
     */
    public static final int REQUEST_PERMISSION_CODE = 1;

    private TextView mTvDays;
    private RecyclerView mRecyclerView;

    private EntityList mEntityList;

    private ButtonActionHandler mButtonActionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 初始化自定义异常处理
        new ExceptionHandler(MainActivity.this, new IExceptionHandler() {
            @Override
            public void exit() {

            }
        });

        // 检测获取权限
        checkPermissions();

        // 初始化写日志和处理异常功能
        new Logs(Paths.mLogPath);

        mButtonActionHandler = new ButtonActionHandler(new WeakReference<>(MainActivity.this));

        // 初始化界面控件
        initWidgets();

        // 初始化出生天数
        initDays();

        this.mEntityList = new EntityList();

        // 初始化RecyclerView
        initRecyclerView(MainActivity.this, this.mEntityList);
    }

    // 重新启动Activity时调用，总是在onStart方法以后执行    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // 当一个Activity变为显示时被调用
    @Override
    protected void onStart() {
        super.onStart();
    }

    // 暂停Activity时被调用
    @Override
    protected void onPause() {
        super.onPause();
    }

    // 当Activity由暂停变为活动状态时调用
    @Override
    protected void onResume() {
        super.onResume();
    }

    // 停止Activity时被调用
    @Override
    protected void onStop() {
        super.onStop();
    }

    // 销毁Activity时被调用
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 检测获取权限
    private void checkPermissions() {
        List<String> requestPermissionsList = new ArrayList<>();

        for (Map.Entry<String, String> entry : mNeedPermissionsMap.entrySet()) {
            String permission = entry.getKey();

            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 没有授权的权限
                requestPermissionsList.add(permission);
            }
        }

        if (!requestPermissionsList.isEmpty()) {
            // 申请未授权的权限
            ActivityCompat.requestPermissions(MainActivity.this, requestPermissionsList.toArray(new String[0]), REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults数组存储的申请的返回结果，
                    // PERMISSION_GRANTED 表示申请成功
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // 授权成功
                    } else {
                        // 授权失败

                        String permissionText = mNeedPermissionsMap.get(permissions[i]);
                        LazyLogger.e("APP权限授权失败：" + permissions[i] + "(" + permissionText + ")");

                        // 提示用户获取权限失败，程序退出
                        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
                        normalDialog.setTitle("警告");
                        normalDialog.setMessage("APP获取" + permissionText + "权限失败，程序即将退出！");
                        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        normalDialog.setCancelable(false); // 点击对话框外不消失
                        normalDialog.show();
                    }
                }
                break;
            default:
                break;
        }
    }

    /*
     * 初始化界面控件
     */
    private void initWidgets() {
        this.mTvDays = findViewById(R.id.tv_days);
        Button btnAdd = findViewById(R.id.btn_item_add);
        Button btnEdit = findViewById(R.id.btn_item_edit);
        Button btnDelete = findViewById(R.id.btn_item_delete);
        Button btnClear = findViewById(R.id.btn_item_clear);
        this.mRecyclerView = findViewById(R.id.v_recycler_view);

        final RecyclerView recyclerView = this.mRecyclerView;

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDlg addDlg = new AddDlg(MainActivity.this, MainActivity.this.mButtonActionHandler);
                addDlg.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                if (recyclerViewAdapter == null) {
                    return;
                }

                int position = recyclerViewAdapter.getSelectedPosition();
                if (position == -1) {
                    Toast.makeText(MainActivity.this, "请选择要编辑的数据！", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                EditDlg editDlg = new EditDlg(MainActivity.this, MainActivity.this.mButtonActionHandler, position, MainActivity.this.mEntityList.get(position));
                editDlg.show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                if (recyclerViewAdapter == null) {
                    return;
                }

                int position = recyclerViewAdapter.getSelectedPosition();
                if (position == -1) {
                    Toast.makeText(MainActivity.this, "请选择要删除的数据！", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                EntityItem entity = MainActivity.this.mEntityList.remove(position);
                if (entity == null) {
                    Toast.makeText(MainActivity.this, "删除失败！", android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    recyclerViewAdapter.setSelectedPosition(-1);
                    Toast.makeText(MainActivity.this, "删除成功！", android.widget.Toast.LENGTH_SHORT).show();
                }

                recyclerViewAdapter.notifyDataSetChanged();

                MainActivity.this.mEntityList.save();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                if (recyclerViewAdapter == null) {
                    return;
                }

                MainActivity.this.mEntityList.clear();

                Toast.makeText(MainActivity.this, "清空成功！", android.widget.Toast.LENGTH_SHORT).show();

                recyclerViewAdapter.notifyDataSetChanged();

                MainActivity.this.mEntityList.save();
            }
        });
    }

    /*
     * 初始化出生天数
     */
    private void initDays() {
        try {
            Date fromDate = stringToDate("2019-09-11 00:00:00", "yyyy-MM-dd HH:mm:ss");

            //获取当前时间
            Date endDate = new Date(System.currentTimeMillis());

            long days = getTimeDistance(fromDate, endDate);

            String s = "宝宝出生：" + days + "天";
            this.mTvDays.setText(s);
        } catch (Exception exception) {

        }
    }

    /*
     * 初始化RecyclerView
     */
    private void initRecyclerView(Context context, EntityList entityList) {
        // 定义一个线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(context);
        // 设置布局管理器
        this.mRecyclerView.setLayoutManager(manager);
        // 设置adapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, entityList);
        this.mRecyclerView.setAdapter(adapter);
//        // 添加分割线
//        this.mRecyclerView.addItemDecoration(new LinearItemDecoration(context, LinearLayoutManager.VERTICAL));

        // 设置监听
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    // strTime要转换的string类型的时间，formatType要转换的格式
    // yyyy-MM-dd HH:mm:ss
    // yyyy年MM月dd日 HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * 获得两个日期间距多少天
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getTimeDistance(Date beginDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(beginDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        fromCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        fromCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        fromCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        toCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        toCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        toCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        double DAY = 24 * 60 * 60 * 1000;
        double dDayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / DAY;
        long lDayDistance = Math.abs(Double.valueOf(Math.ceil(dDayDistance)).longValue()) + 1;

        return lDayDistance;
    }

    private static class ButtonActionHandler extends Handler {
        private final MainActivity mMainActivity;

        private ButtonActionHandler(WeakReference<MainActivity> wrMainActivity) {
            this.mMainActivity = wrMainActivity.get();
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            Bundle bundle = message.getData();
            MsgData msgData = (MsgData) bundle.getSerializable(null);
            if (msgData == null) {
                return;
            }

            RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) this.mMainActivity.mRecyclerView.getAdapter();
            if (recyclerViewAdapter == null) {
                return;
            }

            switch (message.what) {
                case MSG_CODE_ADD_BTN_OK:
                    this.mMainActivity.mEntityList.add(msgData.getRecyclerViewItemEntity());
                    recyclerViewAdapter.notifyDataSetChanged();
                    this.mMainActivity.mEntityList.save();
                    break;
                case MSG_CODE_EDIT_BTN_OK:
                    this.mMainActivity.mEntityList.set(msgData.getIndex(), msgData.getRecyclerViewItemEntity());
                    recyclerViewAdapter.notifyDataSetChanged();
                    this.mMainActivity.mEntityList.save();
                    break;
                default:
                    break;
            }
        }
    }
}
