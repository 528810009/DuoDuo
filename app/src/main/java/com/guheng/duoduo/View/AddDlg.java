package com.guheng.duoduo.View;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.guheng.duoduo.MainActivity;
import com.guheng.duoduo.Object.EntityItem;
import com.guheng.duoduo.Object.MsgData;
import com.guheng.duoduo.R;
import com.mic.etoast2.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddDlg {
    private AlertDialog mAlertDialog; // 弹出式对话框
    private Handler mHandler;

    private EditText mEtDate;
    private Spinner mSpAction;

    public AddDlg(final Context context, Handler handler) {
        this.mHandler = handler;

        View view = LayoutInflater.from(context).inflate(R.layout.add_dlg, null, false);

        mEtDate = view.findViewById(R.id.et_date); // 日期
        mSpAction = view.findViewById(R.id.sp_action); // 事件
        Button btnCancel = view.findViewById(R.id.btn_cancel); // 取消
        Button btnSave = view.findViewById(R.id.btn_ok); // 保存

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = MainActivity.MSG_CODE_ADD_BTN_OK;

                Bundle bundle = new Bundle();
                bundle.putSerializable(null, new MsgData(-1, getRecyclerViewItemEntity()));
                message.setData(bundle);

                AddDlg.this.mHandler.sendMessage(message);

                Toast.makeText(context, "添加成功！", android.widget.Toast.LENGTH_SHORT).show();

                dismiss();
            }
        });

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        mEtDate.setText(date);

        this.mAlertDialog = new AlertDialog.Builder(context).create();
        this.mAlertDialog.setView(view);
        this.mAlertDialog.setCancelable(false); // 点击对话框外地方是否不消失
    }

    public void show() {
        this.mAlertDialog.show();
    }

    public void dismiss() {
        this.mAlertDialog.dismiss();
    }

    public EntityItem getRecyclerViewItemEntity() {
        return new EntityItem(mEtDate.getText().toString(), mSpAction.getSelectedItem().toString());
    }
}
