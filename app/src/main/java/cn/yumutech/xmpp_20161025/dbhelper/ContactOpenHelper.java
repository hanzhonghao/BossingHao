package cn.yumutech.xmpp_20161025.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by hzh on 2016/10/31.
 */
public class ContactOpenHelper extends SQLiteOpenHelper {
    public static final String T_CONTACT = "t_contact";

    public class ContactTable implements BaseColumns {//就是会默认给我们添加一列  _id
        public static final String ACCOUNT = "account";//账号
        public static final String NICKNAME = "nickname";//昵称
        public static final String AVATAR = "avatar";//头像
        public static final String PINYIN = "pinyin";//账号拼音
    }


    public ContactOpenHelper(Context context) {

        super(context, "contact.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + T_CONTACT + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                ContactTable.ACCOUNT + " TEXT, " +
                ContactTable.NICKNAME + " TEXT, " +
                ContactTable.AVATAR + " TEXT, " +
                ContactTable.PINYIN + " TEXT);";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
