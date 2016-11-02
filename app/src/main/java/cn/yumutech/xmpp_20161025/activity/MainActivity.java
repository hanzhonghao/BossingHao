package cn.yumutech.xmpp_20161025.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.yumutech.xmpp_20161025.R;
import cn.yumutech.xmpp_20161025.fragment.ContactsFragment;
import cn.yumutech.xmpp_20161025.fragment.SessionFragment;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.main_tv_title)
    TextView mMainTvTitle;

    private String[] mToolBarTitleArr;
    private ContactsFragment mContactsFragment;
    private FragmentTransaction mFragementTransaction;
    private SessionFragment mSessionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initData();

        mFragementTransaction = getSupportFragmentManager().beginTransaction();
        mFragementTransaction.replace(R.id.main_fragment_content, new SessionFragment());
        mFragementTransaction.commit();

    }

    private void initData() {
        //文字内容
        mToolBarTitleArr = new String[]{"最近会话", "联系人"};
        mMainTvTitle.setText(mToolBarTitleArr[0]);
    }

    public void onClickTabButton(View view) {
        mFragementTransaction = getSupportFragmentManager().beginTransaction();
        int id = view.getId();
        if (id == R.id.btn_session_chat) {
            mSessionFragment = new SessionFragment();
            mFragementTransaction.replace(R.id.main_fragment_content, mSessionFragment);
            mMainTvTitle.setText(mToolBarTitleArr[0]);
        } else if (id == R.id.btn_contacts_chat) {
            mContactsFragment = new ContactsFragment();
            mFragementTransaction.replace(R.id.main_fragment_content, mContactsFragment);
            mMainTvTitle.setText(mToolBarTitleArr[1]);
        }
        mFragementTransaction.commit();
    }
}
