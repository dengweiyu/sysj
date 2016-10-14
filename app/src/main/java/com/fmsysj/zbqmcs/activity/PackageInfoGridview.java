package com.fmsysj.zbqmcs.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fmsysj.zbqmcs.adapter.PackageInfoAdapter;
import com.fmsysj.zbqmcs.floatview.FloatViewService;
import com.fmsysj.zbqmcs.utils.PackageInfoIntComparator;
import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.DefaultPreferences;
import com.li.videoapplication.ui.toast.ToastHelper;

public class PackageInfoGridview extends Activity implements OnClickListener {

    /**
     * 跳转
     */
    public static synchronized void show(Context context) {
        Intent intent = new Intent(context, PackageInfoGridview.class);
        context.startActivity(intent);
    }

	// 返回按钮
	LinearLayout back;
	// 返回桌面
	LinearLayout backWindow;
	// 不再提示勾选框
	LinearLayout noNotify;
	ImageView noNotifyIM;
	// 保存应用信息的列表
	List<PackageInfo> moreApps, newApps;
	// 最多点击，最近点击griview
	GridView packageMoreGridView, packageNewGridView;
	// 最多点击，最近点击适配器
	PackageInfoAdapter packageInfoMoreAdapter, packageInfoNewAdapter;
	Intent intentfloatserver;
	boolean isNotify = false;
	/**
	 * 用以是否开启浮窗标志
	 */
	public static boolean isInPackageInfo = true;
	private Context mContext;
	// 应用包名
	String mPackageName;
	// 启动次数
	int startTimes;

	Date d = new Date();
	long longtime = 0;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fm_packageinfo_gridview);
		// 设置进入退出动画
		overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
		isInPackageInfo = true;

		mContext = this;
		// 查找页面控件
		findviews();
		// 设置监听事件
		setonclick();
		// 获得手机内第三方应用信息
		List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(0);
		// 判断是否系统应用：
		moreApps = new ArrayList<PackageInfo>();
		newApps = new ArrayList<PackageInfo>();
		PackageInfo pak;
		for (int i = 0; i < packageInfoList.size(); i++) {
			pak = (PackageInfo) packageInfoList.get(i);
			// 判断是否为系统预装的应用
			if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
				// 第三方应用
				moreApps.add(pak);
				newApps.add(pak);

			} else {
				// 系统应用
			}
		}
		// 对应用列表按照启动次数进行排序
		Collections.sort(moreApps, new PackageInfoIntComparator(this, 0));
		Collections.sort(newApps, new PackageInfoIntComparator(this, 1));
		// 初始化适配器列表
		packageInfoMoreAdapter = new PackageInfoAdapter(
				PackageInfoGridview.this, moreApps, 0);
		packageInfoNewAdapter = new PackageInfoAdapter(
				PackageInfoGridview.this, newApps, 1);

		packageMoreGridView.setAdapter(packageInfoMoreAdapter);
		packageNewGridView.setAdapter(packageInfoNewAdapter);

	}

	private void setonclick() {

		back.setOnClickListener(this);
		backWindow.setOnClickListener(this);
		noNotify.setOnClickListener(this);

		// 点击打开第三方应用
		packageMoreGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {

					PackageInfo appInfo = moreApps.get(position);
					Intent intent = getPackageManager()
							.getLaunchIntentForPackage(appInfo.packageName);
					startActivity(intent);

					// 将启动次数存入sharedPreferences,包名作为键名
					PackageInfo pinfo = moreApps.get(position);
					mPackageName = pinfo.packageName;
					startTimes = DefaultPreferences.getInstance().getInt(mPackageName, 0);
                    DefaultPreferences.getInstance().putInt(mPackageName, startTimes + 1);
					// 获取当前时间毫秒值
					longtime = d.getTime();
					// 将当前时间毫秒值存入SP
                    DefaultPreferences.getInstance().putLong(mPackageName + "new", longtime);
					isInPackageInfo = false;
					// 如果是无浮窗模式
					if (DefaultPreferences.getInstance().getBoolean("no_float_view_record", false)
							&& DefaultPreferences.getInstance().getBoolean("horizontalRecord", false)) {
                        ScreenRecordActivity.sendEmptyMessage(7);
					} else if (DefaultPreferences.getInstance().getBoolean("no_float_view_record", false)
							&& !DefaultPreferences.getInstance().getBoolean("horizontalRecord", false)) {
                        ScreenRecordActivity.sendEmptyMessage(8);
					}
					// 启动浮窗
					else if (ScreenRecordActivity.isFirstTimeUse) {
						ScreenRecordActivity.isFirstTimeUse = false;
						intentfloatserver = new Intent();
						intentfloatserver.setClass(PackageInfoGridview.this,
								FloatViewService.class);
						startService(intentfloatserver);

					}
					finish();
				} catch (Exception e) {
                    ToastHelper.s("抱歉，应用启动失败，请手动启动该应用");

				}

			}
		});
		packageNewGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {

					PackageInfo appInfo = newApps.get(position);
					Intent intent = getPackageManager()
							.getLaunchIntentForPackage(appInfo.packageName);
					startActivity(intent);

					// 将启动次数存入sharedPreferences,包名作为键名
					PackageInfo pinfo = newApps.get(position);
					mPackageName = pinfo.packageName;
					startTimes = DefaultPreferences.getInstance().getInt(mPackageName, 0);
                    DefaultPreferences.getInstance().putInt(mPackageName, startTimes + 1);
					// 获取当前时间毫秒值
					longtime = d.getTime();
					// 将当前时间毫秒值存入SP
                    DefaultPreferences.getInstance().putLong(mPackageName + "new", longtime);
					isInPackageInfo = false;
					// 如果是无浮窗模式
					if (DefaultPreferences.getInstance().getBoolean("no_float_view_record", false)
							&& DefaultPreferences.getInstance().getBoolean("horizontalRecord", false)) {
                        ScreenRecordActivity.sendEmptyMessage(7);
					} else if (DefaultPreferences.getInstance().getBoolean("no_float_view_record", false)
							&& !DefaultPreferences.getInstance().getBoolean("horizontalRecord", false)) {
//						ScreenRecordActivity.handler.sendEmptyMessage(8);
                        ScreenRecordActivity.sendEmptyMessage(8);

					}
					// 启动浮窗
					else if (ScreenRecordActivity.isFirstTimeUse) {
						ScreenRecordActivity.isFirstTimeUse = false;
						intentfloatserver = new Intent();
						intentfloatserver.setClass(PackageInfoGridview.this,
								FloatViewService.class);
						startService(intentfloatserver);

					}
					finish();
				} catch (Exception e) {
                    ToastHelper.s("抱歉，应用启动失败，请手动启动该应用");
				}

			}

		});
	}

	private void findviews() {
		back = (LinearLayout) this.findViewById(R.id.fm_packageinfo_back);
		backWindow = (LinearLayout) this.findViewById(R.id.fm_packageinfo_window);
		noNotify = (LinearLayout) this.findViewById(R.id.fm_packageinfo_linear);
		noNotifyIM = (ImageView) this.findViewById(R.id.fm_packageinfo_checkbox);
		packageMoreGridView = (GridView) this.findViewById(R.id.fm_packageinfo_more_grid);
		packageNewGridView = (GridView) this.findViewById(R.id.fm_packageinfo_new_grid);

	}

	@Override
	public void onClick(View v) {
		if (v == back) {
			finish();

            ScreenRecordActivity.showNewTask();

			isInPackageInfo = false;

		} else if (v == backWindow) {
			// 启动浮窗
			if (ScreenRecordActivity.isFirstTimeUse) {
				ScreenRecordActivity.isFirstTimeUse = false;
				intentfloatserver = new Intent();
				intentfloatserver.setClass(PackageInfoGridview.this,
						FloatViewService.class);
				startService(intentfloatserver);
			}
			isInPackageInfo = false;
			finish();
//			ScreenRecordActivity.handler.sendEmptyMessage(10);
            ScreenRecordActivity.sendEmptyMessage(10);
		} else if (v == noNotify) {// 不再显示应用列表监听

			isNotify = DefaultPreferences.getInstance().getBoolean("PackageInfoGridviewNotify", false);
			if (!isNotify) {// 取消勾选
                DefaultPreferences.getInstance().putBoolean("PackageInfoGridviewNotify", true);
				noNotifyIM.setBackgroundResource(R.drawable.fm_checkbox_false);
			} else {// 选择勾选
                DefaultPreferences.getInstance().putBoolean("PackageInfoGridviewNotify", false);
				noNotifyIM.setBackgroundResource(R.drawable.fm_checkbox_true);

			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isInPackageInfo = false;
	}
}
