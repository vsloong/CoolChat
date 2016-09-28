package com.cooloongwu.coolchat.base;

import android.content.Context;

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

    private static DaoMaster.DevOpenHelper devOpenHelper;

    /**
     * 初始化OpenHelper
     *
     * @param context      上下文
     * @param dataBaseName 数据库名
     */
    static void initOpenHelper(Context context, String dataBaseName) {
        devOpenHelper = getOpenHelper(context, dataBaseName);
    }

    private static DaoMaster.DevOpenHelper getOpenHelper(Context context, String dataBaseName) {
        return new DaoMaster.DevOpenHelper(context, dataBaseName, null);
    }

    private static DaoSession getDaoSession() {
        return new DaoMaster(devOpenHelper.getWritableDb()).newSession();
    }

    public static ContactDao getContactDao() {
        return getDaoSession().getContactDao();
    }

    public static ConversationDao getConversationDao() {
        return getDaoSession().getConversationDao();
    }

    public static ChatFriendDao getChatFriendDao() {
        return getDaoSession().getChatFriendDao();
    }

    public static ChatGroupDao getChatGroupDao() {
        return getDaoSession().getChatGroupDao();
    }
}
