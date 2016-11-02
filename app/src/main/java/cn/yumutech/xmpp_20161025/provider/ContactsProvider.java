package cn.yumutech.xmpp_20161025.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import cn.yumutech.xmpp_20161025.dbhelper.ContactOpenHelper;


/**
 * Created by hzh on 2016/10/31.
 */
public class ContactsProvider extends ContentProvider {

    // 主机地址的常量-->当前类的完整路径
    public static final String AUTHORITIES = ContactsProvider.class.getCanonicalName(); // 得到一个类的完整路径
    // 地址匹配对象
    static UriMatcher mUriMatcher;

    // 对应联系人表的一个uri常量
    public static Uri URI_CONTACT= Uri.parse("content://" + AUTHORITIES + "/contact");

    public static final int CONTACT = 1;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 添加一个匹配的规则
        mUriMatcher.addURI(AUTHORITIES, "/contact", CONTACT);
        // content://com.itheima.xmpp_20150807.provider.ContactsProvider/contact-->CONTACT
    }


    private ContactOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new ContactOpenHelper(getContext());
        if (mHelper != null) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /* -------------curd begin--------------------- */

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // 数据是存到sqlite-->创建db文件,建立表-->sqliteOpenHelper
        int code = mUriMatcher.match(uri);
        switch (code) {
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                // 新插入的id
                long id = db.insert(ContactOpenHelper.T_CONTACT, "", values);
                if (id != -1) {
                    System.out.println("---------ContactsProvider-----insertSuccess--------------");
                    // 拼接最新的uri
                    // content://com.itheima.xmpp_20150807.provider.ContactsProvider/contact/id
                    uri = ContentUris.withAppendedId(uri, id);
                    // 通知ContentObserver数据改变了
                    // getContext().getContentResolver().notifyChange(ContactsProvider.URI_CONTACT,"指定只有某一个observer可以收到");//
                    //getContext().getContentResolver().notifyChange(ContactsProvider.URI_CONTACT, null);// 为null就是所有都可以收到

                }
                break;

            default:
                break;
        }
        return uri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int deleteCount = 0;
        switch (code) {
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                deleteCount = db.delete(ContactOpenHelper.T_CONTACT, selection, selectionArgs);
                if (deleteCount > 0) {
                    System.out.println("--------ContactsProvider---deletetSuccess--------");
                }
                break;
        }
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int code = mUriMatcher.match(uri);
        int updateCount = 0;
        switch (code) {
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                updateCount = db.update(ContactOpenHelper.T_CONTACT, values, selection, selectionArgs);
                if (updateCount > 0) {
                    System.out.println("--------ContactsProvider---updatetSuccess--------");
                }
                break;
        }
        return updateCount;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        int code = mUriMatcher.match(uri);
        switch (code) {
            case CONTACT:
                SQLiteDatabase db = mHelper.getReadableDatabase();
                cursor = db.query(ContactOpenHelper.T_CONTACT, projection, selection, selectionArgs, null, null, sortOrder);
                System.out.println("--------ContactsProvider---querytSuccess--------");
                break;
        }
        return cursor;
    }

     /* -------------curd begin--------------------- */
}
