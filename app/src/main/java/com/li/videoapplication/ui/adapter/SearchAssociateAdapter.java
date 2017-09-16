package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.utils.StringUtil;

/**
 * 适配器：搜索联想词
 */
@SuppressLint("InflateParams")
public class SearchAssociateAdapter extends BaseArrayAdapter<Associate> {

    private String keyword;

    public SearchAssociateAdapter(Context context, List<Associate> data) {
        super(context, R.layout.adapter_searchassociate, data);
    }

    public void setKeyWord(String keyWord) {
        this.keyword = keyWord;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Associate record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_searchassociate, null);
            holder.text = (TextView) view.findViewById(R.id.searchassociate_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //天天酷跑
        String name = record.getName();

        //关键字变红
        if (!StringUtil.isNull(keyword) && name != null && name.contains(keyword)) {
            int index = name.indexOf(keyword);
            int len = keyword.length();
            Spanned temp = Html.fromHtml(name.substring(0, index)
                    + "<font color=#ff3d2e>"
                    + name.substring(index, index + len) + "</font>"
                    + name.substring(index + len, name.length()));

            holder.text.setText(temp);
        } else {
            holder.text.setText(name);
        }

        
        return view;
    }

    private class ViewHolder {
        TextView text;
    }
}
