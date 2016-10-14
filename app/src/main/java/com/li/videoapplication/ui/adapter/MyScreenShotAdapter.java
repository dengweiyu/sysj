package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.local.ScreenShotHelper;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.dialog.VideoManagerRenameDialog;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 适配器：截图
 */
@SuppressLint("SimpleDateFormat")
public class MyScreenShotAdapter extends BaseAdapter {

    /**
     * 跳转：分享
     */
    private void startShareActivity(final ScreenShotEntity record) {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        ActivityManeger.startActivityShareActivity4MyScreenShot(activity, record.getPath(), record.getDisplayName());
    }

    private List<ScreenShotEntity> data;
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder holder;

    // 用来保存批量删除checkbox的勾选状态值，防止划屏时错位
    private List<Boolean> checkData = new ArrayList<>();

    /**
     * 选择要分享的文件列表
     */
    public List<Boolean> shareData = new ArrayList<>();

    public MyScreenShotAdapter(Context context, List<ScreenShotEntity> data) {
        this.data = data;
        this.context = context;

        inflater = LayoutInflater.from(context);

        checkData.clear();
        for (int i = 0; i < data.size(); i++) {
            checkData.add(false);
        }

        VideoMangerActivity.myScreenShotDeleteData.clear();
        for (int i = 0; i < data.size(); i++) {
            VideoMangerActivity.myScreenShotDeleteData.add(false);
        }

        shareData.clear();
        for (int i = 0; i < data.size(); i++) {
            shareData.add(false);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ScreenShotEntity getItem(int postion) {
        return data.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ScreenShotEntity record = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_myscreenshot, null);
            holder.root = convertView.findViewById(R.id.root);
            holder.cover = (ImageView) convertView.findViewById(R.id.myscreenshot_cover);
            holder.title = (TextView) convertView.findViewById(R.id.myscreenshot_title);
            holder.createTime = (TextView) convertView.findViewById(R.id.myscreenshot_createTime);
            holder.share = (ImageView) convertView.findViewById(R.id.myscreenshot_share);
            holder.delete = (ImageView) convertView.findViewById(R.id.myscreenshot_delete);
            holder.deleteState = (CheckBox) convertView.findViewById(R.id.myscreenshot_deleteState);
            holder.deleteButton = (TextView) convertView.findViewById(R.id.myscreenshot_deleteButton);
            holder.rename = convertView.findViewById(R.id.myscreenshot_rename);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoaderHelper.displayImageWhite4Local(record.getPath(), holder.cover);

        holder.title.setText(record.getDisplayName());

        setCreateTime(record, holder.createTime);
        // setSize(record, holder.size);

        // 如果处于批量删除状态
        if (VideoMangerActivity.isDeleteMode) {
            inDeleteState(holder, position, record);
        } else {
            outDeleteState(holder, position, record);
        }

        // 图片浏览
        holder.cover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                IntentHelper.startActivityActionViewImage(context, record);
                UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "截图-有效");
            }
        });

        // 重命名截图监听
        holder.rename.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showRenameDialog(record, holder);
            }
        });

        // 分享监听
        holder.share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startShareActivity(record);
            }
        });

        // 删除监听
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (VideoMangerActivity.myScreenShotDeleteData.get(position)) {
                    VideoMangerActivity.myScreenShotDeleteData.set(position, false);
                    VideoMangerActivity.removeMyScreenData(data.get(position));
                    VideoMangerActivity.refreshAbTitle2();
                } else {
                    VideoMangerActivity.myScreenShotDeleteData.set(position, true);
                    VideoMangerActivity.addMyScreenData(data.get(position));
                    VideoMangerActivity.refreshAbTitle2();
                }
                notifyDataSetChanged();
            }
        });

        // 长按选择
        holder.root.setId(-1);
        holder.root.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                v.setId(position);
                VideoMangerActivity.performClick2();
                return false;
            }
        });

        return convertView;
    }

    /**
     * 时间
     */
    private void setCreateTime(final ScreenShotEntity record, TextView view) {
        String s;
        try {
            s = "截图于 " + getFileLastModified(record.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            s = "";
        }
        view.setText(s);
    }

    /**
     * 大小
     */
    private void setSize(final ScreenShotEntity record, TextView view) {
        // 18M
        String s;
        try {
            s = convertFileSize(getFileSize((record.getPath())));
        } catch (Exception e) {
            e.printStackTrace();
            s = "";
        }
        view.setText(s);
    }

    /**
     * 重命名弹框
     */
    private void showRenameDialog(final ScreenShotEntity record, final ViewHolder viewHolder) {

        final String oldFileName = record.getDisplayName();
        final File oldFile = new File(record.getPath());

        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity == null)
            return;

        DialogManager.showVideoManagerRenameDialog(
                oldFileName,
                new VideoManagerRenameDialog.Callback() {

                    @Override
                    public void onCall(DialogInterface dialog, String newFileName) {
                        if (newFileName.isEmpty()) {
                            ToastHelper.s("文件名不能为空");
                            return;
                        }
                        dialog.dismiss();
                        ScreenShotHelper.renameScreenShot(oldFile, oldFileName, newFileName);
                        viewHolder.title.setText(newFileName);
                    }
                });
    }

    /**
     * 文件创建时间
     */
    private String getFileLastModified(String filePath) {
        File f = new File(filePath);
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    /**
     * 处于批量删除状态
     */
    private void inDeleteState(final ViewHolder holder, final int position, final ScreenShotEntity record) {
        holder.deleteState.setVisibility(View.VISIBLE);
        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.deleteButton.setId(position);
        // 如果是通过长按召唤出的，将长按的控件设置为选中
        if (holder.root.getId() != -1) {
            VideoMangerActivity.myScreenShotDeleteData.set(position, true);
            VideoMangerActivity.addMyScreenData(record);
            VideoMangerActivity.refreshAbTitle2();
            holder.root.setId(-1);
        }
        holder.deleteState.setChecked(VideoMangerActivity.myScreenShotDeleteData.get(position));
    }

    /**
     * 处于正常状态
     */
    private void outDeleteState(final ViewHolder holder, final int position, final ScreenShotEntity record) {

        holder.deleteState.setVisibility(View.GONE);
        holder.deleteButton.setVisibility(View.GONE);
        holder.cover.setClickable(true);

        // 重新赋值checkbox状态
        VideoMangerActivity.myScreenShotDeleteData.clear();
        for (int i = 0; i < data.size(); i++) {
            VideoMangerActivity.myScreenShotDeleteData.add(false);
        }
        shareData.clear();
        for (int i = 0; i < data.size(); i++) {
            shareData.add(false);
        }
    }

    /**
     * 获得文件大小（字节)
     */
    public long getFileSize(String f) {
        File file = new File(f);
        long s = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                s = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 将文件大小（字节）转为M,K,B
     */
    public static String convertFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#");
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    private static class ViewHolder {
        View root;
        // 图片封面
        ImageView cover;
        // 图片标题
        TextView title;
        // 截图时间
        TextView createTime;
        // 批量删除勾选框
        TextView deleteButton;
        // 显示勾选状态的图片
        CheckBox deleteState;
        // 重命名
        View rename;
        // 分享按钮
        ImageView share;
        // 删除按钮
        ImageView delete;
    }
}
