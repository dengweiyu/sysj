package com.li.videoapplication.ui.activity;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.GetTeamMemberNumEntity;
import com.li.videoapplication.data.model.response.JoinEventsEntity;
import com.li.videoapplication.data.model.response.PhoneRequestMsgEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.AuthCodeUtil;
import com.li.videoapplication.utils.CountDownTimerUtils;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 活动：报名
 */
public class SignUpActivity extends TBaseActivity implements View.OnClickListener {

    @BindView(R.id.signup_teaminfoview)
    View teamInfoView;
    @BindView(R.id.signup_teamqq_1_view)
    View teamQQ_1View;
    @BindView(R.id.signup_teamqq_2_view)
    View teamQQ_2View;
    @BindView(R.id.signup_teamqq_3_view)
    View teamQQ_3View;
    @BindView(R.id.signup_teamqq_4_view)
    View teamQQ_4View;
    @BindView(R.id.signup_teamqq_34_view)
    View teamQQ_34View;
    @BindView(R.id.signup_teamphone_1)
    EditText teamPhone_1;
    @BindView(R.id.signup_teamphone_2)
    EditText teamPhone_2;
    @BindView(R.id.signup_teamphone_3)
    EditText teamPhone_3;
    @BindView(R.id.signup_teamphone_4)
    EditText teamPhone_4;


    private final String PERSONAL_MATCH = "1", TEAM_MATCH = "2";
    private String event_id;
    private EditText teamName, roleName, qq, phone, inviteCode;
    private String type_id;
    private Match match;
    private String customer_service_id, customer_service_name;
    private TextView getCode, changePhoneNum;
    private EditText code;
    private boolean isChanged;

    /**
     * 分享
     */
    public void startShareActivity() {
        if (match != null) {
            final String url = match.getApply_share_url();
            final String title = "精彩赛事分享";
            final String imageUrl = match.getFlag();
            final String content = "快来看看" + match.getTitle();

            ActivityManager.startActivityShareActivity4VideoPlay(this, url, title, imageUrl, content);
        }
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            event_id = getIntent().getStringExtra("event_id");
            match = (Match) getIntent().getSerializableExtra("match");
            type_id = match.getType_id();
            customer_service_id = getIntent().getStringExtra("customer_service");
            customer_service_name = getIntent().getStringExtra("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_signup;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("资料填写");
    }

    @Override
    public void initView() {
        super.initView();

        View teamNameHint = findViewById(R.id.signup_teamname_hint);
        View inviteCodeHint = findViewById(R.id.signup_invitecode_hint);
        View kindlyReminder = findViewById(R.id.signup_kindly_reminder);
        View kindlyReminderContent = findViewById(R.id.signup_kindly_reminder_content);
        teamName = (EditText) findViewById(R.id.signup_teamname_et);
        roleName = (EditText) findViewById(R.id.signup_rolename);
        inviteCode = (EditText) findViewById(R.id.signup_invitecode);
        qq = (EditText) findViewById(R.id.signup_qq);
        phone = (EditText) findViewById(R.id.signup_phone);
        getCode = (TextView) findViewById(R.id.signup_getcode);
        changePhoneNum = (TextView) findViewById(R.id.signup_changephonenum);
        code = (EditText) findViewById(R.id.signup_code);
        TextView tip = (TextView) findViewById(R.id.signup_tip);
        ScrollView scrollView = (ScrollView) findViewById(R.id.signup_scrollview);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        if (match != null) {
            String s = "本次比赛仅限<font color=\"#fc3c2e\">" + match.getEvent_team_server() + "</font>用户";
            tip.setText(Html.fromHtml(s));
        }

        if (type_id.equals(PERSONAL_MATCH)) { //个人赛
            teamNameHint.setVisibility(View.GONE);
            teamName.setVisibility(View.GONE);
            teamInfoView.setVisibility(View.GONE);
            kindlyReminder.setVisibility(View.GONE);
            kindlyReminderContent.setVisibility(View.GONE);
        } else if (type_id.equals(TEAM_MATCH)) { //团体赛
            teamNameHint.setVisibility(View.VISIBLE);
            teamName.setVisibility(View.VISIBLE);
            kindlyReminder.setVisibility(View.VISIBLE);
            kindlyReminderContent.setVisibility(View.VISIBLE);
        }
        if (match.getFormat_type() == 2) { //邀请赛
            inviteCodeHint.setVisibility(View.VISIBLE);
            inviteCode.setVisibility(View.VISIBLE);
        } else { //普通
            inviteCodeHint.setVisibility(View.GONE);
            inviteCode.setVisibility(View.GONE);
        }

        findViewById(R.id.signup_cardview).setOnClickListener(this);
        findViewById(R.id.signup_customerservice).setOnClickListener(this);
        findViewById(R.id.signup_question).setOnClickListener(this);
        abShareBlack.setOnClickListener(this);
        getCode.setOnClickListener(this);
        changePhoneNum.setOnClickListener(this);
        abShareBlack.setVisibility(View.VISIBLE);
        abShareBlack.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();
        if (type_id.equals(TEAM_MATCH))
            DataManager.getTeamMemberNumber(event_id);

        if (!StringUtil.isNull(getUser().getMobile())) {//已有手机号
            phone.setText(getUser().getMobile());
            phone.setFocusableInTouchMode(false);
            phone.setFocusable(false);

            changePhoneNum.setVisibility(View.VISIBLE);
            getCode.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
        } else {//没有手机号
            phone.setFocusableInTouchMode(true);
            phone.setFocusable(true);

            changePhoneNum.setVisibility(View.GONE);
            getCode.setVisibility(View.VISIBLE);
            code.setVisibility(View.VISIBLE);
        }
        if (!StringUtil.isNull(getUser().getQq())) {//已有手机号
            qq.setText(getUser().getQq());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ab_share_black:
                startShareActivity();
                break;

            case R.id.signup_cardview://提交报名
                if (!StringUtil.isNull(getUser().getMobile()) && !isChanged) {//已有手机号
                    signupNow();
                } else { //没有手机号
                    if (StringUtil.isNull(getCodeText())) {
                        showToastShort("验证码不能为空");
                        animationHelper.startAnimationShake(code);
                    } else {
                        //验证手机、验证码
                        DataManager.verifyCodeNew(getPhone(), getCodeText());
                    }
                }
                break;
            case R.id.signup_customerservice://客服
                if (RongIM.getInstance() != null && !StringUtil.isNull(customer_service_id)
                        && !StringUtil.isNull(customer_service_name)) {

                    ActivityManager.startConversationActivity(this, customer_service_id,
                            customer_service_name, false);
                }
                break;
            case R.id.signup_getcode:// 获取验证码
                msgRequestNew();
                break;
            case R.id.signup_changephonenum://更换手机号
                isChanged = true;
                changePhoneNum.setVisibility(View.GONE);
                getCode.setVisibility(View.VISIBLE);
                code.setVisibility(View.VISIBLE);
                phone.setText("");
                phone.setFocusableInTouchMode(true);
                phone.setFocusable(true);
                phone.requestFocus();
                break;
            case R.id.signup_question:
                DialogManager.showSignUpQuestionDialog(this);
                break;
        }
    }

    private String getTeamPhoneText() {

        StringBuilder teamPhone = new StringBuilder();


        if (teamPhone_1.getText() != null)
            teamPhone.append(teamPhone_1.getText().toString());

        if (teamPhone_2.getText() != null)
            teamPhone.append(",").append(teamPhone_2.getText().toString());

        if (teamPhone_3.getText() != null)
            teamPhone.append(",").append(teamPhone_3.getText().toString());

        if (teamPhone_4.getText() != null)
            teamPhone.append(",").append(teamPhone_4.getText().toString());

        Log.d(tag, "getTeamPhoneText: " + teamPhone);

        return teamPhone.toString();
    }

    /**
     * 获取验证码
     */
    private void msgRequestNew() {
        if (StringUtil.isNull(getPhone())) {
            showToastShort("手机号码不能为空");
            animationHelper.startAnimationShake(phone);
            return;
        }
        if (!PatternUtil.isMatchMobile(getPhone())) {
            showToastShort("请输入正确的手机号");
            animationHelper.startAnimationShake(phone);
            return;
        }
        // 加密手机号
        String encode = AuthCodeUtil.authcodeEncode(getPhone(), AuthCodeUtil.APP_KEY);
        // 获取验证码
        DataManager.eventRequestMsg(encode, match.getTitle());
    }

    private void signupNow() {

        if (StringUtil.isNull(getPhone()) || !PatternUtil.isMatchMobile(getPhone())) {
            showToastShort("请输入正确的手机号");
            return;
        }

        String[] teamPhone = getTeamPhone();
        for (int i = 0; i < teamPhone.length; i++) {
            if (!StringUtil.isNull(teamPhone[i])){
                if (!PatternUtil.isMatchMobile(teamPhone[i])){
                    String position="";
                    switch (i){
                        case 0:position="一";break;
                        case 1:position="二";break;
                        case 2:position="三";break;
                        case 3:position="四";break;
                    }
                    showToastShort("队员"+position+"手机号码不正确哦~");
                    return;
                }
            }
        }

        if (type_id.equals(TEAM_MATCH) && StringUtil.isNull(getTeamName())) {
            showToastShort("请输入战队名");
            return;
        }
        if (StringUtil.isNull(getGameRoleName())) {
            showToastShort("请输入角色名");
            return;
        }
        if (StringUtil.isNull(getQQ()) || !PatternUtil.isMatchNumber(getQQ())) {
            showToastShort("请输入正确的qq号");
            return;
        }
        if (StringUtil.isNull(getInviteCodeText()) && match.getFormat_type() == 2) {//邀请赛
            showToastShort("邀请码不能为空");
            return;
        }
        DataManager.joinEvents(getMember_id(), event_id, type_id,
                getTeamName(), getGameRoleName(), getQQ(), getPhone(),
                getInviteCodeText(), getTeamPhoneText());

        // FIXME: 2016/6/7 取消逻辑还没有写，只是显示了对话框！用于在网络不好的时候可以取消菊花圈的显示
        showLoadingDialogWithCancel("报名中", "请稍候几秒钟", "取消",
                "已取消", "报名中断，请检查后重试!", "确定");
    }

    private String getInviteCodeText() {
        if (inviteCode.getText() != null)
            return inviteCode.getText().toString();
        return "";
    }

    private String getCodeText() {
        if (code.getText() != null)
            return code.getText().toString();
        return "";
    }

    private String getPhone() {
        if (phone.getText() == null)
            return "";
        return phone.getText().toString().trim();
    }

    private String[] getTeamPhone(){
        return new String[]{
                teamPhone_1.getText() == null?null:teamPhone_1.getText().toString(),
                teamPhone_2.getText() == null?null:teamPhone_2.getText().toString(),
                teamPhone_3.getText() == null?null:teamPhone_3.getText().toString(),
                teamPhone_4.getText() == null?null:teamPhone_4.getText().toString(),
        };
    }

    private String getQQ() {
        if (qq.getText() == null)
            return "";
        return qq.getText().toString().trim();
    }

    public String getGameRoleName() {
        if (roleName.getText() == null)
            return "";
        return roleName.getText().toString().trim();
    }

    public String getTeamName() {
        if (teamName.getText() == null)
            return "";
        return teamName.getText().toString().trim();
    }

    private void refreshTeamView(int teamMemberNum) {
        if (teamMemberNum > 0)
            teamInfoView.setVisibility(View.VISIBLE);
        switch (teamMemberNum) {
            case 1:
                teamQQ_1View.setVisibility(View.VISIBLE);
                break;
            case 2:
                teamQQ_1View.setVisibility(View.VISIBLE);
                teamQQ_2View.setVisibility(View.VISIBLE);
                break;
            case 3:
                teamQQ_34View.setVisibility(View.VISIBLE);
                teamQQ_1View.setVisibility(View.VISIBLE);
                teamQQ_2View.setVisibility(View.VISIBLE);
                teamQQ_3View.setVisibility(View.VISIBLE);
                break;
            case 4:
                teamQQ_34View.setVisibility(View.VISIBLE);
                teamQQ_1View.setVisibility(View.VISIBLE);
                teamQQ_2View.setVisibility(View.VISIBLE);
                teamQQ_3View.setVisibility(View.VISIBLE);
                teamQQ_4View.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 回调：获取赛事参赛队员人数接口
     */
    public void onEventMainThread(GetTeamMemberNumEntity event) {

        if (event != null && event.isResult()) {
            refreshTeamView(event.getTeamMemberNum());
        }
    }

    /**
     * 回调：报名
     */
    public void onEventMainThread(JoinEventsEntity event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData().getList() != null)
                    showSignUpSuccessDialog(event);

                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "报名完成（成功报名）");
            } else {
                changeType2ErrorDialog("错误", event.getMsg(), "确认");
            }
        }
    }

    private void showSignUpSuccessDialog(JoinEventsEntity event) {
        boolean isNeedSign = event.getData().getList().getIs_sign().equals("1");
        //是否即时匹配
        boolean isOnce = event.getData().getList().getIs_once().equals("1");

        if (isOnce) {//即时匹配
            showSuccessDontNeedSignInDialog(event);
        } else {//手动匹配
            if (isNeedSign) {//需要签到
                try {
                    //签到开始时间 （格式：06月01日 15:30）
                    String sign_starttime = TimeHelper.getMMddHHmmTimeFormat(event.getData().getList().getSign_starttime());
                    //签到结束时间 （格式：15:40）
                    String sign_endtime = TimeHelper.getTime2HmFormat(event.getData().getList().getSign_endtime());
                    //比赛开始时间 （格式：15:40）
                    String schedule_starttime = TimeHelper.getTime2HmFormat(event.getData().getList().getSchedule_starttime());

//                String tag1 = "1.请在" + sign_starttime + "~" + sign_endtime + "到赛规页面签到\n";
//                String tag2 = "2.比赛将于" + schedule_starttime + "开始，请准时参加";
//                SpannableStringBuilder sb = new SpannableStringBuilder();
//                sb.append(TextUtil.stringAtRed(tag1, 4, 22));
//                sb.append(TextUtil.stringAtRed(tag2, 6, 11));
//                change2SuccessWithOKListener("报名成功", sb, "确认");
                    cancelProgressDialog();
                    DialogManager.showSignUpSuccessDialog(this, sign_starttime, sign_endtime, schedule_starttime);

                } catch (Exception e) {
                    change2SuccessWithOKListener("报名成功", "请在规定时间内到赛规页面签到，未签到视为放弃", "确认");
                }
            } else {//不需要签到
                showSuccessDontNeedSignInDialog(event);
            }
        }
    }

    @Override
    protected void confirmButtonEvent() {
        finish();
    }

    private void showSuccessDontNeedSignInDialog(JoinEventsEntity event) {
        try {
            //签到开始时间 （格式：06月01日 15:30）
            String sign_starttime = TimeHelper.getMMddHHmmTimeFormat(event.getData().getList().getSign_starttime());
            //签到结束时间 （格式：15:40）
            String sign_endtime = TimeHelper.getTime2HmFormat(event.getData().getList().getSign_endtime());

            String s1 = "请在" + sign_starttime + "~" + sign_endtime + "参加比赛";

            change2SuccessWithOKListener("报名成功", TextUtil.stringAtRed(s1, 2, 20), "确认");

        } catch (Exception e) {
            change2SuccessWithOKListener("报名成功", "请在规定时间内到赛规页面约战自己的对手进行参加比赛", "确认");
        }
    }

    /**
     * 回调:取验证码
     */
    public void onEventMainThread(PhoneRequestMsgEntity event) {

        if (event != null) {
            showToastShort(event.getMsg());
            if (event.isResult()) {// 成功

                CountDownTimerUtils countDownTimer = new CountDownTimerUtils(getCode, 60000, 1000,
                        R.drawable.dialog_registermobile_gray, R.drawable.dialog_registermobile_red);
                countDownTimer.start();
            }
        }
    }

    /**
     * 回调:提交手机和验证码
     */
    public void onEventMainThread(VerifyCodeNewEntity event) {

        if (event != null) {
            if (event.isResult()) {// 验证成功
                saveUserPhoneNum();
                signupNow();
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    //手机号同步保存到个人资料
    private void saveUserPhoneNum() {
        RequestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Member member = getUser();
                Member newMember = (Member) member.clone();
                newMember.setMobile(getPhone());
                // 编辑个人资料
                DataManager.userProfileFinishMemberInfo(newMember);
            }
        });
    }
}
