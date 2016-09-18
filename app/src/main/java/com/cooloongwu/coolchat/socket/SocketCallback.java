package com.cooloongwu.coolchat.socket;

/**
 * 感谢 Mr_Sanders ，https://github.com/SSOOnline/android-network
 * 获取网络数据的回调接口
 * Created by CooLoongWu on 2016-8-8 14:29.
 */
public abstract interface SocketCallback {
    /**
     * 当建立连接时的回调
     */
    public abstract void connected();

    /**
     * 当获取到网络数据时的回调
     *
     * @param buffer 字节数据
     */
    public abstract void receive(byte[] buffer);

    /**
     * 当连接断开时的回调
     */
    public abstract void disconnect();
}
