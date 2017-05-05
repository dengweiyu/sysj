package com.ifeimo.im.common.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ifeimo.im.framwork.view.SlideView;

import java.util.ArrayList;
import java.util.List;

/**
 * 策划适配器
 * Created by lpds on 2017/4/28.
 */
public abstract class SlideAdapter<M,H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H>{

    private Context context;
    private List<M> datas;
    private RecyclerView recyclerView;


    public List<M> getDatas() {
        return datas;
    }

    public void setDatas(List<M> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public SlideAdapter(Context context,RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public SlideAdapter(Context context,RecyclerView recyclerView, List<M> datas) {
        this.context = context;
        this.datas = datas;
        this.recyclerView = recyclerView;
    }


    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent != null){
            parent = convertAllView();
        }
        return (H) parent.getTag();
    }

    @Override
    public final void onBindViewHolder(final H holder, int position) {
        bindData(holder,getModel(position));
    }

    @Override
    public int getItemCount() {
        if(datas != null){
            return datas.size();
        }
        return 0;
    }

    private ViewGroup convertAllView(){

        SlideView horizontalScrollView = new SlideView(context);

        View centerView = LayoutInflater.from(context).inflate(getCenterView(),null);
        View slideView = LayoutInflater.from(context).inflate(getSildeView(),null);

        horizontalScrollView.setCenterView(centerView);
        horizontalScrollView.setSlideView(slideView);

        H holder = getHolder(horizontalScrollView);
        horizontalScrollView.setTag(holder);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        return horizontalScrollView;
    }

    protected abstract void bindData(H holder,M m);

    private M getModel(int position){
       return datas.get(position);
    }

    protected abstract int getCenterView();

    protected abstract int getSildeView();

    protected abstract H getHolder(View v);

    private class SlideView extends HorizontalScrollView{

        private View centerView;
        private View slideView;
        private LinearLayout l;
        private boolean isShow;
        private int deletViewWidth;

        public View getCenterView() {
            return centerView;
        }

        public void setCenterView(View centerView) {
            this.centerView = centerView;
            l.addView(centerView);
        }

        public View getSlideView() {
            return slideView;
        }

        public void setSlideView(View slideView) {
            this.slideView = slideView;
            l.addView(slideView);
        }

        public SlideView(Context context) {
            super(context);
            init();
        }

        public SlideView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        private void init() {
            LinearLayout itemlayout = new LinearLayout(getContext());
            itemlayout.setOrientation(LinearLayout.HORIZONTAL);
            addView(itemlayout);
        }



        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            post(new Runnable() {
                @Override
                public void run() {
                    getLayoutParams().width = recyclerView.getMeasuredWidth();
                    deletViewWidth = slideView.getMeasuredWidth();
                }
            });
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            isShow = true;
            Rect rect = new Rect();
            if(slideView.getLocalVisibleRect(rect)){
                if(rect.right > 0 && rect.right > deletViewWidth/2){
                    isShow = false;
                }
            }
            super.onScrollChanged(l, t, oldl, oldt);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if(ev.getAction() == MotionEvent.ACTION_UP){
                if(!isShow){
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    },100);
                }else{
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fullScroll(HorizontalScrollView.FOCUS_LEFT);
                        }
                    },100);
                }
            }

            return super.onTouchEvent(ev);
        }
    }

}
