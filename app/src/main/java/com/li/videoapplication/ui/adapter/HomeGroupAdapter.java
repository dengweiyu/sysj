package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.mvp.billboard.view.BillboardActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.HorizontalListView;

/**
 * 适配器：首页
 */
@SuppressLint({"InflateParams", "CutPasteId"})
public class HomeGroupAdapter extends BaseArrayAdapter<VideoImageGroup> {

    /**
     * 跳转：首页更多
     */
    private void startHomeMoreActivity(VideoImageGroup group) {
        ActivityManeger.startHomeMoreActivity(getContext(), group);
    }

    /**
     * 跳转：风云榜
     */
    private void startBillboardActivity() {
        ActivityManeger.startBillboardActivity(getContext(), BillboardActivity.TYPE_PLAYER);
    }

    public HomeGroupAdapter(Context context, List<VideoImageGroup> data) {
        super(context, R.layout.adapter_group, data);
    }

    @Override
    public int getItemViewType(int position) {

        if (getItem(position).getType() == VideoImageGroup.TYPE_FIRST ||
                getItem(position).getType() == VideoImageGroup.TYPE_THIRD) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressWarnings({"unused"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final VideoImageGroup record = getItem(position);
        VideoViewHolder videoViewHolder = null;
        MemberViewHolder memberViewHolder = null;
        if (view == null) {
            if (record.getType() == VideoImageGroup.TYPE_FIRST || record.getType() == VideoImageGroup.TYPE_THIRD) {// 视频
                view = inflater.inflate(R.layout.adapter_group, null);
                videoViewHolder = new VideoViewHolder();
                videoViewHolder.title = (TextView) view.findViewById(R.id.group_title);
                videoViewHolder.play = (ImageView) view.findViewById(R.id.video_play);
                videoViewHolder.icon = (ImageView) view.findViewById(R.id.group_icon);
                videoViewHolder.grid = (GridView) view.findViewById(R.id.gridview);
                view.setTag(videoViewHolder);
            } else {// 热门解说
                view = inflater.inflate(R.layout.header_home_hotnarrate, null);
                memberViewHolder = new MemberViewHolder();
                memberViewHolder.title = (TextView) view.findViewById(R.id.home_hotnarrate_title);
                memberViewHolder.horizontal = (HorizontalListView) view.findViewById(R.id.horizontallistvierw);
                setHotNarrateView(memberViewHolder.horizontal);
                view.setTag(memberViewHolder);
            }
        } else {
            if (record.getType() == VideoImageGroup.TYPE_FIRST || record.getType() == VideoImageGroup.TYPE_THIRD) {// 视频
                videoViewHolder = (VideoViewHolder) view.getTag();
            } else {// 热门解说
                memberViewHolder = (MemberViewHolder) view.getTag();
            }
        }

        if (record.getType() == VideoImageGroup.TYPE_FIRST || record.getType() == VideoImageGroup.TYPE_THIRD) {// 视频
            setTextViewText(videoViewHolder.title, record.getTitle());
            setIcon(videoViewHolder.icon, record);

            VideoAdapter adapter = new VideoAdapter(getContext(), record.getList());
            videoViewHolder.grid.setAdapter(adapter);
            adapter.setVideoType(record.getMore_mark());
            adapter.notifyDataSetChanged();
        } else {// 热门解说
            memberViewHolder.title.setText("热门主播");
            HotNarrateAdapter adapter = new HotNarrateAdapter(getContext(), record.getMembers());
            memberViewHolder.horizontal.setAdapter(adapter);
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (record.getType() == VideoImageGroup.TYPE_FIRST || record.getType() == VideoImageGroup.TYPE_THIRD) {// 舞大大学堂，小小舞玩新游，阿沫爱品评，游戏类型视频
                    startHomeMoreActivity(record);
                } else if (record.getType() == VideoImageGroup.TYPE_SECOND) {// 热门解说
                    startBillboardActivity();
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MAIN, "热门主播更多");
                }
            }
        });

        return view;
    }

    private void setHotNarrateView(HorizontalListView view) {
        // 72
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        int h = ScreenUtil.dp2px(72);
        params.height = h;
        view.setLayoutParams(params);
    }

    private void setIcon(ImageView view, final VideoImageGroup record) {
        if (view != null) {
            view.setBackgroundColor(Color.TRANSPARENT);
            if (record.getIcon_pic() != null && URLUtil.isURL(record.getIcon_pic())) {// 有图 18/22
                setLinearLayoutLayoutParams(view, dp2px(14), dp2px(18));
                view.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));

                setImageViewImageNet(view, record.getIcon_pic());

            } else if (record.getType() == VideoImageGroup.TYPE_THIRD) { // 无图
                // 6/22
                setLinearLayoutLayoutParams(view, dp2px(6), dp2px(22));
                view.setImageDrawable(new ColorDrawable(Color.RED));
            }
        }
    }

    private static class VideoViewHolder {
        TextView title;
        ImageView play;
        ImageView icon;
        GridView grid;
    }

    private static class MemberViewHolder {
        TextView title;
        HorizontalListView horizontal;
    }
}
