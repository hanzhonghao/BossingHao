package cn.yumutech.xmpp_20161025.fragment;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;

import cn.yumutech.xmpp_20161025.R;
import cn.yumutech.xmpp_20161025.activity.LoginActivity;
import cn.yumutech.xmpp_20161025.dbhelper.ContactOpenHelper;
import cn.yumutech.xmpp_20161025.provider.ContactsProvider;
import cn.yumutech.xmpp_20161025.service.IMService;
import cn.yumutech.xmpp_20161025.utils.PinyinUtil;
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
        initListener();
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
    }

    private void initData() {


        //开启线程，同步花名册
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {

                //得到所有联系人
                //需要连接对象
                Roster roster = IMService.conn.getRoster();//得到花名册对象

                //得到所有联系人对象
                final Collection<RosterEntry> entries = roster.getEntries();//Alt+ enter自动返回等号左边

                //打印所有联系人
                for (RosterEntry entry : entries) {
                    System.out.print(entry.toString() + "  ");
                    System.out.print(entry.getUser() + "  ");
                    System.out.print(entry.getName() + "  ");
                    System.out.print(entry.getGroups() + "  ");
                    System.out.print(entry.getType() + "  ");
                    System.out.print(entry.getStatus() + "  ");
                    System.out.println("  ");
                }

                //监听联系人的改变
                roster.addRosterListener(new MyRosterListener());
                for (RosterEntry entry : entries) {
                    saveOrUpdateEntry(entry);
                }

                //对应查询记录
                final Cursor c = getActivity().getContentResolver().query(ContactsProvider.URI_CONTACT, null, null, null, null, null);
                //设置Adapter，然后显示数据
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //数据从数据库来用这个适配器
                        CursorAdapter adapter = new CursorAdapter(getActivity(), c) {
                            //如果convertview为空，返回一个具体的跟视图
                            @Override
                            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                                View view = View.inflate(context, R.layout.item_contact, null);
                                return view;
                            }

                            //设置数据,显示数据
                            @Override
                            public void bindView(View view, Context context, Cursor cursor) {
                                ImageView head = (ImageView) view.findViewById(R.id.image);
                                TextView tvAccount = (TextView) view.findViewById(R.id.account);
                                TextView tvNickName = (TextView) view.findViewById(R.id.nickname);
                                String account = cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.ACCOUNT));
                                String nickname = cursor.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.NICKNAME));
                                tvAccount.setText(account);
                                tvNickName.setText(nickname);
                            }
                        };
                        mListView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void saveOrUpdateEntry(RosterEntry entry) {
        ContentValues values = new ContentValues();
        String account = entry.getUser();
        account = account.substring(0, account.indexOf("@")) + "@" + LoginActivity.SERVICENAME;
        //处理昵称
        String nickname = entry.getName();
        if (nickname == null || "".equals(nickname)) {
            nickname = account.substring(0, account.indexOf("@"));//billy@ithem.com-->billy
        }

        values.put(ContactOpenHelper.ContactTable.ACCOUNT, account);
        values.put(ContactOpenHelper.ContactTable.NICKNAME, nickname);
        values.put(ContactOpenHelper.ContactTable.AVATAR, "0");
        values.put(ContactOpenHelper.ContactTable.PINYIN, PinyinUtil.getPinyin(account));

        //先update后插入
        int updateCount = getContext().getContentResolver().update(ContactsProvider.URI_CONTACT, values, ContactOpenHelper.ContactTable.ACCOUNT + "=?", new String[]{account});
        if (updateCount <= 0) {//没有任何更新记录
            getActivity().getContentResolver().insert(ContactsProvider.URI_CONTACT, values);
        }
    }

    private void initListener() {
    }

    class MyRosterListener implements RosterListener{

        @Override
        public void entriesAdded(Collection<String> addresses) {//联系人添加了
            System.out.println("-----------entriesAdded---------------");
        }

        @Override
        public void entriesUpdated(Collection<String> addresses) {//联系人修改了
            System.out.println("-----------entriesUpdated---------------");
        }

        @Override
        public void entriesDeleted(Collection<String> addresses) {//联系人删除了
            System.out.println("-----------entriesDeleted---------------");
        }

        @Override
        public void presenceChanged(Presence presence) {//联系人状态改变了
            System.out.println("-----------presenceChanged---------------");
        }
    }
}
