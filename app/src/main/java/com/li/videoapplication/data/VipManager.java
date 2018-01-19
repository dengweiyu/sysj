package com.li.videoapplication.data;

import android.os.Handler;
import android.widget.ImageView;


import com.li.videoapplication.component.MainApplication;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.UserChangeEvent;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lpds on 2017/7/10.
 */
public class VipManager {
    static VipManager vipManager;
    Member.VIPInfo vipInfo = null;
    Set<OnVipObserver> onVipChanges;
    Set<ImageView> imageViews;

    static {
        vipManager = new VipManager();
    }

    public static VipManager getInstance() {
        return vipManager;
    }

    private VipManager() {
        imageViews = new HashSet<>();
        EventBus.getDefault().register(this);
        onVipChanges = new HashSet<>();
        vipInfo = PreferencesHepler.getInstance().getUserProfilePersonalInformation().getVipInfo();
    }

    public void onChange() {
        for (final OnVipObserver o : onVipChanges) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    o.onVipObservable(vipInfo);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(UserChangeEvent userInfomationEvent) {
        if (userInfomationEvent.isOut()) {
            vipInfo = null;
        } else {
            try {
                vipInfo = PreferencesHepler.getInstance().getUserProfilePersonalInformation().getVipInfo();
            } catch (Exception ex) {
                ex.printStackTrace();
                vipInfo = null;
            }
        }
        onChange();
    }

    public boolean isVip() {
        return vipInfo != null && !StringUtil.isNull(vipInfo.getLevel());
    }

    public boolean isOldVip() {
        if (isVip() && "1".equals(vipInfo.getValid())) {
            return false;
        }
        return true;
    }


    public String vipLevel() {
        if (!isVip()) {
            return null;
        }
        return vipInfo.getLevel();
    }

    public void registerVipChangeListener(OnVipObserver onVipChange) {
        onVipChanges.add(onVipChange);

    }

    public void ungisterVipChangeListener(Object onVipChange) {
        onVipChanges.remove(onVipChange);

    }

    public static String getLevelString(String Level) {
        if (StringUtil.isNull(Level)) {
            return "";
        }
        switch (Level) {
            case Member.VIPInfo.DIAMOND:
                return "VIP3";
            case Member.VIPInfo.GOLD:
                return "VIP2";
            case Member.VIPInfo.SILVER:
                return "VIP1";
            default:
                return "";
        }

    }

    public boolean isSaleVipUser(int level) {
        return isSaleVipUser(String.valueOf(level));
    }

    public boolean isSaleVipUser(String level) {

        if(!VipManager.getInstance().isVip() || isOldVip()){
            return false;
        }

        if(vipInfo.getLevel().equals(level)){
            return true;
        }

        if (vipInfo.getDetail() != null) {
            for(Member.VIPInfo vipInfo : this.vipInfo.getDetail()){
                if(vipInfo.getLevel().equals(level)){
                    return true;
                }
            }
        }

        return false;
    }


    public boolean isLevel3() {
        if (isVip()) {
            return Member.VIPInfo.DIAMOND.equals(vipInfo.getLevel());
        }
        return false;
    }


    public boolean isLevel2() {
        if (isVip()) {
            return Member.VIPInfo.GOLD.equals(vipInfo.getLevel());
        }
        return false;
    }


    public boolean isLevel1() {
        if (isVip()) {
            return Member.VIPInfo.SILVER.equals(vipInfo.getLevel());
        }
        return false;
    }

    public String getValid() {
        if (!isVip()) {
            return null;
        }
        return vipInfo.getValid();
    }


    public Member.VIPInfo getVipInfo() {
        return vipInfo;
    }

    public interface OnVipObserver {

        void onVipObservable(Member.VIPInfo vipInfo);

    }
}
