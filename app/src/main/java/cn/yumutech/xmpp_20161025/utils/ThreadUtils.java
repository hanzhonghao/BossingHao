package cn.yumutech.xmpp_20161025.utils;


import android.os.Handler;

/**
 * Created by w on 2016/10/25.
 */
public class ThreadUtils {
    /**子线程执行task*/
    public static void runInThread(Runnable task){
        new Thread(task).start();
    }

    /**
      主线程里面的一个handler
     */
    public static Handler mHandler = new Handler(){};
    /**UI线程执行task*/
    public static void runInUIThread(Runnable task){
        mHandler.post(task);
    }
}
