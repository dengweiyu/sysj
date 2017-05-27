package com.li.videoapplication.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.GroupType210Entity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.data.model.response.UserProfileUploadAvatarEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.BitmapLoader;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.PhotoHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.MyPersonalInfoAdapter;
import com.li.videoapplication.ui.dialog.GetMoreMoneyDialog;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.HorizontalListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动：个人资料
 */
public class MyPersonalInfoActivity extends TBaseActivity implements OnClickListener {


    /**
     * 跳转：登录
     */
    public void startLoginActivity() {
        ActivityManager.startLoginActivity(this);
    }

    /**
     * 跳转：历史战绩
     */
    public void startMatchRecordActivity() {
        ActivityManager.startMatchRecordActivity(this);
    }

    private PhotoHelper photoHelper = new PhotoHelper();

    /**
     * 跳转：照相
     */
    public void takePhoto() {
        photoHelper.takePhoto(this);
    }

    /**
     * 跳转：相册
     */
    public void pickPhoto() {
        photoHelper.pickPhoto(this);
    }

    private CircleImageView head;
    private ImageView isV;
    private ImageView mVip;
    private TextView horizonId, name;
    private TextView gender;
    private TextView introduce;
    private TextView qq, mobile, beanNum, matchRecord;

    private Member member = getUser();

    private List<GroupType> groupTypes = new ArrayList<>();

    private MyPersonalInfoAdapter adapter;
    private HorizontalListView mHorizontalListView;
    private List<GroupType> data = new ArrayList<GroupType>();

    @Override
    public void refreshIntent() {
        super.refreshIntent();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mypersonalinfo;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.mypersonalinfo_title);
    }

    @Override
    public void initView() {
        super.initView();
        initContentView();
        refreshContentView(member);
        refreshListView(member);
    }

    @Override
    public void loadData() {
        super.loadData();

        if (isLogin()) {
            // 个人资料
            DataManager.userProfilePersonalInformation(getMember_id(), getMember_id());

            UITask.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 圈子类型
                    DataManager.groupType217();
                }
            }, 400);
        }
    }

    private void initContentView() {

        head = (CircleImageView) findViewById(R.id.mypersonnalinfo_head);
        isV = (ImageView) findViewById(R.id.mypersonnalinfo_isv);
        mVip = (ImageView)findViewById(R.id.iv_person_info_vip);
        horizonId = (TextView) findViewById(R.id.mypersonnalinfo_horizonid);
        name = (TextView) findViewById(R.id.mypersonnalinfo_name);
        gender = (TextView) findViewById(R.id.mypersonnalinfo_gender);
        introduce = (TextView) findViewById(R.id.mypersonnalinfo_introduce);
        beanNum = (TextView) findViewById(R.id.mypersonnalinfo_bean_num);
        matchRecord = (TextView) findViewById(R.id.mypersonnalinfo_matchrecord);
        qq = (TextView) findViewById(R.id.mypersonnalinfo_qq);
        mobile = (TextView) findViewById(R.id.mypersonnalinfo_mobile);

        findViewById(R.id.mypersonnalinfo_head_btn).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_name_btn).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_sex_btn).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_introduce_btn).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_mobile_btn).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_logout_btn).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_matchrecord_btn).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_gameedit).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_qq_btn).setOnClickListener(this);
        findViewById(R.id.mypersonnalinfo_bean_btn).setOnClickListener(this);
        findViewById(R.id.ll_personal_info_vip_center).setOnClickListener(this);

        findViewById(R.id.ll_mypersonnalinfo_horizonid).setOnLongClickListener(mLongClickListener);

        mHorizontalListView = (HorizontalListView) findViewById(R.id.horizontallistvierw);
        adapter = new MyPersonalInfoAdapter(this, data);
        mHorizontalListView.setAdapter(adapter);
    }

    /**
     * long click to copy member id
     */
    final View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.ll_mypersonnalinfo_horizonid){
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("Label",member.getHorizonId());
                manager.setPrimaryClip(data);
                ToastHelper.l("视界ID已复制");
            }
            return true;
        }
    };


    final View.OnClickListener mChoiceListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
               case  R.id.tv_confirm_dialog_no:         //充值
                   ActivityManager.startTopUpActivity(MyPersonalInfoActivity.this, Constant.TOPUP_ENTRY_INFO,0);

                   break;
               case  R.id.tv_confirm_dialog_yes:        //做任务
                   ActivityManager.startMyWalletActivity(MyPersonalInfoActivity.this);
                   break;
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mypersonnalinfo_sex_btn://性别
                DialogManager.showEditSexDialog(this);
                break;
            case R.id.mypersonnalinfo_name_btn://昵称
                ActivityManager.startPersonalInfoEditActivity(this, PersonalInfoEditActivity.NAME);
                break;
            case R.id.mypersonnalinfo_introduce_btn://个性签名
                ActivityManager.startPersonalInfoEditActivity(this, PersonalInfoEditActivity.SIGNATURE);
                break;
            case R.id.mypersonnalinfo_bean_btn:
                new GetMoreMoneyDialog(this,"想获得更多飞磨豆吗？可通过做任务或充值获得哟！",mChoiceListener).show();
                break;
            case R.id.mypersonnalinfo_gameedit://游戏类型
                ActivityManager.startPersonalInfoEditActivity(this, PersonalInfoEditActivity.GAME);
                break;
            case R.id.mypersonnalinfo_matchrecord_btn://赛事战绩
                startMatchRecordActivity();
                break;
            case R.id.mypersonnalinfo_qq_btn://QQ
                DialogManager.showEditQQDialog(this);
                break;
            case R.id.mypersonnalinfo_mobile_btn://手机
                DialogManager.showRegisterMobileDialog(this);
                break;

            case R.id.mypersonnalinfo_head_btn://头像
                DialogManager.showPhotoDialog(this, this, this);
                break;

            case R.id.photo_pick://选择图片
                pickPhoto();
                break;

            case R.id.photo_take://拍照
                takePhoto();
                break;

            case  R.id.ll_personal_info_vip_center: //会员中心
                ActivityManager.startTopUpActivity(this,Constant.TOPUP_ENTRY_INFO,1);
                break;

            case R.id.mypersonnalinfo_logout_btn://注销
                AppAccount.logout();
                ToastHelper.s(R.string.logout_success);
                FeiMoIMHelper.LogOut(this, true);
                finish();
                break;

            default:
                break;
        }
    }

    private void refreshContentView(Member item) {
        if (item != null) {
            setImageViewImageNet(head, item.getAvatar());
            setTextViewText(horizonId, item.getHorizonId());
            setTextViewText(name, item.getNickname());

            if (item.isV()) {
                isV.setVisibility(View.VISIBLE);
            } else {
                isV.setVisibility(View.INVISIBLE);
                if (item.getVipInfo() != null && !StringUtil.isNull(item.getVipInfo().getLevel())){
                    switch (item.getVipInfo().getLevel()){
                        case "1":
                            mVip.setImageResource(R.drawable.vip_level_1_icon);
                            mVip.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            mVip.setImageResource(R.drawable.vip_level_2_icon);
                            mVip.setVisibility(View.VISIBLE);
                            break;
                        case "3":
                            mVip.setImageResource(R.drawable.vip_level_3_icon);
                            mVip.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            if (StringUtil.isNull(item.getSignature())) {
                setTextViewText(introduce, "");
            } else {
                setTextViewText(introduce, item.getSignature());
            }

            setTextViewText(qq, item.getQq());
            setTextViewText(beanNum, StringUtil.formatNum(item.getCurrency()));
            setTextViewText(mobile, item.getMobile());
            //红色：#ff3d2e，蓝色：#48c5ff
            String record = TextUtil.toColor(item.getWin(), "#ff3d2e")
                    + " / " + TextUtil.toColor(item.getFailure(), "#48c5ff");
            matchRecord.setText(Html.fromHtml(record));

            if (item.getSex() == 1) {
                gender.setText("男");
            } else {
                gender.setText("女");
            }
        }
    }

    /**
     * 刷新列表
     */
    private void refreshListView(Member item) {
        data.clear();
        if (groupTypes != null && groupTypes.size() > 0
                && item.getLikeGroupType() != null && item.getLikeGroupType().size() > 0) {

            for (String id : item.getLikeGroupType()) {
                for (GroupType groupType : groupTypes) {
                    if (id.equals(groupType.getGroup_type_id())) {
                        data.add(groupType);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 回调：相册，照相
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(tag, "requestCode=" + requestCode + "/resultCode=" + resultCode + "/data=" + data);

        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PhotoHelper.REQUESTCODE_PHOTO_TAKE) {
            String path = photoHelper.getPath();
            if (path != null)
                updatePhoto(path);
        }
        if (requestCode == PhotoHelper.REQUESTCODE_PHOTO_PICK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    if (null == cursor || !cursor.moveToFirst()) {
                        showToastShort("没有找到您想要的图片");
                        return;
                    }
                    int columnIndex = cursor.getColumnIndex("_data");
                    String path = cursor.getString(columnIndex);
                    if (path != null) {
                        File file = new File(path);
                        if (!file.exists()) {
                            showToastShort("没有找到您想要的图片");
                            return;
                        }
                    }
                    cursor.close();
                    updatePhoto(path);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updatePhoto(String originPath) {
        String newPath = processBitmap(originPath);
        File file = new File(newPath);
        setImageViewImageLocal(head, originPath);
        // 上传头像
        DataManager.userProfileUploadAvatar(getMember_id(), file);
        showProgressDialog(LoadingDialog.LOADING);
    }

    public static final int w = 600;
    public static final int h = 600;

    // 压缩图片
    private String processBitmap(String originPath) {

        File file = new File(originPath);
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        // 图片允许最大空间 单位：KB（因为实际读取的原文件要大，所以设置值要为15K，不是20K)
        double maxSize = 100.00;
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）

            double newWidth = bitmap.getWidth() / Math.sqrt(i);
            double newHeight = bitmap.getHeight() / Math.sqrt(i);

            // 获取这个图片的宽和高
            float width = bitmap.getWidth();
            float height = bitmap.getHeight();
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 缩放图片动作
            matrix.postScale(scaleWidth, scaleHeight);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
        }
        String tempPath = BitmapLoader.saveBitmapToLocal(bitmap);
        return tempPath;
    }

    /**
     * 回调：个人资料
     */
    public void onEventMainThread(UserProfilePersonalInformationEntity event) {
        if (event != null && event.isResult()) {
            member = event.getData();
            if (member != null) {
                refreshContentView(member);
                refreshListView(member);
            }
        }
    }

    /**
     * 回调：圈子類型
     */
    public void onEventMainThread(GroupType210Entity event) {
        if (event != null && event.isResult()) {
            if (event.getData().size() > 0) {
                groupTypes.clear();
                groupTypes.addAll(event.getData());
                refreshListView(member);
                if (isChangeAvatar) {
                    FeiMoIMHelper.upDateUser(member.getNickname(), member.getAvatar());
                    isChangeAvatar = false;
                }
            }
        }
    }

    private boolean isChangeAvatar;

    /**
     * 回调：上传头像
     */
    public void onEventMainThread(UserProfileUploadAvatarEntity event) {
        dismissProgressDialog();
        if (event != null && event.isResult()) {
            showToastShort("头像上传成功");
            isChangeAvatar = true;
        }
    }


}
