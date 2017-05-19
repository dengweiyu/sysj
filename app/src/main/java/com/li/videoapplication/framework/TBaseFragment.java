package com.li.videoapplication.framework;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.LoginActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.functions.Action1;


/**
 * 基本碎片
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public abstract class TBaseFragment extends BaseFragment {

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

    protected Resources resources;

    protected TextImageHelper textImageHelper = new TextImageHelper();

    protected LayoutParamsHelper layoutParamsHelper = new LayoutParamsHelper();

    protected int srceenWidth, srceenHeight;

    protected WindowManager windowManager;

    private View rootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        inflater = LayoutInflater.from(getActivity());
        resources = activity.getResources();
        windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        srceenWidth = windowManager.getDefaultDisplay().getWidth();
        srceenHeight = windowManager.getDefaultDisplay().getHeight();
    }

    protected abstract int getCreateView();

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            if (rootView == null) {
                rootView = inflater.inflate(getCreateView(), container, false);
            }
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initContentView(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }


    protected abstract void initContentView(View view);

    @SuppressWarnings("rawtypes")
    protected abstract IPullToRefresh getPullToRefresh();

    protected void onRefreshCompleteDelayed(int delayMillis) {
        onRefreshCompleteDelayed(getPullToRefresh(), delayMillis);
    }

    protected void onRefreshCompleteDelayed(final IPullToRefresh mIPullToRefresh, int delayMillis) {

        if (mIPullToRefresh != null) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    mIPullToRefresh.onRefreshComplete();

                }
            }, delayMillis);
        }
    }

    protected void onRefreshComplete() {

        onRefreshComplete(getPullToRefresh());

    }

    protected void onRefreshComplete(final IPullToRefresh mIPullToRefresh) {
        onRefreshCompleteDelayed(mIPullToRefresh, 0);
    }

	/*##############  设置加载弹框   ##############*/
    private LoadingDialog pd;
    private SweetAlertDialog sad;
    private Timer timer;
    private int i = -1;

    public final void showProgressDialog(final String message) {
        showProgressDialog(message, true, true);
    }

    public final void showProgressDialog2(final String message) {
        showProgressDialog(message, false, false);
    }

    public final void showProgressDialog(final String message, final boolean isCancelable, final boolean isCanceledOnTouchOutside) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    pd = null;
                }
                pd = new LoadingDialog(getActivity());
                pd.setProgressText(message);
                pd.setCancelable(isCancelable);
                pd.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
                pd.show();
            }
        });
    }

    public final void dismissProgressDialog() {
        handler.post(new Runnable() {

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
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        setProgressText(message);
    }

    public final void setProgressText(final String message) {

        if (StringUtil.isNull(message)) {
            return;
        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (pd != null) {
                    pd.setProgressText(message);
                }
            }
        });
    }

    public final void showLoadingDialog(final String title) {
        showLoadingDialog(title, null, true);
    }

    public final void cancelProgressDialog() {

        handler.post(new Runnable() {

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
    public final void changeType2SuccessDialog(final String title, final CharSequence content, final String confirmText) {
        handler.post(new Runnable() {
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

    //加载圈转为错误提示dialog
    public final void changeType2ErrorDialog(final String title, final String content, final String confirmText) {
        handler.post(new Runnable() {
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
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
            }
        });
    }

    //加载圈dialog
    public final void showLoadingDialog(final String title, final String content, final boolean isCancelable) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (sad.isShowing()) {
                        sad.dismiss();
                    }
                    sad = null;
                }
                sad = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                sad.setTitleText(title);
                sad.setCancelable(isCancelable);
                if (content != null)
                    sad.setContentText(content);
                sad.show();
            }
        });

        changeColorTimerTask();
    }

    public final void showSuccessDialogWithListener(final String title, final CharSequence content, final String confirmText) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (sad.isShowing()) {
                        sad.dismiss();
                    }
                    sad = null;
                }
                sad = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
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

    //加载圈转为成功提示dialog
    public final void change2SuccessWithOKListener(final String title, final CharSequence content, final String confirmText) {
        handler.post(new Runnable() {
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

    //确认按钮点击事件（复写使用,不取消则不使用super）
    protected void confirmButtonEvent() {
        sad.dismiss();
    }

    //带取消按钮的加载圈
    public final void showLoadingDialogWithCancel(final String title, final String content, final String cancelBtnText,
                                                  final String errorTitle,final String errorContent,
                                                  final String errorConfirmText) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (sad != null) {
                    if (sad.isShowing()) {
                        sad.dismiss();
                    }
                    sad = null;
                }
                sad = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
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

    //加载圈每隔800毫秒变色时间任务
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
        timer.schedule(task, 2200, 2200);
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

	/*##############  加载文本和图像   ##############*/

    protected void setTextViewText(TextView mTextView, String text) {
        textImageHelper.setTextViewText(mTextView, text);
    }

    protected void setTextViewText(TextView mTextView, int res) {
        textImageHelper.setTextViewText(getActivity(), mTextView, res);
    }

    protected void setTextViewTextVisibility(TextView mTextView, String text) {
        textImageHelper.setTextViewTextVisibility(mTextView, text);
    }

    protected void setTextViewTextVisibility(TextView mTextView, int res) {
        textImageHelper.setTextViewTextVisibility(getActivity(), mTextView, res);
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
	
	/*##############  提示   ##############*/

    protected void showToastShort(final String text) {
        ToastHelper.s(text);
    }

    protected void showToastShort(int resId) {
        ToastHelper.s(resId);
    }


    protected void showToastLong(final String text) {
        ToastHelper.l(text);
    }

    protected void showToastLogin() {
        this.showToastShort("请先登录!");
    }

    protected void showLoginDialog() {
        DialogManager.showLogInDialog(getActivity());
    }
	
	/*##############  页脚和页头   ##############*/

    protected View newEmptyView(int dpHeight) {

        View view = new View(getActivity());
        view.setBackgroundResource(android.R.color.transparent);
        int pxHeight = ScreenUtil.dp2px(dpHeight);
        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, pxHeight);
        view.setLayoutParams(params);
        return view;
    }
	
	/*##############  屏尺寸转换    ##############*/

    protected int dp2px(int dpValue) {
        return ScreenUtil.dp2px(dpValue);
    }

    protected int px2dp(int pxValue) {
        return ScreenUtil.px2dp(pxValue);
    }
	
	/*##############  布局参数设置    ##############*/

    protected void setAdapterViewLayoutParams(View view, int dpHeight) {
        layoutParamsHelper.setAdapterView(view, dpHeight);
    }

    protected void setListViewLayoutParams(View view, int dpHeight) {
        layoutParamsHelper.setListView(view, dpHeight);
    }

    protected void setGridViewLayoutParams(View view, int dpHeight) {
        layoutParamsHelper.setGridView(view, dpHeight);
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

    protected void setGridViewLayoutParams(View view, int w, int h) {
        layoutParamsHelper.setGridView(view, w, h);
    }

    protected void setAdapterViewLayoutParams(View view, int w, int h) {
        layoutParamsHelper.setAdapterView(view, w, h);
    }
	
	/*##############   页面跳转   ##############*/

    protected void startLoginActivity() {

        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

	/*##############   网络变化   ##############*/
}
