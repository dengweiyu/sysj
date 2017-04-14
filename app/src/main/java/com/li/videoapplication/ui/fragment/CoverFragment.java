package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.view.CoverSeekBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 碎片：封面
 */
public class CoverFragment extends TBaseFragment {


    @BindView(R.id.coverSeekBar)
    CoverSeekBar coverSeekBar;
    Unbinder unbinder;
    private VideoEditorActivity2 activity;
    private VideoCaptureEntity entity;

    public boolean isOnTouch() {
        if (coverSeekBar != null
                && coverSeekBar.isFirstOnTouch)
            return true;
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (VideoEditorActivity2) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initContentView(View view) {
        try {
            entity = (VideoCaptureEntity) getArguments().getSerializable("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        unbinder = ButterKnife.bind(this, view);
        coverSeekBar.init(new CoverSeekBar.Callback() {

            @Override
            public void updateCenterTime(long centerTime) {
                CoverFragment.this.centerTime = centerTime;
                Log.d(tag, "updateCenterTime: centerTime=" + centerTime);
                if (activity != null) {
                    activity.pauseVideo();
                    activity.seekToVideo(centerTime);
                }
            }

            @Override
            public void afterCenterTime(long centerTime) {
                CoverFragment.this.centerTime = centerTime;
                Log.d(tag, "afterCenterTime: centerTime=" + centerTime);
                if (activity != null) {
                    activity.refreshCoverAsync(centerTime);
                }
            }
        });

        updateDuration(duration);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_cover;
    }


    public long centerTime;
    private long duration;

    public void updateDuration(long duration) {
        this.duration = duration;
        if (coverSeekBar != null) {
            Log.d(tag, "updateDuration: ");
            coverSeekBar.setMaxTime(duration);
            coverSeekBar.setCenterTime(0);
            coverSeekBar.notifyChange();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
