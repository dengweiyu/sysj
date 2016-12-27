package com.li.videoapplication.ui.fragment;

import android.content.res.AssetManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.HelpQuestionEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.HelpQuestionAdapter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 碎片：问题解答
 */
public class QuestionFragment extends TBaseFragment implements View.OnClickListener {

    public ExpandableListView expandableListView;
    private HelpQuestionAdapter adapter;
    private List<ArrayList<HelpQuestionEntity>> data;
    public boolean isMore = false;
    /**
     * 文件项目个数，用来动态定位
     */
    private int items = 5;
    private String groups[];
    public TextView moreQuestion,normalQuestion;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "帮助教程-问题解答");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.pager_help_question;
    }

    @Override
    protected void initContentView(View view) {

        normalQuestion=(TextView)view.findViewById(R.id.help_normalquestion);
        moreQuestion=(TextView)view.findViewById(R.id.help_morequetion);
        moreQuestion.setOnClickListener(this);
        initListView(view);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private void initListView(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable);
        if (!isMore) {
            groups = new String[]{getStringForR(R.string.help_question1),
                    getStringForR(R.string.help_question2),
                    getStringForR(R.string.help_question3),
                    getStringForR(R.string.help_question4),
                    getStringForR(R.string.help_question5),
                    getStringForR(R.string.help_question6),
                    getStringForR(R.string.help_question7)};
        } else {
            groups = new String[]{getStringForR(R.string.help_question8),
                    getStringForR(R.string.help_question9),
                    getStringForR(R.string.help_question10),
                    getStringForR(R.string.help_question11)};
        }
        data = new ArrayList<>();
        doMyMission();
        adapter = new HelpQuestionAdapter(getActivity(), groups, data);
        setExpandableHeight(expandableListView);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                items = items + 1;
                setExpandableHeight(expandableListView);
                setExpandableHeight2(expandableListView, groupPosition);
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                items = items - 1;
                setExpandableHeight(expandableListView);
            }
        });
        expandableListView.setAdapter(adapter);
    }

    private String getStringForR(int res) {
        return resources.getString(res);
    }

    /**
     * 使用SAX解析器解析XML文件的方法
     */
    private void doMyMission() {
        try {
            AssetManager as = getActivity().getAssets();
            InputStream is;
            if (!isMore) {
                is = as.open("questions.xml");
            } else {
                is = as.open("questions_more.xml");
            }
            InputSource source = new InputSource(is);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            QuestionSaxHandler handler = new QuestionSaxHandler(data);
            reader.setContentHandler(handler);
            reader.parse(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态改变ExpandableListView的高度
     */
    private void setExpandableHeight(ExpandableListView pull) {
        int totalHeight = 0;
        View listItem;
        int len = groups.length;
        for (int i = 0; i < len; i++) {
            listItem = adapter.getGroupView(i, true, null, pull);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = pull.getLayoutParams();
        params.height = totalHeight + (pull.getDividerHeight() * (pull.getCount()));
        pull.setLayoutParams(params);
    }

    /**
     * 动态改变ExpandableListView的高度(含子项)
     */
    private void setExpandableHeight2(ExpandableListView pull, int groupPosition) {
        int totalHeight = 0;
        View listItem;

        int len = data.get(groupPosition).size();
        for (int i = 0; i < len + items; i++) { // +5(栏目数)表示展开的时候，高度与收回时高度一致
            listItem = adapter.getChildView(groupPosition, 0, true, null, pull);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = pull.getLayoutParams();
        params.height = totalHeight + (pull.getDividerHeight() * (pull.getCount()));
        pull.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.help_morequetion:
                isMore = true;
                initListView(v);
                // 隐藏该按钮
                moreQuestion.setVisibility(View.GONE);
                normalQuestion.setText("更多问题");
                break;
        }
    }

    /**
     * 解析问题解答（questions，question_more)XML文件
     */
    public static class QuestionSaxHandler extends DefaultHandler {

        // 声明一个装载<ArrayList<HelpQuestionEntity>> 类型的List
        private List<ArrayList<HelpQuestionEntity>> mList;
        private ArrayList<HelpQuestionEntity> tmpList;
        // 声明一个QAChildClass类型的变量
        private HelpQuestionEntity QAChild;
        // 声明一个字符串变量
        private String content;

        public QuestionSaxHandler(List<ArrayList<HelpQuestionEntity>> list) {
            this.mList = list;
            tmpList = new ArrayList<>();
        }

        /**
         * 当SAX解析器解析到XML文档开始时，会调用的方法
         */
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        /**
         * 当SAX解析器解析到XML文档结束时，会调用的方法
         */
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        /**
         * 当SAX解析器解析到某个属性值时，会调用的方法 其中参数ch记录了这个属性值的内容
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            content = new String(ch, start, length);
        }

        /**
         * 当SAX解析器解析到某个元素开始时，会调用的方法 其中localName记录的是元素属性名
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if ("question".equals(localName)) {
                QAChild = new HelpQuestionEntity();
            }
        }

        /**
         * 当SAX解析器解析到某个元素结束时，会调用的方法 其中localName记录的是元素属性名
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            if ("title".equals(localName)) {
                QAChild.setTitle(content);
            } else if ("content".equals(localName)) {
                QAChild.setContent(content);
                tmpList.add(QAChild);
                QAChild = new HelpQuestionEntity();
            } else if ("question".equals(localName)) {
                mList.add(tmpList);
                tmpList = new ArrayList<>();
            }
        }
    }
}
