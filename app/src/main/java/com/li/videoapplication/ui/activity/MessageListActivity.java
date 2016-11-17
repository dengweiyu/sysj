package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GroupMessage;
import com.li.videoapplication.data.model.entity.MyMessage;
import com.li.videoapplication.data.model.entity.SysMessage;
import com.li.videoapplication.data.model.response.AllReadEntity;
import com.li.videoapplication.data.model.response.MessageGroupMessageEntity;
import com.li.videoapplication.data.model.response.MessageMyMessageEntity;
import com.li.videoapplication.data.model.response.MessageSysMessageEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.GameMessageAdapter;
import com.li.videoapplication.ui.adapter.SystemMessageAdapter;
import com.li.videoapplication.ui.adapter.VideoMessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：消息
 */
public class MessageListActivity extends PullToRefreshActivity<Game> implements View.OnClickListener {

    /**
     * 跳转：视频消息
     */
    public static void showVideo(Context context) {
        ActivityManeger.startMessageListActivity(context, MESSAGE_VIDEO);
    }

    /**
     * 跳转：圈子消息
     */
    public static void showGame(Context context) {
        ActivityManeger.startMessageListActivity(context, MESSAGE_GAME);
    }

    /**
     * 跳转：系统消息
     */
    public static void showSystem(Context context) {
        ActivityManeger.startMessageListActivity(context, MESSAGE_SYSTEM);
    }

    public static final int MESSAGE_VIDEO = 1;
    public static final int MESSAGE_GAME = 2;
    public static final int MESSAGE_SYSTEM = 3;

    public int message;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            message = getIntent().getIntExtra("message", 0);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        if (message == 0) {
            finish();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_messagelist;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    private VideoMessageAdapter videoAdapter;
    private GameMessageAdapter gameAdapter;
    private SystemMessageAdapter systemAdapter;

    private List<MyMessage> videoData = new ArrayList<>();
    private List<GroupMessage> gameData = new ArrayList<>();
    private List<SysMessage> systemData = new ArrayList<>();

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        if (message == MESSAGE_VIDEO) {
            setAbTitle(R.string.message_video);
        } else if (message == MESSAGE_GAME) {
            setAbTitle(R.string.message_game);
        } else if (message == MESSAGE_SYSTEM) {
            setAbTitle(R.string.message_system);
        }
    }

    @Override
    public void initView() {
        super.initView();

        videoAdapter = new VideoMessageAdapter(this, videoData);
        gameAdapter = new GameMessageAdapter(this, gameData);
        systemAdapter = new SystemMessageAdapter(this, systemData);

        if (message == MESSAGE_VIDEO) {
            abMessageClean.setVisibility(View.VISIBLE);
            abMessageClean.setOnClickListener(this);
            setAdapter(videoAdapter);
        } else if (message == MESSAGE_GAME) {
            setAdapter(gameAdapter);
        } else if (message == MESSAGE_SYSTEM) {
            setListStyle(listView);
            setAdapter(systemAdapter);
        }
    }

    @Override
    public void loadData() {
        super.loadData();

        onPullDownToRefresh(pullToRefreshListView);
    }

    private void setListStyle(ListView listView) {
        listView.setBackgroundColor(Color.TRANSPARENT);
        listView.setDividerHeight(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ab_message_clean:
                DataManager.allRead(getMember_id());
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        doRequest();
    }

    @Override
    public void onLoadMore() {
        doRequest();
    }

    private void doRequest() {

        if (message == MESSAGE_VIDEO) {
            // 视频图文消息
            DataManager.messageMyMessage(getMember_id(), page);
        } else if (message == MESSAGE_GAME) {
            // 圈子消息
            DataManager.messageGroupMessage(getMember_id(), page);
        } else if (message == MESSAGE_SYSTEM) {
            // 系统消息
            DataManager.messageSysMessage(getMember_id(), page);
        }
    }

    /**
     * 回调：清除视频图文消息
     */
    public void onEventMainThread(AllReadEntity event) {
        if (event != null && event.isResult()) {
            onRefresh();
        }
    }

    /**
     * 回调：视频图文消息
     */
    public void onEventMainThread(MessageMyMessageEntity event) {

        if (message == MESSAGE_VIDEO) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    if (page == 1) {
                        videoData.clear();
                    }
                    videoData.addAll(event.getData().getList());
                    videoAdapter.notifyDataSetChanged();
                    ++page;
                }
            }
            isRefreshing = false;
            refreshComplete();
        }
    }

    /**
     * 回调：系统消息
     */
    public void onEventMainThread(MessageSysMessageEntity event) {

        if (message == MESSAGE_SYSTEM) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    if (page == 1) {
                        systemData.clear();
                    }
                    systemData.addAll(event.getData().getList());
                    systemAdapter.notifyDataSetChanged();
                    ++page;
                }
            }
            isRefreshing = false;
            refreshComplete();
        }
    }

    /**
     * 回调：圈子消息
     */
    public void onEventMainThread(MessageGroupMessageEntity event) {

        if (message == MESSAGE_GAME) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    if (page == 1) {
                        gameData.clear();
                    }
                    gameData.addAll(event.getData().getList());
                    gameAdapter.notifyDataSetChanged();
                    ++page;
                }
            }
            isRefreshing = false;
            refreshComplete();
        }
    }
}
