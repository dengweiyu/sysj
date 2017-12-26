package com.li.videoapplication.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;


/**
 *
 */

public class SimpleSwipeRefreshLayout extends SwipeRefreshLayout {

    private WebView mWebView;
    private boolean mShouldIntercept = true;
    public SimpleSwipeRefreshLayout(Context context) {
        super(context);

    }

    public SimpleSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        System.out.println("mWebView:"+mWebView.getScrollY());

        if (mWebView != null && mWebView.getScrollY() > 0 || !mShouldIntercept){
            return false;
        }else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void setWebView(WebView mWebView) {
        this.mWebView = mWebView;
    }

    public void setShouldIntercept(boolean mShouldIntercept) {
        this.mShouldIntercept = mShouldIntercept;
    }
}
