package cn.yumutech.xmpp_20161025.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import cn.yumutech.xmpp_20161025.R;
import cn.yumutech.xmpp_20161025.utils.ThreadUtils;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //停留3秒进入主界面
        ThreadUtils.runInThread(new Runnable(){

            @Override
            public void run() {
                //停留3秒
                SystemClock.sleep(3000);

                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
