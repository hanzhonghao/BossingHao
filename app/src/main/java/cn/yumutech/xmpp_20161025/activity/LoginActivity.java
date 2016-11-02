package cn.yumutech.xmpp_20161025.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import cn.yumutech.xmpp_20161025.R;
import cn.yumutech.xmpp_20161025.utils.ThreadUtils;
import cn.yumutech.xmpp_20161025.utils.ToastUtils;
import cn.yumutech.xmpp_20161025.service.IMService;

public class LoginActivity extends AppCompatActivity {

    public static final String HOST = "222.18.162.150";//主机IP
    public static final int PORT = 5222;//对应的端口号
    public static final String SERVICENAME = "yumutech.cn";
    private TextView mAccountInput;
    private TextView mPassword;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }


    private void initListener() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = mAccountInput.getText().toString();
                final String passWord = mPassword.getText().toString();
                //判断用户名密码是否为空
                if (TextUtils.isEmpty(userName)) {
                    mAccountInput.setError("用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(passWord)) {
                    mPassword.setError("密码不能为空");
                    return;
                }
                ThreadUtils.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        /**把选中代码放到try catch中（ctrl + alt+ T）*/
                        try {
                            //创建连接配置对象
                            ConnectionConfiguration config = new ConnectionConfiguration(HOST,PORT);//抽取成常量Ctrl + shift+c

                            //额外的配置(方便我们开发，上线的时候可以改回来)
                            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);//明文传输
                            config.setDebuggerEnabled(true);//开启调试模式，方便我们查看具体的发送内容

                            //开始创建连接对象
                            XMPPConnection conn = new XMPPConnection(config);
                            //开始连接
                            conn.connect();
                            //开始登陆
                            conn.login(userName, passWord);
                            //已经登陆成功
                            ToastUtils.showToastSafe(LoginActivity.this, "登录成功");
                            finish();
                            //跳到主界面
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            //需要保存连接对象
                            IMService.conn = conn;

                            //启动IMService
                            Intent service = new Intent(LoginActivity.this, IMService.class);
                            startService(service);
                        } catch (XMPPException e) {
                            e.printStackTrace();
                            ToastUtils.showToastSafe(LoginActivity.this, "登录失败");
                            System.out.println("userName:" + userName);
                            System.out.println("password:" + passWord);
                        }
                    }
                });
            }
        });
    }

    private void initView() {
        mAccountInput = (TextView) findViewById(R.id.account_input);//抽取全局变量Ctrl+ Alt+V
        mPassword = (TextView) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.login);
    }

}
