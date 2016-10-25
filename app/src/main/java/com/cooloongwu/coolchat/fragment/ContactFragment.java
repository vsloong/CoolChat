package com.cooloongwu.coolchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.activity.AddFriendsActivity;
import com.cooloongwu.coolchat.activity.GroupActivity;
import com.cooloongwu.coolchat.adapter.ContactAdapter;
import com.cooloongwu.coolchat.base.Api;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.BaseFragment;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.coolchat.entity.Contact;
import com.cooloongwu.greendao.gen.ContactDao;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * 联系人界面
 */
public class ContactFragment extends BaseFragment implements View.OnClickListener {

    private ContactAdapter adapter;
    private ArrayList<Contact> listData = new ArrayList<>();

    private LinearLayout layout_addfriends;
    private RecyclerView recyclerView;

    private TextView contact_text_num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initViews(view);
        getFriendsList();
        return view;
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layout_addfriends = (LinearLayout) view.findViewById(R.id.layout_addfriends);
        contact_text_num = (TextView) view.findViewById(R.id.contact_text_num);

        TextView text_addfriend = (TextView) view.findViewById(R.id.text_addfriend);
        TextView text_group = (TextView) view.findViewById(R.id.text_group);
        text_addfriend.setOnClickListener(this);
        text_group.setOnClickListener(this);

        adapter = new ContactAdapter(getActivity(), listData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void getFriendsList() {
        Api.getFriendsList(getActivity(), AppConfig.getUserId(getActivity()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("获取朋友列表成功", response.toString());
                try {
                    int status = response.getInt("status");
                    switch (status) {
                        case 0:
                            //没有好友
                            layout_addfriends.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            break;
                        case 1:
                            //有好友
                            layout_addfriends.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = response.getJSONArray("friends");
                            List<Contact> contacts = new ArrayList<>();
                            int friendsNum = jsonArray.length();
                            contact_text_num.setText("好友（" + friendsNum + "）");
                            for (int i = 0; i < friendsNum; i++) {
                                JSONObject user = jsonArray.getJSONObject(i);
                                Contact contact = new Contact();
                                contact.setUserId(user.getInt("userId"));
                                contact.setName(user.getString("name"));
                                contact.setAvatar(user.getString("avatar"));
                                contact.setSex(user.getString("sex"));
                                contacts.add(contact);

                                insertOrUpdateContactDB(contact);
                            }
                            listData.addAll(contacts);
                            adapter.notifyDataSetChanged();

                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("获取数据失败", "" + statusCode);
            }
        });
    }

    /**
     * 插入或者更新数据
     *
     * @param contact 实体类
     */
    private void insertOrUpdateContactDB(Contact contact) {
        ContactDao contactDao = GreenDAOUtils.getInstance(getActivity()).getContactDao();
        Contact result = contactDao.queryBuilder()
                .where(ContactDao.Properties.UserId.eq(contact.getUserId()))        //判断是否有该ID
                .build()
                .unique();
        if (result != null) {
            //如果有该用户
            result.setSex(contact.getSex());
            result.setName(contact.getName());
            result.setAvatar(contact.getAvatar());
            result.setPhone(contact.getPhone());
            contactDao.update(result);
        } else {
            contactDao.insert(contact);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_addfriend:
                getActivity().startActivity(new Intent(getActivity(), AddFriendsActivity.class));
                break;
            case R.id.text_group:
                getActivity().startActivity(new Intent(getActivity(), GroupActivity.class));
                break;
            default:
                break;
        }
    }
}
