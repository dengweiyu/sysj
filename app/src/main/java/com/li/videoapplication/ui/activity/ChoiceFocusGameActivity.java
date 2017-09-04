package com.li.videoapplication.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.ChoiceFocusGameAdapter;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 选择关注游戏
 */

public class ChoiceFocusGameActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private SystemBarTintManager tintManager;

    private RecyclerView mGameList;
    @Override
    protected int getContentView() {
        return R.layout.activity_choice_focus_game;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        initSystemBar(this);
        setSystemBarBackgroundResource(R.color.activity_choice_focus_game_bg );

        initToolbar();

        mGameList = (RecyclerView)findViewById(R.id.rv_choice_game_list);
        mGameList.setLayoutManager(new GridLayoutManager(this,3));
        mGameList.setAdapter(new ChoiceFocusGameAdapter(Lists.newArrayList("","","","","","","","","")));
    }


    private void initToolbar(){
        View root = findViewById(R.id.rl_toolbar);
        root.setBackgroundColor(Color.parseColor("#ff3f3f"));
        findViewById(R.id.tb_back).setVisibility(View.GONE);
        TextView title = (TextView)findViewById(R.id.tb_title);
        title.setText("关注游戏");
        title.setTextColor(Color.WHITE);

        TextView go = (TextView)findViewById(R.id.tb_topup_record);
        go.setTextColor(Color.WHITE);
        go.setText("跳过");
        go.setVisibility(View.VISIBLE);
        go.setOnClickListener(this);
    }

    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        tintManager = new SystemBarTintManager(activity);

    }

    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void setSystemBarBackgroundResource(int res) {
        if (tintManager != null) {
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(res);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_topup_record:
                ActivityManager.startMainActivity(ChoiceFocusGameActivity.this);
                break;
        }
    }
}
