package com.li.videoapplication.framework;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.AnimationHelper;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.ui.activity.LoginActivity;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 基本活动
 */
public abstract class TBaseActivity extends BaseActivity implements ITBaseActivity {

    protected String getMember_id() {
        return PreferencesHepler.getInstance().getMember_id();
    }

    protected boolean isLogin() {
        return PreferencesHepler.getInstance().isLogin();
    }

    protected Member getUser() {
        return PreferencesHepler.getInstance().getUserProfilePersonalInformation();
    }

    protected LayoutInflater inflater;
    protected FragmentManager manager;
    protected Resources resources;
    protected Gson gson = new Gson();
    protected TextImageHelper textImageHelper = new TextImageHelper();
    protected AnimationHelper animationHelper = new AnimationHelper();
    protected LayoutParamsHelper layoutParamsHelper = new LayoutParamsHelper();

    protected WindowManager windowManager;
    protected int srceenWidth, srceenHeight;

    @Override
    public int getContentView() {
        return 0;
    }

    @Override
    public int inflateActionBar() {
        return 0;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        inflater = LayoutInflater.from(this);
        manager = getSupportFragmentManager();
        resources = getResources();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        srceenWidth = windowManager.getDefaultDisplay().getWidth();
        srceenHeight = windowManager.getDefaultDisplay().getHeight();

        if (isSystemBar) {
            initSystemBar(this);
        }

        if (getContentView() != 0) {
            setContentView(getContentView());
            ButterKnife.bind(this);
        }

        actionBar = getActionBar();
        if (inflateActionBar() != 0) {
            setActionBar(inflateActionBar());
        }
        initActionBar();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            dismissProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/*##############  设置加载弹框   ##############*/

    private LoadingDialog pd;
    private SweetAlertDialog sad;
    private Timer timer;
    private int i = -1;

    public final void showLoadingDialog(final String title) {
        showLoadingDialog(title, null, true);
    }

    public final void cancelProgressDialog() {

        post(new Runnable() {

            @Override
            public void run() {

                if (sad != null) {
                    if (timer != null) {
                        timer.cancel();
                        i = -1;
                    }
                    if (sad.isShowing()) {
                        sad.cancel();
                    }
                    sad = null;
                }
            }
        });
    }

    //加载圈转为成功提示dialog
    public final void changeType2SuccessDialog(final String title, final String content, final String confirmText) {
        post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (timer != null) {
                        timer.cancel();
                        i = -1;
                    }
                    sad.setTitleText(title)
                            .setContentText(content)
                            .setConfirmText(confirmText)
                            .setConfirmClickListener(null)
                            .showCancelButton(false)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }
            }
        });
    }

    //加载圈转为成功提示dialog
    public final void change2SuccessWithOKListener(final String title, final CharSequence content, final String confirmText) {
        post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (timer != null) {
                        timer.cancel();
                        i = -1;
                    }
                    sad.setTitleText(title)
                            .setContentText(content)
                            .setConfirmText(confirmText)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    confirmButtonEvent();
                                }
                            })
                            .showCancelButton(false)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }
            }
        });
    }

    protected void confirmButtonEvent() {
    }

    //加载圈转为错误提示dialog
    public final void changeType2ErrorDialog(final String title, final String content, final String confirmText) {
        post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (timer != null) {
                        timer.cancel();
                        i = -1;
                    }
                    sad.setTitleText(title)
                            .setContentText(content)
                            .setConfirmText(confirmText)
                            .setConfirmClickListener(null)
                            .showCancelButton(false)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
            }
        });
    }

    //加载圈dialog
    public final void showLoadingDialog(final String title, final String content, final boolean isCancelable) {

        post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (sad.isShowing()) {
                        sad.dismiss();
                    }
                    sad = null;
                }
                sad = new SweetAlertDialog(TBaseActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                sad.setTitleText(title);
                sad.setCancelable(isCancelable);
                if (content != null)
                    sad.setContentText(content);
                sad.show();
            }
        });

        changeColorTimerTask();
    }

    public final SweetAlertDialog getSweetDialog(){
        return sad;
    }

    public final void showSuccessDialogWithListener(final String title, final CharSequence content, final String confirmText) {
        post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (sad.isShowing()) {
                        sad.dismiss();
                    }
                    sad = null;
                }
                sad = new SweetAlertDialog(TBaseActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sad.setTitleText(title)
                        .setContentText(content)
                        .setConfirmText(confirmText)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                confirmButtonEvent();
                            }
                        }).show();
            }
        });
    }

    //加载圈每隔800毫秒变色
    private void changeColorTimerTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (sad != null && sad.isShowing()) {
                    changeColor();
                }
            }
        };
        timer = new Timer(true);
        timer.schedule(task, 800, 800);
    }

    //带取消按钮的加载圈
    public final void showLoadingDialogWithCancel(final String title, final String content, final String cancelBtnText,
                                                  final String errorTitle, final String errorContent,
                                                  final String errorConfirmText) {

        post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (sad.isShowing()) {
                        sad.dismiss();
                    }
                    sad = null;
                }
                sad = new SweetAlertDialog(TBaseActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                sad.setTitleText(title);
                sad.setContentText(content);
                sad.setCancelText(cancelBtnText).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (timer != null) {
                            timer.cancel();
                            i = -1;
                        }
                        sweetAlertDialog.setTitleText(errorTitle)
                                .setContentText(errorContent)
                                .setConfirmText(errorConfirmText)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                }).show();
            }
        });

        changeColorTimerTask();
    }

    private void changeColor() {
        i++;
        switch (i % 7) {
            case 0:
                sad.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                break;
            case 1:
                sad.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                break;
            case 2:
                sad.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                break;
            case 3:
                sad.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                break;
            case 4:
                sad.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                break;
            case 5:
                sad.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                break;
            case 6:
                sad.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                break;
        }
    }

    public final void showProgressDialog(final String message) {
        showProgressDialog(message, true, true);
    }

    public final void showProgressDialog2(final String message) {
        showProgressDialog(message, false, false);
    }

    public final void showProgressDialog(final String message, final boolean isCancelable, final boolean isCanceledOnTouchOutside) {

        post(new Runnable() {

            @Override
            public void run() {
                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    pd = null;
                }
                pd = new LoadingDialog(TBaseActivity.this);
                pd.setProgressText(message);
                pd.setCancelable(isCancelable);
                pd.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
                pd.show();
            }
        });
    }

    public final void dismissProgressDialog() {

        post(new Runnable() {

            @Override
            public void run() {

                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    pd = null;
                }
            }
        });
    }

    public final void setProgressText(final int res) {
        String message = null;
        try {
            message = AppManager.getInstance().getApplication().getResources().getString(res);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        setProgressText(message);
    }

    public final void setProgressText(final String message) {

        if (StringUtil.isNull(message)) {
            return;
        }
        post(new Runnable() {

            @Override
            public void run() {
                if (pd != null) {
                    pd.setProgressText(message);
                }
            }
        });
    }

    /*##############  点击非EditText区域隐藏键盘   ##############*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

	/*##############  设置标题栏   ##############*/

    protected ActionBar actionBar;

    protected View abBackground;
    protected TextView abTitle, abVideoPlayTitle,
            abMyPersonalInfoEdit, abMyPersonalInfoCancel, abMyPersonalInfoSave,
            abReportCancel, abReportConfirm;
    protected TextView abVideoManagerSelected, abVideoManagerDelete, abVideoManagerImport, abVideoManagerCancel;
    protected ImageView abGoback, abGroupDetailPlus, abGroupDetailDownload, abGroupDetailGift,
            abVideoPlayShare,abShareBlack,abQuestion, abVideoPlayRemark;
    protected ImageView abCollectionDelte;
    protected TextView abCollectionCancel, abCollectionConfirm;
    protected FrameLayout abBillboardBg;
    protected LinearLayout abSearchContainer;
    protected AutoCompleteTextView abSearchEdit;
    protected ImageView abSearchDelete, abSearchSubmit;
    protected Button abImageViewDone;
    protected TextView abScanQRCodeCancel;
    protected TextView abTagConfirm;
    protected TextView abMessageClean;

    private void setActionBar(int layout) {
        if (layout != 0) {
            abBackground = inflater.inflate(R.layout.actionbar_second, null);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(abBackground, params);
        } else {
            throw new NullPointerException();
        }
    }

    private void initActionBar() {

        if (abBackground != null) {
            abTagConfirm = (TextView) findViewById(R.id.ab_tag_confirm);

            abTitle = (TextView) findViewById(R.id.ab_title);
            abVideoPlayTitle = (TextView) findViewById(R.id.ab_videoplay_title);
            abGoback = (ImageView) findViewById(R.id.ab_goback);

            abCollectionDelte = (ImageView) findViewById(R.id.ab_collection_delete);
            abCollectionConfirm = (TextView) findViewById(R.id.ab_collection_confirm);
            abCollectionCancel = (TextView) findViewById(R.id.ab_collection_cancel);

            abGroupDetailPlus = (ImageView) findViewById(R.id.ab_groupdetail_plus);
            abGroupDetailDownload = (ImageView) findViewById(R.id.ab_groupdetail_download);
            abGroupDetailGift = (ImageView) findViewById(R.id.ab_groupdetail_gift);

            abScanQRCodeCancel = (TextView) findViewById(R.id.ab_scanqrcode_cancel);

            abVideoPlayShare = (ImageView) findViewById(R.id.ab_videoplay_share);
            abShareBlack = (ImageView) findViewById(R.id.ab_share_black);
            abQuestion = (ImageView) findViewById(R.id.ab_question);
            abVideoPlayRemark = (ImageView) findViewById(R.id.ab_videoplay_remark);

            abReportCancel = (TextView) findViewById(R.id.ab_report_cancel);
            abReportConfirm = (TextView) findViewById(R.id.ab_report_confirm);

            abVideoManagerSelected = (TextView) findViewById(R.id.ab_videomanager_selected);
            abVideoManagerDelete = (TextView) findViewById(R.id.ab_videomanager_delete);
            abVideoManagerImport = (TextView) findViewById(R.id.ab_videomanager_import);
            abVideoManagerCancel = (TextView) findViewById(R.id.ab_videomanager_cancel);

            abBillboardBg = (FrameLayout) findViewById(R.id.ab_billboard);
            abMyPersonalInfoCancel = (TextView) findViewById(R.id.ab_mypersonalinfo_cancel);
            abMyPersonalInfoEdit = (TextView) findViewById(R.id.ab_mypersonalinfo_edit);
            abMyPersonalInfoSave = (TextView) findViewById(R.id.ab_mypersonalinfo_save);

            abSearchContainer = (LinearLayout) findViewById(R.id.ab_search_container);
            abSearchEdit = (AutoCompleteTextView) findViewById(R.id.ab_search_edit);
            abSearchDelete = (ImageView) findViewById(R.id.ab_search_delete);
            abSearchSubmit = (ImageView) findViewById(R.id.ab_search);

            abImageViewDone = (Button) findViewById(R.id.ab_imageview_done);

            abMessageClean = (TextView)findViewById(R.id.ab_message_clean);

            setAbTitle("");
            abTitle.setVisibility(View.VISIBLE);
            abGoback.setVisibility(View.VISIBLE);

            setAbStyleWhite();

            abTagConfirm.setVisibility(View.GONE);
            abScanQRCodeCancel.setVisibility(View.GONE);

            abVideoManagerSelected.setVisibility(View.GONE);
            abVideoManagerDelete.setVisibility(View.GONE);
            abVideoManagerCancel.setVisibility(View.GONE);
            abVideoManagerImport.setVisibility(View.GONE);

            abMyPersonalInfoCancel.setVisibility(View.GONE);
            abMyPersonalInfoEdit.setVisibility(View.GONE);
            abMyPersonalInfoSave.setVisibility(View.GONE);

            abCollectionDelte.setVisibility(View.GONE);
            abCollectionConfirm.setVisibility(View.GONE);
            abCollectionCancel.setVisibility(View.GONE);

            abImageViewDone.setVisibility(View.GONE);

            abGroupDetailPlus.setVisibility(View.GONE);
            abGroupDetailDownload.setVisibility(View.GONE);
            abGroupDetailGift.setVisibility(View.GONE);

            abReportCancel.setVisibility(View.GONE);
            abReportConfirm.setVisibility(View.GONE);

            abVideoPlayTitle.setVisibility(View.GONE);
            abVideoPlayShare.setVisibility(View.GONE);
            abShareBlack.setVisibility(View.GONE);
            abQuestion.setVisibility(View.GONE);
            abVideoPlayRemark.setVisibility(View.GONE);

            abSearchContainer.setVisibility(View.GONE);

            abBillboardBg.setVisibility(View.GONE);

            abMessageClean.setVisibility(View.GONE);
        }
    }

    /**
     * 标题
     */
    protected void setAbTitle(String text) {
        if (abTitle != null) {
            try {
                abTitle.setText(text);
            } catch (Exception e) {
                e.printStackTrace();
                abTitle.setText("");
            }
        }
    }

    /**
     * 标题
     */
    protected void setAbTitle(int res) {
        if (abTitle != null) {
            try {
                abTitle.setText(res);
                abTitle.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                abTitle.setText("");
            }
        }
    }

    /**
     * 标题颜色
     */
    protected void setAbTitleColor(String colorString) {
        if (abTitle != null) {
            try {
                abTitle.setTextColor(Color.parseColor(colorString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 标题颜色白色
     */
    protected void setAbTitleWhite() {
        if (abTitle != null) {
            try {
                abTitle.setTextColor(Color.WHITE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 标题颜色白色
     */
    protected void setAbTitleGray() {
        setAbTitleColor("#dd333333");
    }

    /**
     * 返回键图标白色
     */
    protected void setAbGobackWhite() {
        setAbGoback(R.drawable.ab_goback_white);
    }

    /**
     * 返回键图标灰色
     */
    protected void setAbGobackGray() {
        setAbGoback(R.drawable.ab_goback_gray);
    }

    /**
     * 返回键图标
     */
    protected void setAbGoback(int res) {
        if (abGoback != null) {
            try {
                abGoback.setImageResource(res);
                abGoback.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                abGoback.setImageBitmap(null);
            }
        }
    }

    /**
     * 标题栏背景
     */
    protected void setAbBackground(String colorString) {
        if (abBackground != null) {
            try {
                abBackground.setBackgroundColor(Color.parseColor(colorString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 标题栏背景
     */
    protected void setAbBackground(int res) {
        if (abBackground != null) {
            try {
                abBackground.setBackgroundResource(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 标题栏背景透明
     */
    protected void setAbBackgroundTranceparent() {
        if (abBackground != null) {
            try {
                abBackground.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 标题栏背景白色
     */
    protected void setAbBackgroundWhite() {
        if (abBackground != null) {
            try {
                abBackground.setBackgroundColor(Color.WHITE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 标题栏透明
     */
    protected void setAbStyleTranceparent() {
        setAbBackgroundTranceparent();
        setAbGobackWhite();
        setAbTitleWhite();
    }

    /**
     * 标题栏白色
     */
    protected void setAbStyleWhite() {
        setAbBackgroundWhite();
        setAbGobackGray();
        setAbTitleGray();
    }

	/*##############  设置状态栏   ##############*/

    private SystemBarTintManager tintManager;

    private boolean isSystemBar = true;

    /**
     * 是否设置状态栏
     *
     * @param isSystemBar
     */
    public void setSystemBar(boolean isSystemBar) {
        this.isSystemBar = isSystemBar;
    }

    /**
     * 初始化系统状态栏
     *
     * @param activity
     * @throws Exception
     */
    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        tintManager = new SystemBarTintManager(activity);

    }

    /**
     * 设置状态栏颜色
     *
     * @param res
     */
    protected void setSystemBarBackgroundResource(int res) {
        if (tintManager != null) {
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(res);
        }
    }

    /**
     * 设置状态栏颜色
     */
    protected void setSystemBarBackgroundColor(int color) {
        if (tintManager != null) {
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }
    }

    /**
     * 设置状态栏白色
     */
    protected void setSystemBarBackgroundWhite() {
        if (tintManager != null) {
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor("#ffffff"));
            setStatusBarDarkMode(true, this);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = (Window) activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //上面这个static块是判断系统是否是MIUI6
    private static boolean sIsMiuiV6;

    static {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            sIsMiuiV6 = "V6".equals((String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将状态栏的文字样式设置
     *
     * @param darkmode false:状态栏字体颜色是白色 true:颜色是黑色
     * @param activity
     */
    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        if (true) {
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	
	/*##############  定义页面跳转动画和返回按健   ##############*/

    /**
     * 标题栏返回
     */
    public void doBack(View view) {
        onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        this.overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        this.overridePendingTransition(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

    protected void startActivityBottomIn(Intent intent) {
        super.startActivity(intent);
        this.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    protected void startActivityForResultBottomIn(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        this.overridePendingTransition(R.anim.push_bottom_in, R.anim.activity_hold);
    }

    protected void finishBottomOut() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_hold, R.anim.push_bottom_out);
    }
	
	/*##############  加载文本和图像   ##############*/

    protected void setTextViewText(TextView mTextView, String text) {
        textImageHelper.setTextViewText(mTextView, text);
    }

    protected void setTextViewText(TextView mTextView, int res) {
        textImageHelper.setTextViewText(this, mTextView, res);
    }

    protected void setTextViewTextVisibility(TextView mTextView, String text) {
        textImageHelper.setTextViewTextVisibility(mTextView, text);
    }

    protected void setTextViewTextVisibility(TextView mTextView, int res) {
        textImageHelper.setTextViewTextVisibility(this, mTextView, res);
    }

    protected void setImageViewImageRes(ImageView mImageView, int res) {
        textImageHelper.setImageViewImageRes(mImageView, res);
    }

    protected void setImageViewImageLocal(ImageView mImageView, String path) {
        textImageHelper.setImageViewImageLocal(mImageView, path);
    }

    protected void setImageViewImageNet(ImageView mImageView, String url) {
        textImageHelper.setImageViewImageNet(mImageView, url);
    }
	
	/*##############  屏尺寸转换    ##############*/

    protected int dp2px(float dpValue) {
        return ScreenUtil.dp2px(dpValue);
    }

    protected int px2dp(float pxValue) {
        return ScreenUtil.px2dp(pxValue);
    }
	
	/*##############  提示   ##############*/

    protected void showToastShort(final String text) {
        ToastHelper.s(text);
    }

    protected void showToastShort(final int res) {
        ToastHelper.s(res);
    }

    protected void showToastLong(final String text) {
        ToastHelper.l(text);
    }

    protected void showToastLong(final int res) {
        ToastHelper.l(res);
    }

    protected void showToastLogin() {
        this.showToastShort("请先登录!");
    }
	
	/*##############  页脚和页头   ##############*/

    protected View newEmptyView(int dpHeight) {

        View view = new View(this);
        view.setBackgroundResource(android.R.color.transparent);
        int pxHeight = ScreenUtil.dp2px(dpHeight);
        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, pxHeight);
        view.setLayoutParams(params);
        return view;
    }
	
	/*##############  布局参数设置    ##############*/

    protected void setAdapterViewLayoutParams(View view, int dpHeight) {
        layoutParamsHelper.setAdapterView(view, dpHeight);
    }

    protected void setListViewLayoutParams(View view, int dpHeight) {
        layoutParamsHelper.setListView(view, dpHeight);
    }

    protected void setLinearLayoutLayoutParams(View view, int dpHeight) {
        layoutParamsHelper.setLinearLayout(view, dpHeight);
    }

    protected void setGridViewLayoutParams(View view, int dpHeight) {
        layoutParamsHelper.setGridView(view, dpHeight);
    }

    protected void setFrameLayoutLayoutParams(View view, int dpHeight) {
        layoutParamsHelper.setFrameLayout(view, dpHeight);
    }

    protected void setListViewLayoutParams(View view, int w, int h) {
        layoutParamsHelper.setListView(view, w, h);
    }

    protected void setLinearLayoutLayoutParams(View view, int w, int h) {
        layoutParamsHelper.setLinearLayout(view, w, h);
    }

    protected void setRelativeLayoutLayoutParams(View view, int w, int h) {
        layoutParamsHelper.setRelativeLayout(view, w, h);
    }

    protected void setFrameLayoutLayoutParams(View view, int w, int h) {
        layoutParamsHelper.setFrameLayout(view, w, h);
    }

    protected void setGridViewLayoutParams(View view, int w, int h) {
        layoutParamsHelper.setGridView(view, w, h);
    }

    protected void setAdapterViewLayoutParams(View view, int w, int h) {
        layoutParamsHelper.setAdapterView(view, w, h);
    }
	
	/*##############   页面跳转   ##############*/

    protected void startLoginActivity() {

        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }
}