package com.li.videoapplication.tools;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.li.videoapplication.data.preferences.BasePreferences;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏圈消息已读工具
 * Created by cx on 2018/2/2.
 */

public class GameGroupMsgCountHelper {
    private static final String TAG = GameGroupMsgCountHelper.class.getSimpleName();

    private volatile static GameGroupMsgCountHelper mInstance;

    public static GameGroupMsgCountHelper getInstance() {
        if (mInstance == null) {
            synchronized ((GameGroupMsgCountHelper.class)) {
                if (mInstance == null) {
                    mInstance = new GameGroupMsgCountHelper();
                }
            }
        }
        return mInstance;
    }

    public void setRead(String group_id, long time) {
        if (isOutmoded(group_id)) {
            Preferences.getInstance().save(group_id, time);
        }
    }

    public boolean isOutmoded(String group_id) {
        long oldTime = getTime(group_id);
        long currentTime = System.currentTimeMillis();
        long dur = currentTime - oldTime;
        if (dur > 3 * 60 * 1000)
            return true;
        else
            return false;
    }

    public int isReadAmount() {
        List<Msg> messages = get();
        if (messages == null) {
            return 0;
        }
        int amount = 0;
        for (Msg msg : messages) {
            if (!isOutmoded(msg.getGroup_id()))
                amount++;
        }
        return amount;
    }

    private long getTime(String group_id) {
        List<Msg> messages = get();
        if (messages == null) {
            return 0;
        }
        for (Msg msg : messages) {
            if (msg.getGroup_id().equals(group_id)) {
                return msg.getTime();
            }
        }
        return 0;
    }


    private List<Msg> get() {
        return Preferences.getInstance().get();
    }

    public void clear(){
        Preferences.getInstance().clear();
    }

    private static class Msg {
        private String group_id;
        private long time;

        public Msg() {
        }

        public Msg(String group_id, long time) {
            this.group_id = group_id;
            this.time = time;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String toJSON() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    private static class Preferences extends BasePreferences {

        private static final String NAME = "game_group_count";
        private static final String KEY = "messages";

        @Override
        protected String getName() {
            return NAME;
        }

        private static Preferences instance;

        public static Preferences getInstance() {
            if (instance == null) {
                synchronized (Preferences.class) {
                    if (instance == null) {
                        instance = new Preferences();
                    }
                }
            }
            return instance;
        }

        public void save(String group_id, long time) {
            if (StringUtil.isNull(group_id)) {
                throw new NullPointerException();
            }
            List<Msg> messages = get();
            if (messages == null) {
                messages = new ArrayList<>();
                messages.add(new Msg(group_id, time));
            } else
                if (messages.size() == 0)
                    messages.add(new Msg(group_id, time));
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getGroup_id().equals(group_id)){
                        messages.get(i).setTime(time);
                        break;
                    }

                    if (i == messages.size() - 1)
                        if (!messages.get(i).getGroup_id().equals(group_id))
                            messages.add(new Msg(group_id, time));
                }
            String jsonMsgs = new Gson().toJson(messages, new TypeToken<List<Msg>>() {
            }.getType());
            putString(KEY, jsonMsgs);
            Log.d(tag, "save/group_id=" + group_id);
            Log.d(tag, "save/time=" + time);
        }

        public List<Msg> get() {
            String jsonMsgs = getString(KEY);
            if (jsonMsgs == null || jsonMsgs.length() == 0) {
                Log.d(tag, "no messages..");
                return null;
            }
            List<Msg> messages = new Gson().fromJson(jsonMsgs, new TypeToken<List<Msg>>() {
            }.getType());
            for (Msg msg : messages) {
                Log.d(tag, "get/group_id = " + msg.getGroup_id() + "; time = " + msg.getTime());
            }
            return messages;
        }

    }

}
