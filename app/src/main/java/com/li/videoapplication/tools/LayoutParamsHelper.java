package com.li.videoapplication.tools;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.ScreenUtil;

public class LayoutParamsHelper {

    private Context context;

    public WindowManager windowManager;

    private int screenWidth, screenHeight;

    @SuppressWarnings("deprecation")
    public LayoutParamsHelper() {
        super();

        context = AppManager.getInstance().getApplication();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
    }

    public void setAdapterView(View view, int dpHeight) {
        if (view != null) {
            int pxHeight = ScreenUtil.dp2px(dpHeight);
            setAdapterView(view, ListView.LayoutParams.MATCH_PARENT, pxHeight);
        }
    }

    public void setListView(View view, int dpHeight) {
        if (view != null) {
            int pxHeight = ScreenUtil.dp2px(dpHeight);
            setListView(view, ListView.LayoutParams.MATCH_PARENT, pxHeight);
        }
    }

    public void setGridView(View view, int dpHeight) {
        if (view != null) {
            int pxHeight = ScreenUtil.dp2px(dpHeight);
            setGridView(view, ListView.LayoutParams.MATCH_PARENT, pxHeight);
        }
    }

    public void setLinearLayout(View view, int dpHeight) {
        if (view != null) {
            int pxHeight = ScreenUtil.dp2px(dpHeight);
            setLinearLayout(view, LinearLayout.LayoutParams.MATCH_PARENT, pxHeight);
        }
    }

    public void setRelativeLayout(View view, int dpHeight) {
        if (view != null) {
            int pxHeight = ScreenUtil.dp2px(dpHeight);
            setLinearLayout(view, RelativeLayout.LayoutParams.MATCH_PARENT, pxHeight);
        }
    }

    public void setFrameLayout(View view, int dpHeight) {
        if (view != null) {
            int pxHeight = ScreenUtil.dp2px(dpHeight);
            setFrameLayout(view, RelativeLayout.LayoutParams.MATCH_PARENT, pxHeight);
        }
    }

    /*********************************/

    public void setListView(View view, int w, int h) {
        if (view != null) {
            ListView.LayoutParams params = new ListView.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    public void setGridView(View view, int w, int h) {
        if (view != null) {
            GridView.LayoutParams params = new GridView.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    public void setAdapterView(View view, int w, int h) {
        if (view != null) {
            AdapterView.LayoutParams params = new AdapterView.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    public void setLinearLayout(View view, int w, int h) {
        if (view != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    public void setRelativeLayout(View view, int w, int h) {
        if (view != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    public void setFrameLayout(View view, int w, int h) {
        if (view != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    public void setViewGroupLayout(View view, int w, int h) {
        if (view != null) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = w;
            params.height = h;
            view.setLayoutParams(params);
        }
    }
}
