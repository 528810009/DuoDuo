package com.guheng.duoduo;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.guheng.duoduo.Adapter.RecyclerViewAdapter;
import com.guheng.duoduo.Object.RecyclerViewItemEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView mTvDays;
    private Button mBtnAdd;
    private Button mBtnEdit;
    private Button mBtnDelete;
    private Button mBtnClear;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.mTvDays = findViewById(R.id.tv_days);
        this.mBtnAdd = findViewById(R.id.btn_item_add);
        this.mBtnEdit = findViewById(R.id.btn_item_edit);
        this.mBtnDelete = findViewById(R.id.btn_item_delete);
        this.mBtnClear = findViewById(R.id.btn_item_clear);
        this.mRecyclerView = findViewById(R.id.v_recycler_view);

        // 初始化出生天数
        initDays();

        // 初始化元素操作
        initItemOperator();


        List<RecyclerViewItemEntity> itemEntityList = null;

        // 测试数据
        itemEntityList = new ArrayList<>();
        Date dateNow = new Date();
        RecyclerViewItemEntity entity = null;
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗A");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗B");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗C");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗D");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗E");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗F");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗A");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗B");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗C");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗D");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗E");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗F");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗A");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗B");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗C");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗D");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗E");
        itemEntityList.add(entity);
        entity = new RecyclerViewItemEntity(dateNow.getTime(), "逗F");
        itemEntityList.add(entity);

        // 初始化RecyclerView
        initRecyclerView(MainActivity.this, itemEntityList);
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

            this.mTvDays.setText("宝宝出生：" + days + "天");
        } catch (Exception exception) {

        }
    }

    /*
     * 初始化元素操作
     */
    private void initItemOperator() {
        final RecyclerView recyclerView = this.mRecyclerView;

        this.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                if (recyclerViewAdapter == null) {
                    return;
                }

                int position = recyclerViewAdapter.getSelectedPosition();
                if (position == -1) {
                    Toast.makeText(MainActivity.this, "请选择要删除的行数据！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (recyclerViewAdapter.remove(position) == null) {
                    Toast.makeText(MainActivity.this, "删除选择行数据失败！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "删除选择行数据成功！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                if (recyclerViewAdapter == null) {
                    return;
                }

                recyclerViewAdapter.clear();

                Toast.makeText(MainActivity.this, "清空所有行数据成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * 初始化RecyclerView
     */
    private void initRecyclerView(Context context, List<RecyclerViewItemEntity> recyclerViewItemEntityList) {
        // 定义一个线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(context);
        // 设置布局管理器
        this.mRecyclerView.setLayoutManager(manager);
        // 设置adapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, recyclerViewItemEntityList);
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
}
