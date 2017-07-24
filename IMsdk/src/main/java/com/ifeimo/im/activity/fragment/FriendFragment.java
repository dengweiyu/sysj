package com.ifeimo.im.activity.fragment;

import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.base.CommonAdapter;
import com.ifeimo.im.common.adapter.base.ViewHolder;
import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.framwork.Proxy;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpds on 2017/1/17.
 */
@Deprecated
public class FriendFragment extends BaseFragment {
    private ListView listView;
    private List<AccountBean> list = new ArrayList<>();

    @Override
    protected int getContentViewByID() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void init() {

        listView = (ListView) contentView.findViewById(R.id.listview);
        listView.setAdapter(new CommonAdapter<AccountBean>(getActivity(), R.layout.item_contract, list) {
            @Override
            protected void convert(ViewHolder viewHolder, AccountBean item, int position) {
                viewHolder.setText(R.id.muc_left_username,item.getNickName());
            }
        });
        runThread();
    }


    private void runThread() {
        new Thread() {
            @Override
            public void run() {

                for (RosterEntry rosterEntry : Proxy.getAccountManger().getAllFriend()) {
                    log(" ------- RosterEntry --------" + rosterEntry);
                    if(Proxy.getAccountManger().getUserMemberId().equals(rosterEntry.getUser())){
                        try {
                            rosterEntry.setName("自己");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    list.add(AccountBean.buildeAccountBeanByRoster(rosterEntry));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                    }
                });

            }
        }.start();
    }
}
