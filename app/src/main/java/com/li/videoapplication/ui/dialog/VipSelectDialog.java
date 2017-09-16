package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.VipRechargeEntity;
import com.li.videoapplication.framework.BaseDialog;

import java.util.List;


/**
 * Created by lpds on 2017/6/28.
 */
public class VipSelectDialog extends BaseDialog  {

    private List<VipRechargeEntity.DataBean> dataList = null;
    private OnSelectItem<VipRechargeEntity.DataBean> onSelectItem;
    private int selectPosition = 0;


    private boolean isClickMode = true;

    public boolean isClickMode() {
        return isClickMode;
    }

    public void setClickMode(boolean clickMode) {
        isClickMode = clickMode;
    }

    public VipSelectDialog(final Context context, final List<VipRechargeEntity.DataBean> data, OnSelectItem<VipRechargeEntity.DataBean> s) {
        super(context);
        this.dataList = data;
        this.onSelectItem = s;
        id_sure = findViewById(R.id.id_sure);
        id_vip_recyclerView = (RecyclerView) findViewById(R.id.id_vip_recyclerView);
        id_vip_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        id_vip_recyclerView.setAdapter(new RecyclerView.Adapter<VIPHolder>() {
            @Override
            public VIPHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new VIPHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_vip_info,null));
            }

            @Override
            public void onBindViewHolder(VIPHolder holder, final int position) {
                final VipRechargeEntity.DataBean vipData = dataList.get(position);
                holder.id_vip_info_text.setText(vipData.getName());
                addVipInfo(holder,position);
                if(isClickMode) {
                    holder.id_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("onBindViewHolder", "onClick: " + position);
                           // onSelect(vipData, position);
                            selectPosition = position;
                            notifyChange();
                        }
                    });
                }
                if(position == selectPosition){
                    holder.id_vip_info_text.setTextColor(Color.parseColor("#ffc110"));
                }else{
                    holder.id_vip_info_text.setTextColor(Color.BLACK);
                }
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }
        });
        id_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onSelectItem != null) {
                    onSelectItem.onSelect(dataList.get(selectPosition), selectPosition);
                }
                dismiss();
            }
        });
    }

    public VipSelectDialog(Context context, List<VipRechargeEntity.DataBean> data){
        this(context,data,null);
    }

    /**
     * 添加vip注释
     * @param holder
     * @param position
     */
    private void addVipInfo(VIPHolder holder,int position) {

        VipRechargeEntity.DataBean data = dataList.get(position);
        if(holder.id_vip_info_list.getChildCount() > 0) {
            holder.id_vip_info_list.removeAllViews();
        }
        for(String s : data.getDescription()){
            View v = LayoutInflater.from(getContext()).inflate(R.layout.adapter_vip_desc,null);
            TextView textView = ((TextView)v.findViewById(R.id.vip_desc_text));
//            if(position == 0){
//                if(s.contains("头像")){
//                    s = ""
//                }
//            }
            textView.setText(s);

            holder.id_vip_info_list.addView(v);
            if(position > selectPosition){
                textView.setTextColor(Color.parseColor("#999999"));
                ((ImageView)v.findViewById(R.id.id_desc_iv)).setImageResource(R.drawable.hook_gray);
            }else{
                textView.setTextColor(Color.parseColor("#333333"));
                ((ImageView)v.findViewById(R.id.id_desc_iv)).setImageResource(R.drawable.hook);
            }
        }

    }


    private RecyclerView id_vip_recyclerView;
    private View id_sure;

    @Override
    protected int getContentView() {
        return R.layout.dialog_vip_select;
    }

  /*  @Override
    public void onSelect(VipRechargeEntity.DataBean data, int position) {
        selectPosition = position;
        notifyChange();
    }*/

    @Override
    @Deprecated
    public void show() {
        show(0);
    }

    public void show(int i) {
        this.selectPosition = i;
        super.show();
        notifyChange();

    }

    public  interface OnSelectItem<T> {

        void onSelect(T t, int position);

    }

    private static final class VIPHolder extends RecyclerView.ViewHolder{

        public VIPHolder(View itemView) {
            super(itemView);
            id_vip_info_text = (TextView) itemView.findViewById(R.id.id_vip_info_text);
            id_vip_info_list = (ViewGroup) itemView.findViewById(R.id.id_vip_info_list);
            id_layout = itemView.findViewById(R.id.id_layout);
        }
        TextView id_vip_info_text;
        ViewGroup id_vip_info_list;
        View id_layout;
    }

    @Override
    protected void afterContentView(Context context) {
        Window window = getWindow();
        WindowManager manager = ((Activity) context).getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getWidth()*0.9f);
        window.setAttributes(params);
    }

    public void notifyChange(){
        id_vip_recyclerView.getAdapter().notifyDataSetChanged();
    }


}
