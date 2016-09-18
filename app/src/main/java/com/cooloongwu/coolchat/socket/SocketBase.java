package com.cooloongwu.coolchat.socket;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 感谢 Mr_Sanders ，https://github.com/SSOOnline/android-network
 * Socket连接的操作类
 * Created by CooLoongWu on 2016-8-8 14:27.
 */
public class SocketBase {
    private Socket mSocket;// socket连接对象
    private DataOutputStream out;
    private DataInputStream in;// 输入流
    private SocketCallback callback;// 信息回调接口
    private int timeOut = 1000 * 30;

    /**
     * 构造方法传入信息回调接口对象
     *
     * @param callback 回调接口
     */
    public SocketBase(SocketCallback callback) {
        this.callback = callback;
    }


    /**
     * 连接服务器
     *
     * @param ip
     * @param port
     * @throws Exception
     */
    public void connect(final String ip, final int port) throws Exception {
        try {
            mSocket = new Socket(ip, port);

            if (mSocket.isConnected()) {
                out = new DataOutputStream(mSocket.getOutputStream());// 获取网络输出流
                in = new DataInputStream(mSocket.getInputStream());// 获取网络输入流
                callback.connected();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Socket连接出现错误", e.getMessage());
        }

    }

    /**
     * 设置超时时间
     *
     * @param timeOut 超时时间
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isConnected() {
        if (mSocket != null) {
            return mSocket.isConnected();
        }
        return false;
    }

    /**
     * 发送数据
     *
     * @param buffer 信息字节数据
     * @throws IOException
     */
    public void write(byte[] buffer) throws IOException {
        if (out != null) {
            out.write(buffer);
            out.flush();
        }
    }

    /**
     * 断开连接
     *
     * @throws IOException
     */
    public void disconnect() {
        try {
            if (mSocket != null) {
                if (!mSocket.isInputShutdown()) {
                    mSocket.shutdownInput();
                }
                if (!mSocket.isOutputShutdown()) {
                    mSocket.shutdownOutput();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                mSocket.close();// 关闭socket
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            callback.disconnect();
            out = null;
            in = null;
            mSocket = null;// socket对象值为空
        }
    }

    /**
     * 读取网络数据
     *
     * @throws IOException
     */
    public void read() throws IOException {
        if (in != null) {
            byte[] buffer = new byte[1024];// 缓冲区字节数组，信息不能大于此缓冲区
            byte[] tmpBuffer;// 临时缓冲区
            int len;// 读取长度
            while ((len = in.read(buffer)) > 0) {
                tmpBuffer = new byte[len];// 创建临时缓冲区
                System.arraycopy(buffer, 0, tmpBuffer, 0, len);// 将数据拷贝到临时缓冲区
                callback.receive(tmpBuffer);// 调用回调接口传入得到的数据
                tmpBuffer = null;
            }
        }
    }

}
