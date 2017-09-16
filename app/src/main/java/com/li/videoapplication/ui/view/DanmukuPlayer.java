package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
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
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
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
         //   Emojix.wrap(context);
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



    public void initDanmuku() {

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        SpannedCacheStuffer cacheStuffer = new SpannedCacheStuffer();
        cacheStuffer.setPadding(6);         //因为内容居中了 宽度高度各增加了6 保证了每个方向的内边距都是3

        danmakuView = (DanmakuView) view.findViewById(R.id.danmukuview);
        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 2)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.4f)
                .setScaleTextSize(0.8f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair)
                .setCacheStuffer(cacheStuffer,mCacheStufferAdapter);
    }


    private String[] expressionArray;
    private String[] expressionCnArray;


    /**
     * TODO 更改字幕，适配融云表情,Unicode表情
     */
    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {


        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku == null ){
                return;
            }

            if (danmaku.text == null || StringUtil.isNull(danmaku.text.toString())){
                return;
            }

            //已经处理过 不再处理
            if (danmaku.text instanceof Spanned){
                return;
            }
            String content ;
            SpannableStringBuilder spannableString = null;
            try {
                content = new String(danmaku.text.toString().getBytes(StandardCharsets.UTF_8));

                //后台之前的版本是传\\过来
                if (PatternUtil.isContainUnicode(content)) {
                    content = content.replace("\\\\", "\\");// \\ud83d\\ude24 --> \ud83d\ude24
                }
                //处理Unicode表情
                //content = "\ud83d\ude24"  这样肯定是可以显示表情的 因为编译器做了转义 所以从后台传过来的就需要自己转
                content = encodeHex(content);
                spannableString = new SpannableStringBuilder(content);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            //处理融云表情
            int len = 0;
            int starts = 0;
            int end = 0;
            //字体大小
            int size = (int)(25f * (parser.getDisplayer().getDensity() - 0.6f));

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
                            drawable.setBounds(0, 0, size, size);
                            CenterImageSpan span = new CenterImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                            spannableString.setSpan(span, starts, end + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

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



            danmaku.textSize = size;
            danmaku.text = spannableString;
            if (danmakuView != null){
                danmakuView.invalidateDanmaku(danmaku,true);
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

        danmaku.text =text+' ';
        danmaku.padding = 0;        //只设置了left top 这样会导致边框压到内容 因此设置SpannedCacheStuffer来增加占位宽度和高度从而设置内边距更合适
        danmaku.priority = 9;
        danmaku.isLive = true;
        danmaku.time = danmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f * (parser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
    //    danmaku.textShadowColor = Color.WHITE;
         //danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.WHITE;
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

    /**
     *emoji需要进行转码
     * @param content
     * @return
     */
    private final String encodeHex(String content) {
        if (content == null){
            return content;
        }

        StringBuffer sb = new StringBuffer();
        try {
                String pattern = "\\\\[u].{4}\\\\[u].{4}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(content);
                if (m.find()){
                    content = m.replaceAll(encodeEmoji(m.group()));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().equals("")?content:sb.toString();
    }


    private final String encodeEmoji(String content){
        if (content == null || content.length() != 12){
            return content;
        }
        try {
            String start = content.substring(2,6);
            String end = content.substring(8,12);

            int ss1 = Integer.parseInt(start, 16);
            int ss2 = Integer.parseInt(end, 16);

            char chars = Character.toChars(ss1)[0];
            char chars2 = Character.toChars(ss2)[0];

            int codePoint = Character.toCodePoint(chars, chars2);
            String emojiString = new String(Character.toChars(codePoint));
            return emojiString;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return content;
    }
}
