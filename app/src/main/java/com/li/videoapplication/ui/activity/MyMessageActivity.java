package com.li.videoapplication.ui.activity;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ifeimo.im.common.adapter.OnAdapterItemOnClickListener;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.framwork.view.InformationView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.MessageMsgRedEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 活动：我的消息
 */
public class MyMessageActivity extends TBaseActivity implements OnItemClickListener {
    /**
     * 跳转：视频消息列表
     */
    private void startMessageListActivity1() {
        MessageListActivity.showVideo(this);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "我的消息-视频消息");
    }

    /**
     * 跳转：圈子消息列表
     */
    private void startMessageListActivity2() {
        MessageListActivity.showGame(this);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "我的消息-圈子消息");
    }

    /**
     * 跳转：系统消息列表
     */
    private void startMessageListActivity3() {
        MessageListActivity.showSystem(this);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "我的消息-系统消息");
    }


    private ListView listView;
    private MessageAdapter adapter;
    private List<MessageAdapter.Message> data = new ArrayList<>();
    private InformationView feiMoIMList;
    private View mEmptyView;
    private int mFMCount = 0;
    private int mRongCount = 0;
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mymessage;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle(R.string.message_title);
    }

    @Override
    public void initView() {
        super.initView();
        //因为聊天使用融云和openFire两套系统，因此将融云的empty View隐藏，自己来判断
        mEmptyView = findViewById(R.id.ll_my_message_is_empty);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new MessageAdapter(this, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        //飞磨IM消息列表
        feiMoIMList = (InformationView) findViewById(R.id.feimo_im_list);
        //初始化
        feiMoIMList.init();
        //消息列表点击时重连（避免账号在异端登陆时挤掉线）
        feiMoIMList.setOnAdapterItemOnClickListener(new OnAdapterItemOnClickListener() {
            @Override
            public boolean onItemOnClick(InformationModel informationModel) {
                Member member = getUser();
                FeiMoIMHelper.Login(getMember_id(), member.getNickname(), member.getAvatar());
                return true;
            }
        });

    }

    @Override
    public void loadData() {
        super.loadData();
        MessageAdapter.Message video = new MessageAdapter.Message();
        video.setContent(resources.getString(R.string.message_video));
        video.setCount(0);
        video.setPic(R.drawable.message_vedio);

        MessageAdapter.Message game = new MessageAdapter.Message();
        game.setContent(resources.getString(R.string.message_game));
        game.setCount(0);
        game.setPic(R.drawable.message_game);

        MessageAdapter.Message system = new MessageAdapter.Message();
        system.setContent(resources.getString(R.string.message_system));
        system.setCount(0);
        system.setPic(R.drawable.message_system);

        data.add(video);
        data.add(game);
        data.add(system);

        adapter.notifyDataSetChanged();
        enterFragment();
    }

    private void enterFragment() {
        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlistFragment);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);

        //message is empty
        mRongCount = fragment.getAdapter().getCount();
        mFMCount = ((RecyclerView)feiMoIMList.findViewById(R.id.com_im_id_main_list)).getAdapter().getItemCount();
        setEmptyView();

        feiMoIMList.setSupport(new InformationView.Support() {
            @Override
            public void messageCount(int count) {
                mFMCount = count;
                setEmptyView();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 圈子消息总数
        DataManager.messageMsgRed(getMember_id());

    }

    private void setEmptyView(){
        if (mRongCount+mFMCount == 0){
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mEmptyView.setVisibility(View.GONE);
        }

    }

    /**
     * 回调：圈子消息总数
     */
    public void onEventMainThread(MessageMsgRedEntity event) {

        if (event.isResult()) {
            String vpNum = event.getData().getVpNum();
            String groupNum = event.getData().getGroupNum();
            String sysNum = event.getData().getSysNum();

            int a = 0;
            try {
                a = Integer.valueOf(vpNum);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            data.get(0).setCount(a);

            a = 0;
            try {
                a = Integer.valueOf(groupNum);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            data.get(1).setCount(a);

            a = 0;
            try {
                a = Integer.valueOf(sysNum);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            data.get(2).setCount(a);

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MessageAdapter.Message message = (MessageAdapter.Message) parent.getAdapter().getItem(position);
        if (message.getContent().equals(resources.getString(R.string.message_video))) {
            startMessageListActivity1();
        } else if (message.getContent().equals(resources.getString(R.string.message_game))) {
            startMessageListActivity2();

        } else if (message.getContent().equals(resources.getString(R.string.message_system))) {
            startMessageListActivity3();
        }
    }
}
