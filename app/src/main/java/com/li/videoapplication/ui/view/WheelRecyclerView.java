package com.li.videoapplication.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.li.videoapplication.R;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * Created by liuwei on 2017/6/22.
 * 基于RecyclerView 滚轮选择器
 */

public class WheelRecyclerView extends RecyclerView {

    private int mItemHolder = 1;        //顶部占位Item数

    private int mItemHeight;            //item 的高度

    private boolean isScrolling;

    private int mSelectPosition;

    private OnMeasureChild mMeasureChild;

    private int mScrollState;

    private float mStartOffset;


    public WheelRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public WheelRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        setNestedScrollingEnabled(true);

        setLayoutManager(new LinearLayoutManager(context));
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getChildCount() > 0) {
                    //以第一个Item为标准
                    mItemHeight = getChildAt(0).getMeasuredHeight();
                    //根据占位 算最大高度
                    int height = mItemHeight * (mItemHolder * 2 + 1);
                    getLayoutParams().height = height;

                    invalidate();
                    if (mMeasureChild != null) {
                        mMeasureChild.onChildView(getChildAt(mItemHolder));
                    }
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //真的停止
                    if (isScrolling) {
                        isScrolling = false;
                        mStartOffset = mSelectPosition * mItemHeight;
                        return;
                    }

                    //滚动到合适的位置
                    mSelectPosition = getScrollToPosition() - mItemHolder;
                    smoothScrollBy(0, (getScrollToPosition() - mItemHolder) * mItemHeight - computeVerticalScrollOffset());
                    isScrolling = true;

                }
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isScrolling = false;
                }
                mScrollState = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mItemHeight == 0) {
                    return;
                }
                int currentOffset = computeVerticalScrollOffset();
                float scale = (currentOffset % mItemHeight) / (float) mItemHeight;

                System.out.println("mItemHeight:"+mItemHeight);
                if (currentOffset % mItemHeight == 0){
                    if (!isScrolling){
                        mStartOffset = currentOffset;
                    }
                    entryAnimator(getChildAt(mCurrentPosition),1);
                }else {
                    if (currentOffset > mStartOffset){

                        exitAnimator(getChildAt(mCurrentPosition),scale);
                        entryAnimator(getChildAt(mCurrentPosition+mItemHolder),scale);
                        System.out.println("BB  mCurrentPosition:"+(mCurrentPosition+1)+" "+" mCurrentPosition:"+mCurrentPosition+"  scale:"+scale+" mStartOffset:"+mStartOffset+" offset:"+currentOffset);
                    }else  {

                        exitAnimator(getChildAt(mCurrentPosition),scale);
                        entryAnimator(getChildAt(mCurrentPosition+mItemHolder),scale);
                        System.out.println("AA  mCurrentPosition:"+(mCurrentPosition-1)+" "+" mCurrentPosition:"+mCurrentPosition+"  scale:"+scale+" mStartOffset:"+mStartOffset+" offset:"+currentOffset);
                    }
                }
            }
        });
    }

    private int mCurrentPosition = mItemHolder;

    /**
     * 根据偏移选择合适的 position
     */
    public int getScrollToPosition() {
        int position = (int) Math.rint(computeVerticalScrollOffset() / (float) mItemHeight);
        return position + mItemHolder;
    }


    /**
     * 当前选中position 注意了 这里已经去除了 占位的实际位置 对应数据的位置
     *
     * @return
     */
    public int getSelectPosition() {
        return mSelectPosition;
    }

    /**
     * child view 绘制完成监听回调 可以绘制分割线
     *
     * @param measureChild
     */

    public void setMeasureChild(OnMeasureChild measureChild) {
        mMeasureChild = measureChild;
    }


    public int getItemHolder() {
        return mItemHolder;
    }

    public int getItemHeight() {
        return mItemHeight;
    }

    /**
     * 进入动画
     *
     * @param entryView
     * @param scale
     */
    protected void entryAnimator(View entryView, float scale) {
        if (entryView == null){

            return;
        }

        ColorShadeTextView text = ( ColorShadeTextView)entryView.findViewById(R.id.tv_item_content);
        if (text != null){
            text.setEntry(true);
            text.refreshScale(scale);
        }
    }
    /**
     *退出动画
     * @param exitView
     * @param scale
     *
     */
    protected void exitAnimator(View exitView,float scale) {
        if (exitView == null){
            return;
        }

        ColorShadeTextView text = (ColorShadeTextView)exitView.findViewById(R.id.tv_item_content);
        if (text != null){
            text.setEntry(false);
            text.refreshScale(scale);
        }
    }

        /**
         * 内容适配器 需要继承此适配器
         */
        public static abstract class WheelRecyclerViewAdapter extends Adapter {

            private int mRootId;

            protected WheelRecyclerView mList;

            public WheelRecyclerViewAdapter(WheelRecyclerView list) {
                mList = list;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
                //隐藏顶部占位
                if (position < mList.getItemHolder()) {
                    holder.itemView.setVisibility(INVISIBLE);
                } else {
                    holder.itemView.setVisibility(VISIBLE);
                }
                mRootId = getRootViewId();
                if (mRootId != 0) {
                    View root = holder.itemView.findViewById(mRootId);
                    if (root != null) {
                        root.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position = holder.getAdapterPosition();
                                smoothScrollByPosition(position, true);
                                mList.mSelectPosition = position - mList.getItemHolder();
                            }
                        });
                    }
                }

            }

            @Override
            public int getItemCount() {
                return mList.getItemHolder() * 2;           //顶部和底部一致 都需要留同样的占位
            }

            /**
             * 滚动到对应 position
             */
            public void smoothScrollByPosition(int position, boolean animator) {

                if (animator) {
                    mList.smoothScrollBy(0, (position - mList.getItemHolder()) * mList.getItemHeight() - mList.computeVerticalScrollOffset());
                } else {
                    mList.scrollBy(0, (position - mList.getItemHolder()) * mList.getItemHeight() - mList.computeVerticalScrollOffset());
                }


            }

            /**
             * 返回 根视图的Id 用于响应点击事件
             *
             * @return
             */
            public abstract int getRootViewId();
        }
    /**
     * 第一个child view的大小
     */
    public interface OnMeasureChild {
        void onChildView(View child);
    }


}
