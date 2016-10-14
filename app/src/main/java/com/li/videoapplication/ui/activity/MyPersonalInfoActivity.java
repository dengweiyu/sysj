package com.li.videoapplication.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.BaseInfoEntity;
import com.li.videoapplication.data.model.response.GroupType210Entity;
import com.li.videoapplication.data.model.response.UserProfileFinishMemberInfoEntity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.data.model.response.UserProfileUploadAvatarEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.BitmapLoader;
import com.li.videoapplication.tools.PhotoHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.MyPersonalInfoAdapter;
import com.li.videoapplication.ui.dialog.EditNameDialog;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.dialog.RegisterMobileDialog;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.StringUtil;
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
public class MyPersonalInfoActivity extends TBaseActivity implements OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        RadioGroup.OnCheckedChangeListener {


    /**
     * 跳转：登录
     */
    public void startLoginActivity() {
        ActivityManeger.startLoginActivity(this);
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
    private RelativeLayout headBtn, nameBtn, mobileBtn;
    private TextView name;;
    private TextView gender;
    private RadioGroup genderRadio;
    private RadioButton female;
    private RadioButton male;
    private TextView introduce;
    private EditText introduceEdit;
    private TextView qq, mobile;
    private EditText qqEdit;
    private CheckBox publicCheck;
    private RelativeLayout logoutBtn;

    private Member member = getUser();

    private Member newMember = new Member();

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
        Log.d(tag, "initView: refreshContentView");
        refreshContentView(member);
        refreshListView(member);
        setContentNormal();
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
                    DataManager.groupType210();
                }
            }, 400);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //一次性提示对话框：经验转视豆
        DialogManager.showChangeExp2ShidouDialog_Center(this);
    }

    private void initContentView() {

        head = (CircleImageView) findViewById(R.id.mypersonnalinfo_head);
        headBtn = (RelativeLayout) findViewById(R.id.mypersonnalinfo_head_btn);
        name = (TextView) findViewById(R.id.mypersonnalinfo_name);
        nameBtn = (RelativeLayout) findViewById(R.id.mypersonnalinfo_name_btn);
        gender = (TextView) findViewById(R.id.mypersonnalinfo_gender);
        genderRadio = (RadioGroup) findViewById(R.id.mypersonnalinfo_gender_radio);
        female = (RadioButton) findViewById(R.id.mypersonnalinfo_female);
        male = (RadioButton) findViewById(R.id.mypersonnalinfo_male);
        introduce = (TextView) findViewById(R.id.mypersonnalinfo_introduce);
        introduceEdit = (EditText) findViewById(R.id.mypersonnalinfo_introduce_edit);
        qq = (TextView) findViewById(R.id.mypersonnalinfo_qq);
        qqEdit = (EditText) findViewById(R.id.mypersonnalinfo_qq_edit);
        mobile = (TextView) findViewById(R.id.mypersonnalinfo_mobile);
        mobileBtn = (RelativeLayout) findViewById(R.id.mypersonnalinfo_mobile_btn);

        publicCheck = (CheckBox) findViewById(R.id.mypersonnalinfo_public);
        logoutBtn = (RelativeLayout) findViewById(R.id.mypersonnalinfo_logout_btn);

        genderRadio.setOnCheckedChangeListener(this);
        publicCheck.setOnCheckedChangeListener(this);

        introduceEdit.addTextChangedListener(getTextWatcher(introduceEdit));
        qqEdit.addTextChangedListener(getTextWatcher(qqEdit));

        mobileBtn.setOnClickListener(this);
        headBtn.setOnClickListener(this);
        nameBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        publicCheck.setOnClickListener(this);

        abMyPersonalInfoEdit.setOnClickListener(this);
        abMyPersonalInfoCancel.setOnClickListener(this);
        abMyPersonalInfoSave.setOnClickListener(this);

        mHorizontalListView = (HorizontalListView) findViewById(R.id.horizontallistvierw);
        adapter = new MyPersonalInfoAdapter(this, data);
        mHorizontalListView.setAdapter(adapter);
    }

    private TextWatcher getTextWatcher(final EditText view) {

        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if (view == introduceEdit) {
                    newMember.setSignature(text);
                    setTextViewText(introduce, text);
                } else if (view == qqEdit) {
                    newMember.setQq(text);
                    setTextViewText(qq, text);
                }
            }
        };
    }

    private String getMobileText() {
        if (mobile.getText() != null)
            return mobile.getText().toString();
        return "";
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.mypersonnalinfo_mobile_btn:
                if (adapter.getMode() == MyPersonalInfoAdapter.MODE_EDIT) {
                    DialogManager.showRegisterMobileDialog(this, getMobileText(), new RegisterMobileDialog.MobileCallback() {
                        @Override
                        public void onMobileCallback(DialogInterface dialog, String mobileNum) {
                            dialog.dismiss();
                            newMember.setMobile(mobileNum);
                            setTextViewText(mobile, mobileNum);
                        }
                    });
                }
                break;

            case R.id.mypersonnalinfo_head_btn:
                DialogManager.showPhotoDialog(this, this, this);
                break;

            case R.id.mypersonnalinfo_name_btn:
                if (adapter.getMode() == MyPersonalInfoAdapter.MODE_EDIT) {
                    DialogManager.showEditNameDialog(this, getName(), new EditNameDialog.NameCallback() {
                        @Override
                        public void onNameCallback(DialogInterface dialog, String newName) {
                            dialog.dismiss();
                            newMember.setNickname(newName);
                            setTextViewText(name, newName);
                        }
                    });
                }
                break;

            case R.id.photo_pick:
                pickPhoto();
                break;

            case R.id.photo_take:
                takePhoto();
                break;

            case R.id.ab_mypersonalinfo_edit:
                newMember = (Member) member.clone();
                setContentEdit();
                setListEdit(member);
                break;

            case R.id.ab_mypersonalinfo_cancel:
                newMember = new Member();
                setContentNormal();
                setListNormal(member);
                break;

            case R.id.ab_mypersonalinfo_save:
                //昵称和个性签名 敏感词过滤
                DataManager.baseInfo(getName() + " " + getIntroduce());
                break;

            case R.id.mypersonnalinfo_logout_btn:
                AppAccount.logout();
                startLoginActivity();
                finish();
                break;

            default:
                break;
        }
    }

    private String getName() {
        if (StringUtil.isNull(name.getText().toString())) {
            return "";
        } else {
            return name.getText().toString();
        }
    }

    private String getIntroduce() {
        if (StringUtil.isNull(introduceEdit.getText().toString())) {
            return "";
        } else {
            return introduceEdit.getText().toString();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int display;
        if (isChecked)
            display = 1;
        else
            display = 0;
        // 私密资料
        Member m = new Member();
        m.setId(member.getId());
        m.setSex(111);
        m.setDisplay(display);

        LogHelper.d(tag, "isChecked: " + isChecked);
        LogHelper.d(tag, "display: " + display);
        // 编辑个人资料
        DataManager.userProfileFinishMemberInfo(m);
        member.setDisplay(display);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.mypersonnalinfo_female:
                newMember.setSex(0);
                break;

            case R.id.mypersonnalinfo_male:
                newMember.setSex(1);
                break;

            default:
                break;
        }
    }

    private void refreshContentView(Member item) {

        if (item != null) {
            setImageViewImageNet(head, item.getAvatar());
            setTextViewText(name, item.getNickname());

            if (StringUtil.isNull(item.getSignature())) {
                setTextViewText(introduce, "");
                setTextViewText(introduceEdit, "");
            } else {
                setTextViewText(introduce, item.getSignature());
                setTextViewText(introduceEdit, item.getSignature());
            }

            setTextViewText(qq, item.getQq());
            setTextViewText(qqEdit, item.getQq());

            setTextViewText(mobile, item.getMobile());

            if (item.getSex() == 1) {
                male.setChecked(true);
                gender.setText("男");
            } else {
                female.setChecked(true);
                gender.setText("女");
            }
            setDisplay(member);
        }
    }



    private void setContentNormal() {

        try {
            InputUtil.closeKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        abTitle.setVisibility(View.VISIBLE);
        abGoback.setVisibility(View.VISIBLE);
        abMyPersonalInfoEdit.setVisibility(View.VISIBLE);
        abMyPersonalInfoCancel.setVisibility(View.GONE);
        abMyPersonalInfoSave.setVisibility(View.GONE);

        name.setVisibility(View.VISIBLE);

        gender.setVisibility(View.VISIBLE);
        genderRadio.setVisibility(View.GONE);

        introduce.setVisibility(View.VISIBLE);
        introduceEdit.setVisibility(View.GONE);

        qq.setVisibility(View.VISIBLE);
        qqEdit.setVisibility(View.GONE);

        mobile.setVisibility(View.VISIBLE);

        headBtn.setOnClickListener(null);
        nameBtn.setOnClickListener(null);
        mobileBtn.setOnClickListener(null);

        nameBtn.setClickable(false);
        mobileBtn.setClickable(false);
    }

    private void setContentEdit() {

        abTitle.setVisibility(View.GONE);
        abGoback.setVisibility(View.GONE);
        abMyPersonalInfoEdit.setVisibility(View.GONE);
        abMyPersonalInfoCancel.setVisibility(View.VISIBLE);
        abMyPersonalInfoSave.setVisibility(View.VISIBLE);

        gender.setVisibility(View.GONE);
        genderRadio.setVisibility(View.VISIBLE);

        introduce.setVisibility(View.GONE);
        introduceEdit.setVisibility(View.VISIBLE);

        qq.setVisibility(View.GONE);
        qqEdit.setVisibility(View.VISIBLE);

        mobile.setVisibility(View.VISIBLE);

        headBtn.setOnClickListener(this);
        nameBtn.setOnClickListener(this);
        mobileBtn.setOnClickListener(this);

        nameBtn.setClickable(true);
        mobileBtn.setClickable(true);
    }

    /**
     * 刷新列表
     */
    private void refreshListView(Member item) {
        data.clear();
        if (groupTypes != null && groupTypes.size() > 0 && item.getLikeGroupType() != null && item.getLikeGroupType().size() > 0) {
            for (String id : item.getLikeGroupType()) {
                for (GroupType groupType : groupTypes) {
                    if (id.equals(groupType.getGroup_type_id())) {
                        data.add(groupType);
                    }
                }
            }
        }
        adapter.setMode(MyPersonalInfoAdapter.MODE_NORMAL);
        adapter.notifyDataSetChanged();
    }

    /**
     * 列表正常狀態
     */
    private void setListNormal(Member item) {

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < groupTypes.size(); i++) {
            GroupType groupType = groupTypes.get(i);
            if (groupType.isSelected()) {
                list.add(groupType.getGroup_type_id());
            }
        }
        item.setLikeGroupType(list);
        refreshListView(item);
    }

    /**
     * 列表選着狀態
     */
    private void setListEdit(Member item) {

        data.clear();
        if (groupTypes != null && groupTypes.size() > 0 && item.getLikeGroupType() != null && item.getLikeGroupType().size() > 0) {
            for (String id : item.getLikeGroupType()) {
                for (GroupType groupType : groupTypes) {
                    if (id.equals(groupType.getGroup_type_id())) {
                        groupType.setSelected(true);
                    }
                }
            }
        }
        data.addAll(groupTypes);
        adapter.setMode(MyPersonalInfoAdapter.MODE_EDIT);
        adapter.notifyDataSetChanged();
    }

    /**
     * 性別
     */
    public String getGender(int gender) {

        if (gender == 1) {
            return "男";
        } else if (gender == 0) {
            return "女";
        } else {
            return "无";
        }
    }

    /**
     * 是否公開
     */
    public void setDisplay(Member item) {

        if (item != null) {
            if (item.getDisplay() == 1) {
                publicCheck.setChecked(true);
            } else {
                publicCheck.setChecked(false);
            }
        }
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

        if (event != null) {
            if (event.isResult()) {
                member = event.getData();
                if (member != null) {
                    Log.d(tag, "onEventMainThread: UserProfilePersonalInformationEntity refreshContentView");
                    refreshContentView(member);
                    refreshListView(member);
                    setContentNormal();
                }
            }
        }
    }

    /**
     * 回调：圈子類型
     */
    public void onEventMainThread(GroupType210Entity event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData().size() > 0) {
                    this.groupTypes.clear();
                    this.groupTypes.addAll(event.getData());
                    refreshListView(member);
                }
            }
        }
    }

    /**
     * 回调：敏感词过滤
     */
    public void onEventMainThread(BaseInfoEntity event) {

        if (event != null && event.isResult()) {
            if (event.getData().isHasBad()) {
                ToastHelper.s("请勿使用敏感词汇");
                return;
            }
            member = (Member) newMember.clone();
            setContentNormal();
            setListNormal(member);
            refreshContentView(member);
            // 编辑个人资料
            DataManager.userProfileFinishMemberInfo(member);
        }
    }

    /**
     * 回调：编辑个人资料
     */
    public void onEventMainThread(UserProfileFinishMemberInfoEntity event) {

        if (event != null) {
            if (event.isResult()) {
                // 提交完善个人资料任务
                DataManager.TASK.doTask_16(getMember_id());
            }
        }
    }

    /**
     * 回调：上传头像
     */
    public void onEventMainThread(UserProfileUploadAvatarEntity event) {

        dismissProgressDialog();
        if (event != null) {
            if (event.isResult()) {
                showToastShort("头像上传成功");
            }
        }
    }
}
