package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.animation.ViewPagerZoomOutTransformer;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.pageradapter.WelcomePagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;

/**
 * 碎片：启动-欢迎
 */
public class WelcomeFragment extends TBaseChildFragment implements OnPageChangeListener, OnClickListener, OnCheckedChangeListener {

    /**
     * 跳转：主页
     */
    public void startMainActivity() {
        ActivityManager.startMainActivity(getActivity());
    }

    private List<View> views;
    private ViewPagerY4 viewPager;
    private WelcomePagerAdapter adapter;
    private RadioGroup radioGroup;
    private List<RadioButton> radios;

    private TextView gointo;
    private CheckBox sex;
    private ImageView male, female;
    private CheckBox action, role, shot, athletics, strategy, card, car, music;
    private Map<CheckBox, Boolean> flags;

    public List<String> getGroupIds() {
        List<String> list = new ArrayList<String>();
        if (flags != null) {
            Set<CheckBox> keys = flags.keySet();
            for (CheckBox key : keys) {
                Boolean value = flags.get(key);
                if (value) {
                    if (key == action) {
                        list.add("3");
                    } else if (key == role) {
                        list.add("1");
                    } else if (key == shot) {
                        list.add("7");
                    } else if (key == athletics) {
                        list.add("2");
                    } else if (key == strategy) {
                        list.add("5");
                    } else if (key == card) {
                        list.add("4");
                    } else if (key == car) {
                        list.add("6");
                    } else if (key == music) {
                        list.add("8");
                    }
                }
            }
        }
        return list;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_welcome;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected void initContentView(View view) {

        gointo = (TextView) view.findViewById(R.id.welcome_gointo);

        radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        radioGroup.setEnabled(false);
        radioGroup.setClickable(false);
        radios = new ArrayList<RadioButton>();
        radios.add((RadioButton) view.findViewById(R.id.welcome_first));
        radios.add((RadioButton) view.findViewById(R.id.welcome_second));
        radios.add((RadioButton) view.findViewById(R.id.welcome_third));
        radios.add((RadioButton) view.findViewById(R.id.welcome_fouth));
        viewPager = (ViewPagerY4) view.findViewById(R.id.viewpager);
        viewPager.setScrollable(true);
        viewPager.setOffscreenPageLimit(3);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.fragment_welcome_first, null));
        views.add(inflater.inflate(R.layout.fragment_welcome_second, null));
        views.add(inflater.inflate(R.layout.fragment_welcome_third, null));
        View v = inflater.inflate(R.layout.fragment_welcome_fouth, null);
        views.add(v);
        adapter = new WelcomePagerAdapter(views);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
		viewPager.setPageTransformer(true, new ViewPagerZoomOutTransformer());
//        viewPager.setPageTransformer(true, new ViewPagerDepthTransformer());
        viewPager.setCurrentItem(0);

        sex = (CheckBox) v.findViewById(R.id.welcome_sex);
        male = (ImageView) v.findViewById(R.id.welcome_male);
        female = (ImageView) v.findViewById(R.id.welcome_female);
        role = (CheckBox) v.findViewById(R.id.welcome_group_role);
        action = (CheckBox) v.findViewById(R.id.welcome_group_action);
        car = (CheckBox) v.findViewById(R.id.welcome_group_car);
        strategy = (CheckBox) v.findViewById(R.id.welcome_group_strategy);
        athletics = (CheckBox) v.findViewById(R.id.welcome_group_athletics);
        shot = (CheckBox) v.findViewById(R.id.welcome_group_shot);
        music = (CheckBox) v.findViewById(R.id.welcome_group_music);
        card = (CheckBox) v.findViewById(R.id.welcome_group_card);

        flags = new HashMap<CheckBox, Boolean>();
        flags.put(role, true);
        flags.put(athletics, true);
        flags.put(action, false);
        flags.put(strategy, false);
        flags.put(shot, false);
        flags.put(card, false);
        flags.put(car, false);
        flags.put(music, false);

        Set<CheckBox> keys = flags.keySet();
        for (CheckBox key : keys) {
            Boolean value = flags.get(key);
            key.setChecked(value);
            key.setOnCheckedChangeListener(this);
        }

        gointo.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
       // sex.setOnCheckedChangeListener(this);
        switchTab(0);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.welcome_gointo:
                if (getGroupIds() != null && getGroupIds().size() > 0) {
                    // 保存问卷
                    PreferencesHepler.getInstance().saveGroupIds(getGroupIds());
                    // 问卷
                    DataManager.indexDoSurvey(getMember_id(), PreferencesHepler.getInstance().getGroupIds2());
                }
                startMainActivity();
                break;

            case R.id.welcome_male:
                sex.setChecked(false);
                break;

            case R.id.welcome_female:
                sex.setChecked(true);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked && getGroupIds().size() == 3) {
            showToastShort("只能选择3个喜欢的游戏类型");
            buttonView.setChecked(!isChecked);
            return;
        }

        switch (buttonView.getId()) {

            case R.id.welcome_group_role:
                flags.put(role, isChecked);
                break;

            case R.id.welcome_group_action:
                flags.put(action, isChecked);
                break;

            case R.id.welcome_group_car:
                flags.put(car, isChecked);
                break;

            case R.id.welcome_group_strategy:
                flags.put(strategy, isChecked);
                break;

            case R.id.welcome_group_athletics:
                flags.put(athletics, isChecked);
                break;

            case R.id.welcome_group_shot:
                flags.put(shot, isChecked);
                break;

            case R.id.welcome_group_music:
                flags.put(music, isChecked);
                break;

            case R.id.welcome_group_card:
                flags.put(card, isChecked);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        switchTab(position);
    }

    private void switchTab(int position) {

        RadioButton r = radios.get(position);
        radioGroup.check(r.getId());

        if (position == radios.size() - 1) {// 第4页
            gointo.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
        } else {
            gointo.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
        }
    }
}
