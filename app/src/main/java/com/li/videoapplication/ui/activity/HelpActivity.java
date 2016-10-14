package com.li.videoapplication.ui.activity;

import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.data.model.entity.HelpQuestionEntity;
import com.li.videoapplication.ui.adapter.HelpQuestionAdapter;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.fragment.MatchCourseFragment;
import com.li.videoapplication.ui.fragment.MyFinishedMatchFragment;
import com.li.videoapplication.ui.fragment.MyOnGoingMatchFragment;
import com.li.videoapplication.ui.fragment.QuestionFragment;
import com.li.videoapplication.ui.fragment.RecordCourseFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 碎片：使用帮助
 */
public class HelpActivity extends TBaseActivity implements OnClickListener {

    private ViewPagerAdapter adapter;
    public ViewPager viewPager;
    private List<Fragment> fragments;
    public QuestionFragment questionFragment;

    @Override
    public int getContentView() {
        return R.layout.activity_help;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundResource(R.color.white);
        setStatusBarDarkMode(false, this);
        setAbTitle(R.string.help_title);

        setAbTitleGray();
        setAbTitleGray();
        abGoback.setOnClickListener(this);
    }

    @Override
    public void initView() {
        super.initView();
        initViewPager();
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();

            fragments.add(new MatchCourseFragment());
            fragments.add(new RecordCourseFragment());
            questionFragment = new QuestionFragment();
            fragments.add(questionFragment);
        }

        final String[] tabTitle = {"赛事教程", "录屏教程", "问题解答"};
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        viewPager.setAdapter(adapter);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_help);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {

        if (v == abGoback) {
            if (questionFragment.isMore) {
                questionFragment.isMore = false;
                // 隐藏该按钮
                questionFragment.moreQuestion.setVisibility(View.VISIBLE);
                questionFragment.normalQuestion.setText("常见问题");
            } else {
                finish();
            }
        }
    }
}
