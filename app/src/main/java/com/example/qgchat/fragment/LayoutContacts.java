package com.example.qgchat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.qgchat.R;
import com.example.qgchat.activity.AtyChatRoom;
import com.example.qgchat.adapter.ContactsRecycleAdapter;
import com.example.qgchat.bean.GroupMessage;
import com.example.qgchat.bean.UserGroup;
import com.example.qgchat.bean.UserList;
import com.example.qgchat.db.DBUserGruop;
import com.example.qgchat.db.DBUserList;
import com.example.qgchat.socket.LoginEvent;
import com.example.qgchat.socket.ServerManager;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.BeanUtil;
import com.example.qgchat.util.DBUtil;
import com.example.qgchat.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.media.CamcorderProfile.get;
import static com.example.qgchat.activity.BaseActivity.account;
import static org.litepal.crud.DataSupport.findAll;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutContacts extends Fragment {
    private static final String TAG = "LayoutContacts";
    public ServerManager serverManager = ServerManager.getServerManager();
    @BindView(R.id.contactsRecycleView)
    RecyclerView contactsRecycleView;
    Unbinder unbinder;
    private View rootView;
    private ContactsRecycleAdapter adapter;
    private ArrayList<MultiItemEntity> list;
    private boolean isFirstLoad = true;

    public LayoutContacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_contacts, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initAdapter();
        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstLoad && AccessNetwork.getNetworkState(getActivity()) != AccessNetwork.INTERNET_NONE) {
            queryFromServer();
            isFirstLoad = false;
        }
    }

    private void initAdapter() {
        list = loadLocalMessage();
        adapter = new ContactsRecycleAdapter(list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserList ulist= (UserList) list.get(position);
                Intent intent = new Intent(getActivity(), AtyChatRoom.class);
                intent.putExtra("friend_account", ulist.account);
                startActivity(intent);
            }
        });

        contactsRecycleView.setAdapter(adapter);
        contactsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //contactsRecycleView.addItemDecoration(new GridItemDecoration(getActivity(),R.drawable.recycleview_divider));
    }


    private void queryFromServer() {
        if (serverManager.getAccount() != null) {
            String url=HttpUtil.getGroupMessageURL+"?account="+serverManager.getAccount();
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.i("info", "onResponse: "+result);
                    if (!result.equals("failed")) {
                        GroupMessage groupMessage = BeanUtil.handleGroupMessageResponse(result);
                        DataSupport.deleteAll(DBUserGruop.class);
                        DataSupport.deleteAll(DBUserList.class);
                        DBUtil.saveUserGroup(groupMessage);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initAdapter();
                            }
                        });
                    }
                }
            });
        }
    }


    private ArrayList<MultiItemEntity> loadLocalMessage() {
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        List<DBUserGruop> groupList= DataSupport.findAll(DBUserGruop.class,true);

        for (int i = 0; i < groupList.size(); i++) {
            DBUserGruop dbGroup = groupList.get(i);
            UserGroup group = new UserGroup(dbGroup.getGroupName(),"1","1");
            List<DBUserList> userList = dbGroup.getDbUserLists();
            for (int i1 = 0; i1 < userList.size(); i1++) {
                DBUserList dbUser = userList.get(i1);
                UserList list = new UserList(dbUser.getAccount(),dbUser.getIconURL(),dbUser.getUsername());
                group.addSubItem(list);
            }
            res.add(group);
        }
        return res;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
