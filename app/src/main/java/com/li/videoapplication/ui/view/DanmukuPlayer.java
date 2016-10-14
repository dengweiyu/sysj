package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;

import java.io.InputStream;
import java.util.HashMap;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * 视图：弹幕
 */
public class DanmukuPlayer extends RelativeLayout implements IDanmukuPlayer {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private Context context;

    private View view, root;

    public DanmukuPlayer(Context context) {
        this(context, null);
    }

    public DanmukuPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        inflater = LayoutInflater.from(context);

        initContentView();
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_danmuku, this);
        root = findViewById(R.id.root);
    }

    @Override
    public void showView() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hideView() {
        setVisibility(GONE);
    }

    private IDanmakuView danmakuView;
    private BaseDanmakuParser parser;
    private DanmakuContext danmakuContext;

    public void initDanmuku() {

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        danmakuView = (DanmakuView) view.findViewById(R.id.danmukuview);
        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 2)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.4f)
                .setScaleTextSize(0.8f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
    }

    @Override
    public void hideDanmaku() {
        if (danmakuView != null) {
            danmakuView.hide();
        }
    }

    @Override
    public boolean isShownDanmaku() {
        if (danmakuView != null) {
            return danmakuView.isShown();
        }
        return false;
    }

    public void showDanmaku() {
        if (danmakuView != null) {
            danmakuView.show();
        }
    }

    public boolean prepared = false;

    @Override
    public void loadDanmaku(InputStream is) {
        if (is == null)
            return;
        if (danmakuView == null)
            return;
        // getResources().openRawResource(R.raw.comments)
        parser = createParser(is);
        if (parser == null)
            return;
        danmakuView.setCallback(callback);
        danmakuView.setOnDanmakuClickListener(onDanmakuClickListener);
        if (context == null)
            return;
        danmakuView.prepare(parser, danmakuContext);
        danmakuView.showFPS(false);
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.hide();
        prepared = true;
        Log.i(tag, "loadDanmaku");
    }

    @Override
    public void addDanmaku(String text) {
        if (danmakuView == null) {
        return;
        }
        if (TextUtils.isEmpty(text))
            return;
        if (danmakuContext == null)
            return;
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null) {
            return;
        }
        danmaku.text = text;
        danmaku.padding = 5;
        danmaku.priority = 9;
        danmaku.isLive = true;
        danmaku.time = danmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f * (parser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.GREEN;
        danmakuView.addDanmaku(danmaku);
        Log.i(tag, "addDanmaku");
    }

    @Override
    public void seekToDanmaku(long ms) {
        if (danmakuView != null && danmakuView.isPrepared()) {
            Log.i(tag, "seekToDanmaku/ms=" + ms);
            danmakuView.seekTo(ms);
        }
    }

    private DrawHandler.Callback callback = new DrawHandler.Callback() {

        @Override
        public void updateTimer(DanmakuTimer timer) {
            // Log.d(tag, "updateTimer");
        }

        @Override
        public void drawingFinished() {
            Log.d(tag, "drawingFinished");
        }

        @Override
        public void danmakuShown(BaseDanmaku danmaku) {
            Log.d(tag, "danmakuShown/text=" + danmaku.text);
        }

        @Override
        public void prepared() {
            Log.d(tag, "prepared");
            if (danmakuView != null)
                danmakuView.start();
        }
    };

    private IDanmakuView.OnDanmakuClickListener onDanmakuClickListener = new IDanmakuView.OnDanmakuClickListener() {

        @Override
        public void onDanmakuClick(BaseDanmaku latest) {
            Log.d(tag, "onDanmakuClick/text=" + latest.text);
        }

        @Override
        public void onDanmakuClick(IDanmakus danmakus) {
            Log.d(tag, "onDanmakuClick/size=" + danmakus.size());
        }
    };

    private BaseDanmakuParser createParser(InputStream stream) {
        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }
        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }

    @Override
    public void pauseDanmaku() {
        if (danmakuView != null && danmakuView.isPrepared()) {
            Log.i(tag, "pauseDanmaku");
            danmakuView.pause();
        }
    }

    @Override
    public void resumeDanmaku() {
        if (danmakuView != null && danmakuView.isPaused()) {
            Log.i(tag, "resumeDanmaku");
            danmakuView.resume();
        }
    }

    @Override
    public void destroyDanmaku() {
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
            Log.i(tag, "destroyDanmaku");
        }
    }
}
