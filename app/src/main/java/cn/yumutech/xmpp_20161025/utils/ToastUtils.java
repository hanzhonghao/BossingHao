package cn.yumutech.xmpp_20161025.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 可以在子线程中探出Toast
 * Created by hzh on 2016/10/26.
 */
public class ToastUtils {
    public static void showToastSafe(final Context context, final String text){
        ThreadUtils.runInUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
