package cn.yumutech.xmpp_20161025.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yumutech.xmpp_20161025.R;
import cn.yumutech.xmpp_20161025.dbhelper.ContactOpenHelper;
import cn.yumutech.xmpp_20161025.provider.ContactsProvider;
import cn.yumutech.xmpp_20161025.utils.ThreadUtils;

/**
 * A simple {@link Fragment} subclass.
 */
/*
 * @描述 联系人的Fagment
 */
public class ContactsFragment extends Fragment {
    private static ContactsFragment contactsFragments;
    private ListView mListView;
    private CursorAdapter mAdapter;

    public static ContactsFragment getInstance() {
        if (contactsFragments == null) {
            contactsFragments = new ContactsFragment();
        }
        return contactsFragments;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData();
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        registerContentObserver();
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
    }

    private void initData() {

        setOrUpdateAdapter();
    }

    /**
     * 设置或者更新adapter
     */
    private void setOrUpdateAdapter() {
        // 判断adapter是否存在
        if (mAdapter != null) {
            // 刷新adapter就行了
            mAdapter.getCursor().requery();
            return;
        }
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                // 对应查询记录
                final Cursor c =
                        getActivity().getContentResolver().query(ContactsProvider.URI_CONTACT, null, null, null, null);

                // 假如没有数据的时候
                if (c.getCount() <= 0) {
                    return;
                }
                // 设置adapter,然后显示数据
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         BaseAdapter    -->getView
                         |-CursorAdapter
                         */
                        mAdapter = new CursorAdapter(getActivity(), c) {
                            // 如果convertView==null,返回一个具体的根视图
                            @Override
                            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                                View view = View.inflate(context, R.layout.item_contact, null);
                                return view;
                            }

                            // 设置数据显示数据
                            @Override
                            public void bindView(View view, Context context, Cursor cursor) {
                                ImageView ivHead = (ImageView) view.findViewById(R.id.image);
                                TextView tvAccount = (TextView) view.findViewById(R.id.account);
                                TextView tvNickName = (TextView) view.findViewById(R.id.nickname);

                                String acccount =
                                        cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.ACCOUNT));

                                String nickName =
                                        cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.NICKNAME));

                                tvAccount.setText(acccount);
                                tvNickName.setText(nickName);
                            }
                        };

                        mListView.setAdapter(mAdapter);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        // 按照常理,我们Fragment销毁了.那么我们就不应该去继续去监听
        // 但是,实际,我们是需要一直监听对应roster的改变
        // 所以,我们把联系人的监听和同步操作放到Service去
        unRegisterContentObserver();
        super.onDestroy();
    }

	/*=============== 监听数据库记录的改变 ===============*/

    MyContentObserver mMyContentObserver = new MyContentObserver(new Handler());

    /**注册监听*/
    public void registerContentObserver() {
        // content://xxxx/contact
        // content://xxxx/contact/i
        getActivity().getContentResolver().registerContentObserver(ContactsProvider.URI_CONTACT, true,
                mMyContentObserver);
    }

    public void unRegisterContentObserver() {
        getActivity().getContentResolver().unregisterContentObserver(mMyContentObserver);
    }

    /**反注册监听*/

    class MyContentObserver extends ContentObserver {

        public MyContentObserver(Handler handler) {
            super(handler);
        }

        /**
         * 如果数据库数据改变会在这个方法收到通知
         */
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            // 更新adapter或者刷新adapter
            setOrUpdateAdapter();
        }
    }
}


