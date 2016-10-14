package com.cooloongwu.coolchat.base;

import android.content.Context;
import android.util.Log;

import com.cooloongwu.greendao.gen.ChatFriendDao;
import com.cooloongwu.greendao.gen.ChatGroupDao;
import com.cooloongwu.greendao.gen.ContactDao;
import com.cooloongwu.greendao.gen.ConversationDao;
import com.cooloongwu.greendao.gen.DaoMaster;
import com.cooloongwu.greendao.gen.DaoSession;

/**
 * 操作GreenDAO的类
 * Created by CooLoongWu on 2016-9-28 14:25.
 */

public class GreenDAO {

    private static GreenDAO instance;
    private DaoMaster.DevOpenHelper devOpenHelper;

    public static GreenDAO getInstance(Context context) {
        if (instance == null) {
            instance = new GreenDAO(context);
        }
        return instance;
    }

    GreenDAO(Context context) {
        String dataBaseName = AppConfig.getUserDB(context);
        Log.e("加载的用户的数据库", dataBaseName);
        devOpenHelper = new DaoMaster.DevOpenHelper(context, dataBaseName, null);
    }

    private DaoSession getDaoSession() {
        return new DaoMaster(devOpenHelper.getWritableDb()).newSession();
    }

    public ContactDao getContactDao() {
        return getDaoSession().getContactDao();
    }

    public ConversationDao getConversationDao() {
        return getDaoSession().getConversationDao();
    }

    public ChatFriendDao getChatFriendDao() {
        return getDaoSession().getChatFriendDao();
    }

    public ChatGroupDao getChatGroupDao() {
        return getDaoSession().getChatGroupDao();
    }
}
