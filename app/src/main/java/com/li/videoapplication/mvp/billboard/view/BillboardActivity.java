package com.li.videoapplication.mvp.billboard.view;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：主播榜，视频榜
 */
public class BillboardActivity extends TBaseAppCompatActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    @BindView(R.id.playerbillboard_viewpager)
    ViewPager viewPager;

    public static final int TYPE_PLAYER = 1;//主播榜
    public static final int TYPE_VIDEO = 2;//视频榜

    private List<Fragment> fragments;
    private List<RelativeLayout> topButtons;
    private List<ImageView> topLine;
    private List<ImageView> topIcon;
    private List<TextView> topTextView;
    private int type;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            type = getIntent().getIntExtra("type", TYPE_PLAYER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_playerbillboard;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        initTopMenu();
        initViewPager();
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();
            if (type == TYPE_PLAYER) {
                fragments.add(PlayerBillboardFragment.newInstance(PlayerBillboardFragment.PLAYERBILLBOARD_CURRENCY));
                fragments.add(PlayerBillboardFragment.newInstance(PlayerBillboardFragment.PLAYERBILLBOARD_VIDEO));
                fragments.add(PlayerBillboardFragment.newInstance(PlayerBillboardFragment.PLAYERBILLBOARD_FANS));
            } else {
                fragments.add(VideoBillboardFragment.newInstance(VideoBillboardFragment.VIDEOBILLBOARD_CLICK));
                fragments.add(VideoBillboardFragment.newInstance(VideoBillboardFragment.VIDEOBILLBOARD_LIKE));
                fragments.add(VideoBillboardFragment.newInstance(VideoBillboardFragment.VIDEOBILLBOARD_COMMENT));
            }
        }
        GamePagerAdapter adapter = new GamePagerAdapter(getSupportFragmentManager(), fragments);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.tb_back).setOnClickListener(this);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        if (type == TYPE_PLAYER) {
            tb_title.setText("主播榜");
        } else {
            tb_title.setText("视频榜");
        }
    }

    private void initTopMenu() {
        if (topButtons == null) {
            topButtons = new ArrayList<>();
            topButtons.add((RelativeLayout) findViewById(R.id.top_left));
            topButtons.add((RelativeLayout) findViewById(R.id.top_middle));
            topButtons.add((RelativeLayout) findViewById(R.id.top_right));
        }

        if (topLine == null) {
            topLine = new ArrayList<>();
            topLine.add((ImageView) findViewById(R.id.top_left_line));
            topLine.add((ImageView) findViewById(R.id.top_middle_line));
            topLine.add((ImageView) findViewById(R.id.top_right_line));
        }

        if (topTextView == null) {
            topTextView = new ArrayList<>();
            topTextView.add((TextView) findViewById(R.id.top_left_text));
            topTextView.add((TextView) findViewById(R.id.top_middle_text));
            topTextView.add((TextView) findViewById(R.id.top_right_text));
        }

        if (topIcon == null) {
            topIcon = new ArrayList<>();
            topIcon.add((ImageView) findViewById(R.id.top_left_icon));
            topIcon.add((ImageView) findViewById(R.id.top_middle_icon));
            topIcon.add((ImageView) findViewById(R.id.top_right_icon));
        }
        for (int i = 0; i < topButtons.size(); i++) {
            topButtons.get(i).setOnClickListener(new OnTabClickListener(i));
        }

        if (type == TYPE_PLAYER) {
            setTextViewText(topTextView.get(0), R.string.playerBillboard_left);
            setTextViewText(topTextView.get(1), R.string.playerBillboard_middle);
            setTextViewText(topTextView.get(2), R.string.playerBillboard_right);
        }else {
            setTextViewText(topTextView.get(0), R.string.videoBillboard_left);
            setTextViewText(topTextView.get(1), R.string.videoBillboard_middle);
            setTextViewText(topTextView.get(2), R.string.videoBillboard_right);
        }

        switchTab(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switchTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 菜单点击事件
     */
    private class OnTabClickListener implements View.OnClickListener {

        private int index;

        public OnTabClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            switchTab(index);
            viewPager.setCurrentItem(index);
        }
    }

    /**
     * 叶卡选择
     */
    private void switchTab(int index) {

        for (int i = 0; i < topTextView.size(); i++) {
            if (index == i) {
                if (type == TYPE_PLAYER){
                    switch (index) {
                        case 0:
                            topTextView.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_yellow));
                            break;
                        case 1:
                            topTextView.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_blue));
                            break;
                        case 2:
                            topTextView.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_purple));
                            break;
                    }
                }else {
                    switch (index) {
                        case 0:
                            topTextView.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_thin_blue));
                            break;
                        case 1:
                            topTextView.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_purple));
                            break;
                        case 2:
                            topTextView.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_blue));
                            break;
                    }
                }

            } else {
                topTextView.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_gray));
            }
        }

        for (int i = 0; i < topLine.size(); i++) {
            if (index == i) {
                if (type == TYPE_PLAYER){
                    switch (index) {
                        case 0:
                            topLine.get(i).setImageResource(R.drawable.playerbillboard_bean_top_line);
                            break;
                        case 1:
                            topLine.get(i).setImageResource(R.drawable.playerbillboard_video_top_line);
                            break;
                        case 2:
                            topLine.get(i).setImageResource(R.drawable.playerbillboard_fans_top_line);
                            break;
                    }
                }else {
                    switch (index) {
                        case 0:
                            topLine.get(i).setImageResource(R.drawable.playerbillboard_watched_top_line);
                            break;
                        case 1:
                            topLine.get(i).setImageResource(R.drawable.playerbillboard_liked_top_line);
                            break;
                        case 2:
                            topLine.get(i).setImageResource(R.drawable.playerbillboard_comment_top_line);
                            break;
                    }
                }

            } else {
                topLine.get(i).setImageResource(R.color.menu_billboard_transperent);
            }
        }

        for (int i = 0; i < topIcon.size(); i++) {
            if (index == i) {
                if (type == TYPE_PLAYER) {
                    switch (index) {
                        case 0:
                            setImageViewImageRes(topIcon.get(0), R.drawable.slider_bean);
                            setImageViewImageRes(topIcon.get(1), R.drawable.playerbillboard_vedio_gray);
                            setImageViewImageRes(topIcon.get(2), R.drawable.playerbillboard_fans_gray);
                            break;
                        case 1:
                            setImageViewImageRes(topIcon.get(0), R.drawable.slider_bean_gray);
                            setImageViewImageRes(topIcon.get(1), R.drawable.playerbillboard_vedio);
                            setImageViewImageRes(topIcon.get(2), R.drawable.playerbillboard_fans_gray);
                            break;
                        case 2:
                            setImageViewImageRes(topIcon.get(0), R.drawable.slider_bean_gray);
                            setImageViewImageRes(topIcon.get(1), R.drawable.playerbillboard_vedio_gray);
                            setImageViewImageRes(topIcon.get(2), R.drawable.playerbillboard_fans);
                            break;
                    }
                }else {
                    switch (index) {
                        case 0:
                            setImageViewImageRes(topIcon.get(0), R.drawable.vediobiilboard_watched);
                            setImageViewImageRes(topIcon.get(1), R.drawable.vediobiilboard_like_gray);
                            setImageViewImageRes(topIcon.get(2), R.drawable.vediobiilboard_comment_gray);
                            break;
                        case 1:
                            setImageViewImageRes(topIcon.get(0), R.drawable.vediobiilboard_watched_gray);
                            setImageViewImageRes(topIcon.get(1), R.drawable.vediobiilboard_like);
                            setImageViewImageRes(topIcon.get(2), R.drawable.vediobiilboard_comment_gray);
                            break;
                        case 2:
                            setImageViewImageRes(topIcon.get(0), R.drawable.vediobiilboard_watched_gray);
                            setImageViewImageRes(topIcon.get(1), R.drawable.vediobiilboard_like_gray);
                            setImageViewImageRes(topIcon.get(2), R.drawable.vediobiilboard_comment);
                            break;
                    }
                }
            }
        }
    }
}
