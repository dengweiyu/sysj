package com.ifeimo.im.common.adapter;

import android.database.Cursor;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.holder.Holder;
import com.ifeimo.im.common.adapter.base.RecyclerViewCursorAdapter;
import com.ifeimo.im.common.bean.MsgBean;
import com.ifeimo.im.common.postentity.IMTextHtmlEntity;
import com.ifeimo.im.common.util.DateFormatUtil;
import com.ifeimo.im.common.util.MatchUtil;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.interface_im.IMWindow;
import com.ifeimo.im.provider.ChatProvider;
import com.ifeimo.im.view.RoundedImageView;
import com.ypy.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lpds on 2017/2/18.
 */
public abstract class BaseChatReCursorAdapter<T extends Holder> extends RecyclerViewCursorAdapter<T> {

    protected IMWindow activity;
    protected Map<String, String> time = new HashMap();
    protected String datelast = "0";
    protected int HEAD = 0;
    protected boolean isHadMore = true;

    protected View headView;

    /**
     * 最大页数
     */
    protected int maxPage = 1;
    /**
     * 最大行数
     */
    protected int maxCount = 0;
    /**
     * 当前页
     */
    protected int page = 1;
    /**
     * 每页最大行数
     */
    public static final int MAX_PAGE_COUNT = 15;

    private boolean isRefreshNow = false;


    @Override
    protected void onContentChanged() {
        //
    }

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public BaseChatReCursorAdapter(IMWindow context, Cursor c, int flags) {
        super(context.getContext(), c, flags);
        this.activity = context;
    }

    @Override
    public final void onBindViewHolder(T holder, Cursor cursor) {
        timeSetToMsg(holder, bindHolder(holder, cursor));
    }

    @Override
    public final void onBindViewHolder(T holder, int position) {
//        /**
//         * position 是第一项时
//         */
//        if (getItemViewType(position) == HEAD) {
//            if (isHadMore) {
//                holder.id_process.setVisibility(View.VISIBLE);
//            } else {
//                holder.id_process.setVisibility(View.GONE);
//            }
//        } else {
            super.onBindViewHolder(holder, position);
//        }
    }

    private void timeSetToMsg(T holder, MsgBean msgBean) {
        String formatTime = time.get(msgBean.getCreateTime());
        if (!StringUtil.isNull(formatTime)) {
            holder.id_msgTime_layout.setVisibility(View.VISIBLE);
            holder.timeTv.setText(formatTime);
        }

    }

    protected abstract MsgBean bindHolder(T holder, Cursor cursor);

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == HEAD) {
//            View convertView = LayoutInflater.from(activity.getContext()).inflate(R.layout.load_item, parent, false);
//            T holder = (T) new Holder(convertView);
//            holder.id_process = convertView.findViewById(R.id.id_process);
//            return holder;
//        } else {
            View convertView = LayoutInflater.from(activity.getContext()).inflate(R.layout.chat_item, parent, false);
            T holder = (T) new Holder(convertView);
            holder.id_msgTime_layout = convertView.findViewById(R.id.id_msgTime_layout);
            holder.leftLayout = convertView.findViewById(R.id.muc_left_layout);
            holder.rightLayout = convertView.findViewById(R.id.muc_right_layout);
            holder.leftFace = (RoundedImageView) convertView.findViewById(R.id.muc_left_face);
            holder.rightFace = (RoundedImageView) convertView.findViewById(R.id.muc_right_face);
            holder.leftName = (TextView) convertView.findViewById(R.id.muc_left_username);
            holder.leftMsg = (TextView) convertView.findViewById(R.id.muc_left_msg);
            holder.rightMsg = (TextView) convertView.findViewById(R.id.muc_right_msg);
            holder.reConnectIv = (ImageView) convertView.findViewById(R.id.id_reSendIV);
            holder.id_process = convertView.findViewById(R.id.id_process);
            holder.timeTv = (TextView) convertView.findViewById(R.id.id_msg_tip_time);
            return holder;
//        }
    }

    @Override
    public void changeCursor(Cursor cursor) {
        cheackCursor(cursor);
        super.changeCursor(cursor);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        }
        return 1;
    }


    /**
     * 时间计算
     *
     * @param newCursor
     */
    private void cheackCursor(Cursor newCursor) {
        if (newCursor != null && newCursor.getCount() > 0) {
            time.clear();
            datelast = "0";
            if (newCursor.moveToFirst()) {
                do {
                    doWhere(newCursor);
                } while (newCursor.moveToNext());
            }
        }
    }

    private void doWhere(Cursor lineCursor) {

        String thisDate = lineCursor.getString(lineCursor.getColumnIndex(Fields.ChatFields.CREATE_TIME));
        String content = lineCursor.getString(lineCursor.getColumnIndex(Fields.ChatFields.CONTENT));
        String formatdate = DateFormatUtil.getBorderDate(thisDate, datelast);

//        Log.e("55555", "(对比的时间 -- 格式化前 = " + datelast + " ,格式化后 = " + DateFormatUtil.getstyleByDateStr(datelast, "yyyy年MM月dd日 HH:mm") +
//                " ),content = " + content + " ,  显示时间 = " +
//                formatdate + "  , (自己的时间 格式化前 = " + thisDate + " ,格式化后 = " + DateFormatUtil.getstyleByDateStr(thisDate, "yyyy年MM月dd日 HH:mm"));
        if (formatdate != null) {
            time.put(thisDate, formatdate);
            datelast = thisDate;
        }

    }

    public int getPage() {
        return page;
    }

    public synchronized void setPage(int page) {
        this.page = page;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public synchronized void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public synchronized void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        sum();
    }

    /**
     * 根据最大行数计算最大页数
     */
    private synchronized void sum() {
        int maxPage = getMaxCount() / MAX_PAGE_COUNT;
        if (getMaxCount() % MAX_PAGE_COUNT != 0) {
            maxPage++;
        }
        setMaxPage(maxPage);
        //最大页数小于1 ，当前页数大于等于最大页数。不在是有更多消息
        if (maxPage < 1 || page >= maxPage) {
            isHadMore = false;
            headView.post(new Runnable() {
                @Override
                public void run() {
                    headView.setVisibility(View.GONE);
                }
            });
        } else {
            isHadMore = true;
            headView.post(new Runnable() {
                @Override
                public void run() {
                    headView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public View getHeadView() {
        return headView;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public boolean isHadMore() {
        return isHadMore;
    }

    public boolean isRefreshNow() {
        return isRefreshNow;
    }

    public void setRefreshNow(boolean refreshNow) {
        isRefreshNow = refreshNow;
    }

    protected void textViewCheck(Holder holder,String content){

        final String memberid = new String(holder.memberID);
        final String contentText = new String(content);

        View.OnClickListener textOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                if(view.getId() == R.id.muc_left_msg){
                    flag = false;
                }
                sendToClient(contentText,memberid,flag);
            }
        };
        holder.leftMsg.setOnClickListener(textOnclick);
        holder.rightMsg.setOnClickListener(textOnclick);
    }

    /**
     * 获得html匹配的字符串
     * @param content
     * @return
     */
    protected Spanned getSpanna(String content){
        if (MatchUtil.isHtml(content)) {
            String[] contents = MatchUtil.returnHtmlStr(content);
            if (content != null) {
                for (String text : contents) {
                    if(StringUtil.isNull(text)){
                        continue;
                    }
                    content = content.replace(text, "<font color=#3f54dc>" + text + "</font>");
                }
            }
        }

        return Html.fromHtml(content);

    }

    private void sendToClient(String defaultStr,String memberid,boolean isme){
        if(MatchUtil.isHtml(defaultStr)) {
            if (Proxy.getMessageManager().getOnHtmlItemClickListener() != null) {
                Proxy.getMessageManager().getOnHtmlItemClickListener().onClick(memberid,defaultStr, MatchUtil.returnHtmlStr(defaultStr), isme);
            }
//            IMTextHtmlEntity imTextHtmlEntity = new IMTextHtmlEntity();
//            imTextHtmlEntity.setDefaultStr(defaultStr);
//            imTextHtmlEntity.setMatchStr(MatchUtil.returnHtmlStr(defaultStr));
//            imTextHtmlEntity.setMe(isme);
//            imTextHtmlEntity.setSendMemberId(memberid);
//            EventBus.getDefault().post(imTextHtmlEntity);
        }else{

        }

    }

}
