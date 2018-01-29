package com.li.videoapplication.ui.popupwindows.gameselect;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by linhui on 2017/9/18.
 *
 * 定义了弹出框管理者的基本方法
 *
 */
public interface IPopup extends IDataHandler,ISimpleHandler,ISubmit {
    int HOT_GAME_PAGE = 0;
    int LIFE_PAGE = 1;
    int HOT_GAME_FLAG = 0xaaa;
    int LIFE_FLAG = 0xbbb;
    void addPopupListener(PopupListener popupListener);
    int getThisPage();
    void setThisPage(int page);
    void requestHotGameData();
    void requestLifeData();
    void requestAssociateGame(String text);
    List<IPageView> getPageViews();
    FragmentActivity getActivity();
    List<IInfoEntity> getThisPageAllDatas(int flag);
    int getChooseFlag();
    void setChooseFlag(int i);

    interface PopupListener{
        void show();
        void dismiss(IInfoEntity iInfoEntity, boolean isReprtition);
    }
}
