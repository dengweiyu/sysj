package com.li.videoapplication.ui.fragment;

import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.CommentDelEntity;
import com.li.videoapplication.data.model.response.VideoCommentListEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.adapter.VideoPlayCommentAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.sparkbutton.SparkButton;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.AbsListViewOverScrollDecorAdapter;

/**
 * 碎片：视频播放/图文详情-视频评论
 */
public class VideoPlayCommentFragment extends TBaseFragment implements OnRefreshListener2<ListView>,
        View.OnClickListener {


    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManeger.startPlayerDynamicActivity(getActivity(), member);
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-头像");
    }

    private VideoImage videoImage;

    public void setVideoImage(VideoImage videoImage) {
        this.videoImage = videoImage;

        refreshContentView(videoImage);
        onPullDownToRefresh(pullToRefreshListView);
    }

    private VideoPlayActivity activity;
    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private VideoPlayCommentAdapter adapter;
    private List<Comment> data;
    private boolean isFirstIn = true;

    private int page = 1;

    public List<Comment> getData() {
        return data;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_videoplay_video;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshListView;
    }

    public void smoothScrollToPosition(int position) {
        listView.smoothScrollToPosition(position);
    }

    @Override
    protected void initContentView(View view) {
        activity = (VideoPlayActivity) getActivity();

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(Mode.PULL_FROM_END);
        listView = pullToRefreshListView.getRefreshableView();
        listView.addHeaderView(getHeaderView());
        new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(listView));

        data = new ArrayList<>();
        adapter = new VideoPlayCommentAdapter(getActivity(), data);
        listView.setAdapter(adapter);

        pullToRefreshListView.setOnRefreshListener(this);

        refreshContentView(videoImage);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        onPullUpToRefresh(refreshView);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

        // 视频评论列表
        DataManager.videoCommentList211(videoImage.getId(), getMember_id(), page);
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
    }

    private View headerView;
    private CircleImageView head;
    private ImageView isV;
    private TextView name;
    private TextView time;
    private TextView focus;
    private TextView content;
    private TextView more;

    private TextView playCount;
    private SparkButton good;
    private TextView goodCount;
    private SparkButton bad;
    private TextView badCount;
    private SparkButton star;
    private TextView starCount;
    private View empty;

    private View getHeaderView() {

        if (headerView == null) {
            headerView = inflater.inflate(R.layout.header_videoplay_comment, null);
            head = (CircleImageView) headerView.findViewById(R.id.videoplay_head);
            isV = (ImageView) headerView.findViewById(R.id.videoplay_v);
            name = (TextView) headerView.findViewById(R.id.videoplay_name);
            time = (TextView) headerView.findViewById(R.id.videoplay_time);
            focus = (TextView) headerView.findViewById(R.id.videoplay_focus);
            content = (TextView) headerView.findViewById(R.id.videoplay_content);
            more = (TextView) headerView.findViewById(R.id.videoplay_more);

            playCount = (TextView) headerView.findViewById(R.id.videoplay_playCount);
            good = (SparkButton) headerView.findViewById(R.id.videoplay_good);
            goodCount = (TextView) headerView.findViewById(R.id.videoplay_goodCount);
            bad = (SparkButton) headerView.findViewById(R.id.videoplay_bad);
            badCount = (TextView) headerView.findViewById(R.id.videoplay_badCount);
            star = (SparkButton) headerView.findViewById(R.id.videoplay_star);
            starCount = (TextView) headerView.findViewById(R.id.videoplay_starCount);

            empty = headerView.findViewById(R.id.ll_video_player_empty);

            head.setOnClickListener(this);
            focus.setOnClickListener(this);
            good.setOnClickListener(this);
            bad.setOnClickListener(this);
            star.setOnClickListener(this);
            more.setOnClickListener(this);

            badCount.setText("0");
        }
        return headerView;
    }

    private void refreshTextLength() {
        content.post(new Runnable() {
            @Override
            public void run() {
                Layout l = content.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();
                    if (lines > 0) {
                        if (isFirstIn) {
                            if (l.getEllipsisCount(lines - 1) > 0) {//有...
                                more.setVisibility(View.VISIBLE);
                            } else {//没...
                                more.setVisibility(View.GONE);
                            }
                            isFirstIn = false;
                        } else {
                            if (l.getEllipsisCount(lines - 1) > 0) {//有...
                                content.setEllipsize(null); // 展开
                                content.setSingleLine(false);
                                more.setText("点击收起");
                            } else {//没...
                                content.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                                content.setLines(1);
                                more.setText("展开全文");
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.videoplay_head:// 头像
                startPlayerDynamicActivity(getMember(videoImage));
                break;

            case R.id.ab_videoplay_remark:// 分享，举报
                DialogManager.showVideoPlayDialog(getActivity(), videoImage);
                break;

            case R.id.videoplay_focus:// 关注
                if (isLogin()) {
                    boolean flag;
                    if (videoImage.getMember_tick() == 0) {
                        videoImage.setMember_tick(1);
                        flag = true;
                    } else {
                        videoImage.setMember_tick(0);
                        flag = false;
                    }
                    refreshContentView(videoImage);
                    // 玩家关注
                    DataManager.memberAttention201(videoImage.getMember_id(), getMember_id());
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-关注");
                } else {
                    DialogManager.showLogInDialog(getActivity());
                }
                break;

            case R.id.videoplay_more:
                refreshTextLength();
                break;

            case R.id.videoplay_star:// 收藏
                if (!isLogin()) {
                    DialogManager.showLogInDialog(getActivity());
                    return;
                }
                if (videoImage.getCollection_tick() == 0) {
                    star.setChecked(true);
                    star.playAnimation();
                    videoImage.setCollection_tick(1);
                    videoImage.setCollection_count(Integer.valueOf(videoImage.getCollection_count()) + 1 + "");
                    // 提交收藏任务
                    DataManager.TASK.doTask_20(getMember_id());
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-收藏");
                } else {
                    star.setChecked(false);
                    videoImage.setCollection_tick(0);
                    videoImage.setCollection_count(Integer.valueOf(videoImage.getCollection_count()) - 1 + "");
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-取消收藏");
                }
                setTextViewText(starCount, videoImage.getCollection_count());
                // 视频收藏
                DataManager.videoCollect2(videoImage.getId(), getMember_id());
                activity.videoPlayView.controllerViewLand.refreshIconView(videoImage);
                break;

            case R.id.videoplay_good:// 点赞
                if (videoImage.getFlower_tick() == 0) {
                    good.setChecked(true);
                    good.playAnimation();
                    videoImage.setFlower_tick(1);
                    videoImage.setFlower_count(Integer.valueOf(videoImage.getFlower_count()) + 1 + "");

                    if (videoImage.getFndown_tick() == 1) {//如果已点踩
                        //将踩去掉
                        bad.setChecked(false);
                        videoImage.setFndown_tick(0);
                        videoImage.setFndown_count(Integer.valueOf(videoImage.getFndown_count()) - 1 + "");
                        // 刷新点踩数量
                        setTextViewText(badCount, videoImage.getFndown_count());
                        // 踩
                        DataManager.fndownClick203(videoImage.getId(), getMember_id());
                    }
                    // 提交点赞任务
                    DataManager.TASK.doTask_21(getMember_id());
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-点赞");
                } else {
                    good.setChecked(false);
                    videoImage.setFlower_tick(0);
                    videoImage.setFlower_count(Integer.valueOf(videoImage.getFlower_count()) - 1 + "");
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-取消赞");
                }
                // 点赞数量
                setTextViewText(goodCount, videoImage.getFlower_count());
                // 视频点赞
                DataManager.videoFlower2(videoImage.getId(), getMember_id());
                activity.videoPlayView.controllerViewLand.refreshIconView(videoImage);
                break;

            case R.id.videoplay_bad:// 踩
                if (videoImage.getFndown_tick() == 0) {
                    bad.setChecked(true);
                    bad.playAnimation();
                    videoImage.setFndown_tick(1);
                    videoImage.setFndown_count(Integer.valueOf(videoImage.getFndown_count()) + 1 + "");
                    if (videoImage.getFlower_tick() == 1) {
                        //因为点踩，所以将赞去掉
                        good.setChecked(false);
                        videoImage.setFlower_tick(0);
                        videoImage.setFlower_count(Integer.valueOf(videoImage.getFlower_count()) - 1 + "");
                        // 刷新点赞数量
                        setTextViewText(goodCount, videoImage.getFlower_count());
                        // 视频点赞
                        DataManager.videoFlower2(videoImage.getId(), getMember_id());
                    }
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-点踩");
                } else {
                    bad.setChecked(false);
                    videoImage.setFndown_tick(0);
                    videoImage.setFndown_count(Integer.valueOf(videoImage.getFndown_count()) - 1 + "");
                    UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-取消踩");
                }
                setTextViewText(badCount, videoImage.getFndown_count());
                // 视频踩
                DataManager.fndownClick203(videoImage.getId(), getMember_id());
                activity.videoPlayView.controllerViewLand.refreshIconView(videoImage);
                break;
        }
    }

    private Member getMember(VideoImage item) {
        Member member = new Member();
        member.setMember_id(item.getMember_id());
        member.setNickname(item.getNickname());
        member.setAvatar(item.getAvatar());
        return member;
    }

    //视频全屏状态下点赞等操作后，刷新评论页面图标状态。
    public void refreshStar() {
        if (videoImage.getCollection_tick() == 0) {
            star.setChecked(false);
        } else {
            star.setChecked(true);
        }
        setTextViewText(starCount, videoImage.getCollection_count());
    }

    public void refreshGood() {
        if (videoImage.getFlower_tick() == 0) {//未点赞
            good.setChecked(false);
        } else {
            good.setChecked(true);
        }
        setTextViewText(goodCount, videoImage.getFlower_count());
    }

    public void refreshBad() {
        if (videoImage.getFndown_tick() == 0) {
            bad.setChecked(false);
        } else {
            bad.setChecked(true);
        }
        setTextViewText(badCount, videoImage.getFndown_count());
    }

    /**
     * 刷新界面
     */
    private void refreshContentView(VideoImage item) {
        if (item != null && headerView != null) {
            setImageViewImageNet(head, item.getAvatar());
            setTextViewText(name, item.getNickname());
            setUptime(time, item);

            setTextViewTextVisibility(content, item.getV_description());

            isFirstIn = true;
            refreshTextLength();

            setPlayCount(playCount, item);
            setFocus(item);
            setGood(good, item);
            setBad(bad, item);
            setStar(star, item);
            setTextViewText(goodCount, item.getFlower_count());
            setTextViewText(starCount, item.getCollection_count());
            setTextViewText(badCount, item.getFndown_count());

            if (item.isV())
                isV.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 上传时间
     */
    private void setUptime(TextView view, VideoImage item) {
        // 16小时前上传
        try {
            setTextViewText(view, TimeHelper.getVideoImageUpTime(item.getUptime()));
        } catch (Exception e) {
            e.printStackTrace();
            setTextViewText(view, item.getUptime());
        }
    }

    /**
     * 关注
     */
    public void setFocus(VideoImage item) {
        if (item != null) {
            if (item.getMember_tick() == 1) {// 已关注状态
                focus.setBackgroundResource(R.drawable.player_focus_gray);
                focus.setTextColor(resources.getColorStateList(R.color.groupdetail_player_white));
                focus.setText(R.string.videoplay_focused);
            } else {// 未关注状态
                focus.setBackgroundResource(R.drawable.player_focus_red);
                focus.setTextColor(resources.getColorStateList(R.color.groupdetail_player_red));
                focus.setText(R.string.videoplay_focus);
            }
        }
    }

    /**
     * 点赞
     */
    private void setGood(SparkButton view, VideoImage item) {
        if (item != null) {
            if (item.getFlower_tick() == 1) {// 已点赞状态
                view.setChecked(true);
            } else {// 未点赞状态
                view.setChecked(false);
            }
        }
    }

    /**
     * 踩
     */
    private void setBad(SparkButton view, VideoImage item) {
        if (item != null) {
            if (item.getFndown_tick() == 1) {// 已点踩状态
                view.setChecked(true);
            } else {// 未点踩状态
                view.setChecked(false);
            }
        }
    }

    /**
     * 收藏
     */
    private void setStar(SparkButton view, VideoImage item) {
        if (item != null) {
            if (item.getCollection_tick() == 1) {// 已收藏状态
                view.setChecked(true);
            } else {// 未收藏状态
                view.setChecked(false);
            }
        }
    }

    /**
     * 播放次数
     */
    private void setPlayCount(TextView view, VideoImage item) {
        if (item != null && !StringUtil.isNull(item.getClick_count())) {
            // 2.1万次播放
            setTextViewText(view, StringUtil.formatNum(item.getClick_count()) + " 次播放");
        }
    }

    /**
     * 回调:视频评论列表
     */
    public void onEventMainThread(VideoCommentListEntity event) {

        if (event.isResult()) {
            if (event.getData().getList() != null && event.getData().getList().size() > 0) {
                if (page == 1) {
                    data.clear();
                }
                empty.setVisibility(View.GONE);
                data.addAll(event.getData().getList());
                adapter.notifyDataSetChanged();
                ++page;
            }else {
                empty.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        }
        onRefreshComplete();
    }

    /**
     * 回调:视频评论删除
     */
    public void onEventMainThread(CommentDelEntity event) {

        if (event != null && event.isResult()) {
            ToastHelper.s("评论删除成功");
            onPullDownToRefresh(pullToRefreshListView);
        }
    }
}
