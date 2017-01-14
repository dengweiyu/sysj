package com.li.videoapplication.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;

import java.util.List;

/**
 * 腾讯广告联盟(广点通)
 */
public class GDTUtil {

    public static final String TAG = GDTUtil.class.getSimpleName();

    private static final String APP_ID = "1105863419";
    public static final String POS_ID_GROUP_HOT = "7040915834619050";
    public static final String POS_ID_GROUP_NEW = "1010616894519061";

    public interface GDTonLoaded{
        void onADLoaded(NativeADDataRef adItem);
    }

    /**
     * Banner广告
     */
    public static void nativeAD(Activity activity, String pos_id, final GDTonLoaded callback) {
        NativeAD nativeAD = new NativeAD(activity, APP_ID, pos_id, new NativeAD.NativeAdListener() {
            @Override
            public void onADLoaded(List<NativeADDataRef> list) {
                Log.d(TAG, "onADLoaded: ");
                if (list.size() > 0){
                    // 回调：广点通广告
                    callback.onADLoaded(list.get(0));
                }
            }

            @Override
            public void onNoAD(int i) {
                Log.d(TAG, "onNoAD: ");
            }

            @Override
            public void onADStatusChanged(NativeADDataRef nativeADDataRef) {
                Log.d(TAG, "onADStatusChanged: ");
            }

            @Override
            public void onADError(NativeADDataRef nativeADDataRef, int i) {
                Log.d(TAG, "onADError: ");
            }
        });
        nativeAD.loadAD(1);// 一次拉取的广告条数：范围1-10
    }

    /**
     * Banner广告
     */
    public static BannerView bannerView(Activity activity, RelativeLayout container, String pos_id) {

        BannerView bannerView = new BannerView(activity, ADSize.BANNER, APP_ID, pos_id);
        bannerView.setRefresh(30);
        bannerView.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Log.i(TAG, "onNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Log.i(TAG, "onADReceiv");
            }
        });
        // 6.4
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int screenWidth = ScreenUtil.getScreenWidth();
        params.width = screenWidth;
        params.height = (int) (((float) screenWidth) / 6.4f);
        container.addView(bannerView, params);
        bannerView.loadAD();
        return bannerView;
    }

    /**
     * 插屏广告
     */
    public static InterstitialAD interstitialAD(Activity activity, String pos_id) {
        final InterstitialAD interstitialAD = new InterstitialAD(activity, APP_ID, pos_id);
        interstitialAD.setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onADReceive() {
                Log.i(TAG, "onADReceive: ");
                interstitialAD.show();
            }

            @Override
            public void onNoAD(int arg0) {
                Log.i(TAG, "onNoAD: " + arg0);
            }
        });
        interstitialAD.loadAD();
        return interstitialAD;
    }
}
