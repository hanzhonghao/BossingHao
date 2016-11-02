package cn.yumutech.xmpp_20161025.utils;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

/**
 * Created by hzh on 2016/11/1.
 */
public class PinyinUtil {
    public static String getPinyin(String str){
        return PinyinHelper.convertToPinyinString(str,"", PinyinFormat.WITHOUT_TONE);
    }
}
