package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.VipManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.image.VideoCover;
import com.li.videoapplication.data.image.VideoDuration;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.Tag;
import com.li.videoapplication.data.model.response.Vip3AndAuthoryEntity;
import com.li.videoapplication.data.qiniu.storage.UploadManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper2;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.MyLocalVideoAdapter;
import com.li.videoapplication.ui.dialog.Vip3ShowDialog;
import com.li.videoapplication.ui.popupwindows.ApplyPopupWindow;
import com.li.videoapplication.ui.popupwindows.IInfoEntity;
import com.li.videoapplication.utils.BitmapUtil;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.SpanUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.SmoothCheckBox;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import rx.Observable;
import rx.Subscriber;

/**
 * 活动：视频分享
 * Created by cx on 2018/1/24.
 */

@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoShare2Activity extends TBaseActivity implements VipManager.OnVipObserver{

    private IInfoEntity mIInfoEntity;
    private VideoCaptureEntity mVideoCaptureEntity;
    private IPopup iPopup;
    private String goodsId = null;
    private long video_length;
    private String fileSize;
    private Bitmap bitmap;
    private String descriptionFormat = "";
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    // 文件大小， 视频图片，编辑
    @BindView(R.id.videoshare_size)
    TextView videoshare_size;
    @BindView(R.id.videoshare_cover)
    ImageView videoshare_cover;
    @BindView(R.id.videoshare_edit)
    TextView videoshare_edit;
    // 视频描述
    @BindView(R.id.videoshare_description)
    EditText videoshare_description;
    @BindView(R.id.videoshare_descriptionCount)
    TextView videoshare_descriptionCount;
    @BindView(R.id.videoshare_type_text)
    TextView videoshare_type_text;
    @BindView(R.id.videoshare_type)
    View videoshare_type;
    // 分享
    @BindView(R.id.videoshare_share)
    TextView videoshare_share;
    @BindView(R.id.videoshare_apply)
    ImageView videoshare_apply;
    @BindView(R.id.videoshare_apply_text)
    TextView videoshare_apply_text;
    @BindView(R.id.videoshare_apply_question)
    ImageView videoshare_apply_question;
    @BindView(R.id.bottom_layout)
    LinearLayout bottom_layout;

    /**
     * 整个标签的跟布局
     */
    @BindView(R.id.id_tag_layout)
    View id_tag_layout;
    /**
     * 标签列表
     */
    @BindView(R.id.id_recyclerView_tag)
    RecyclerView id_recyclerView_tag;
    /**
     * 更多按钮
     */
    @BindView(R.id.id_more_tag_tv)
    View id_more_tag_tv;
    /**
     * 确定选择标签按钮
     */
    @BindView(R.id.id_choose_tag_list_layout)
    View id_choose_tag_list_layout;
    /**
     * 已选择提示文本
     */
    @BindView(R.id.id_had_select_tv)
    TextView id_had_select_tv;
    @BindView(R.id.id_complete_tag_stutas_layout)
    ViewGroup id_complete_tag_stutas_layout;
    @BindView(R.id.id_choose_tag_stutas_layout)
    ViewGroup id_choose_tag_stutas_layout;
    @BindView(R.id.id_choose_tag_1)
    TextView id_choose_tag_1;
    @BindView(R.id.id_choose_tag_2)
    TextView id_choose_tag_2;
    @BindView(R.id.id_choose_tag_3)
    TextView id_choose_tag_3;
    @BindViews({R.id.id_line_1, R.id.id_line_2, R.id.id_line_3})
    View[] id_lines;
    TextView[] tagTextViews;
    @BindView(R.id.id_vip_authority_choose_recyclerView)
    RecyclerView id_vip_authority_choose_recyclerView;
    @BindView(R.id.id_vip_choose_layout)
    View id_vip_choose_layout;
    @BindView(R.id.id_vip_authority_tip_title_tv)
    TextView id_vip_authority_tip_title_tv;
    Vip3ShowDialog vip3ShowDialog;
    boolean isChecked;
    Boolean isChooseTagStatus = false;
    boolean isPrepareMore = false;
    /**
     * 选择的背景图片
     */
    Drawable selectTagDrawable;
    /**
     * 已选择列表
     */
    final List<Tag> tagList = new ArrayList<>();
    /**
     * 当前的game id的所有标签
     */
    private final List<Tag> tagAllDatas = new ArrayList<>();
    private final List<Vip3AndAuthoryEntity.PrivilegeDataBean.PrivilegeTextBean> vipAuthoritys = new ArrayList<>();
    private final BaseQuickAdapter<Vip3AndAuthoryEntity.PrivilegeDataBean.PrivilegeTextBean, BaseViewHolder> vipAuthorityAdapter =
            new BaseQuickAdapter<Vip3AndAuthoryEntity.PrivilegeDataBean.PrivilegeTextBean, BaseViewHolder>(R.layout.adapter_button_choose, vipAuthoritys) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder,
                                       final Vip3AndAuthoryEntity.PrivilegeDataBean.PrivilegeTextBean privilegeTextBean) {
                    TextView id_choose_tag_1 = baseViewHolder.getView(R.id.id_choose_tag_1);
                    id_choose_tag_1.setText(privilegeTextBean.getText());
                    id_choose_tag_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID, UmengAnalyticsHelper2.SHARE_VIDEO_VIP_PRIVILEGE_TYPE);
                            vipAuthorityChoose(privilegeTextBean);
                        }
                    });
                    if (privilegeTextBean.isHighlighted()) {
                        id_choose_tag_1.
                                setBackground(ActivityCompat.getDrawable(getApplication(), R.drawable.shape_main_bule));
                        id_choose_tag_1.setTextColor(ActivityCompat.getColor(getApplication(), R.color.menu_help_blue_1));
                    } else {
                        id_choose_tag_1.
                                setBackground(ActivityCompat.getDrawable(getApplication(), R.drawable.shape_gray));
                        id_choose_tag_1.setTextColor(ActivityCompat.getColor(getApplication(), R.color.color_999999));
                    }
                }
            };

    private void vipAuthorityChoose(Vip3AndAuthoryEntity.PrivilegeDataBean.PrivilegeTextBean vipAuthorityEntity) {
        if (vipAuthorityEntity.isHighlighted()) {
            ToastHelper.s("您已经拥有此特权！");
        } else {
            vip3Show();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vipAuthorityAdapter.notifyDataSetChanged();
            }
        });
    }

    private void vip3Show(){
        if (vip3ShowDialog == null) {
            vip3ShowDialog = new Vip3ShowDialog(this, vip3AndAuthoryEntity);
        }
        vip3ShowDialog.show();
    }

    public void dialogDismiss() {
        if (isChecked) {
            isChecked = false;
            videoshare_apply.setImageResource(R.drawable.videoshare_apply_gray);
        }
    }

    void chooseTagLayout() {
        if (isChooseTagStatus) {
            id_recyclerView_tag.setVisibility(View.VISIBLE);
            id_more_tag_tv.setVisibility(View.VISIBLE);
        } else {
            id_recyclerView_tag.setVisibility(View.GONE);
            id_more_tag_tv.setVisibility(View.GONE);
        }
        hadChooseOrNoChoose();
        choosetagChange();
        changeChooseTagStatus();
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    @OnClick({R.id.id_choose_tag_stutas_layout, R.id.id_complete_tag_ll_layout})
    void onChooseTagLayoutClick(View v) {
        if (v.getId() == R.id.id_choose_tag_stutas_layout) {
            if (tagList.size() == 0) {
                return;
            }
        }
        notifyChooseTagLayout(!isChooseTagStatus);
    }

    void notifyChooseTagLayout(boolean flag) {
        isChooseTagStatus = flag;
        chooseTagLayout();
        id_recyclerView_tag.getAdapter().notifyDataSetChanged();
    }

    /**
     * 选择标签时的下划线状态
     */
    void tagLineChnage(boolean flag) {
        final int v;
        if (flag) {
            v = View.VISIBLE;
        } else {
            v = View.GONE;
        }

        Observable.from(id_lines).subscribe(new Subscriber<View>() {
            @Override
            public void onCompleted() {
                Log.i(tag, "onCompleted: id_lines .setVisibility onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(View view) {
                view.setVisibility(v);
            }
        });
    }

    void changeChooseTagStatus() {
        tagLineChnage(isChooseTagStatus);
        if (isChooseTagStatus) {
            id_complete_tag_stutas_layout.setVisibility(View.GONE);
            id_choose_tag_stutas_layout.setVisibility(View.VISIBLE);
        } else {
            id_complete_tag_stutas_layout.setVisibility(View.VISIBLE);
            id_choose_tag_stutas_layout.setVisibility(View.GONE);

            int tagSize = tagList.size() - 1;

            for (int i = 0; i < 3; i++) {
                if (tagSize < i) {
                    tagTextViews[i].setVisibility(View.GONE);
                } else {
                    tagTextViews[i].setVisibility(View.VISIBLE);
                    tagTextViews[i].setText(tagList.get(i).getName());
                }
            }
        }
    }


    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            mVideoCaptureEntity = (VideoCaptureEntity) getIntent().getSerializableExtra("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mVideoCaptureEntity == null)
            finish();
    }


    private String getTypeText() {
        if (videoshare_type_text.getText() == null)
            return "";
        return videoshare_type_text.getText().toString().trim();
    }

    private String getDescription() {
        if (videoshare_description.getText() == null)
            return "";
        String s = "";
        try {
            s = SpanUtil.getText(videoshare_description);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    int getIsofficial() {
        if (isChecked)
            return 1;
        else
            return 0;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_videoshare2;
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        setSystemBar(true);
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        ShareSDK.initSDK(this);
        setStatusBar(Color.WHITE, true);
        setAbTitle(R.string.videoshare_title);
    }

    @Override
    public void initView() {
        super.initView();
        VipManager.getInstance().registerVipChangeListener(this);
        selectTagDrawable = ActivityCompat.getDrawable(this, R.drawable.shape_main_bule);
        tagTextViews = new TextView[]{id_choose_tag_1, id_choose_tag_2, id_choose_tag_3};
        iPopup = PopupImpl.createPopupImpl(new IPopupContext() {
            @Override
            public AppCompatActivity getActivity() {
                return VideoShare2Activity.this;
            }

            @Override
            public int getMaxWidth() {
                return ScreenUtil.getScreenWidth();
            }

            @Override
            public int getMaxHeigt() {
                return ScreenUtil.getScreenHeight();
            }

            @Override
            public void closeSoftKeyboard() {
                InputUtil.closeKeyboard(VideoShare2Activity.this);
            }

            @Override
            public void runOnUiThread(Runnable runnable) {
                VideoShare2Activity.this.runOnUiThread(runnable);
            }

            @Override
            public void show() {
                Log.i(tag, "show: ");
            }

            @Override
            public void dismiss(IInfoEntity iInfoEntity, boolean isRepetition) {
                Log.i(tag, "dismiss: isRepetition = " + isRepetition + ", iInfoEntity = " + iInfoEntity);
                mIInfoEntity = iInfoEntity;

                if (iInfoEntity != null && !isRepetition) {
                    isChooseTagStatus = true;
                    tagList.clear();
                    sendChange(false);
                    if (iPopup.getChooseFlag() == IPopup.HOT_GAME_FLAG) {
                        EventManager.postSearchGame2VideoShareEvent((Associate) iInfoEntity, true);
                    } else {
                        EventManager.postSearchGame2VideoShareEvent((Associate) iInfoEntity, false);
                    }
                    descriptionFormat = String.format("%s%s%s", "《", iInfoEntity.getName(), "》");
                    updateData();
                    handlerVipInit();
                }

            }
        });
        iPopup.init();
        initContentView();
        initData();
    }

    private void handlerVipInit() {
        final String type;
        if (iPopup.getChooseFlag() == IPopup.HOT_GAME_FLAG) {
            type = "game";
        } else {
            type = "life";
        }
        DataManager.getVipShareChooseAuthprity(getMember_id(), type);
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    protected void onDestroy() {
        iPopup.onDestroy();
        ShareSDK.stopSDK(this);
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    @OnClick(R.id.id_vip_authority_choose_right_iv)
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.id_vip_authority_choose_right_iv:
                vip3Show();
                break;
            case R.id.videoshare_edit:
                if (video_length > 30000) {
                    ActivityManager.startVideoEditorActivity(this, mVideoCaptureEntity);
                } else {
                    ToastHelper.s("30秒以上的时长才可以编辑");
                }
                break;
            case R.id.videoshare_cover:
                if (mVideoCaptureEntity != null) {
                    MyLocalVideoAdapter.startPlayerActivity(this, mVideoCaptureEntity);
                }
                break;
            case R.id.videoshare_type:
                InputUtil.closeKeyboard(this);
                iPopup.show();
                break;
            case R.id.videoshare_share:

                if(tagList.size() > 0){
                    UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID, UmengAnalyticsHelper2.ShARE_TAG_TYPE);
                }

                if (readyShare()) {
                    if (isGame() || VipManager.getInstance().isLevel3()) {
                        if(isGame()){

                        }
                        updateDatabase();
                        shareNow();
                    } else {
                        IntentHelper.shareSingleVideo(this, mVideoCaptureEntity.getVideo_path());
                    }
                }
                break;
            case R.id.videoshare_apply_question:
                showApplyPopupWindow();
                break;
            case R.id.videoshare_apply:
            case R.id.videoshare_apply_text:
                if (isGame()) {
                    UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID, UmengAnalyticsHelper2.SHARE_OFFICIAL_RECOMMEND_TYPE);
                    isChecked = !isChecked;
                    if (isChecked) {
                        videoshare_share.setClickable(false);
                        DataManager.newCurrencyMallRecommendedLocation(getMember_id());
                        videoshare_apply.setImageResource(R.drawable.tag_tick);
                    } else {
                        videoshare_apply.setImageResource(R.drawable.videoshare_apply_gray);
                    }
                } else {
                    ToastHelper.s(R.string.videoshare_tip_a);
                }
                break;
//            case R.id.videoshare_privacy:
//            case R.id.videoshare_privacy_1:
//                ActivityManager.startPrivacyActivity(this);
//                break;
            case R.id.id_confirm_tag_choose_tv:
                notifyChooseTagLayout(false);
                break;
        }
    }

    private boolean readyShare() {
        if (StringUtil.isNull(videoshare_description.getText().toString().trim())) {
            ToastHelper.s("请输入视频标题！");
            return false;
        } else if (StringUtil.isNull(videoshare_type_text.getText().toString().trim())) {
            ToastHelper.s("请选择视频类型！");
            return false;
        }
        return true;
    }

    void initContentView() {
        SpannableString spanned = new SpannableString(getString(R.string.videoshare_description_1));
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(ScreenUtil.dp2px(12));
        spanned.setSpan(sizeSpan, 6, spanned.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        videoshare_description.setHint(spanned);
        videoshare_edit.setOnClickListener(this);
        videoshare_share.setOnClickListener(this);
        videoshare_apply_question.setOnClickListener(this);
        videoshare_cover.setOnClickListener(this);
//        videoshare_privacy.setOnClickListener(this);
//        videoshare_privacy_1.setOnClickListener(this);
        videoshare_apply.setOnClickListener(this);
        videoshare_apply_text.setOnClickListener(this);
        videoshare_type.setOnClickListener(this);
        findViewById(R.id.id_confirm_tag_choose_tv).setOnClickListener(this);
        videoshare_descriptionCount.setText(R.string.videoshare_description_text_count);
        id_recyclerView_tag = (RecyclerView) findViewById(R.id.id_recyclerView_tag);
        id_recyclerView_tag.setItemAnimator(new DefaultItemAnimator());
        id_recyclerView_tag.setLayoutManager(new GridLayoutManager(this, 4));
        id_recyclerView_tag.setAdapter(new BaseQuickAdapter<Tag, BaseViewHolder>(R.layout.adapter_tag_selcet, tagAllDatas) {
            @Override
            public int getItemCount() {
                if (isPrepareMore) {
                    return super.getItemCount();
                } else {
                    return 8;
                }
            }

            @Override
            protected void convert(BaseViewHolder baseViewHolder, final Tag mTag) {
                final TextView textView = baseViewHolder.getView(R.id.id_tag_tv);
                textView.setText(mTag.getName());

                Observable.from(tagList).subscribe(new Subscriber<Tag>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: tagList.size = " + tagList.size());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Tag tag) {
                        if (!isUnsubscribed()) {
                            if (mTag.getGame_tag_id().equals(tag.getGame_tag_id())
                                    && mTag.getName().equals(tag.getName())) {
                                textView.setBackground(selectTagDrawable);
                                textView.setTextColor(ActivityCompat.getColor(VideoShare2Activity.this, R.color.ab_background_blue_3));
                                unsubscribe();
                            } else if (textView.getBackground() == selectTagDrawable) {
                                textView.setBackground(new BitmapDrawable());
                                textView.setTextColor(ActivityCompat.getColor(VideoShare2Activity.this, R.color.color_666666));
                            }
                        }
                    }
                });

                if (tagList.size() == 0 && textView.getBackground() == selectTagDrawable) {
                    textView.setBackground(new BitmapDrawable());
                    textView.setTextColor(ActivityCompat.getColor(VideoShare2Activity.this, R.color.color_666666));
                }

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (Tag t : tagList) {
                            if (mTag.getGame_tag_id().equals(t.getGame_tag_id())
                                    && mTag.getName().equals(t.getName())) {
                                tagList.remove(t);
                                sendChange(false);
                                return;
                            }
                        }
                        if (tagList.size() > 2) {
                            ToastHelper.s("最多只能选择3个标签");
                            return;
                        }
                        tagList.add((Tag) mTag.clone());
                        sendChange(false);
                    }
                });

            }

        });
        id_more_tag_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrepareMore = !isPrepareMore;
                if (isPrepareMore) {
                    ((TextView) v).setText("收起");
                    UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID, UmengAnalyticsHelper2.SHARE_MORE_TAG_TYPE);
                } else {
                    ((TextView) v).setText("更多");
                }
                sendChange(true);
            }
        });
//        isShowContract(false);
//        findViewById(R.id.id_agreement_close).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isShowContract(true);
//            }
//        });
//        findViewById(R.id.id_agreement_open).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isShowContract(false);
//            }
//        });
        videoshare_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith(descriptionFormat)) {
                    String str = String.valueOf(s.toString());
                    videoshare_descriptionCount.setText(String.format("%d/50", str.replace(descriptionFormat, "").length()));
                } else {
                    videoshare_description.setText(descriptionFormat);
                    SpanUtil.lastSelection(videoshare_description);
                }
            }
        });

        id_vip_authority_choose_recyclerView.setItemAnimator(new DefaultItemAnimator());
        id_vip_authority_choose_recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        id_vip_authority_choose_recyclerView.setAdapter(vipAuthorityAdapter);
        id_vip_choose_layout.setVisibility(View.GONE);

        vipAuthorityShowText();


    }

    private void vipAuthorityShowText() {
//        id_vip_authority_tip_title_tv
        if (VipManager.getInstance().isLevel3()) {
            id_vip_authority_tip_title_tv.setText(convertVipText(new SpannableString("视频分享特权（V3）")));
        } else if (VipManager.getInstance().isLevel2()) {
            id_vip_authority_tip_title_tv.setText(convertVipText(new SpannableString("视频分享特权（V2）")));
        } else if (VipManager.getInstance().isLevel1()) {
            id_vip_authority_tip_title_tv.setText(convertVipText(new SpannableString("视频分享特权（V1）")));
        }else{
            id_vip_authority_tip_title_tv.setText("视频分享特权（可选）");
        }
    }

    private CharSequence convertVipText(Spannable spannable){
        TextUtil.setForegroundColorText(
                TextUtil.setStyleSpan(
                        TextUtil.setStyleSpan(spannable, spannable.length() - 3, spannable.length()-1, Typeface.BOLD)
                        , spannable.length() - 3, spannable.length()-1, Typeface.ITALIC
                ),
                spannable.length() - 3, spannable.length()-1, ActivityCompat.getColor(this, R.color.menu_help_blue_1));
        return spannable;
    }

//    void isShowContract(boolean t) {
//        if (t) {
//            id_agreement_close_status.setVisibility(View.GONE);
//            id_agreement_open_status.setVisibility(View.VISIBLE);
//        } else {
//            id_agreement_close_status.setVisibility(View.VISIBLE);
//            id_agreement_open_status.setVisibility(View.GONE);
//        }
//        scrollView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        }, 100);

//    }

    /**
     * 当前是否有选择的标签
     * 之后会显示选择标签数量view
     */
    void hadChooseOrNoChoose() {
        if (tagList.size() > 0 && isChooseTagStatus) {
            id_choose_tag_list_layout.setVisibility(View.VISIBLE);
        } else {
            id_choose_tag_list_layout.setVisibility(View.GONE);
        }
    }

    void choosetagChange() {
        id_had_select_tv.setText(Html.fromHtml("已选<font color=#2daeff>" + tagList.size() + "</font>个标签"));
    }


    void sendChange(boolean isScroll) {
        if (iPopup.getSelect() != null &&
                iPopup.getChooseFlag() == IPopup.HOT_GAME_FLAG) {

            hadChooseOrNoChoose();
            chooseTagLayout();
            if (tagAllDatas.size() > 8) {
                id_more_tag_tv.setVisibility(View.VISIBLE);
            } else {
                id_more_tag_tv.setVisibility(View.GONE);
            }
            if (id_recyclerView_tag.getAdapter() != null) {
                id_recyclerView_tag.getAdapter().notifyDataSetChanged();
            }
            if (isScroll) {
                id_tag_layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);
            }

        } else {
            id_tag_layout.setVisibility(View.GONE);
//            isShowContract(false);
        }
    }


    void initData() {

        try {
            bitmap = BitmapUtil.readLocalBitmapQuarter(LPDSStorageUtil.createCoverPath(mVideoCaptureEntity.getVideo_path()).getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            try {
                bitmap = VideoCover.generateBitmap(mVideoCaptureEntity.getVideo_path());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bitmap != null)
            videoshare_cover.setImageBitmap(bitmap);

        try {
            fileSize = FileUtil.formatFileSize(FileUtil.getFileSize(mVideoCaptureEntity.getVideo_path()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoshare_size.setText(fileSize);
        try {
            video_length = Long.valueOf(VideoDuration.getDuration(mVideoCaptureEntity.getVideo_path()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtil.isValidDate(mVideoCaptureEntity.getVideo_name())) {
            videoshare_description.setText(mVideoCaptureEntity.getVideo_name());
        }
        SpanUtil.lastSelection(videoshare_description);
        if (isChecked) {
            videoshare_apply.setImageResource(R.drawable.tag_tick);
        } else {
            videoshare_apply.setImageResource(R.drawable.videoshare_apply_gray);
        }
    }

    /**
     * 更新
     */
    void updateData() {
        if (mIInfoEntity != null) {
            videoshare_type_text.setText(mIInfoEntity.getName());

            if (getDescription().contains("《") &&
                    getDescription().contains("》")) {
                try {
                    videoshare_description.getText().replace(getDescription().indexOf("《") + 1,
                            getDescription().indexOf("》"),
                            mIInfoEntity.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    videoshare_description.getText().insert(0,
                            "《" + mIInfoEntity.getName() + "》");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        SpanUtil.lastSelection(videoshare_description);
    }

    // ----------------------------------------------------------------------------------------

    /**
     * 提示
     */
    ApplyPopupWindow applyPopupWindow;

    void showApplyPopupWindow() {
        dismissApplyPopupWindow();
        applyPopupWindow = new ApplyPopupWindow(this, videoshare_apply_question);
        applyPopupWindow.showPopupWindow();
    }

    void dismissApplyPopupWindow() {
        if (applyPopupWindow != null && applyPopupWindow.isShowing())
            applyPopupWindow.dismiss();
    }

    /**
     * 保存数据库
     */
    void updateDatabase() {
        VideoCaptureManager.updateNamePathByPath(mVideoCaptureEntity.getVideo_path(),
                mVideoCaptureEntity.getVideo_name(),
                null);
    }

    /**
     * 分享
     */
    void shareNow() {
        if (StringUtil.isNull(getDescription())) {
            ToastHelper.s("请输入游戏标题");
            return;
        }
        if (StringUtil.isNull(getTypeText())) {
            ToastHelper.s("请选择视频类型");
            return;
        }
        if (isChecked && getDescription().length() < 10) {
            ToastHelper.s("申请官方推荐游戏标题不少于10个字");
            return;
        }

        if (NetUtil.isConnected()) {
            if (isChecked) {
                ActivityManager.startShareActivity_MyLocalVideo(this);
            } else {
                ActivityManager.startShareActivity_MyLocalVideo(this);
            }
        } else {
            ToastHelper.s(R.string.connectivity_disabled);
        }
    }

    // ----------------------------------------------------------------------------------------

    /**
     * 组件间的通讯：查找游戏 查找精彩生活
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(SearchGame2VideoShareEvent event) {
        if (!event.isGame()) {
            bottom_layout.setVisibility(View.GONE);
            if (isChecked) {
                isChecked = false;
                videoshare_apply.setImageResource(R.drawable.videoshare_apply_gray);
            }
        } else {
            DataManager.gameTagList(event.getAssociate().getId());
            bottom_layout.setVisibility(View.VISIBLE);
        }

        try {
            videoshare_description.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 组件间的通讯：分享
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(Share2VideoShareEvent event) {
        Log.d(tag, "event=" + event);
        iPopup.submit();
        String shareChannel = event.getShareChannel();
        if (!shareChannel.equals("MeiPai")) {

            // 保存文件名
            VideoCaptureManager.updateNameByPath(mVideoCaptureEntity.getVideo_path(), getDescription());

            String game_id = mIInfoEntity.getId();
            String game_name = mIInfoEntity.getName();
            List<String> ids = new ArrayList<>();

            if (isGame()) {
                if (tagList.size() > 0) {
                    for (Tag t : tagList) {
                        ids.add(t.getGame_tag_id());
                    }
                }
            } else {
                ids.clear();
            }


            // 保存游戏，活动，标签
            VideoCaptureManager.updateMatchGameTagsByPath(mVideoCaptureEntity.getVideo_path(),
                    game_id,
                    game_name,
                    "",
                    "",
                    ids,
                    tagList);
            // 保存上传状态
            VideoCaptureManager.updateStationByPath(mVideoCaptureEntity.getVideo_path(),
                    shareChannel,
                    getMember_id(),
                    "",
                    getDescription(),
                    "",
                    getIsofficial(),
                    ids);

            UploadManager.startVideo(
                    shareChannel,
                    getMember_id(),
                    getDescription(),
                    game_id,
                    "",//赛事ID在这里没用
                    "",
                    getIsofficial(),
                    ids,
                    mVideoCaptureEntity,
                    goodsId);

            // 本地视频
            EventManager.postVideoEditor2VideoManagerEvent(new String[]{mVideoCaptureEntity.getVideo_path()}, 2);
        } else {
            MeiPaiShareUtils.meipaiShare(mVideoCaptureEntity.getVideo_path(), this);
        }

        finish();
    }

    /**
     * 回调：推荐位信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(NewCurrencyMallRecommendedLocationEntity event) {
        if (event.isResult()) {
            DialogManager.showVideoSharePaymentDialog(this, event,
                    new VideoSharePaymentDialog.ClickListener() {
                        @Override
                        public void onEnterClick(DialogInterface dialog, int witch, String goodsId) {
                            ActivityManager.startShareActivity_MyLocalVideo(VideoShare2Activity.this);
                            VideoShare2Activity.this.goodsId = goodsId;
                        }

                        @Override
                        public void onDismissClick(DialogInterface dialog, int witch) {
                            if (isChecked) {
                                isChecked = false;
                                videoshare_apply.setImageResource(R.drawable.videoshare_apply_gray);
                            }
                            VideoShare2Activity.this.goodsId = null;
                        }
                    });
        } else {
            if (isChecked) {
                isChecked = false;
                videoshare_apply.setImageResource(R.drawable.videoshare_apply_gray);
            }
        }

        videoshare_share.setClickable(true);
    }

    /**
     * 回调：视频分享标签
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(GameTagListEntity event) {
        if (event.isResult()) {
            if (event.getData() != null
                    && event.getData().size() > 0) {
                id_tag_layout.setVisibility(View.VISIBLE);
                tagAllDatas.clear();
                tagAllDatas.addAll(event.getData());
                isPrepareMore = false;
            }
        }
        sendChange(false);

    }

    Vip3AndAuthoryEntity vip3AndAuthoryEntity;
//    List<Vip3AndAuthoryEntity.DiamondVipDataBean.DiamondVipTextBean> diamondVipTextdatas = ;

    /**
     * vip的一些消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(Vip3AndAuthoryEntity event) {
        if (event.isResult()) {
            vip3AndAuthoryEntity = event;
            refreshVip3AndAuthoryEntity();
        }
    }

    private void refreshVip3AndAuthoryEntity() {
        id_vip_choose_layout.setVisibility(View.VISIBLE);
        vipAuthoritys.clear();
        vipAuthoritys.addAll(vip3AndAuthoryEntity.getPrivilegeData().getPrivilegeText());
        vipAuthorityAdapter.notifyDataSetChanged();
    }

    boolean isGame() {
        return IPopup.HOT_GAME_FLAG == iPopup.getChooseFlag();
    }


    @Override
    public void onVipObservable(Member.VIPInfo vipInfo) {
        Log.i(tag, "onVipObservable: "+vipInfo+"  vip 改变");
        if(isChooseTagStatus) {
            vipAuthorityShowText();
            handlerVipInit();
        }
    }
}
