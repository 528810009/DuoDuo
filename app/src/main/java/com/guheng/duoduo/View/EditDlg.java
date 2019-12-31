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

public class EditDlg {
    private AlertDialog mAlertDialog; // 弹出式对话框
    private Handler mHandler;

    private EditText mEtDate;
    private Spinner mSpAction;

    public EditDlg(final Context context, Handler handler, final int position, EntityItem entityItem) {
        this.mHandler = handler;

        View view = LayoutInflater.from(context).inflate(R.layout.edit_dlg, null, false);

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
                message.what = MainActivity.MSG_CODE_EDIT_BTN_OK;

                Bundle bundle = new Bundle();
                bundle.putSerializable(null, new MsgData(position, getRecyclerViewItemEntity()));
                message.setData(bundle);

                EditDlg.this.mHandler.sendMessage(message);

                Toast.makeText(context, "编辑成功！", android.widget.Toast.LENGTH_SHORT).show();

                dismiss();
            }
        });

        mEtDate.setText(entityItem.getDate());
        mSpAction.setSelection(getSpinnerIndex(context, entityItem.getSummarized()));

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

    public int getSpinnerIndex(Context context, String s) {
        int index = -1;

        CharSequence[] items = context.getResources().getStringArray(R.array.actions);
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(s)) {
                index = i;
                break;
            }
        }

        return index;
    }

    public EntityItem getRecyclerViewItemEntity() {
        return new EntityItem(mEtDate.getText().toString(), mSpAction.getSelectedItem().toString());
    }
}
