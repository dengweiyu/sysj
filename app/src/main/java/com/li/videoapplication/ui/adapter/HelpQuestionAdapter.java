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
	private String[] groupsMore;
	private Context mContext;
	private ViewHolderGroud holderGroup;
	private ViewHolderChild holderChild;
	private LayoutInflater inflater;
	private List<ArrayList<HelpQuestionEntity>> list;
	private Animation animation;
	private boolean siamin = false;

	public HelpQuestionAdapter(Context mContext, String[] groups,String[] groupsMore, List<ArrayList<HelpQuestionEntity>> list) {
		this.groups = groups;
		this.list = list;
		this.mContext = mContext;
		this.groupsMore = groupsMore;

		inflater = LayoutInflater.from(mContext);
	}

	private int expandGroup = -1;
	public void expandGroup(int position){
		expandGroup = position;
	}

	@Override
	public int getGroupCount() {
		return groups.length+groupsMore.length+2;			//2个头部
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == 0 || groupPosition == groups.length+1){
			return 0;
		}
		int offset = 1;
		if (groupPosition > groups.length+1){
			offset = 2;
		}
		return list.get(groupPosition -offset).size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return  groupPosition <= groups.length?groups[groupPosition-1]:groupsMore[groupPosition-groups.length-2];
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

		if (groupPosition == 0 || groupPosition == groups.length+1 ){
			convertView = inflater.inflate(R.layout.pager_help_question_header, null);
			TextView header = (TextView)convertView.findViewById(R.id.iv_help_header);
			if (groupPosition == 0){
				header.setText("常见问题");
			}else {
				header.setText("更多问题");
			}
		}else {
			//if (convertView == null) {
				convertView = inflater.inflate(R.layout.adapter_helpquestion_group, null);
				TextView text = (TextView) convertView.findViewById(R.id.helpquestion_text);
				ImageView image = (ImageView) convertView.findViewById(R.id.helpquestion_image);
			//	convertView.setTag(holderGroup);
			//} else {
			//	holderGroup = (ViewHolderGroud) convertView.getTag();
			//}
			text.setText((String)getGroup(groupPosition));
			if (isExpanded) {
				image.setBackgroundResource(R.drawable.arrow_down_gray);
			} else {
				image.setBackgroundResource(R.drawable.arrow_right);

			}
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		int offset = 1;
		if (groupPosition == 0 || groupPosition == groups.length+1){
			return new View(mContext);
		}
		if (groupPosition > groups.length+1){
			offset = 2;
		}
		holderChild = new ViewHolderChild();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_helpquestion_child, null);
			holderChild.ask = (TextView) convertView.findViewById(R.id.helpquestion_ask);
			holderChild.answer = (TextView) convertView.findViewById(R.id.helpquestion_answer);
			convertView.setTag(holderChild);

		} else {
			holderChild = (ViewHolderChild) convertView.getTag();
		}
		holderChild.ask.setText(list.get(groupPosition-offset).get(childPosition).getTitle());
		holderChild.answer.setText(list.get(groupPosition-offset).get(childPosition).getContent());
		holderChild.animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_top_enter);
		if (expandGroup == groupPosition){
			convertView.setAnimation(holderChild.animation);
			expandGroup = -1;
		}

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
