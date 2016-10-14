package com.li.videoapplication.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.li.videoapplication.R;

/**
 * ListView下拉刷新和加载更多
 * <p>
 * 
 * <strong>变更说明:</strong>
 * <p>
 * 默认如果设置了OnRefreshListener接口和OnLoadMoreListener接口，<br>
 * 并且不为null，则打开这两个功能了。
 * <p>
 * 剩余两个Flag：isAutoLoadMore(是否自动加载更多)和 <br>
 * isMoveToFirstItemAfterRefresh(下拉刷新后是否显示第一条Item)
 * 
 * <p>
 * <strong>有改进意见，请发送到俺的邮箱哈~ 多谢各位小伙伴了！^_^</strong>
 * 
 * @date 2013-11-11 下午10:09:26
 * @change JohnWatson
 * @mail xxzhaofeng5412@gmail.com
 * @version 1.0
 */
public class CustomListView extends ListView implements OnScrollListener {

	/** 显示格式化日期模板 */
	private final static String DATE_FORMAT_STR = "yyyy年MM月dd日 HH:mm";

	/** 实际的padding的距离与界面上偏移距离的比例 */
	private final static int RATIO = 3;

	private final static int RELEASE_TO_REFRESH = 0;
	private final static int PULL_TO_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	/** 加载中 */
	private final static int ENDINT_LOADING = 1;
	/** 手动完成刷新 */
	private final static int ENDINT_MANUAL_LOAD_DONE = 2;
	/** 自动完成刷新 */
	private final static int ENDINT_AUTO_LOAD_DONE = 3;

	/**
	 * 0:RELEASE_TO_REFRESH;
	 * <p>
	 * 1:PULL_To_REFRESH;
	 * <p>
	 * 2:REFRESHING;
	 * <p>
	 * 3:DONE;
	 * <p>
	 * 4:LOADING
	 */
	private int headerState;
	/**
	 * 0:完成/等待刷新 ;
	 * <p>
	 * 1:加载中
	 */
	private int footerState;

	// ============ 功能设置Flag

	/** 可以加载更多？ */
	private boolean canLoadMore = false;
	/** 可以下拉刷新？ */
	private boolean canRefresh = false;
	/** 可以自动加载更多吗？（注意，先判断是否有加载更多，如果没有，这个flag也没有意义） */
	private boolean isAutoLoadMore = true;
	/** 下拉刷新后是否显示第一条Item */
	private boolean isMoveToFirstItemAfterRefresh = false;

	public boolean isCanLoadMore() {
		return canLoadMore;
	}

	public void setCanLoadMore(boolean pCanLoadMore) {
		canLoadMore = pCanLoadMore;
		if (canLoadMore && getFooterViewsCount() == 0) {
			addFooterView();
		}
	}

	public boolean isCanRefresh() {
		return canRefresh;
	}

	public void setCanRefresh(boolean pCanRefresh) {
		canRefresh = pCanRefresh;
	}

	public boolean isAutoLoadMore() {
		return isAutoLoadMore;
	}

	public void setAutoLoadMore(boolean pIsAutoLoadMore) {
		isAutoLoadMore = pIsAutoLoadMore;
	}

	public boolean isMoveToFirstItemAfterRefresh() {
		return isMoveToFirstItemAfterRefresh;
	}

	public void setMoveToFirstItemAfterRefresh(
			boolean pIsMoveToFirstItemAfterRefresh) {
		isMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
	}

	// ============================================================================

	private LayoutInflater inflater;

	private LinearLayout headView;
	private TextView headerTips;
	private TextView headerLastUpdated;
	private ImageView headerArrow;
	private ProgressBar headerProgress;

    private View footerView;
	private ProgressBar footerProgress;
	private TextView footerTips;

	/** headView动画 */
	private RotateAnimation arrowAnim;
	/** headView反转动画 */
	private RotateAnimation arrowReverseAnim;

	/** 用于保证startY的值在一个完整的touch事件中只被记录一次 */
	private boolean isRecored;

	private int headViewWidth;
	private int headViewHeight;

	private int startY;
	private boolean isBack;

	private int firstItemIndex;
	private int lastItemIndex;
	private int count;
	private boolean enoughCount;// 足够数量充满屏幕？
	private Context context;

	private OnRefreshListener refreshListener;
	private OnLoadMoreListener loadMoreListener;

	public CustomListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		context = pContext;
		init(pContext);
	}

	public CustomListView(Context pContext) {
		super(pContext);
		context = pContext;
		init(pContext);
	}

	public CustomListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		context = pContext;
		init(pContext);
	}

	/**
	 * 初始化操作
	 * 
	 * @param pContext
	 * @date 2013-11-20 下午4:10:46
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void init(Context pContext) {
		setCacheColorHint(pContext.getResources().getColor(R.color.gray));
		inflater = LayoutInflater.from(pContext);
		context = pContext;
		addHeadView();

		setOnScrollListener(this);

		initPullImageAnimation(0);
	}

	/**
	 * 添加下拉刷新的HeadView
	 * 
	 * @date 2013-11-11 下午9:48:26
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void addHeadView() {

		headView = (LinearLayout) inflater.inflate(R.layout.p2refresh_header, null);

		headerArrow = (ImageView) headView.findViewById(R.id.p2refresh_header_arrow);
		headerArrow.setMinimumWidth(10);
		headerArrow.setMinimumHeight(20);

		headerProgress = (ProgressBar) headView.findViewById(R.id.p2refresh_header_progress);

		headerTips = (TextView) headView.findViewById(R.id.p2refresh_header_tips);

		headerLastUpdated = (TextView) headView.findViewById(R.id.p2refresh_header_lastUpdated);

		measureView(headView);
		headViewHeight = headView.getMeasuredHeight();
		headViewWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headViewHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView, null, false);

		headerState = DONE;
	}

	/**
	 * 添加加载更多FootView
	 * 
	 * @date 2013-11-11 下午9:52:37
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void addFooterView() {

		footerView = inflater.inflate(R.layout.p2refresh_footer, null);
		footerView.setVisibility(View.VISIBLE);

		footerProgress = (ProgressBar) footerView.findViewById(R.id.p2refresh_footer_progress);

		footerTips = (TextView) footerView.findViewById(R.id.p2refresh_footer_tips);
		footerView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canLoadMore) {
                    if (canRefresh) {
                        // 当可以下拉刷新时，如果FootView没有正在加载，并且HeadView没有正在刷新，才可以点击加载更多。
                        if (footerState != ENDINT_LOADING && headerState != REFRESHING) {
                            footerState = ENDINT_LOADING;
                            onLoadMore();
                        }
                    } else if (footerState != ENDINT_LOADING) {
                        // 当不能下拉刷新时，FootView不正在加载时，才可以点击加载更多。
                        footerState = ENDINT_LOADING;
                        onLoadMore();
                    }
                }
            }
        });

		addFooterView(footerView);

		if (isAutoLoadMore) {
			footerState = ENDINT_AUTO_LOAD_DONE;
		} else {
			footerState = ENDINT_MANUAL_LOAD_DONE;
		}
	}

	/**
	 * 实例化下拉刷新的箭头的动画效果
	 * 
	 * @param pAnimDuration
	 *            动画运行时长
	 * @date 2013-11-20 上午11:53:22
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void initPullImageAnimation(final int pAnimDuration) {

		int _Duration;

		if (pAnimDuration > 0) {
			_Duration = pAnimDuration;
		} else {
			_Duration = 250;
		}
		// Interpolator _Interpolator;
		// switch (pAnimType) {
		// case 0:
		// _Interpolator = new AccelerateDecelerateInterpolator();
		// break;
		// case 1:
		// _Interpolator = new AccelerateInterpolator();
		// break;
		// case 2:
		// _Interpolator = new AnticipateInterpolator();
		// break;
		// case 3:
		// _Interpolator = new AnticipateOvershootInterpolator();
		// break;
		// case 4:
		// _Interpolator = new BounceInterpolator();
		// break;
		// case 5:
		// _Interpolator = new CycleInterpolator(1f);
		// break;
		// case 6:
		// _Interpolator = new DecelerateInterpolator();
		// break;
		// case 7:
		// _Interpolator = new OvershootInterpolator();
		// break;
		// default:
		// _Interpolator = new LinearInterpolator();
		// break;
		// }

		Interpolator _Interpolator = new LinearInterpolator();

		arrowAnim = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrowAnim.setInterpolator(_Interpolator);
		arrowAnim.setDuration(_Duration);
		arrowAnim.setFillAfter(true);

		arrowReverseAnim = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrowReverseAnim.setInterpolator(_Interpolator);
		arrowReverseAnim.setDuration(_Duration);
		arrowReverseAnim.setFillAfter(true);
	}

	/**
	 * 测量HeadView宽高(注意：此方法仅适用于LinearLayout，请读者自己测试验证。)
	 * 
	 * @param pChild
	 * @date 2013-11-20 下午4:12:07
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void measureView(View pChild) {
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 为了判断滑动到ListView底部没
	 */
	@Override
	public void onScroll(AbsListView pView, int pFirstVisibleItem, int pVisibleItemCount, int pTotalItemCount) {
		firstItemIndex = pFirstVisibleItem;
		lastItemIndex = pFirstVisibleItem + pVisibleItemCount - 2;
		count = pTotalItemCount - 2;
		if (pTotalItemCount > pVisibleItemCount) {
			enoughCount = true;
			// endingView.setVisibility(View.VISIBLE);
		} else {
			enoughCount = false;
		}
	}

	/**
	 * 这个方法，可能有点乱，大家多读几遍就明白了。
	 */
	@Override
	public void onScrollStateChanged(AbsListView pView, int pScrollState) {
		if (canLoadMore) {// 存在加载更多功能
			if (lastItemIndex == count && pScrollState == SCROLL_STATE_IDLE) {
				// SCROLL_STATE_IDLE=0，滑动停止
				if (footerState != ENDINT_LOADING) {
					if (isAutoLoadMore) {// 自动加载更多，我们让FootView显示 “更 多”
						if (canRefresh) {
							// 存在下拉刷新并且HeadView没有正在刷新时，FootView可以自动加载更多。
							if (headerState != REFRESHING) {
								// FootView显示 : 更 多 ---> 加载中...
								footerState = ENDINT_LOADING;
								onLoadMore();
								changeFooterViewByState();
							}
						} else {// 没有下拉刷新，我们直接进行加载更多。
								// FootView显示 : 更 多 ---> 加载中...
							footerState = ENDINT_LOADING;
							onLoadMore();
							changeFooterViewByState();
						}
					} else {// 不是自动加载更多，我们让FootView显示 “点击加载”
							// FootView显示 : 点击加载 ---> 加载中...
						footerState = ENDINT_MANUAL_LOAD_DONE;
						changeFooterViewByState();
					}
				}
			}
		} else if (footerView != null
				&& footerView.getVisibility() == VISIBLE) {
			// 突然关闭加载更多功能之后，我们要移除FootView。
			footerView.setVisibility(View.GONE);
			this.removeFooterView(footerView);
		}
	}

	/**
	 * 改变加载更多状态
	 * 
	 * @date 2013-11-11 下午10:05:27
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void changeFooterViewByState() {
		if (canLoadMore) {
			// 允许加载更多
			switch (footerState) {
			case ENDINT_LOADING:// 刷新中

				// 加载中...
				if (footerTips.getText().equals(R.string.p2refresh_doing_end_refresh)) {
					break;
				}

				footerTips.setText(R.string.p2refresh_doing_end_refresh);
				footerTips.setVisibility(View.VISIBLE);
				footerProgress.setVisibility(View.VISIBLE);
				break;
			case ENDINT_MANUAL_LOAD_DONE:// 手动刷新完成

				// 点击加载
				footerTips.setText(R.string.p2refresh_end_click_load_more);
				footerTips.setVisibility(View.VISIBLE);
				footerProgress.setVisibility(View.GONE);

				footerView.setVisibility(View.VISIBLE);
				break;
			case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成

				// 更 多
				footerTips.setText(R.string.p2refresh_end_load_more);
				footerTips.setVisibility(View.VISIBLE);
				footerProgress.setVisibility(View.GONE);

				footerView.setVisibility(View.VISIBLE);
				break;
			default:
				// 原来的代码是为了：当所有item的高度小于ListView本身的高度时，
				// 要隐藏掉FootView，大家自己去原作者的代码参考。

				 if (enoughCount) {
                     footerView.setVisibility(View.VISIBLE);
				 } else {
                     footerView.setVisibility(View.GONE);
				 }
				break;
			}
		}
	}

	/**
	 * 原作者的，我没改动，请读者自行优化。
	 */
	public boolean onTouchEvent(MotionEvent event) {

		if (canRefresh) {
			if (canLoadMore && footerState == ENDINT_LOADING) {
				// 如果存在加载更多功能，并且当前正在加载更多，默认不允许下拉刷新，必须加载完毕后才能使用。
				return super.onTouchEvent(event);
			}

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:
				if (headerState != REFRESHING && headerState != LOADING) {
					if (headerState == DONE) {

					}
					if (headerState == PULL_TO_REFRESH) {
						headerState = DONE;
						changeHeaderViewByState();
					}
					if (headerState == RELEASE_TO_REFRESH) {
						headerState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				isRecored = false;
				isBack = false;
				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}
				if (headerState != REFRESHING && isRecored
						&& headerState != LOADING) {
					// 保证在设置padding的过程中，当前的位置一直是在head，
					// 否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (headerState == RELEASE_TO_REFRESH) {
						setSelection(0);
						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headViewHeight)
								&& (tempY - startY) > 0) {
							headerState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							headerState = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (headerState == PULL_TO_REFRESH) {
						setSelection(0);
						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headViewHeight) {
							headerState = RELEASE_TO_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							headerState = DONE;
							changeHeaderViewByState();
						}
					}
					if (headerState == DONE) {
						if (tempY - startY > 0) {
							headerState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						}
					}
					if (headerState == PULL_TO_REFRESH) {
						headView.setPadding(0, -1 * headViewHeight + (tempY - startY) / RATIO, 0, 0);

					}
					if (headerState == RELEASE_TO_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO - headViewHeight, 0, 0);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 当HeadView状态改变时候，调用该方法，以更新界面
	 * 
	 * @date 2013-11-20 下午4:29:44
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void changeHeaderViewByState() {
		switch (headerState) {
		case RELEASE_TO_REFRESH:
			headerArrow.setVisibility(View.VISIBLE);
			headerProgress.setVisibility(View.GONE);
			headerTips.setVisibility(View.VISIBLE);
			headerLastUpdated.setVisibility(View.VISIBLE);

			headerArrow.clearAnimation();
			headerArrow.startAnimation(arrowAnim);
			// 松开刷新
			headerTips.setText(R.string.p2refresh_release_refresh);

			break;
		case PULL_TO_REFRESH:
			headerProgress.setVisibility(View.GONE);
			headerTips.setVisibility(View.VISIBLE);
			headerLastUpdated.setVisibility(View.VISIBLE);
			headerArrow.clearAnimation();
			headerArrow.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				headerArrow.clearAnimation();
				headerArrow.startAnimation(arrowReverseAnim);
				// 下拉刷新

				headerTips.setText(R.string.p2refresh_pull_to_refresh);
			} else {
				// 下拉刷新

				headerTips.setText(R.string.p2refresh_pull_to_refresh);
			}
			break;

		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);

			// 华生的建议：
			// 实际上这个的setPadding可以用动画来代替。我没有试，但是我见过。其实有的人也用Scroller可以实现这个效果，
			// 我没时间研究了，后期再扩展，这个工作交给小伙伴你们啦~ 如果改进了记得发到我邮箱噢~
			// 本人邮箱： xxzhaofeng5412@gmail.com

			headerProgress.setVisibility(View.VISIBLE);
			headerArrow.clearAnimation();
			headerArrow.setVisibility(View.GONE);
			// 正在刷新...

			headerTips.setText(R.string.p2refresh_doing_head_refresh);
			headerLastUpdated.setVisibility(View.VISIBLE);

			break;
		case DONE:
			headView.setPadding(0, -1 * headViewHeight, 0, 0);

			// 此处可以改进，同上所述。

			headerProgress.setVisibility(View.GONE);
			headerArrow.clearAnimation();

			headerArrow.setImageResource(R.drawable.p2refresh_arrow);
			// 下拉刷新

			headerTips.setText(R.string.p2refresh_pull_to_refresh);
			headerLastUpdated.setVisibility(View.VISIBLE);

			break;
		}
	}

	/**
	 * 下拉刷新监听接口
	 * 
	 * @date 2013-11-20 下午4:50:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * 加载更多监听接口
	 * 
	 * @date 2013-11-20 下午4:50:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public void setOnRefreshListener(OnRefreshListener pRefreshListener) {
		if (pRefreshListener != null) {
			refreshListener = pRefreshListener;
			canRefresh = true;
		}
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			loadMoreListener = pLoadMoreListener;
			canLoadMore = true;
			if (canLoadMore && getFooterViewsCount() == 0) {
				addFooterView();
			}
		}
	}

	/**
	 * 正在下拉刷新
	 * 
	 * @date 2013-11-20 下午4:45:47
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	/**
	 * 下拉刷新完成
	 * 
	 * @date 2013-11-20 下午4:44:12
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void onRefreshComplete() {
		// 下拉刷新后是否显示第一条Item
		if (isMoveToFirstItemAfterRefresh)
			setSelection(0);

		headerState = DONE;
		// 最近更新: Time

		headerLastUpdated.setText(getResources().getString(R.string.p2refresh_refresh_lastUpdated) + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
		changeHeaderViewByState();
	}

	/**
	 * 正在加载更多，FootView显示 ： 加载中...
	 * 
	 * @date 2013-11-20 下午4:35:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void onLoadMore() {
		if (loadMoreListener != null) {
			// 加载中...
			footerTips.setText(R.string.p2refresh_doing_end_refresh);
			footerTips.setVisibility(View.VISIBLE);
			footerProgress.setVisibility(View.VISIBLE);

			loadMoreListener.onLoadMore();
		}
	}

	/**
	 * 加载更多完成
	 * 
	 * @date 2013-11-11 下午10:21:38
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void onLoadMoreComplete() {
		if (isAutoLoadMore) {
			footerState = ENDINT_AUTO_LOAD_DONE;
		} else {
			footerState = ENDINT_MANUAL_LOAD_DONE;
		}
		changeFooterViewByState();
	}

	/**
	 * 主要更新一下刷新时间
	 * 
	 * @param adapter
	 * @date 2013-11-20 下午5:35:51
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void setAdapter(BaseAdapter adapter) {
		// 最近更新: Time

		headerLastUpdated.setText(getResources().getString(R.string.p2refresh_refresh_lastUpdated) + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
		super.setAdapter(adapter);
	}
}
