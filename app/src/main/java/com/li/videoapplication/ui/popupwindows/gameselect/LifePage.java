package com.li.videoapplication.ui.popupwindows.gameselect;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhui on 2017/9/18.
 */
final class LifePage implements IPageView<LifeEntity> {

    private String title = AppManager.getInstance().getApplication().getResources().getString(R.string.moretype_life);
    private View contentView;
    private IPopup iPopup;
    private List<IInfoEntity> LIFE_LIST = new ArrayList<>();
    private RecyclerView id_wonderful_rc;

    private Drawable blueDrawable;
    private Drawable spaceDrawable = new BitmapDrawable();

    public LifePage(IPopup iPopup) {
        this.iPopup = iPopup;
    }

    @Override
    public View getView() {
        return contentView;
    }

    @Override
    public CharSequence getTitle() {
        Spannable spanned = new SpannableString(title);
        spanned.setSpan(new ForegroundColorSpan(ActivityCompat.
                getColor(AppManager.
                        getInstance().
                        getContext(),
                        R.color.color_999999)),
                4,
                title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanned.setSpan(new AbsoluteSizeSpan(ScreenUtil.dp2px(12)),4,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanned;
    }

    @Override
    public void sendChange() {
        iPopup.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (id_wonderful_rc != null) {
                    id_wonderful_rc.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void handlerData(LifeEntity lifeEntity) {
        LIFE_LIST.clear();
        LIFE_LIST.addAll(iPopup.getThisPageAllDatas(IPopup.LIFE_FLAG));
        sendChange();
    }

    @Override
    public synchronized void init() {
        if (contentView == null) {
            blueDrawable = ActivityCompat.getDrawable(iPopup.getActivity(), R.drawable.shape_main_bule);
            contentView = LayoutInflater.from(iPopup.getActivity()).inflate(getContentView(), null, false);
            id_wonderful_rc = (RecyclerView) contentView.findViewById(R.id.id_wonderful_rc);
            id_wonderful_rc.setItemAnimator(new DefaultItemAnimator());
            id_wonderful_rc.addItemDecoration(new GridDecoration());
            id_wonderful_rc.setLayoutManager(new GridLayoutManager(iPopup.getActivity(), 2));
            id_wonderful_rc.setAdapter(new BaseQuickAdapter<IInfoEntity, BaseViewHolder>(R.layout.adaper_wonderful_life_name, LIFE_LIST) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, final IInfoEntity i) {
                    final TextView id_game_tv = baseViewHolder.getView(R.id.id_life_tv);
                    final IInfoEntity infoEntity = (IInfoEntity) i.clone();
                    id_game_tv.setText(infoEntity.getName());
                    id_game_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getSelect() != null && getSelect().getName().equals(infoEntity.getName())
                                    && getSelect().getId().equals(infoEntity.getId())) {
                                return;
                            }
                            iPopup.setChooseFlag(IPopup.LIFE_FLAG);
                            setSelect((IInfoEntity) infoEntity.clone());
                        }
                    });
                    if (getSelect() != null
                            && getSelect().getName().equals(infoEntity.getName())
                            && getSelect().getId().equals(infoEntity.getId())
                            && iPopup.getChooseFlag() == IPopup.LIFE_FLAG) {
                        id_game_tv.setBackground(blueDrawable);
                        id_game_tv.setTextColor(ActivityCompat.getColor(iPopup.getActivity(),R.color.ab_background_blue_2));
                    } else if (id_game_tv.getBackground() == blueDrawable) {
                        id_game_tv.setBackground(new BitmapDrawable());
                        id_game_tv.setTextColor(ActivityCompat.getColor(iPopup.getActivity(),R.color.color_666666));
                    }

                }
            });
        }
    }

    @Override
    public void loadData() {
        LIFE_LIST.clear();
        LIFE_LIST.addAll(iPopup.getThisPageAllDatas(IPopup.LIFE_FLAG));
        sendChange();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_wonderful_life;
    }

    @Override
    public void show() {
        iPopup.show();
    }

    @Override
    public void dismiss() {
        iPopup.dismiss();
    }

    @Override
    public IInfoEntity getSelect() {
        return iPopup.getSelect();
    }

    @Override
    public void setSelect(IInfoEntity iInfoEntity) {
        iPopup.setSelect(iInfoEntity);
    }

    @Override
    public IInfoEntity getOladSelect() {
        return iPopup.getOladSelect();
    }
}
