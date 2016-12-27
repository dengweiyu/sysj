package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.data.model.entity.Tag;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.VideoActivity;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.SpanUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;
/**
 * 适配器：云端视频
 */
public class MyCloudVideoAdapter extends BaseAdapter {

    public static final String TAG = MyCloudVideoAdapter.class.getSimpleName();

	/**
	 * 跳转：分享
	 */
	public void startShareActivity(String url, String imageUrl, String videoTitle) {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
		ActivityManeger.startActivityShareActivity4MyCloudVideo(activity, url, imageUrl, videoTitle);
	}

	/**
	 * 跳转：视频播放
	 */
	private void startPlayerActivity(VideoImage item) {
        if (item == null)
            return;
        String yk_url = item.getYk_url();
        String youku_url = AppConstant.getYoukuUrl(yk_url);
        String qn_key = item.getQn_key();
        String qn_url = AppConstant.getQnUrl(qn_key);
        Log.d(TAG, "startPlayerActivity: yk_url=" + yk_url);
        Log.d(TAG, "startPlayerActivity: youku_url=" + youku_url);
        Log.d(TAG, "startPlayerActivity: qn_key=" + qn_key);
        Log.d(TAG, "startPlayerActivity: qn_url=" + qn_url);
        if (StringUtil.isNull(qn_key)) {
            // 优酷视频
            try {
                IntentHelper.startActivityActionView(context, youku_url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 七牛视频
            try {
                // IntentHelper.startActivityActionViewVideo(context, qn_url);
                VideoActivity.startVideoActivity(context, qn_url, item.getTitle());
                Log.d(TAG, "startPlayerActivity: 1");
            } catch (Exception exc) {
                exc.printStackTrace();
                try {
                    IntentHelper.startActivityActionView(context, qn_url);
                    Log.d(TAG, "startPlayerActivity: 2");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d(TAG, "startPlayerActivity: 3");
                }
            }
        }
    }

	private List<VideoImage> data;
	private Context context;
	private ViewHolder holder;
	private LayoutInflater inflater;

	public MyCloudVideoAdapter(List<VideoImage> data, Context context) {
        this.data = data;
		this.context = context;
		this.inflater = LayoutInflater.from(context);

		VideoMangerActivity.myCloudVideoDeleteData.clear();
		for (int i = 0; i < this.data.size(); i++) {
			VideoMangerActivity.myCloudVideoDeleteData.add(false);
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public VideoImage getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

    public View getView(final int position, View convertView, ViewGroup parent) {

        final VideoImage record = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_mycloudvideo, null);
            holder.root = convertView.findViewById(R.id.root);
            holder.cover = (ImageView) convertView.findViewById(R.id.mycloudvideo_cover);
            holder.title = (EditText) convertView.findViewById(R.id.mycloudvideo_title);
            holder.playTime = (TextView) convertView.findViewById(R.id.mycloudvideo_playTime);
            holder.commentTime = (TextView) convertView.findViewById(R.id.mycloudvideo_commentTime);
            holder.likeTime = (TextView) convertView.findViewById(R.id.mycloudvideo_likeTime);
            holder.shareTime = (TextView) convertView.findViewById(R.id.mycloudvideo_shareTime);
            holder.deleteButton = (TextView) convertView.findViewById(R.id.mycloudvideo_deleteButton);
            holder.deleteState = (CheckBox) convertView.findViewById(R.id.mycloudvideo_deleteState);
            holder.play = (ImageView) convertView.findViewById(R.id.mycloudvideo_play);
            holder.share = (ImageView) convertView.findViewById(R.id.mycloudvideo_share);
            holder.state = (TextView) convertView.findViewById(R.id.mycloudvideo_state);
            holder.delete = (ImageView) convertView.findViewById(R.id.mycloudvideo_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(record.getTitle());
        SpanUtil.lastSelection(holder.title);
        List<Tag> tags = record.getGame_tag();
        if (tags != null && tags.size() > 0) {
            for (Tag tag :
                    tags) {
                try {
                    SpanUtil.insertText(holder.title, "#" + tag.getName() + "#");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ImageLoaderHelper.displayImageWhite(record.getFlag(), holder.cover);

        setLikeTime(record, holder.likeTime);
        setCommentTime(record, holder.commentTime);
        setPlayTime(record, holder.playTime);
        setShareTime(record, holder.shareTime);
        setState(record, holder.state);

		// 如果处于批量删除状态
		if (VideoMangerActivity.isDeleteMode) {
			inDeleteState(convertView, position, record);
		} else {
			outDeleteState(convertView, position, record);
		}

        // 播放监听
        holder.cover.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
/*
                1：转码完成，可以播放
                3：转码中
                4：审核中*/
                if (record.getState() == 1) {
                    startPlayerActivity(record);
                } else if (record.getState() == 3) {
                    ToastHelper.s("视频转码中...");
                } else if (record.getState() == 4) {
                    ToastHelper.s("视频审核中...");
                }
            }
        });

        // 分享监听
        holder.share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                final String url = AppConstant.getMUrl(record.getQn_key());
                final String imageUrl = record.getFlag();
                final String VideoTitle = record.getTitle();

                startShareActivity(url, imageUrl, VideoTitle);
            }
        });

        holder.deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = VideoMangerActivity.myCloudVideoDeleteData.get(position);
                if (b) {
                    VideoMangerActivity.myCloudVideoDeleteData.set(position, false);
                    VideoMangerActivity.removeMyCloudData(record);
                    VideoMangerActivity.refreshAbTitle2();
                } else {
                    VideoMangerActivity.myCloudVideoDeleteData.set(position, true);
                    VideoMangerActivity.addMyCloudData(data.get(position));
                    VideoMangerActivity.refreshAbTitle2();
                }
                boolean c = VideoMangerActivity.myCloudVideoDeleteData.get(position);
                notifyDataSetChanged();
            }
        });

        holder.root.setId(-1);
        holder.root.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                v.setId(position);
                VideoMangerActivity.performClick2();
                return false;
            }
        });
		return convertView;
	}

    /**
     * 状态
     *
     1：转码完成，可以播放
     3：转码中
     4：审核中
     */
    private void setState(final VideoImage record, TextView view) {
        String s;
        if (record.getState() == 3) {
            s = "视频转码中";
        } else if (record.getState() == 4) {
            s = "视频审核中";
        } else {
            s = "";
        }
        view.setText(s);
    }

    /**
     * 上传时间
     */
    private void setUptime(final VideoImage record, TextView view) {
        //上传于：\n\t\t2015-12-11 10:00
        // 2015-12-11
        String s;
        try {
            s = TimeHelper.getSysMessageTime(record.getUptime());
        } catch (Exception e) {
            e.printStackTrace();
            s = "";
        }
        view.setText(s);
    }

    /**
     * 上传时间
     */
    private void setAllTime(final VideoImage record, TextView view) {
        // 12:16
        String s;
        try {
            s = TimeHelper.getVideoPlayTime(record.getTime_length());
        } catch (Exception e) {
            e.printStackTrace();
            s = "";
        }
        view.setText(s);
    }

    /**
     * 点赞
     */
    private void setLikeTime(final VideoImage record, TextView view) {
        String s;
        try {
            s = "点赞 " + numberTransform(record.getFlower_count());
        } catch (Exception e) {
            e.printStackTrace();
            s = "点赞 0";
        }
        view.setText(s);
    }

    /**
     * 评论
     */
    private void setCommentTime(final VideoImage record, TextView view) {
        String s;
        try {
            s = "评论 " + numberTransform(record.getComment_count());
        } catch (Exception e) {
            e.printStackTrace();
            s = "评论 0";
        }
        view.setText(s);
    }

    /**
     * 播放
     */
    private void setPlayTime(final VideoImage record, TextView view) {
        String s;
        try {
            s = "播放 " + numberTransform(record.getClick_count());
        } catch (Exception e) {
            e.printStackTrace();
            s = "播放 0";
        }
        view.setText(s);
    }

    /**
     * 分享
     */
    private void setShareTime(final VideoImage record, TextView view) {
        String s;
        try {
            s = "收藏 " + numberTransform(record.getCollection_count());
        } catch (Exception e) {
            e.printStackTrace();
            s = "收藏 0";
        }
        view.setText(s);
    }

    /**
     * 数据转换 10万以下，数字全显 10万或以上，万为单位，如12.12万
     */
    private String numberTransform(String number) throws Exception {
        float f = Float.parseFloat(number);
        if (f < 100000) {
            int i = (int) f;
            return i + "";
        } else {
            f = f / 10000;
            return f + "万";
        }
    }

	/**
	 * 处于批量删除状态
	 */
	private void inDeleteState(final View view, final int position, final VideoImage record) {
		// 如果是通过长按召唤出的，将长按的控件设置为选中
		if (view.getId() != -1) {
            VideoMangerActivity.myCloudVideoDeleteData.set(position, true);
            VideoMangerActivity.addMyCloudData(record);
            VideoMangerActivity.refreshAbTitle2();
            view.setId(-1);
        }
        holder.play.setVisibility(View.GONE);
        holder.deleteButton.setVisibility(View.VISIBLE);
        holder.deleteButton.setId(position);
        holder.deleteState.setVisibility(View.VISIBLE);
        holder.deleteState.setChecked(VideoMangerActivity.myCloudVideoDeleteData.get(position));
	}

    /**
     * 处于正常状态
     */
	private void outDeleteState(final View view, final int position, final VideoImage record) {
		VideoMangerActivity.myCloudVideoDeleteData.clear();
		for (int i = 0; i < data.size(); i++) {
			VideoMangerActivity.myCloudVideoDeleteData.add(false);
		}
		holder.play.setVisibility(View.VISIBLE);
		holder.deleteButton.setVisibility(View.GONE);
		holder.deleteState.setVisibility(View.GONE);
        holder.deleteState.setChecked(VideoMangerActivity.myCloudVideoDeleteData.get(position));
	}

    private static class ViewHolder {
        View root;
        // 封面
        ImageView cover;
        // 标题
        EditText title;
        // 点赞，播放，评论，分享
        TextView likeTime, playTime, commentTime, shareTime;
        //播放
        ImageView play;
        //批量选择勾选按键
        CheckBox deleteState;
        //批量选择绿色图标
        TextView deleteButton;
        // 状态
        TextView state;
        // 分享
        ImageView share;
        // 删除
        ImageView delete;
    }
}
