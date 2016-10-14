package com.li.videoapplication.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.data.model.entity.HelpQuestionEntity;
import com.li.videoapplication.R;

/**
 * 适配器：帮助与教程——问题解答
 *
 */
public class HelpQuestionAdapter extends BaseExpandableListAdapter {

	private String[] groups;
	private Context mContext;
	private ViewHolderGroud holderGroup;
	private ViewHolderChild holderChild;
	private LayoutInflater inflater;
	private List<ArrayList<HelpQuestionEntity>> list;
	private Animation animation;
	private boolean siamin = false;

	public HelpQuestionAdapter(Context mContext, String[] groups, List<ArrayList<HelpQuestionEntity>> list) {
		this.groups = groups;
		this.list = list;
		this.mContext = mContext;

		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getGroupCount() {
		return groups.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return list.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		holderGroup = new ViewHolderGroud();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_helpquestion_group, null);
			holderGroup.text = (TextView) convertView.findViewById(R.id.helpquestion_text);
			holderGroup.image = (ImageView) convertView.findViewById(R.id.helpquestion_image);
			convertView.setTag(holderGroup);
		} else {
			holderGroup = (ViewHolderGroud) convertView.getTag();
		}

		holderGroup.text.setText(groups[groupPosition]);
		if (isExpanded) {
			holderGroup.image.setBackgroundResource(R.drawable.arrow_down_gray);
		} else {
			holderGroup.image.setBackgroundResource(R.drawable.arrow_right);

		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		holderChild = new ViewHolderChild();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_helpquestion_child, null);
			holderChild.ask = (TextView) convertView.findViewById(R.id.helpquestion_ask);
			holderChild.answer = (TextView) convertView.findViewById(R.id.helpquestion_answer);
			convertView.setTag(holderChild);
		} else {
			holderChild = (ViewHolderChild) convertView.getTag();
		}
		holderChild.ask.setText(list.get(groupPosition).get(childPosition).getTitle());
		holderChild.answer.setText(list.get(groupPosition).get(childPosition).getContent());
		holderChild.animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_top_enter);
		convertView.setAnimation(holderChild.animation);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private static class ViewHolderGroud {
		TextView text;
		ImageView image;
	}

	private static class ViewHolderChild {
		TextView ask;
		TextView answer;
		Animation animation;
	}
}
