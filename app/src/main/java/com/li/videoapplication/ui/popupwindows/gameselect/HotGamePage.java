package com.li.videoapplication.ui.popupwindows.gameselect;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.response.Associate201Entity;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

final class HotGamePage implements IPageView<Associate201Entity>, ISubmit {

    private static final String TAG = "HotGamePage";
    private final String CACHE_XML_NAME = "cache_game_hoGamePage";
    private final String CACHE_KEY = "cache_game";

    /**
     * 选择的来源，最近选择
     */
    private int CACHE_GAME_CHANNEL = 0xbbbb;
    /**
     * 选择的来源，热门游戏选择
     */
    private int HOT_GAME_CHANNEL = 0xaaaa;
    /**
     * 当前选择的
     */
    private int selectChannel;
    private View contenView;
    private IPopup iPopup;

    private final AtomicBoolean isSearchStatus = new AtomicBoolean(false);

    private final List<IInfoEntity> HOT_GAME_LIST = new ArrayList<>();
    private final List<IInfoEntity> CACHE_GAME_LIST = new ArrayList<>();

    @BindView(R.id.id_search_game_et)
    EditText id_search_game_et;
    @BindView(R.id.id_renmenber_game_layout)
    View id_renmenber_game_layout;
    @BindView(R.id.id_remenber_game_rv)
    RecyclerView id_remenber_game_rv;
    @BindView(R.id.id_hot_game_rv)
    RecyclerView id_hot_game_rv;
    @BindView(R.id.id_delete_remember_tv)
    View id_delete_remember_tv;
    @BindView(R.id.id_hot_game_tv_layout)
    View id_hot_game_tv_layout;
    @BindView(R.id.id_sousuo_layout)
    View id_sousuo_layout;

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;


    @OnClick(R.id.id_sousuo_layout)
    public void sreach(){
        selectByFilter(id_search_game_et.getText().toString());
    }

    private Drawable blueDrawable;
    private Drawable spaceDrawable = new BitmapDrawable();

    private Handler childThread;

    public HotGamePage(IPopup iPopup) {
        this.iPopup = iPopup;
    }

    @Override
    public View getView() {
        return contenView;
    }

    @Override
    public CharSequence getTitle() {
        return AppManager.getInstance().getApplication().getResources().getString(R.string.moretype_game);
    }

    /**
     * 刷新
     */
    @Override
    public void sendChange() {
        iPopup.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (id_hot_game_rv != null) {
                    id_hot_game_rv.getAdapter().notifyDataSetChanged();
                }
                if (CACHE_GAME_LIST.size() > 0 && id_remenber_game_rv != null
                        && !isSearchStatus.get()) {
                    id_renmenber_game_layout.setVisibility(View.VISIBLE);
                    id_remenber_game_rv.getAdapter().notifyDataSetChanged();
                } else if (id_renmenber_game_layout != null) {
                    id_renmenber_game_layout.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 处理网络获取到的数据
     *
     * @param hotGameEntity
     */
    @Override
    public void handlerData(Associate201Entity hotGameEntity) {
        if (isSearchStatus.get() && hotGameEntity instanceof SearcheHotGameEntity) {
            Log.i(TAG, "handlerData: 搜索回调");
            synchronized (id_search_game_et) {
                if (id_search_game_et.getText().toString().equals(
                        hotGameEntity.getExtra().get("keyWord"))) {
                    HOT_GAME_LIST.clear();
                    HOT_GAME_LIST.addAll(hotGameEntity.getData());
                } else {
                    return;
                }
            }
        } else {
            Log.i(TAG, "handlerData: 无搜索");
            HOT_GAME_LIST.clear();
            HOT_GAME_LIST.addAll(iPopup.getThisPageAllDatas(IPopup.HOT_GAME_FLAG));
        }
        sendChange();
    }

    /**
     * 初始化handler
     */
    private void initHandler() {
        HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName());
        handlerThread.start();
        childThread = new Handler(handlerThread.getLooper());
    }

    @Override
    public synchronized void init() {
        if (contenView == null) {
            initHandler();
            getCacheGame();
            blueDrawable = ActivityCompat.getDrawable(iPopup.getActivity(), R.drawable.shape_main_bule);
            contenView = LayoutInflater.from(iPopup.getActivity()).inflate(getContentView(), null, false);
            ButterKnife.bind(this, contenView);
            id_remenber_game_rv.setItemAnimator(new DefaultItemAnimator());
            id_remenber_game_rv.addItemDecoration(new GridDecoration());
            id_remenber_game_rv.setLayoutManager(new GridLayoutManager(iPopup.getActivity(), 2));
            id_remenber_game_rv.setAdapter(new BaseQuickAdapter<IInfoEntity, BaseViewHolder>(R.layout.adaper_game_name, CACHE_GAME_LIST) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, final IInfoEntity infoEntity) {
                    final TextView id_game_tv = baseViewHolder.getView(R.id.id_game_tv);
                    id_game_tv.setText(infoEntity.getName());
                    id_game_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /**
                             * 最新最热的选择
                             * 已选的不为空
                             * && 已选的和即将被选的名字必须匹配
                             * && 已选的和即将被选的ID必须匹配
                             * && 选择来源不在同一个（最近/热门）
                             *
                             * 以上不成立，就可以选择这次的项为新的item
                             *
                             */
                            if (getSelect() != null && getSelect().getName().equals(infoEntity.getName())
                                    && getSelect().getId().equals(infoEntity.getId())
                                    && selectChannel == CACHE_GAME_CHANNEL) {
                                return;
                            }
                            selectChannel = CACHE_GAME_CHANNEL;
                            iPopup.setChooseFlag(IPopup.HOT_GAME_FLAG);
                            setSelect((IInfoEntity) infoEntity.clone());
                        }
                    });
                    if (getSelect() != null
                            && getSelect().getName().equals(infoEntity.getName())
                            && getSelect().getId().equals(infoEntity.getId())
                            && isSame(CACHE_GAME_CHANNEL)
                            && iPopup.getChooseFlag() == IPopup.HOT_GAME_FLAG) {
                        id_game_tv.setTextColor(ActivityCompat.getColor(iPopup.getActivity(), R.color.ab_background_blue_2));
                        id_game_tv.setBackground(blueDrawable);
                    } else if (id_game_tv.getBackground() == blueDrawable) {
                        id_game_tv.setBackground(spaceDrawable);
                        id_game_tv.setTextColor(ActivityCompat.getColor(iPopup.getActivity(), R.color.color_666666));
                    }

                }
            });
            id_hot_game_rv.setItemAnimator(new DefaultItemAnimator());
            id_hot_game_rv.addItemDecoration(new GridDecoration());
            id_hot_game_rv.setLayoutManager(new GridLayoutManager(iPopup.getActivity(), 2));
            id_hot_game_rv.setAdapter(new BaseQuickAdapter<IInfoEntity, BaseViewHolder>(R.layout.adaper_game_name, HOT_GAME_LIST) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, final IInfoEntity i) {
                    final TextView id_game_tv = baseViewHolder.getView(R.id.id_game_tv);
                    final IInfoEntity infoEntity = (IInfoEntity) i.clone();
                    if (isSearchStatus.get()) {
                        id_game_tv.setText(InputUtil.setSpecifiedTextsColor(
                                infoEntity.getName(),
                                id_search_game_et.getText().toString(),
                                ActivityCompat.getColor(
                                        iPopup.getActivity(),
                                        R.color.ab_background_blue_2)));
                    } else {
                        id_game_tv.setText(infoEntity.getName());
                    }
                    id_game_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getSelect() != null && getSelect().getName().equals(infoEntity.getName())
                                    && getSelect().getId().equals(infoEntity.getId())
                                    && selectChannel == HOT_GAME_CHANNEL) {
                                return;
                            }
                            selectChannel = HOT_GAME_CHANNEL;
                            iPopup.setChooseFlag(IPopup.HOT_GAME_FLAG);
                            setSelect((IInfoEntity) infoEntity.clone());
                        }
                    });
                    if (getSelect() != null
                            && getSelect().getName().equals(infoEntity.getName())
                            && getSelect().getId().equals(infoEntity.getId())
                            && isSame(HOT_GAME_CHANNEL)
                            && iPopup.getChooseFlag() == IPopup.HOT_GAME_FLAG) {
                        id_game_tv.setBackground(blueDrawable);
                        id_game_tv.setTextColor(ActivityCompat.getColor(iPopup.getActivity(), R.color.ab_background_blue_2));
                    } else if (id_game_tv.getBackground() == blueDrawable) {
                        id_game_tv.setBackground(spaceDrawable);
                        id_game_tv.setTextColor(ActivityCompat.getColor(iPopup.getActivity(), R.color.color_666666));
                    }

                }
            });


            id_search_game_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.i(TAG, "beforeTextChanged: " + s);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.i(TAG, "onTextChanged: " + s);

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.i(TAG, "afterTextChanged: " + s);
                    selectByFilter(id_search_game_et.getText().toString());
                }
            });
            id_delete_remember_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearCache();
                }
            });
        }
    }

    /**
     * 关键字查询
     *
     * @param s
     */
    private void selectByFilter(final String s) {

        if (StringUtil.isNull(s)) {
            isSearchStatus.set(false);
            id_hot_game_tv_layout.setVisibility(View.VISIBLE);
            if (HOT_GAME_LIST.size() != iPopup.getThisPageAllDatas(IPopup.HOT_GAME_FLAG).size()) {
                HOT_GAME_LIST.clear();
                HOT_GAME_LIST.addAll(iPopup.getThisPageAllDatas(IPopup.HOT_GAME_FLAG));
                sendChange();
            }
        } else {
            isSearchStatus.set(true);
            id_hot_game_tv_layout.setVisibility(View.GONE);
            if (NetUtil.isConnect()) {
                iPopup.requestAssociateGame(s);
            } else {
                childThread.post(new Runnable() {
                    @Override
                    public void run() {
                        final List<IInfoEntity> bill = new ArrayList<IInfoEntity>();
                        Observable.from(iPopup.getThisPageAllDatas(IPopup.HOT_GAME_FLAG)).
                                subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<IInfoEntity>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "onCompleted: edittext 查询完成");
                                HOT_GAME_LIST.clear();
                                HOT_GAME_LIST.addAll(bill);
                                sendChange();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(IInfoEntity iInfoEntity) {
                                if (iInfoEntity.getName().contains(s)) {
                                    bill.add(iInfoEntity);
                                }
                            }
                        });
                    }
                });
            }
        }


    }

    private SharedPreferences getCacheSharedPreferences() {
        return iPopup.getActivity().getSharedPreferences(CACHE_XML_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取最近游戏的集合
     */
    private void getCacheGame() {
        if (CACHE_GAME_LIST.size() > 0) {
            CACHE_GAME_LIST.clear();
        }
        SharedPreferences s = getCacheSharedPreferences();
        String json = s.getString(CACHE_KEY, "");
        if (!json.isEmpty()) {
            try {
                CACHE_GAME_LIST.addAll(new Gson().fromJson(json, LifeEntity.class).getData());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 上次选择的是否在同一个栏目里（最近和最热比较）
     *
     * @param channel
     * @return
     */
    private boolean isSame(int channel) {
        return selectChannel == channel;
    }

    @Override
    public void loadData() {

    }

    /**
     * 摧毁被调用
     */
    @Override
    public void onDestroy() {
        childThread.removeCallbacksAndMessages(null);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_game_select;
    }

    @Override
    public void show() {
        iPopup.show();
    }

    @Override
    public void dismiss() {
        iPopup.dismiss();
    }

    /**
     * 获取当前被选中的项
     *
     * @return
     */
    @Override
    public IInfoEntity getSelect() {
        return iPopup.getSelect();
    }

    /**
     * 设置选择的项
     *
     * @param iInfoEntity
     */
    @Override
    public void setSelect(IInfoEntity iInfoEntity) {
        iPopup.setSelect(iInfoEntity);
    }

    @Override
    public IInfoEntity getOladSelect() {
        return iPopup.getOladSelect();
    }


    /**
     * 提交保存数据到最近选择
     */
    @Override
    public void submit() {
        if (getSelect() != null && iPopup.getChooseFlag() == iPopup.HOT_GAME_FLAG) {
            Observable.from(CACHE_GAME_LIST).subscribe(new Subscriber<IInfoEntity>() {
                @Override
                public void onCompleted() {
                    LifeEntity l = new LifeEntity();
                    l.setData(new ArrayList<Associate>());
                    l.addData(CACHE_GAME_LIST);
                    l.getData().add(getSelect());
                    final String json = new Gson().toJson(l);
                    getCacheSharedPreferences().edit().putString(CACHE_KEY, json).apply();
                    Log.i(TAG, "onCompleted: 提交最热游戏缓存选择完成");
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    Log.i(TAG, "onError: 提交最热游戏缓存选择错误");
                }

                @Override
                public void onNext(IInfoEntity iInfoEntity) {
                    if (getSelect().getId().equals(iInfoEntity.getId())
                            && getSelect().getName().equals(iInfoEntity.getName())
                            && !isUnsubscribed()) {
                        unsubscribe();
                    }
                }
            });
        }
    }

    /**
     * 清除最近选择
     */
    private void clearCache() {
        getCacheSharedPreferences().edit().clear().apply();
        getCacheGame();
        if (iPopup.getChooseFlag() == IPopup.HOT_GAME_FLAG
                && selectChannel == CACHE_GAME_CHANNEL) {
            selectChannel = HOT_GAME_CHANNEL;
        }
        ValueAnimator va = ValueAnimator.ofFloat(1, 0);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = (float) animation.getAnimatedValue();
                id_renmenber_game_layout.setAlpha(i);
                if (i == 0) {
                    id_renmenber_game_layout.setVisibility(View.GONE);
                    id_renmenber_game_layout.setAlpha(1);
                    sendChange();
                }
            }
        });
        va.start();

    }

}
