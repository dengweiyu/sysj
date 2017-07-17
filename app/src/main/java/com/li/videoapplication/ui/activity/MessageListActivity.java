package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GroupMessage;
import com.li.videoapplication.data.model.entity.MyMessage;
import com.li.videoapplication.data.model.entity.SysMessage;
import com.li.videoapplication.data.model.event.ReadMessageEntity;
import com.li.videoapplication.data.model.response.AllReadEntity;
import com.li.videoapplication.data.model.response.MessageGroupMessageEntity;
import com.li.videoapplication.data.model.response.MessageMyMessageEntity;
import com.li.videoapplication.data.model.response.MessageSysMessageEntity;
import com.li.videoapplication.data.model.response.RewardAndPlayWithMsgEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.mvp.adapter.RewardAndPlayWithAdapter;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.GameMessageAdapter;
import com.li.videoapplication.ui.adapter.SystemMessageAdapter;
import com.li.videoapplication.ui.adapter.VideoMessageAdapter;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;
import io.rong.imkit.model.Event;

/**
 * 活动：消息
 */
public class MessageListActivity extends PullToRefreshActivity<Game> implements View.OnClickListener {

    /**
     * 跳转：视频消息
     */
    public static void showActivity(Context context,String title ,String url,String type) {
        ActivityManager.startMessageListActivity(context, title,url,type);
    }

    public String mTitle;
    private String mUrl;
    private String mType;
    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            mTitle = getIntent().getStringExtra("title");
            mUrl = getIntent().getStringExtra("url");
            mType = getIntent().getStringExtra("type");
        } catch (Exception e) {
            e.printStackTrace();
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
    private RewardAndPlayWithAdapter mRewardAdapter;

    private List<MyMessage> videoData = new ArrayList<>();
    private static List<GroupMessage> gameData = new ArrayList<>();
    private List<SysMessage> systemData = new ArrayList<>();

    private  List<RewardAndPlayWithMsgEntity.DataBean.ListBean> mRewardData = new ArrayList<>();
    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle(mTitle);
    }

    @Override
    public void initView() {
        super.initView();

        if (gameData == null){
            gameData = new ArrayList<>();
        }
        videoAdapter = new VideoMessageAdapter(this, videoData,mType);
        gameAdapter = new GameMessageAdapter(this, gameData,mType);
        systemAdapter = new SystemMessageAdapter(this, systemData);
        mRewardAdapter = new RewardAndPlayWithAdapter(this,mRewardData,mType);

        View emptyView = getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) listView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        listView.setEmptyView(emptyView);

        abMessageClean.setVisibility(View.VISIBLE);
        abMessageClean.setOnClickListener(this);

        if ("video".equals(mType)) {

            setAdapter(videoAdapter);
            emptyText.setText("没有视频消息");
        } else if ("group".equals(mType)) {
            setAdapter(gameAdapter);
            emptyText.setText("没有圈子消息");
        } else if ("sysm".equals(mType)) {
            setAdapter(systemAdapter);
            emptyText.setText("没有系统消息");
            abMessageClean.setVisibility(View.GONE);
        }else if ("training".equals(mType)){
            setAdapter(mRewardAdapter);
            emptyText.setText("没有陪练消息");
        }else if ("reward".equals(mType)){
            setAdapter(mRewardAdapter);
            emptyText.setText("没有打赏消息");
        }
    }

    @Override
    public void loadData() {
        super.loadData();

        onPullDownToRefresh(pullToRefreshListView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ab_message_clean:
               // DataManager.allRead(getMember_id(),mType);

                ReadMessageEntity entity = new ReadMessageEntity();
                entity.setSymbol(mType);
                entity.setAll(1);
                EventBus.getDefault().post(entity);

                clearUnReadeMsg();
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

        if ("video".equals(mType)) {
            // 视频图文消息
            DataManager.messageMyMessage(getMember_id(), page,mUrl);
        } else if ("group".equals(mType)) {
            //还有本地缓存数据 将使用本地缓存
            if (gameData != null && gameData.size() > 0){
                return;
            }
            // 圈子消息
            DataManager.messageGroupMessage(getMember_id(), page,mUrl);
        } else if ("sysm".equals(mType)) {
            // 系统消息
            DataManager.messageSysMessage(getMember_id(), page,mUrl);
        }else if ("training".equals(mType) || "reward".equals(mType)){
            DataManager.getRewardAndPlayWithMsg(getMember_id(), page,mUrl);
        }
    }

    public String getMemberId(){
        return getMember_id();
    }

    public void clearUnReadeMsg(){
        if(videoData != null){
            for (int i = 0; i < videoData.size(); i++) {
                videoData.get(i).setMark("0");
            }
            if (videoAdapter != null){
                videoAdapter.notifyDataSetChanged();
            }
        }

        if(gameData != null){
            for (int i = 0; i < gameData.size(); i++) {
                gameData.get(i).setNew_data_num("0");
            }
            if (gameAdapter != null){
                gameAdapter.notifyDataSetChanged();
            }
        }

        if(mRewardData != null){
            for (int i = 0; i < mRewardData.size(); i++) {
                mRewardData.get(i).setMark("0");
            }
            if (mRewardAdapter != null){
                mRewardAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 清除圈子本地缓存
     */
    public static void clearGameData(){
        if (gameData != null){
            gameData.clear();
            gameData =  null;
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

        if ("video".equals(mType)) {
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

        if ("sysm".equals(mType)) {
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

        if ("group".equals(mType)) {
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

    /**
     * 打赏或者陪练消息
     */

    public void onEventMainThread(RewardAndPlayWithMsgEntity entity){

        if ("reward".equals(mType) || "training".equals(mType)){
            if (entity.isResult()){
                if (entity.getData().getList().size() > 0){
                    if (page == 1){
                        mRewardData.clear();
                    }
                    mRewardData.addAll(entity.getData().getList());
                    mRewardAdapter.notifyDataSetChanged();
                    ++page;
                }
            }
            isRefreshing = false;
            refreshComplete();
        }
    }
}
