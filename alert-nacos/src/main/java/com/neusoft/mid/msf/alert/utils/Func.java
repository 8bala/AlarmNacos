package com.neusoft.mid.msf.alert.utils;

import com.neusoft.mid.msf.common.util.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangyanjie
 * @date 2022/1/21 11:06
 */
public class Func extends StringUtils {

    public static final String NULL = "null";

    /**
     * 强转string,并去掉多余空格
     *
     * @param str 字符串
     * @return {String}
     */
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return {String}
     */
    public static String toStr(Object str, String defaultValue) {
        if (null == str || str.equals(NULL)) {
            return defaultValue;
        }
        return str.toString();
    }


    /**
     * 判断一个字符串是否是数字
     *
     * @param cs the CharSequence to check, may be null
     * @return {boolean}
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (StringUtils.isBlank(cs)) {
            return false;
        }
        for (int i = cs.length(); --i >= 0; ) {
            int chr = cs.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }





    public static boolean isEmpty(Collection list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNotEmpty(Collection list) {
        return list != null && list.size() > 0;
    }

    /**
     * 数组包含判断 b[i] == a
     * @param a
     * @param b
     * @return
     */
    public static boolean containsAny(int a, int... b) {
        for (int i = 0; i < b.length; i++) {
            if (a == b[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 一对多 equals
     * a.equals(b[i]) true
     * @param a
     * @param b
     * @return
     */
    public static boolean containsAny(String a, String... b) {
        for (int i = 0; i < b.length; i++) {
            if (a.equals(b[i])) {
                return true;
            }
        }
        return false;
    }

    static Pattern patternPhone = Pattern.compile("^((\\+?86)?)(\\d{1,20})");

    /**
     * 号码校验 +86 1-20
     * @return 匹配返回true
     */
    public static boolean phoneMatches(String number){
        Matcher matcher = patternPhone.matcher(number);
        return matcher.matches();
    }


    /**
     * 得到不带86开头的号码
     *
     * @param number 支持多号码 逗号分割
     * @return
     */
    public static String getNoWith86Number(String number) {
        String[] numbers = number.split(",");
        StringJoiner num = new StringJoiner(",");
        for (String s : numbers) {
            if (phoneMatches(number)) {
                String regular = s;
                if (StringUtils.isNotBlank(regular)) {
                    if (regular.startsWith("+86")) {
                        regular = regular.substring(3);
                    }
                    // 号码以86开始,去掉前缀
                    if (regular.startsWith("86")) {
                        regular = regular.substring(2);
                    }
                }
                num.add(regular);
            }else{
                num.add(s);
            }
        }
        return num.toString();
    }

    /**
     * query 添加排序
     * @param query query
     * @param property 字段
     * @param direction 排序  desc  asc
     */
    public static void setOrderQuery(Query query, String property, String direction) {
        List<Query.OrderItem> list = query.getOrders();
        if(Func.isEmpty(list)){
            list = new ArrayList<>();
        }
        Query.OrderItem order = new Query.OrderItem();
        order.setProperty(property);
        order.setDirection(direction);
        list.add(order);
        query.setOrders(list);
    }

    /**
     * 校验密码长度
     * @param pwd 密码
     * @return
     */
    public static boolean checkPwd(String... pwd){
        for (String s:pwd){
            int length = s.length();
            if (length<4||length>12){
                return false;
            }
        }
        return true;
    }
}
