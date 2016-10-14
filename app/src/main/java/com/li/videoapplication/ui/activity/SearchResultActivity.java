package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.response.AssociateEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.adapter.SearchAssociateAdapter;
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;
import com.li.videoapplication.ui.fragment.SearchGameFragment;
import com.li.videoapplication.ui.fragment.SearchGiftFragment;
import com.li.videoapplication.ui.fragment.SearchMemberFragment;
import com.li.videoapplication.ui.fragment.SearchVideoFragment;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.ViewPagerY4;
/**
 * 活动：搜索结果
 */
public class SearchResultActivity extends TBaseActivity implements OnClickListener, TextWatcher, OnItemClickListener, OnFocusChangeListener {

    private String content;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            content = getIntent().getStringExtra("content");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNull(content)) {
            finish();
        }
    }
    
	private List<Associate> associateData = new ArrayList<>();
    private SearchAssociateAdapter associateAdapter;
    
    private String getAbSearchEdit() {
		return abSearchEdit.getText().toString().trim();
    }

	@Override
	public int getContentView() {
		return R.layout.activity_searchresult;
	}

    public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                AppManager.getInstance().removeActivity(SearchActivity.class);
            }
        }, 300);
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();
        initTopMenu();
    }

    private void initContentView() {

		abSearchContainer.setVisibility(View.VISIBLE);
		abSearchDelete.setVisibility(View.GONE);
		abSearchSubmit.setOnClickListener(this);
		abSearchDelete.setOnClickListener(this);
		
		abSearchEdit.setText(content);
		
		//设置输入多少字符后提示，默认值为1
		abSearchEdit.setThreshold(1);
		abSearchEdit.addTextChangedListener(this);
		abSearchEdit.setOnFocusChangeListener(this);

        abSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    abSearchSubmit.performClick();
                    return true;
                }
                return false;
            }
        });
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		if (hasFocus && !StringUtil.isNull(getAbSearchEdit())) {
    		// 搜索关键字(v1.1.5接口)
    		DataManager.associate(getAbSearchEdit());
		}
		if (hasFocus && !StringUtil.isNull(getAbSearchEdit())) {
        	abSearchDelete.setVisibility(View.VISIBLE);
		} else {
            abSearchDelete.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.ab_search:
            if (StringUtil.isNull(getAbSearchEdit())) {
                showToastShort("请输入搜索内容");
                return;
            }
            try {
                InputUtil.closeKeyboard(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            refreshFragment();
            PreferencesHepler.getInstance().addSearchHistory(getAbSearchEdit());
			break;

		case R.id.ab_search_delete:
			abSearchEdit.setText("");
			break;
		}
	}
	
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    	if (!StringUtil.isNull(getAbSearchEdit())) {
    		// 搜索关键字(v1.1.5接口)
    		DataManager.associate(getAbSearchEdit());
		}
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (StringUtil.isNull(getAbSearchEdit())) {
        	abSearchDelete.setVisibility(View.GONE);
        } else {
            abSearchDelete.setVisibility(View.VISIBLE);
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		if (parent.getAdapter() == abSearchEdit.getAdapter()) {
			Associate record = (Associate) parent.getAdapter().getItem(position);
			if (!StringUtil.isNull(record.getGame_name())) {
				abSearchEdit.setText(record.getGame_name());
				Editable e = abSearchEdit.getText();
				Selection.setSelection(e, e.length());
                try {
                    InputUtil.closeKeyboard(this);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                refreshFragment();
			}
		}
	}
    
    private void refreshFragment() {

        video.setContent(getAbSearchEdit());
        game.setContent(getAbSearchEdit());
    	member.setContent(getAbSearchEdit());
        gift.setContent(getAbSearchEdit());
    }

    //动画图片
    private ImageView cursor;
    private TextView first, second, third, fourth;
    private int padding = 24;// 游标边距
    private int size = 4;// 页卡数量
    /** 处在哪一个页面，0为本地视频页，1为云端视频页，2为截图页 */
    private int currIndex = 0, inWitchPage = 0;
	private List<Fragment> fragments = new ArrayList<>();
	private ViewPagerY4 viewPager;
	private GamePagerAdapter adapter;
    private SearchVideoFragment video;
    private SearchGameFragment game;
	private SearchMemberFragment member;
    private SearchGiftFragment gift;

	/**
	 * 初始化菜单
	 */
	protected void initTopMenu() {

        cursor = (ImageView) findViewById(R.id.cursor);
        // 游标宽度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(srceenWidth / size - 2 * padding, 3);
        cursor.setLayoutParams(params);

        Matrix matrix = new Matrix();
        matrix.postTranslate(padding, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置

        first = (TextView) findViewById(R.id.top_first);
        second = (TextView) findViewById(R.id.top_second);
        third = (TextView) findViewById(R.id.top_third);
        fourth = (TextView) findViewById(R.id.top_fourth);

        first.setOnClickListener(new ClickListener(0));
        second.setOnClickListener(new ClickListener(1));
        third.setOnClickListener(new ClickListener(2));
        fourth.setOnClickListener(new ClickListener(3));

        video = SearchVideoFragment.newInstance(content);
        fragments.add(video);
        game = SearchGameFragment.newInstance(content);
        fragments.add(game);
        member = SearchMemberFragment.newInstance(content);
        fragments.add(member);
        gift = SearchGiftFragment.newInstance(content);
        fragments.add(gift);

        viewPager = (ViewPagerY4) findViewById(R.id.viewpager);
        viewPager.setScrollable(true);
        viewPager.setOffscreenPageLimit(size);
        viewPager.setOnPageChangeListener(new PagerChangeListener());
        adapter = new GamePagerAdapter(manager, fragments);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0);
        first.performClick();
	}

    private class ClickListener implements OnClickListener {

        private int index = 0;

        public ClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    @SuppressLint("ResourceAsColor")
    public class PagerChangeListener implements OnPageChangeListener {

        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 0) {// 静止

            } else if (arg0 == 1) { // 正在滑动 滑动时，编辑，导入按钮不能点击

            } else if (arg0 == 2) {// 滑屏结束

            } else if (arg0 == 3) {// 滑屏结束

            }
        }

        // 滑屏中
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        public void onPageSelected(int arg0) {

            switchCursor(arg0);
            switchMenu(arg0);
            switchContent(arg0);

            currIndex = arg0;
        }

        /**
         * 移动游标
         */
        private void switchCursor(int arg0) {
            float fromXDelta = padding + currIndex * (srceenWidth / size);
            float toXDelta = padding + arg0 * (srceenWidth / size);
            Animation animation = new TranslateAnimation(fromXDelta, toXDelta , 0, 0);
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }

    /**
     * 选择内容
     */
    private void switchContent(int arg0) {

        if (arg0 == 0) {
            inWitchPage = 0;
        } else if (arg0 == 1) {
            inWitchPage = 1;
        } else if (arg0 == 2) {
            inWitchPage = 2;
        } else if (arg0 == 3) {
            inWitchPage = 3;
        }
    }

    /**
     * 设置字体颜色
     */
    private void switchMenu(int i) {
        if (i == 0) {
            first.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_red));
            second.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            third.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            fourth.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
        } else if (i == 1) {
            first.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            second.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_red));
            third.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            fourth.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
        } else if (i == 2) {
            first.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            second.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            third.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_red));
            fourth.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
        } else if (i == 3) {
            first.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            second.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            third.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            fourth.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_red));
        }
    }

	/**
	 * 回调：搜索联想词
	 */
	public void onEventMainThread(AssociateEntity event) {

        if (event.isResult()) {
            if (event.getData().size() > 0) {
                associateData.clear();
                associateData.addAll(event.getData());
                associateAdapter = new SearchAssociateAdapter(this, associateData);
                abSearchEdit.setAdapter(associateAdapter);
                abSearchEdit.setOnItemClickListener(this);
                associateAdapter.notifyDataSetChanged();
            }
        }
	}
}
