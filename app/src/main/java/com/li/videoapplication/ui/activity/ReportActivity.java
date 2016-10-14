package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.ReportType;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.ReportEntity;
import com.li.videoapplication.data.model.response.ReportTypeEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.ReportAdapter;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.ListViewY1;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：举报
 */
public class ReportActivity extends TBaseActivity implements OnClickListener, TextWatcher {

    private ListViewY1 listView;
    private ReportAdapter adapter;
    private List<ReportType> data;

    private EditText description;
    private TextView textCount;

    public String getDescription() {
        return description.getText().toString().trim();
    }

    private VideoImage videoImage;

    @Override
    public void refreshIntent() {
        try {
            videoImage = (VideoImage) getIntent().getSerializableExtra("videoImage");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (videoImage == null) {
            finish();
        }
    }

	@Override
	public int getContentView() {
		return R.layout.activity_report;
	}

    public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.report_title);
    }

    @Override
    public void initView() {
        super.initView();

        abGoback.setVisibility(View.GONE);

        abReportCancel.setVisibility(View.VISIBLE);
        abReportConfirm.setVisibility(View.VISIBLE);

        abReportCancel.setOnClickListener(this);
        abReportConfirm.setOnClickListener(this);

        description = (EditText) findViewById(R.id.report_description);
        textCount = (TextView) findViewById(R.id.report_textCount);
        description.addTextChangedListener(this);
        description.setText("");
        description.requestFocus();

        listView = (ListViewY1) findViewById(R.id.listview);
        data = new ArrayList<>();
        adapter = new ReportAdapter(this, data);
        listView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();

        // 举报类型列表
        DataManager.reportType();
    }

	@Override
	public void onClick(View v) {

        if (v == abReportCancel) {
            onBackPressed();
        } else if (v == abReportConfirm){
            report();
        }
	}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        textCount.setText(s.length() + "/100");
        if (s.length() > 100)
            showToastShort("举报描述多100个字");
    }

    private void report() {
        if (adapter.checkPosition < 0 && adapter.checkPosition >= data.size()) {
            showToastShort("请选择举报原因");
            return;
        }
        if (StringUtil.isNull(getDescription())) {
            showToastShort("举报描述不能为空");
            return;
        }
        if (getDescription().length() < 10) {
            showToastShort("举报描述最少10个字");
            return;
        }
        try {
            InputUtil.closeKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReportType reportType = data.get(adapter.checkPosition);
        // 举报
        DataManager.report(videoImage.getVideo_id(), getMember_id(), getDescription(), reportType.getType_id());
    }

    /**
     * 回调：举报类型列表
     */
    public void onEventMainThread(ReportTypeEntity event) {

        if (event.isResult()) {
            if (event.getData().size() > 0) {
                data.clear();
                data.addAll(event.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 回调：举报
     */
    public void onEventMainThread(ReportEntity event) {

        if (!StringUtil.isNull(event.getMsg())) {
            showToastLong(event.getMsg());
        }
        if (event.isResult()) {
            ActivityManeger.startReportResultActivity(this);
        }
        finish();
    }
}
