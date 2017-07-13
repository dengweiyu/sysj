package com.li.videoapplication.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.li.videoapplication.R;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.network.RequestService;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.fragment.BannerFragment;
import com.li.videoapplication.ui.fragment.SplashFragment;
import com.li.videoapplication.ui.fragment.WelcomeFragment;
import com.li.videoapplication.component.service.AppStartService;
import com.li.videoapplication.utils.StringUtil;
import com.umeng.analytics.MobclickAgent;
import java.io.File;

/**
 * 活动：启动
 */
public class AppstartActivity extends TBaseActivity {

    private boolean firstSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //申请读写权限
        requestPermission();
    }

    /**
     * 跳转：主页
     */
    public void startMainActivity() {
        ActivityManager.startMainActivity(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_appstart;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        MobclickAgent.setDebugMode(true);
    }

    @Override
    public void initView() {
        setTheme(R.style.AppTheme_Light);
        super.initView();

        MainActivity mainActivity = AppManager.getInstance().getMainActivity();
        firstSetup = NormalPreferences.getInstance().getBoolean(Constants.APPSTART_ACTIVITY_FIRSTSETUP, true);


        if (mainActivity != null) {
            ActivityManager.startMainActivityBottom(this);
        } else {
            if (firstSetup) {
                // 启动-欢迎
                replaceFragment(new WelcomeFragment());
                 NormalPreferences.getInstance().putBoolean(Constants.APPSTART_ACTIVITY_FIRSTSETUP, false);
            }
        }


        // 初始化下载器
        DownLoadManager.getInstance();
        // 网络请求服务
        RequestService.startRequestService();
        // 启动服务
        AppStartService.startAppStartService();
    }

    @Override
    public void loadData() {
        super.loadData();

    }

    @Override
    public void onResume() {
        super.onResume();


       /* RequestExecutor.execute(new Runnable() {
            @Override
            public void run() {*/
               if (!firstSetup) {
                    if (AppConstant.SHOW_DOWNLOAD_AD && haveLaunchAd()) { //正常渠道 && 有广告
                        UITask.post(new Runnable() {
                            @Override
                            public void run() {
                                replaceBanner();
                            }
                        });
                    } else { //无广告 或 特殊渠道
                        // 启动商标
                        replaceFragment(new SplashFragment());
                        UITask.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                startMainActivity();
                            }
                        }, 1500);
                    }
                }
      //  Debug.stopMethodTracing();
        /*UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(AppstartActivity.this, AppSdkInitIntentService.class));
            }
        },4000);*/

    }

    private boolean haveLaunchAd() {
        //有广告
        if (PreferencesHepler.getInstance().getIndexLaunchImage() != null) {

            String name = PreferencesHepler.getInstance()
                    .getIndexLaunchImage().getData().get(0).getServer_pic_a();
            if (StringUtil.isNull(name)){
                return false;
            }
            String imageName = StringUtil.getFileNameWithExt(name);
            String path = SYSJStorageUtil.getSysjDownload() + File.separator + imageName;
            File file = new File(path);
            return file.exists();
        } else { //无广告
            return false;
        }
    }

    public void replaceFragment(Fragment target) {
        try {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.zoom_enter, R.anim.dialog_out);
            transaction.replace(R.id.container, target).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void replaceBanner() {
        try {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, new BannerFragment()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestPermission(){
        super.checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
    }

}
