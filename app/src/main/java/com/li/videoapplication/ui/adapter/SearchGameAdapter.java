package com.li.videoapplication.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseBaseAdapter;

import java.util.ArrayList;

/**
 * 适配器:图文分享-搜索游戏
 */
public class SearchGameAdapter extends BaseBaseAdapter {

    private Activity activity;
    private ArrayList<Associate> data;

    @Override
    protected Context getContext() {
        return activity;
    }

    public SearchGameAdapter(Context context, ArrayList<Associate> data) {
        try {
            this.activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.data = data;

        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final Associate record = (Associate) getItem(position);
        ViewHodler hodler;
        if (view == null) {
            hodler = new ViewHodler();
            view = inflater.inflate(R.layout.adapter_searchgame, null);
            hodler.root = view.findViewById(R.id.root);
            hodler.title = (TextView) view.findViewById(R.id.searchgame_title);
            hodler.pic = (ImageView) view.findViewById(R.id.searchgame_pic);
            view.setTag(hodler);
        } else {
            hodler = (ViewHodler) view.getTag();
        }

        hodler.pic.setVisibility(View.VISIBLE);

        hodler.title.setText(data.get(position).getGame_name());
        GlideHelper.displayImageWhite(getContext(),record.getFlag(), hodler.pic);

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 保存本地
                PreferencesHepler.getInstance().addAssociate201List(record);
                activity.finish();
                EventManager.postSearchGame2VideoShareEvent(record);
            }
        });
        return view;
    }

    private static class ViewHodler {
        View root;
        TextView title;
        ImageView pic;
    }
}
