package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import cn.nekocode.emojix.Emojix;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.danmaku.util.IOUtils;
import master.flame.danmaku.ui.widget.CenterImageSpan;
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

        {
            Emojix.wrap(context);
            expressionArray = context.getResources().getStringArray(R.array.expressionArray);
            expressionCnArray = context.getResources().getStringArray(R.array.expressionCnArray);
        }
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


    /**
     * Fixme 这里可以修改字幕背景样式 可以参照sample尝试修改融云表情显示的问题
     */
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
                .preventOverlapping(overlappingEnablePair)
                .setCacheStuffer(new SpannedCacheStuffer(),mCacheStufferAdapter);
    }


    private String[] expressionArray;
    private String[] expressionCnArray;


    /**
     * Todo 更改字幕，适配融云表情
     */
    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {


        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku == null){
                return;
            }

            if (StringUtil.isNull(danmaku.text.toString())){
                return;
            }

            //已经处理过 不在处理
            if (danmaku.text instanceof Spanned){
                return;
            }

            String content ;
            try {
                content = new String(danmaku.text.toString().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }
            SpannableString spannableString = null;

            //处理显示表情
            int len = 0;
            int starts = 0;
            int end = 0;

            while (len < content.length()) {
                if (content.indexOf("[", starts) != -1 && content.indexOf("]", end) != -1) {
                    starts = content.indexOf("[", starts);
                    end = content.indexOf("]", end);
                    String face = content.substring(starts + 1, end);
                    for (int i = 0; i < expressionCnArray.length; i++) {
                        if (face.equals(expressionCnArray[i])) {
                            face = expressionArray[i];
                            break;
                        }
                    }
                    try {
                        Field f = R.drawable.class.getDeclaredField(face);
                        int i = f.getInt(R.drawable.class);
                        Drawable drawable = context.getResources().getDrawable(i);
                        if (drawable != null) {

                            int w = (int)(25f * (parser.getDisplayer().getDensity() - 0.6f));
                            int h = w;
                            float scale = 1f;
                            danmaku.padding = 0;


                            danmaku.textSize =  w > h ? (int)(w*scale) : (int)(h*scale);
                            drawable.setBounds(0, 0, (int)(w*scale), (int)(h*scale));

                            CenterImageSpan span = new CenterImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);

                            if (spannableString == null) {
                                spannableString = new SpannableString(content);
                            }

                            spannableString.setSpan(span, starts, end + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            danmaku.text = spannableString;
                            if (danmakuView != null){
                                danmakuView.invalidateDanmaku(danmaku,true);
                            }
                        }
                    } catch (SecurityException | NoSuchFieldException | IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {

                    }
                    starts = end;
                    len = end;
                    end++;
                } else {
                    starts++;
                    end++;
                    len = end;
                    }
                }


        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {

        }
    };

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
    //    danmaku.padding = 5;
        danmaku.priority = 9;
        danmaku.isLive = true;
        danmaku.time = danmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f * (parser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
         //danmaku.underlineColor = Color.GREEN;
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
