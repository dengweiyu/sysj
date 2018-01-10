package com.li.videoapplication.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.BaseInfoEntity;
import com.li.videoapplication.data.model.response.GroupType210Entity;
import com.li.videoapplication.data.model.response.IsRepeatEntity;
import com.li.videoapplication.data.model.response.SelectMyGameEntity;
import com.li.videoapplication.data.model.response.UserProfileFinishMemberInfoEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.adapter.EditGameTypeAdapter;
import com.li.videoapplication.ui.adapter.EditMyGameAdapter;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：个人资料编辑
 */
public class PersonalInfoEditActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    public static final int NAME = 0;
    public static final int SIGNATURE = 1;
    public static final int GAME = 2;

    private int type;
    private RecyclerView recyclerView;
    private EditText edit;
    private TextView editLeftNum;
    private int left;//剩余可输入字数
    private Member member = getUser();
    private Member newMember;
    private EditGameTypeAdapter groupAdapter;
    private EditMyGameAdapter myGameAdapter;
    private List<GroupType> groupData;
    private List<SelectMyGameEntity.Bean> games;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        type = getIntent().getIntExtra("type", NAME);
        if (type == GAME) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_personalinfoedit;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        newMember = (Member) member.clone();
        initContentView();
        if (type == GAME) {
            initAdapter();
        }
    }

    private void initContentView() {
        edit = (EditText) findViewById(R.id.edit);
        editLeftNum = (TextView) findViewById(R.id.edit_num);
        TextView editTip = (TextView) findViewById(R.id.edit_tip);
        View editView = findViewById(R.id.editview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        switch (type) {
            case NAME:
                tb_title.setText("设置昵称");
                editView.setVisibility(View.VISIBLE);
                int maxLength = 10;
                edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                if (!StringUtil.isNull(member.getNickname())) {
                    edit.setText(member.getNickname());
                    initEditText(maxLength);
                } else {
                    editLeftNum.setText("10");
                }
                break;
            case SIGNATURE:
                tb_title.setText("个性签名");
                editView.setVisibility(View.VISIBLE);
                editTip.setVisibility(View.GONE);
                maxLength = 30;
                edit.setMaxLines(2);
                edit.setHint(R.string.mypersonalinfo_introduce_hint);
                edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                if (!StringUtil.isNull(member.getSignature())) {
                    edit.setText(member.getSignature());
                    initEditText(maxLength);
                } else {
                    editLeftNum.setText("30");
                }
                break;
            case GAME:
                tb_title.setText("选择游戏类型");
                editView.setVisibility(View.GONE);
                break;
        }

        findViewById(R.id.tb_save).setVisibility(View.VISIBLE);

        findViewById(R.id.tb_save).setOnClickListener(this);
        findViewById(R.id.tb_back).setOnClickListener(this);

        edit.addTextChangedListener(textWatcher);
    }

    private void initEditText(int maxLength) {
        //设置新光标所在的位置
        Editable e = edit.getText();
        Selection.setSelection(e, e.length());
        if (edit.length() <= maxLength) {
            left = maxLength - edit.length();
            editLeftNum.setText("" + left);
        } else {
            editLeftNum.setText("0");
        }
    }

    private void initAdapter() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.VISIBLE);

        List<GroupType> datas = new ArrayList<>();
        List<SelectMyGameEntity.Bean> entities = new ArrayList<>();
        groupAdapter = new EditGameTypeAdapter(datas);
        myGameAdapter = new EditMyGameAdapter(entities);
        recyclerView.setAdapter(groupAdapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        if (type == GAME) {
//            // 圈子类型
//            DataManager.groupType217();
            DataManager.selectMyGameList(getMember_id());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(tag, "afterTextChanged: s.length() = " + s.length());
            switch (type) {
                case NAME:
                    if (s.length() <= 10) {
                        left = 10 - s.length();
                    }
                    break;
                case SIGNATURE:
                    if (s.length() <= 30) {
                        left = 30 - s.length();
                    }
                    break;
            }
            editLeftNum.setText(left + "");
        }
    };


    private String getEditTextContent() {
        if (StringUtil.isNull(edit.getText().toString())) {
            return "";
        } else {
            return edit.getText().toString().trim();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_save:
                if (StringUtil.isNull(getEditTextContent()) && type == NAME) {
                    ToastHelper.s("昵称不能为空");
                    return;
                }
                String content = getEditTextContent();
                switch (type) {
                    case NAME:
                        //检测用户昵称重复
                        DataManager.isRepeat(content);
                        break;
                    case SIGNATURE:
                        newMember.setSignature(content);
                        //昵称和个性签名 敏感词过滤
                        DataManager.baseInfo(content);
                        break;
                    case GAME:
                        saveGameType();
                        break;
                }
        }
    }

    /**
     * 列表正常狀態
     */
    private void saveGameType() {
        List<String> group_type_id = groupAdapter.getGroup_type_id();
        newMember.setLikeGroupType(group_type_id);
        Log.d(tag, "saveGameType: setLikeGroupType == " + group_type_id);
        // 编辑个人资料
        DataManager.userProfileFinishMemberInfo(newMember);
    }

    /**
     * 回调：检测用户昵称重复接口
     */
    public void onEventMainThread(IsRepeatEntity event) {
        if (event != null && event.isResult()) {
            if (event.getData().isRepeat()) {
                ToastHelper.s(event.getData().getInfo());
            } else {
                newMember.setNickname(getEditTextContent());
                //昵称和个性签名 敏感词过滤
                DataManager.baseInfo(getEditTextContent());
            }
        }
    }

    /**
     * 回调：敏感词过滤
     */
    public void onEventMainThread(BaseInfoEntity event) {

        if (event != null && event.isResult()) {
            if (event.getData().isHasBad()) {
                ToastHelper.s("请勿使用敏感词汇");
                return;
            }
            // 编辑个人资料
            DataManager.userProfileFinishMemberInfo(newMember);
        }
    }

    /**
     * 回调：编辑个人资料
     */
    public void onEventMainThread(UserProfileFinishMemberInfoEntity event) {

        if (event != null && event.isResult()) {
            finish();
        }
    }

    /**
     * 回调：圈子類型
     */
    public void onEventMainThread(GroupType210Entity event) {

        if (event != null && event.isResult()) {
            groupData = event.getData();
            if (groupData != null && groupData.size() > 0) {
                groupAdapter.setNewData(setSelectedData(groupData));
            }
        }
    }


    /**
     * 回调：圈子類型
     */
    public void onEventMainThread(SelectMyGameEntity event) {

        if (event != null && event.isResult()) {
            games = event.getAData();
            if (games != null && games.size() > 0) {
                myGameAdapter.setNewData(games);
            }
        }
    }

    /**
     * 列表選着狀態
     */
    private List<GroupType> setSelectedData(List<GroupType> groupTypes) {
        Member item = getUser();
        List<String> likeGroupTypeList = item.getLikeGroupType();
        if (likeGroupTypeList != null && likeGroupTypeList.size() > 0) {
            for (String id : likeGroupTypeList) {
                for (GroupType groupType : groupTypes) {
                    if (id.equals(groupType.getGroup_type_id())) {
                        groupType.setSelected(true);
                    }
                }
            }
        }
        return groupTypes;
    }

    /**
     * 列表選着狀態
     */
    private List<SelectMyGameEntity.Bean> setSelectedGame(List<SelectMyGameEntity.Bean> data) {
        Member item = getUser();
        List<Member.LikeGameGroup> likeGameGroups = item.getLikeGameGroup();
        if (likeGameGroups != null && likeGameGroups.size() > 0) {
            for (int i = 0; i < likeGameGroups.size(); i++) {
                for (SelectMyGameEntity.Bean bean : data) {
                    if (likeGameGroups.get(i).getGroup_id().equals(bean.getGroup_id())) {
                        bean.setIs_attention(1);
                    }
                }
            }
        }
        return data;
    }

}
