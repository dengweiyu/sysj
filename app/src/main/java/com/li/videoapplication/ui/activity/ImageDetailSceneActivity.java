package com.li.videoapplication.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.github.chrisbanes.photoview.PhotoView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 支持场景动画
 *
 */

public class ImageDetailSceneActivity extends AppCompatActivity {

    public final static String SHARED_TRANSITION = "image";

    public final static String CURRENT_ITEM = "current_item";

    public final static int REQUEST_CODE = 1;

    private String[] resUrls;
    private List<View> viewList = new ArrayList<>();
    private int currentItem;
    private boolean isTransition;

    private ViewPager pager;


    public static void  startImageDetailActivity(Activity activity,View source, int position, String[] urls,boolean isTransition){
        Intent intent = new Intent(activity, ImageDetailSceneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("url_array",urls);
        bundle.putInt(CURRENT_ITEM,position);
        bundle.putBoolean("is_transition",isTransition);
        intent.putExtra("data",bundle);
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    new Pair<View, String>( source,SHARED_TRANSITION));
            activity.startActivityForResult(intent,REQUEST_CODE,options.toBundle());
        }else {
            activity.startActivity(intent);
            activity.overridePendingTransition(android.R.anim.fade_in , R.anim.activity_hold);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle data = getIntent().getBundleExtra("data");
        if (data != null){
            resUrls = data.getStringArray("url_array");
            currentItem = data.getInt(CURRENT_ITEM);
            isTransition = data.getBoolean("is_transition",false);
        }
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    int position = pager.getCurrentItem();
                    if (position != currentItem){
                        names.clear();
                        names.add(SHARED_TRANSITION);
                        sharedElements.clear();
                        sharedElements.put(SHARED_TRANSITION,viewList.get(position));
                    }
                }
            });
        }

    }


    @Override
    public void finishAfterTransition() {
        Intent intent = new Intent();
        Bundle data = new Bundle();
        data.putInt(CURRENT_ITEM,pager.getCurrentItem());
        intent.putExtra("data",data);
        setResult(REQUEST_CODE,intent);
        super.finishAfterTransition();
    }

    private void init(){
        if (resUrls != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                postponeEnterTransition();
            }

            for (int i = 0; i < resUrls.length; i++) {
                final PhotoView detailItem = (PhotoView) LayoutInflater.from(this).inflate(R.layout.pager_item,null);
                if (i == currentItem && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    detailItem.setTransitionName(SHARED_TRANSITION);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        detailItem.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @TargetApi(21)
                            @Override
                            public void onGlobalLayout() {
                                startPostponedEnterTransition();
                                detailItem.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
                    }

                }

                detailItem.setOnClickListener(mListener);

                GlideHelper.displayImageNoFade(this,resUrls[i],detailItem);
                viewList.add(detailItem);
            }
            pager = (ViewPager)findViewById(R.id.vp_pager);
            pager.setAdapter(mAdapter);
            pager.setCurrentItem(currentItem,false);
        }

    }

    @Override
    public void finish() {
        super.finish();
        if (!isTransition){
            overridePendingTransition(R.anim.activity_hold , android.R.anim.fade_out);
        }
    }

    final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                ImageDetailSceneActivity.this.finishAfterTransition();
            }else {
                finish();
            }
        }
    };


    final PagerAdapter mAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View item = viewList.get(position);
            container.addView(item);
            return item;
        }
    };
}
