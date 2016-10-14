package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.CircleImageView;

import java.util.List;

/**
 * 适配器：搜索玩家
 */
@SuppressLint("InflateParams") 
public class SearchMemberAdapter extends BaseArrayAdapter<Member> {

	public static final int PAGE_SEARCHMEMBER = 1;
	public static final int PAGE_MYFANS = 2;
	public static final int PAGE_MYFOCUS = 3;

	private int page;

	/**
	 * 跳转：玩家动态
	 */
	private void startDynamicActivity(Member member) {
		ActivityManeger.startPlayerDynamicActivity(getContext(), member);
	}

	public SearchMemberAdapter(Context context, int page, List<Member> data) {
		super(context, R.layout.adapter_searchmember, data);
		this.page = page;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final Member record = getItem(position);
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.adapter_searchmember, null);
			holder.head = (CircleImageView) view.findViewById(R.id.searchmember_head);
			holder.head_v = (ImageView) view.findViewById(R.id.searchmember_v);
			holder.name = (TextView) view.findViewById(R.id.playerbillboard_name);
			holder.mark = (TextView) view.findViewById(R.id.searchmember_mark);
			holder.content = (LinearLayout) view.findViewById(R.id.content);
//			holder.left = (TextView) view.findViewById(R.id.searchmember_left);
			holder.middle = (TextView) view.findViewById(R.id.searchmember_middle);
			holder.right = (TextView) view.findViewById(R.id.searchmember_right);
//			holder.level = (TextView) view.findViewById(R.id.searchmember_level);
			holder.focusContainer = (LinearLayout) view.findViewById(R.id.searchmember_focus);
			holder.focus = (TextView) view.findViewById(R.id.groupdetail_player_focus);
			holder.go = (ImageView) view.findViewById(R.id.myplayer_go);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (record.isV())
			holder.head_v.setVisibility(View.VISIBLE);
		setImageViewImageNet(holder.head, record.getAvatar());
		setTextViewText(holder.name, record.getNickname());
		
		setMark(holder.mark, record);
//		setLevel(holder.level, record);

		holder.content.setVisibility(View.VISIBLE);
//		setDegree(holder.left, record);
		setVideo(holder.middle, record);
		setFans(holder.right, record);
		
		setFocus(record, holder.focusContainer, holder.focus, holder.go);
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Member info = gson.fromJson(record.toJSON(), Member.class);
				startDynamicActivity(info);
			}
		});
		
		setListViewLayoutParams(view, 58);
		
		return view;
	}

	/**
	 * 关注
	 */
	public void setFocus(final Member record, LinearLayout focusContainer, TextView focus, ImageView go) {
		focusContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!isLogin()) {
					showToastShort("请先登录！");
					return;
				}
				boolean flag;
				if (page == PAGE_SEARCHMEMBER) {
					if (record.getMember_tick() == 1) {// 已关注状态
						flag = false;
						record.setFans(Integer.valueOf(record.getFans()) - 1 + "");
						record.setMember_tick(0);
					} else {// 未关注状态
						flag = true;
						record.setFans(Integer.valueOf(record.getFans()) + 1 + "");
						record.setMember_tick(1);
					}
				} else if (page == PAGE_MYFANS) {
					if (record.getTick() == 1) {// 已关注状态
						flag = false;
						record.setFans(Integer.valueOf(record.getFans()) - 1 + "");
						record.setTick(0);
					} else {// 未关注状态
						flag = true;
						record.setFans(Integer.valueOf(record.getFans()) + 1 + "");
						record.setTick(1);
					}
				}
				if (page == PAGE_MYFANS || page == PAGE_SEARCHMEMBER) {
					notifyDataSetChanged();
					// 玩家关注
					DataManager.memberAttention201(record.getMember_id(), getMember_id());
				}
			}
		});
		focus.setVisibility(View.GONE);
		if (page == PAGE_SEARCHMEMBER) {
			focusContainer.setVisibility(View.VISIBLE);
			go.setVisibility(View.GONE);
			if (record.getMember_tick() == 1) {
				focusContainer.setBackgroundResource(R.drawable.focus_gray);
			} else {
				focusContainer.setBackgroundResource(R.drawable.focus_red);
			}
		} else if (page == PAGE_MYFOCUS) {
			focusContainer.setVisibility(View.GONE);
			go.setVisibility(View.VISIBLE);
		} else if (page == PAGE_MYFANS) {
			focusContainer.setVisibility(View.VISIBLE);
			go.setVisibility(View.GONE);
			if (record.getTick() == 1) {
				focusContainer.setBackgroundResource(R.drawable.focus_gray);
			} else {
				focusContainer.setBackgroundResource(R.drawable.focus_red);
			}
		} else {
			focusContainer.setVisibility(View.GONE);
			go.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 等级
	 */
	private void setLevel(TextView view, final Member record) {
		setTextViewText(view, "Lv." + record.getRank());
		view.setVisibility(View.GONE);
	}
	
	/**
	 * 等级
	 */
	private void setDegree(TextView view, final Member record) {
		
		view.setText("Lv." + record.getDegree());
	}
	
	/**
	 * 视频
	 */
	private void setVideo(TextView view, final Member record) {

		if (!StringUtil.isNull(record.getVideo_num())) {
			setTextViewText(view, "视频\t" + record.getVideo_num());
		} else if (!StringUtil.isNull(record.getUploadVideoCount())) {
			setTextViewText(view, "视频\t" + record.getUploadVideoCount());
		} else {
			setTextViewText(view, "");
		}
	}
	
	/**
	 * 粉丝
	 */
	private void setFans(TextView view, final Member record) {

		if (!StringUtil.isNull(record.getFans())) {
			setTextViewText(view, "粉丝\t" + record.getFans());
		} else {
			setTextViewText(view, "");
		}
	}
	
	/**
	 * 内容:等级 19 视频 236 粉丝 56
	 */
	private void setMark(TextView view, final Member record) { 

        //<!-- #ffb535 黄色 -->
        //<!-- #a9a9a9 灰色 -->
        //<!-- #ff5f5d 红色 -->
		StringBuffer buffer = new StringBuffer();
		if (!StringUtil.isNull(record.getRank())) {
			buffer.append(TextUtil.toColorItalic(String.valueOf("Lv." + record.getDegree()), "#ffb535"));
		}
		if (!StringUtil.isNull(record.getVideo_num())) {
			buffer.append("\t\t视频\t" + record.getVideo_num());
		} else if (!StringUtil.isNull(record.getUploadVideoCount())) {
			buffer.append("\t\t视频\t" + record.getUploadVideoCount());
		}
		if (!StringUtil.isNull(record.getFans())) {
			buffer.append("\t\t粉丝\t" + record.getFans());
		}
		view.setText(Html.fromHtml(buffer.toString()));
		view.setVisibility(View.GONE);
	}

	private static class ViewHolder {
		CircleImageView head;
		TextView mark;// 等级 19 视频 236 粉丝 56
//		TextView level;//Lv.1
		LinearLayout content;
//		TextView left;//Lv.1
		TextView middle;// 视频 236
		TextView right;// 粉丝 56
		TextView name;
		LinearLayout focusContainer;
		TextView focus;
		ImageView go,head_v;
	}
}
