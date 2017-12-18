package com.li.videoapplication.data.image;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.module.AppGlideModule;

import static com.bumptech.glide.load.engine.executor.GlideExecutor.newDiskCacheExecutor;
import static com.bumptech.glide.load.engine.executor.GlideExecutor.newSourceExecutor;

/**
 * Created by cx on 2017/12/13.
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        final GlideExecutor.UncaughtThrowableStrategy myUncaughtThrowableStrategy = new GlideExecutor.UncaughtThrowableStrategy() {
            @Override
            public void handle(Throwable t) {
                Log.e("MyAppGlideModule", "未捕获异常..");
                t.printStackTrace();
            }
        };
        builder.setDiskCacheExecutor(newDiskCacheExecutor(myUncaughtThrowableStrategy));
        builder.setResizeExecutor(newSourceExecutor(myUncaughtThrowableStrategy));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
