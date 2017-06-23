package com.ifeimo.im.common.bean;

import com.ifeimo.im.common.MD5;

/**
 * Created by admin on 2016/12/19.
 *
 * 用户信息
 *
 */

public class UserBean {

    private static String MemberID;
    private static String NickName;
    private static String AvatarUrl;
    private static String first;
    private static String last;
    private static String mobile;
    private static String email;
    private static String city;
    private static String description;
    private static String md5MemberID;
    /**
     * 最后一次设置的账号和昵称和图片地址的md5
     */
    private static String l_MD5;
//    private static boolean

    public static void clear(){
        MemberID = null;
        NickName = null;
        AvatarUrl = null;
    }


    public static String getMd5MemberID() {
        return md5MemberID;
    }

    public static void setLoginUser(String memberID, String nickName, String avatarUrl,
                                    String mfirst, String mlast, String mmobile, String memail, String mcity, String mdescription){
        MemberID = memberID;
        md5MemberID = MD5.getMD5(MemberID);
        NickName = nickName;
        AvatarUrl = avatarUrl;
        first = mfirst;
        last = mlast;
        mobile = mmobile;
        email = memail;
        city = mcity;
        description = mdescription;
    }

    public static String getMemberID() {
        return MemberID;
    }

    public static String getNickName() {
        return NickName;
    }

    public static String getAvatarUrl() {
        return AvatarUrl;
    }

    public static boolean isNullMsg(){
        return MemberID == null || "".equals(MemberID);
    }

    public static String getCity() {
        return city;
    }

    public static String getDescription() {
        return description;
    }

    public static String getEmail() {
        return email;
    }

    public static String getFirst() {
        return first;
    }

    public static String getLast() {
        return last;
    }

    public static String getMobile() {
        return mobile;
    }

    public static void setMemberID(String memberID) {
        MemberID = memberID;
        md5MemberID = MD5.getMD5(MemberID);
    }

    public static void setNickName(String nickName) {
        NickName = nickName;
    }

    public static void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }

    public static void setFirst(String first) {
        UserBean.first = first;
    }

    public static void setLast(String last) {
        UserBean.last = last;
    }

    public static void setMobile(String mobile) {
        UserBean.mobile = mobile;
    }

    public static void setEmail(String email) {
        UserBean.email = email;
    }

    public static void setCity(String city) {
        UserBean.city = city;
    }

    public static void setDescription(String description) {
        UserBean.description = description;
    }

    public static boolean isMemberIdNull(){
        return MemberID == null || MemberID.equals("");
    }

    public static String getL_MD5() {
        return l_MD5;
    }

    public static void setL_MD5(String l_MD5) {
        UserBean.l_MD5 = l_MD5;
    }
}
