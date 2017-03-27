package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.BaseDialog;

public class DeleteApkDialog extends BaseDialog implements View.OnClickListener{
    private View.OnClickListener deleteListener;
    private View.OnClickListener cancelLister;

    private TextView cancel;

    public DeleteApkDialog(Context context, View.OnClickListener deleteListener, View.OnClickListener cancelLister){
        super(context);

        this.deleteListener = deleteListener;
        this.cancelLister = cancelLister;

        TextView delete = (TextView) findViewById(R.id.delete_apk_delete);
        cancel = (TextView)findViewById(R.id.delete_apk_cancel);

        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_delete_apk;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_apk_delete:
                deleteListener.onClick(v);
                dismiss();
                break;
            case R.id.delete_apk_cancel:
                cancelLister.onClick(v);
                dismiss();
                break;
        }
    }
}
