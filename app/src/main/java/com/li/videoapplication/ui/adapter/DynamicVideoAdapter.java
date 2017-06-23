package com.li.videoapplication.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;

/**
 * 适配器：个人中心，动态
 */
@SuppressLint("InflateParams")
public class DynamicVideoAdapter extends BaseArrayAdapter<VideoImage>{

	/**
	 * 跳转：视频播放
	 */
	private void startVideoPlayActivity(VideoImage videoImage) {
		ActivityManager.startVideoPlayActivity(getContext(), videoImage);
	}

	public DynamicVideoAdapter(Context context, List<VideoImage> data) {
		super(context, R.layout.adapter_dynamic_video, data);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final VideoImage record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_dynamic_video, null);
			holder.day = (TextView) view.findViewById(R.id.dynamic_day);
			holder.month = (TextView) view.findViewById(R.id.dynamic_month);
			holder.title = (TextView) view.findViewById(R.id.dynamic_title);
			holder.cover = (ImageView) view.findViewById(R.id.dynamic_cover);

			holder.allTime = (TextView) view.findViewById(R.id.dynamic_allTime);
			holder.playCount = (TextView) view.findViewById(R.id.dynamic_playCount);

			holder.like = (ImageView) view.findViewById(R.id.dynamic_like);
			holder.star = (ImageView) view.findViewById(R.id.dynamic_star);
			holder.comment = (ImageView) view.findViewById(R.id.dynamic_comment);
			holder.likeCount = (TextView) view.findViewById(R.id.dynamic_likeCount);
			holder.starCount = (TextView) view.findViewById(R.id.dynamic_starCount);
			holder.commentCount = (TextView) view.findViewById(R.id.dynamic_commentCount);
			holder.upliadTime = (TextView) view.findViewById(R.id.tv_video_upload_time);
			holder.root = view.findViewById(R.id.ll_root);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		setDayAndMonth(holder.day, holder.month, record);
		setTextViewText(holder.title, record.getName());
		setImageViewImageNet(holder.cover, record.getFlag());

		// 播放时长
		setTimeLength(holder.allTime, record);
		setTextViewText(holder.playCount, record.getView_count());

		setTextViewText(holder.likeCount, record.getFlower_count());
		setTextViewText(holder.starCount, record.getCollection_count());
		setTextViewText(holder.commentCount, record.getComment_count());

		// 点赞设置
		setLike(record, holder.like);

		// 收藏设置
		setStar(record, holder.star);

		// 评论
		setComment(record, holder.comment);

		//上传时间
		if (record.getTime() != null){
			try {
				holder.upliadTime.setText(TimeHelper.getVideoImageUpTime(record.getTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//
		holder.root.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startVideoPlayActivity(record);
			}
		});

	/*	holder.cover.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startVideoPlayActivity(record);
			}
		});*/

		return view;
	}


	/**
	 * 视频时间长度
	 */
	private void setTimeLength(TextView view, VideoImage record) {
		// 17:00
		try {
			setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
		} catch (Exception e) {
			e.printStackTrace();
			setTextViewText(view, "");
		}
	}

	/**
	 * 设置天和月份
	 */
	private void setDayAndMonth(TextView dayTextView, TextView monthTextView, VideoImage record) {

		Long l = new Long(record.getTime());
		Date originalDate = new Date(l * 1000L);
		int originalYear = originalDate.getYear();
		int originalMonth = originalDate.getMonth();
		int originalDay = originalDate.getDay();
		int originalHour = originalDate.getHours();
		int originalMinute = originalDate.getMinutes();
		int originalSecond = originalDate.getSeconds();
		try {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(originalDate));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Date nowDate = new Date(System.currentTimeMillis());
		int nowYear = nowDate.getYear();
		int nowMonth = nowDate.getMonth();
		int nowDay = nowDate.getDay();
		int nowHour = nowDate.getHours();
		int nowMinute = nowDate.getMinutes();
		int nowSecond = nowDate.getSeconds();
		try {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate));
		} catch (Exception e) {
			e.printStackTrace();
		}

		int differYear = nowYear - originalYear;
		int differMonth = nowMonth - originalMonth;
		int differDay = nowDay - originalDay;
		int differHour = nowHour - originalHour;
		int differMinute = nowMinute - originalMinute;
		int differSecond = nowSecond - originalSecond;

		long lo = nowDate.getTime() - originalDate.getTime();
		long day = lo / (24 * 60 * 60 * 1000);
		long hour = (lo / (60 * 60 * 1000) - day * 24);
		long minute = ((lo / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long second = (lo / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
		System.out.println("相差时间：" + day + "天" + hour + "小时" + minute + "分" + second + "秒");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(originalDate);

		// 今天
		if (differYear == 0 && differMonth == 0 && differDay == 0) {
			setTextViewText(dayTextView, "今天");
			setTextViewText(monthTextView, "");
		}

		// 昨天
		if (differYear == 0 && differMonth == 0 && differDay == 1) {
			setTextViewText(dayTextView, "昨天");
			setTextViewText(monthTextView, "");
		}

		setTextViewText(dayTextView, String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
		setTextViewText(monthTextView, getMonth(calendar.get(Calendar.MONTH)));
	}

	private String getMonth(int month) {
		String mon;
		if (month == 0) {
			mon = "一月";
		} else if (month == 1) {
			mon = "二月";
		} else if (month == 2) {
			mon = "三月";
		} else if (month == 3) {
			mon = "四月";
		} else if (month == 4) {
			mon = "五月";
		} else if (month == 5) {
			mon = "六月";
		} else if (month == 6) {
			mon = "七月";
		} else if (month == 7) {
			mon = "八月";
		} else if (month == 8) {
			mon = "九月";
		} else if (month == 9) {
			mon = "十月";
		} else if (month == 10) {
			mon = "十一月";
		} else if (month == 11) {
			mon = "十二月";
		} else {
			mon = "";
		}
		return mon;
	}

	/**
	 * 点赞状态
	 */
	private void setLike(final VideoImage record, ImageView view) {
		if (record != null) {
			if (record.getFlower_tick() == 1) {// 已点赞状态
				view.setImageResource(R.drawable.videoplay_good_red_205);
			} else {// 未点赞状态
				view.setImageResource(R.drawable.videoplay_good_gray_205);
			}
		}

		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!isLogin()) {
					showToastShort("请先登录！");
					return;
				}
				if (StringUtil.isNull(record.getFlower_count())) {
					record.setFlower_count("0");
				}
				// 视频
				if (record.getFlower_tick() == 1) {// 已点赞状态
					record.setFlower_count(Integer.valueOf(record.getFlower_count()) - 1 + "");
					record.setFlower_tick(0);
				} else {// 未点赞状态
					record.setFlower_count(Integer.valueOf(record.getFlower_count()) + 1 + "");
					record.setFlower_tick(1);
				}
				// 视频点赞
				DataManager.videoFlower2(record.getVideo_id(), getMember_id());
				notifyDataSetChanged();
			}
		});
	}

	/**
	 * 收藏状态
	 */
	private void setStar(final VideoImage record, ImageView view) {
		if (record != null) {
			if (record.getCollection_tick() == 1) {// 已收藏状态
				view.setImageResource(R.drawable.videoplay_star_red_205);
			} else {// 未收藏状态
				view.setImageResource(R.drawable.videoplay_star_gray_205);
			}
		}

		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!isLogin()) {
					showToastShort("请先登录！");
					return;
				}
				if (StringUtil.isNull(record.getCollection_count())) {
					record.setCollection_count("0");
				}
				// 视频
				if (record.getCollection_tick() == 1) {// 已收藏状态
					record.setCollection_count(Integer.valueOf(record.getCollection_count()) - 1 + "");
					record.setCollection_tick(0);
				} else {// 未收藏状态
					record.setCollection_count(Integer.valueOf(record.getCollection_count()) + 1 + "");
					record.setCollection_tick(1);
				}
				// 视频收藏
				DataManager.videoCollect2(record.getVideo_id(), getMember_id());
				notifyDataSetChanged();
			}
		});
	}

	/**
	 * 评论
	 */
	private void setComment(final VideoImage record, ImageView view) {

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startVideoPlayActivity(record);
			}
		});
	}

	private class ViewHolder {

		TextView day;// 06
		TextView month;// 十二月
		TextView title;
		ImageView cover;

		TextView allTime;
		TextView playCount;

		ImageView like;
		TextView likeCount;
		ImageView star;
		TextView starCount;
		ImageView comment;
		TextView commentCount;
		TextView upliadTime;

		View root;
	}
}
