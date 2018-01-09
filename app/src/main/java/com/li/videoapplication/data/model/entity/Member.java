package com.li.videoapplication.data.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.utils.StringUtil;

/**
 * 实体类：登录个人信息
 */
@SuppressWarnings("serial")
public class Member extends BaseEntity {

    private String id;
    private String member_grade;
    private String name;
    private String nickname;
    private String openid;
    private String password;
    private String pwd_origin;
    private String avatar;
    private String avatar_min;
    private String mobile;
    private String email;
    private String qq;
    private String isTelpass;
    private String isMailpass;
    private int sex;
    private String address;
    private String like_gametype;
    private String like_grouptype;
    private String time;
    private String isAdmin;
    private String logintime;
    private String loginip;
    private int degree;
    private String rank;
    private String regOrigin;
    private String attention;
    private String fans = "0";
    private String video_num;
    private String description;
    private String signature;
    private String like_gametypename;
    private String rank_id;
    private String exp;
    private String title;
    private int next_exp;
    private String taskNum;
    private String isForbid;
    private String msgNum;
    private int mark;
    private String attention_time;
    private int attentionMark;
    private String member_id;
    private int member_tick;
    private int tick;
    private int display;
    private String cover;
    private int isAttent;
    private String uploadVideoCount;
    private String NAME;
    private String token;
    private boolean isV;    //指的是热门主播
    private String currency;
    private String coin;
    private String win;
    private String failure;
    private String horizonId;
    private boolean isCoach;

    private VIPInfo vipInfo;

    private RewardInfo rewardInfo;

    private Token sysj_token;

    private List<LikeGameGroup> likeGameGroup;

    public boolean isCoach() {
        return isCoach;
    }

    public void setCoach(boolean coach) {
        isCoach = coach;
    }

    public Token getSysj_token() {
        return sysj_token;
    }

    public void setSysj_token(Token sysj_token) {
        this.sysj_token = sysj_token;
    }

    public RewardInfo getRewardInfo() {
        return rewardInfo;
    }

    public void setRewardInfo(RewardInfo rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    public VIPInfo getVipInfo() {
        return vipInfo;
    }

    public void setVipInfo(VIPInfo vipInfo) {
        this.vipInfo = vipInfo;
    }

    public static class VIPInfo implements Serializable {
        public static final String SILVER = "1";
        public static final String GOLD = "2";
        public static final String DIAMOND = "3";

        private String level;
        private String member_id;
        private String end_time;
        private String valid;
        private String order_id;
        private String is_mg = "-1";

        public String getValid() {
            return valid;
        }

        public void setValid(String valid) {
            this.valid = valid;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getIs_migu() {
            return is_mg;
        }

        public void setIs_migu(String is_migu) {
            this.is_mg = is_migu;
        }

        private List<VIPInfo> details;

        public List<VIPInfo> getDetail() {
            return details;
        }

        public void setDetail(List<VIPInfo> detail) {
            this.details = detail;
        }
    }

    public static class RewardInfo implements Serializable {
        private boolean hasGift;
        private List<String> gift_icon;

        public boolean isHasGift() {
            return hasGift;
        }

        public void setHasGift(boolean hasGift) {
            this.hasGift = hasGift;
        }

        public List<String> getGift_icon() {
            return gift_icon;
        }

        public void setGift_icon(List<String> gift_icon) {
            this.gift_icon = gift_icon;
        }
    }

    public static class Token implements Serializable {
        private String access_token;
        private int expires_in;
        private String token_type;
        private String refresh_token;

        private long accessTokenTime;
        private long refreshTokenTime;

        public void setAccessTokenTime(long accessTokenTime) {
            this.accessTokenTime = accessTokenTime;
        }

        public void setRefreshTokenTime(long refreshTokenTime) {
            this.refreshTokenTime = refreshTokenTime;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
            accessTokenTime = System.currentTimeMillis();
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
            refreshTokenTime = System.currentTimeMillis();
        }

        public long getAccessTokenTime() {
            return accessTokenTime;
        }

        public long getRefreshTokenTime() {
            return refreshTokenTime;
        }
    }

    public String getHorizonId() {
        return horizonId;
    }

    public void setHorizonId(String horizonId) {
        this.horizonId = horizonId;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isV() {
        return isV;
    }

    public void setV(boolean v) {
        isV = v;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_grade() {
        return member_grade;
    }

    public void setMember_grade(String member_grade) {
        this.member_grade = member_grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPwd_origin() {
        return pwd_origin;
    }

    public void setPwd_origin(String pwd_origin) {
        this.pwd_origin = pwd_origin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_min() {
        return avatar_min;
    }

    public void setAvatar_min(String avatar_min) {
        this.avatar_min = avatar_min;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIsTelpass() {
        return isTelpass;
    }

    public void setIsTelpass(String isTelpass) {
        this.isTelpass = isTelpass;
    }

    public String getIsMailpass() {
        return isMailpass;
    }

    public void setIsMailpass(String isMailpass) {
        this.isMailpass = isMailpass;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLike_gametype() {
        return like_gametype;
    }

    public void setLike_gametype(String like_gametype) {
        this.like_gametype = like_gametype;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRegOrigin() {
        return regOrigin;
    }

    public void setRegOrigin(String regOrigin) {
        this.regOrigin = regOrigin;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getVideo_num() {
        return video_num;
    }

    public void setVideo_num(String video_num) {
        this.video_num = video_num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLike_gametypename() {
        return like_gametypename;
    }

    public void setLike_gametypename(String like_gametypename) {
        this.like_gametypename = like_gametypename;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getRank_id() {
        return rank_id;
    }

    public void setRank_id(String rank_id) {
        this.rank_id = rank_id;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNext_exp() {
        return next_exp;
    }

    public void setNext_exp(int next_exp) {
        this.next_exp = next_exp;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }

    public String getIsForbid() {
        return isForbid;
    }

    public void setIsForbid(String isForbid) {
        this.isForbid = isForbid;
    }

    public String getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(String msgNum) {
        this.msgNum = msgNum;
    }

    public String getAttention_time() {
        return attention_time;
    }

    public void setAttention_time(String attention_time) {
        this.attention_time = attention_time;
    }

    public int getAttentionMark() {
        return attentionMark;
    }

    public void setAttentionMark(int attentionMark) {
        this.attentionMark = attentionMark;
    }

    public int getMember_tick() {
        return member_tick;
    }

    public void setMember_tick(int member_tick) {
        this.member_tick = member_tick;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLike_grouptype() {
        return like_grouptype;
    }

    public void setLike_grouptype(String like_grouptype) {
        this.like_grouptype = like_grouptype;
    }

    public int getIsAttent() {
        return isAttent;
    }

    public void setIsAttent(int isAttent) {
        this.isAttent = isAttent;
    }

    public String getUploadVideoCount() {
        return uploadVideoCount;
    }

    public void setUploadVideoCount(String uploadVideoCount) {
        this.uploadVideoCount = uploadVideoCount;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String nAME) {
        NAME = nAME;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public List<LikeGameGroup> getLikeGameGroup() {
        return likeGameGroup;
    }

    public void setLikeGameGroup(List<LikeGameGroup> likeGameGroup) {
        this.likeGameGroup = likeGameGroup;
    }

    public List<String> getLikeGroupType() {
        List<String> list = new ArrayList<String>();
        if (!StringUtil.isNull(this.getLike_grouptype())) {
            String[] arr = this.getLike_grouptype().split(",");
            list = Arrays.asList(arr);
        }
        return list;
    }

    public void setLikeGroupType(List<String> list) {

        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < (list.size() - 1)) {
                    sb.append(list.get(i));
                    sb.append(",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        setLike_grouptype(sb.toString().trim() + "");
    }

    public static class LikeGameGroup {
        private String group_id;
        private String flag;
        private String group_name;

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }
    }
}
