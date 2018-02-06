package com.li.videoapplication.ui.popupwindows.gameselect;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.tools.UmengAnalyticsHelper2;
import com.li.videoapplication.views.PagerTab;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.rong.eventbus.EventBus;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.li.videoapplication.R;

public final class PopupImpl implements IPopup, ViewPager.OnPageChangeListener {
    private final int SHADOW_ID = 0x98aaa;
    private static final String TAG = "PopupImpl";
    private PopupWindow popupWindow;
    private final Set<PopupListener> popupListeners = new HashSet<>();
    private IPopupContext iPopupContext;
    private int thisPage = HOT_GAME_PAGE;
    private View contextView;
    private ViewPager id_viewpage;
    private PagerTab id_tabLayout;
    private IInfoEntity selectInfoEntity;
    private IInfoEntity oldSelectInfoEntity;
    private final List<IPageView> PAGE_VIEW_LIST = new ArrayList<>();
    private final List<IInfoEntity> LIFE_LISTS = new ArrayList<>();
    private final List<IInfoEntity> HOT_GAME_LISTS = new ArrayList<>();
    private int oldCheckFlag;

    @Override
    public int getChooseFlag() {
        return oldCheckFlag;
    }

    @Override
    public void setChooseFlag(int i) {
        oldCheckFlag = i;
    }

    @Override
    public void submit() {

        Observable.from(PAGE_VIEW_LIST).subscribe(new Subscriber<IPageView>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: 提交成功");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.i(TAG, "onCompleted: 提交失败");
            }

            @Override
            public void onNext(IPageView iPageView) {
                if (iPageView instanceof ISubmit) {
                    ((ISubmit) iPageView).submit();
                }
            }
        });
    }

    private PopupImpl(IPopupContext iPopupContext) {
//        EventBus.getDefault().register(this);
        this.iPopupContext = iPopupContext;
        addPopupListener(this.iPopupContext);
//        init();
    }

    public static IPopup createPopupImpl(IPopupContext iPopupContext) {
        return new PopupImpl(iPopupContext);
    }

    @Override
    public void addPopupListener(PopupListener popupListener) {
        if (popupListener != null) {
            synchronized (popupListeners) {
                popupListeners.add(popupListener);
            }
        }
    }

    @Override
    public IInfoEntity getSelect() {
        return selectInfoEntity;
    }

    /**
     * 最终选择的项
     * @param iInfoEntity 被选中的项
     */
    @Override
    public void setSelect(IInfoEntity iInfoEntity) {
        this.selectInfoEntity = iInfoEntity;

        Observable.from(HOT_GAME_LISTS).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<IInfoEntity>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: "+ Thread.currentThread());
                HOT_GAME_LISTS.add(0, (IInfoEntity) selectInfoEntity.clone());
                Observable.from(PAGE_VIEW_LIST).subscribe(new Action1<IPageView>() {
                    @Override
                    public void call(IPageView iPageView) {
                        iPageView.sendChange();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(IInfoEntity iInfoEntity) {
                /**
                 * 遍历 本地热门游戏 集合，当选择的是搜索出来的时候，就会被添加进去集合里
                 * 当选的是 LIFE_FLAG 精彩生活的话就不做处理
                 */
                Log.i(TAG, "onNext:  "+ Thread.currentThread());
                if((iInfoEntity.getId().equals(selectInfoEntity.getId())
                        && iInfoEntity.getName().equals(selectInfoEntity.getName())
                        && !isUnsubscribed())
                        || getChooseFlag() == LIFE_FLAG){
                    unsubscribe();
                    Observable.from(PAGE_VIEW_LIST).subscribe(new Action1<IPageView>() {
                        @Override
                        public void call(IPageView iPageView) {
                            iPageView.sendChange();
                        }
                    });
                }
            }
        });
        this.dismiss();
    }

    @Override
    public IInfoEntity getOladSelect() {
        return oldSelectInfoEntity;
    }

    @Override
    public int getThisPage() {
        return thisPage;
    }

    @Override
    public void setThisPage(int page) {
        id_viewpage.setCurrentItem(page);
    }

    /**
     * 获取全部热门游戏
     */
    @Override
    public void requestHotGameData() {
        DataManager.hotGameList2();
    }

    /**
     * 获取精彩生活
     */
    @Override
    public void requestLifeData() {
        LifeEntity lifeEntity = new LifeEntity("life", "空");
        DataManager.associate201_2("life", "空", lifeEntity);
    }


    /**
     * 搜索游戏
     * @param text
     */
    public void requestAssociateGame(String text) {
        DataManager.associate201_2("game", text, new SearchHotGameEntity());
    }

    public List<IPageView> getPageViews() {
        createPageView();
        return PAGE_VIEW_LIST;
    }

    @Override
    public FragmentActivity getActivity() {
        return iPopupContext.getActivity();
    }

    /**
     * 获取当前本地缓存的数据集合
     * @param flag
     * HOT_GAME_FLAG 热门游戏集合
     * LIFE_FLAG 精彩生活集合
     * @return
     */
    @Override
    public List<IInfoEntity> getThisPageAllDatas(int flag) {
        switch (flag) {
            case HOT_GAME_FLAG:
                return HOT_GAME_LISTS;
            case LIFE_FLAG:
                return LIFE_LISTS;
        }
        return null;
    }

    private void createPageView() {
        if (PAGE_VIEW_LIST.size() < 2) {
            PAGE_VIEW_LIST.clear();
            PAGE_VIEW_LIST.add(new HotGamePage(this));
            PAGE_VIEW_LIST.add(new LifePage(this));
            PAGE_VIEW_LIST.get(0).init();
            PAGE_VIEW_LIST.get(1).init();

        }

    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        checkPopupWindow();
    }

    /**
     * 检查并创建 当前主view 和 popuowindows
     */
    private synchronized void checkPopupWindow() {
        if (popupWindow == null && contextView == null) {
            getPageViews();
            contextView = LayoutInflater.from(getActivity()).inflate(getContentView(), null, false);
            id_viewpage = (ViewPager) contextView.findViewById(R.id.id_viewpage);
            id_tabLayout = (PagerTab) contextView.findViewById(R.id.id_tabLayout);
            contextView.findViewById(R.id.id_dismiss_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            id_viewpage.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return PAGE_VIEW_LIST.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public void destroyItem(ViewGroup view, int position, Object object) {
                    view.removeView(PAGE_VIEW_LIST.get(position).getView());
                }

                // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
                @Override
                public Object instantiateItem(ViewGroup view, int position) {
                    view.addView(PAGE_VIEW_LIST.get(position).getView());
                    return PAGE_VIEW_LIST.get(position).getView();
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return PAGE_VIEW_LIST.get(position).getTitle();
                }
            });


            id_tabLayout.setViewPager(id_viewpage);
            id_tabLayout.setIndicatorLength(PagerTab.INDICATOR_LENGTH_MODE_2);
            id_tabLayout.setIndicatorColor(ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1));
            id_tabLayout.setTabTextColor(ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1), Color.parseColor("#666666"));




//            id_tabLayout.setSelectedTabIndicatorColor(ActivityCompat.getColor(getActivity(), R.color.ab_background_blue_2));
//            id_tabLayout.setTabTextColors(ActivityCompat.getColor(getActivity(), R.color.color_999999), ActivityCompat.getColor(getActivity(), R.color.ab_background_blue_2));
//            id_tabLayout.setupWithViewPager(id_viewpage);
//            for (int i = 0; i < id_tabLayout.getTabCount(); i++) {
//                id_tabLayout.getTabAt(i).setText(PAGE_VIEW_LIST.get(i).getTitle());
//            }
//            ((LineTabLayout)id_tabLayout).postSetIndicator(45,45);
            /**
             * 初始化popupWindow
             * 重写 showAtLocation(View parent, int gravity, int x, int y)，dismiss()方法
             * popupwindows宽为屏幕宽，高为屏幕 0.75
             */
            popupWindow = new PopupWindow(
                    contextView,
                    iPopupContext.getMaxWidth(),
                    (int) (iPopupContext.getMaxHeigt() * 0.75),
                    true) {

                @Override
                public void showAtLocation(View parent, int gravity, int x, int y) {
                    addShadow();
                    iPopupContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 更新
                             */
                            Observable.from(PAGE_VIEW_LIST).subscribe(new Observer<IPageView>() {
                                @Override
                                public void onCompleted() {
                                    Log.i(TAG, "onError: show 遍历成功");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.i(TAG, "onError: show 遍历错误");
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(IPageView iPageView) {
                                    iPageView.sendChange();
                                }
                            });

                            /**
                             * show回调
                             */
                            Observable.from(popupListeners).subscribe(new Observer<PopupListener>() {
                                @Override
                                public void onCompleted() {
                                    Log.i(TAG, "onError: show 遍历成功");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.i(TAG, "onError: show 遍历错误");
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(PopupListener popupListener) {
                                    popupListener.show();
                                }
                            });
                        }
                    });
                    super.showAtLocation(parent, gravity, x, y);
                }


                @Override
                public void dismiss() {

                    iPopupContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            final boolean[] flag = {false};

                            if(oldSelectInfoEntity != null &&
                                    selectInfoEntity !=null ){
                                if(selectInfoEntity.getId().equals(oldSelectInfoEntity.getId())
                                        && selectInfoEntity.getName().equals(oldSelectInfoEntity.getName())){
                                    flag[0] = true;
                                }
                            }

                            /**
                             * dismiss回调
                             */
                            Observable.from(popupListeners).subscribe(new Observer<PopupListener>() {
                                @Override
                                public void onCompleted() {
                                    Log.i(TAG, "onError: dismiss 遍历成功");
                                    removeShadow();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.i(TAG, "onError: dismiss 遍历错误");
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(PopupListener popupListener) {




                                    popupListener.dismiss(getSelect(),flag[0]);
                                }
                            });
                        }
                    });
                    super.dismiss();
                }
            };
            popupWindow.setOutsideTouchable(false);
            popupWindow.setAnimationStyle(R.style.take_photo_anim);
            loadData();
        }
    }

    /**
     * 请求初始化本地集合
     */
    @Override
    public void loadData() {
        requestHotGameData();
        requestLifeData();
    }

    /**
     * 销毁
     *
     * submit()测试才调用
     *
     */
    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);
        Observable.from(PAGE_VIEW_LIST).subscribe(new Subscriber<IPageView>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: onDestroy onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onCompleted: onDestroy onError");
                e.printStackTrace();
            }

            @Override
            public void onNext(IPageView iPageView) {
                iPageView.onDestroy();
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.view_share_info;
    }

    /**
     * 显示弹窗
     */
    @Override
    public void show() {
        checkPopupWindow();
        if (!popupWindow.isShowing()) {
            oldSelectInfoEntity = selectInfoEntity;
            popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 关闭弹窗
     */
    @Override
    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == HOT_GAME_PAGE) {
            thisPage = HOT_GAME_PAGE;
        } else if (position == LIFE_PAGE) {
            UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID, UmengAnalyticsHelper2.GENRE_CHOOSE_LIFE_TYPE);
            thisPage = LIFE_PAGE;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 精彩生活数据源
     * @param lifeEntity
     */
    public void onEventMainThread(final LifeEntity lifeEntity) {
        if (lifeEntity.isResult() && lifeEntity.getData() != null) {
            Log.i(TAG, "onEventBackgroundThread: 接收到数据 lifeEntity");
            LIFE_LISTS.clear();
            LIFE_LISTS.addAll(lifeEntity.getData());
            iPopupContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PAGE_VIEW_LIST.get(1).handlerData(lifeEntity);
                }
            });

        }
    }


    /**
     * 热门游戏数据源
     * @param hotGameEntity
     */
    public void onEventMainThread(final HotGameEntity hotGameEntity) {
        if (hotGameEntity.isResult() && hotGameEntity.getData() != null) {
            Log.i(TAG, "onEventBackgroundThread: 接收到数据 hotGameEntity");
            HOT_GAME_LISTS.clear();
            HOT_GAME_LISTS.addAll(hotGameEntity.getData());
            iPopupContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PAGE_VIEW_LIST.get(0).handlerData(hotGameEntity);
                }
            });
        }
    }

    /**
     * 搜索的游戏
     * @param searcheHotGame
     */
    public void onEventMainThread(final SearchHotGameEntity searcheHotGame) {
        if (searcheHotGame.isResult() && searcheHotGame.getData() != null) {
            Log.i(TAG, "onEventBackgroundThread: 接收到数据 SearchHotGameEntity");
            iPopupContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PAGE_VIEW_LIST.get(0).handlerData(searcheHotGame);
                }
            });
        }
    }

    /**
     * 添加阴影
     */
    private void addShadow() {
        ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
        View v = viewGroup.findViewById(SHADOW_ID);
        if (v == null) {
            v = new View(getActivity());
            v.setId(SHADOW_ID);
            v.setBackground(new ColorDrawable(0xbb000000));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            viewGroup.addView(v, layoutParams);
        }


    }

    /**
     * 移除阴影
     */
    private void removeShadow() {
        ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
        View v = viewGroup.findViewById(SHADOW_ID);
        if (v != null) {
            viewGroup.removeView(v);
        }


    }


}
