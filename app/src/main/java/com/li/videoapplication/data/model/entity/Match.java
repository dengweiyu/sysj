package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：活动
 */
@SuppressWarnings("serial")
public class Match extends BaseEntity {

    private String match_id;
    private String match_type;
    private String name;
    private String gameName;
    private String game_id;
    private String member_id;
    private String cover_chart1;
    private String cover_chart2;
    private String starttime;
    private String endtime;
    private String addtime;
    private String rewards;
    private String display;
    private String url;
    private String reward_url;
    private String match_rule;
    private String end_flag;
    private String pic_hd;
    private String pic_pld;
    private String pic_tj;
    private String pic_160_120;
    private String pic_200_113;
    private String pic_110_110;
    private String pic_600_x;
    private String flower_count;
    private String winners;
    private String upload_time;
    private String pc_rewards;
    private String ios_download;
    private String android_download;
    private String mark;
    private String width;
    private String height;
    private String description;
//	private String status;//赛事状态: 进行中 推荐 已结束

    //2.0.4赛事列表接口字段
    private String event_id;//赛事id
    private String cover;//列表封面
    private String reg_starttime;//报名开始时间
    private String reg_endtime;//报名结束时间
    private String type_name;//类型名称:个人赛，团体赛
    private String status;//赛事状态:火热 进行中 报名中 已结束
//	private String title;//标题
//	private String starttime;//比赛开始时间
//	private String endtime;//比赛结束时间

    //2.0.4赛事详情接口字段
//	private String event_id;
//	private String title；
//	private String flag;
//	private String status;//赛事的进行状态（0-未发布，1-报名中，2-进行中，3-已结束）
    private String chatroom_id;
    private String android_address;
    private String apple_address;
    private String rule_url;//规则页面url
    private String result_status;//当result_status=0时，则result_url=''；=1时，result_url为赛果页面url；
    private String result_url;//赛果页面url
    private int btn_status;//btn_status有多个状态，控制按钮文字内容，在btn_text已给出文字
    private String btn_text;
    private String type_id;
    private String event_team_server; //比赛服务器
    private String[] customer_service;
    private String chatroom_group_id;//群组id
    private String chatroom_group_name;//群组名称
    private String is_once;//是否即时匹配 0手动模式，1即时匹配
    private int alert_status;//胜利弹窗显示状态标识：0(没有弹框),1(弹框非最后轮),2(弹框最后轮)
    private String next_time;//下轮比赛开始时间

    //2.0.4赛程时间表接口字段
//	private String name;//赛程名，xx进xx
    private String schedule_id;//赛程id
    private String progress;//第几轮
    private String schedule_time;//对战时间
    private String pk_status;//对阵表发布状态
    private String schedule_starttime;//对战开始时间
    private String schedule_endtime;//对战结束时间
    private String is_last;//是否冠季军赛，0否1是


    //2.0.4赛程用户PK列表接口字段
//    private String status;//pk状态
    private String pk_a;//a方战队id
    private String pk_b;//B方战队id
    private String winner;//胜方战队id
    private String a_name;//a方名称
    private String b_name;//b方名称
    private String a_avatar;//a方头像
    private String b_avatar;//b方头像
    private String a_member_id;
    private String b_member_id;
    private String b_qq;
    private String a_qq;
    private String is_mate;//判断b队是轮空或匹配中（1正常,2轮空,3匹配中）

    //2.0.4赛程我的赛程（进行中）接口字段
//    private String schedule_id;
//    private String name;
//    private String pk_status;
//    private String progress;
//    private String schedule_time;
//    private String a_avatar;
//    private String b_avatar;
    private String is_sign;
    private String sign_starttime;
    private String sign_endtime;
    private String team_id;
    private String leader_id;
    private String team_name;
    private String leader_game_role;
    private String sign_times;
    private String pk_id;
    private String avatar;
    private String is_uppic;
    private String is_upvideo;
    private String result_pic;
    private String over_time;
    private String qq;

    //2.0.4赛程我的赛程（已结束）接口字段
    private Match schedule;
    private Match team_a;
    private Match team_b;
    private int is_win;

    private String share_url;
    private String apply_share_url;

    //2.1.4
    private String new_cover;
    private String event_award;//总奖金3000元
    private String event_format;//5v5淘汰赛
    private String match_format;//5v5淘汰赛
    private int format_type; //1:普通淘汰赛, 2:邀请赛
    private int is_invite; //是否设置邀请码0为否，1为是
    private String a_video_id; //0没传过，1传过视频
    private String b_video_id; //
    private String uptime;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getMatch_format() {
        return match_format;
    }

    public void setMatch_format(String match_format) {
        this.match_format = match_format;
    }

    public String getA_video_id() {
        return a_video_id;
    }

    public void setA_video_id(String a_video_id) {
        this.a_video_id = a_video_id;
    }

    public String getB_video_id() {
        return b_video_id;
    }

    public void setB_video_id(String b_video_id) {
        this.b_video_id = b_video_id;
    }

    public String getIs_upvideo() {
        return is_upvideo;
    }

    public void setIs_upvideo(String is_upvideo) {
        this.is_upvideo = is_upvideo;
    }

    public int getIs_invite() {
        return is_invite;
    }

    public void setIs_invite(int is_invite) {
        this.is_invite = is_invite;
    }

    public String getNew_cover() {
        return new_cover;
    }

    public void setNew_cover(String new_cover) {
        this.new_cover = new_cover;
    }

    public String getEvent_award() {
        return event_award;
    }

    public void setEvent_award(String event_award) {
        this.event_award = event_award;
    }

    public String getEvent_format() {
        return event_format;
    }

    public void setEvent_format(String event_format) {
        this.event_format = event_format;
    }

    public int getFormat_type() {
        return format_type;
    }

    public void setFormat_type(int format_type) {
        this.format_type = format_type;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getApply_share_url() {
        return apply_share_url;
    }

    public void setApply_share_url(String apply_share_url) {
        this.apply_share_url = apply_share_url;
    }

    public String getA_qq() {
        return a_qq;
    }

    public void setA_qq(String a_qq) {
        this.a_qq = a_qq;
    }

    public String getIs_mate() {
        return is_mate;
    }

    public void setIs_mate(String is_mate) {
        this.is_mate = is_mate;
    }

    public String getNext_time() {
        return next_time;
    }

    public void setNext_time(String next_time) {
        this.next_time = next_time;
    }

    public int getAlert_status() {
        return alert_status;
    }

    public void setAlert_status(int alert_status) {
        this.alert_status = alert_status;
    }

    public int getBtn_status() {
        return btn_status;
    }

    public void setBtn_status(int btn_status) {
        this.btn_status = btn_status;
    }

    public String getIs_once() {
        return is_once;
    }

    public void setIs_once(String is_once) {
        this.is_once = is_once;
    }

    public String getChatroom_group_name() {
        return chatroom_group_name;
    }

    public void setChatroom_group_name(String chatroom_group_name) {
        this.chatroom_group_name = chatroom_group_name;
    }

    public String getChatroom_group_id() {
        return chatroom_group_id;
    }

    public void setChatroom_group_id(String chatroom_group_id) {
        this.chatroom_group_id = chatroom_group_id;
    }

    public String getB_qq() {
        return b_qq;
    }

    public void setB_qq(String b_qq) {
        this.b_qq = b_qq;
    }

    public String getIs_last() {
        return is_last;
    }

    public void setIs_last(String is_last) {
        this.is_last = is_last;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getA_member_id() {
        return a_member_id;
    }

    public void setA_member_id(String a_member_id) {
        this.a_member_id = a_member_id;
    }

    public String getB_member_id() {
        return b_member_id;
    }

    public void setB_member_id(String b_member_id) {
        this.b_member_id = b_member_id;
    }

    public String getOver_time() {
        return over_time;
    }

    public void setOver_time(String over_time) {
        this.over_time = over_time;
    }

    public String getSign_endtime() {
        return sign_endtime;
    }

    public void setSign_endtime(String sign_endtime) {
        this.sign_endtime = sign_endtime;
    }

    public String getResult_pic() {
        return result_pic;
    }

    public void setResult_pic(String result_pic) {
        this.result_pic = result_pic;
    }

    public String getIs_uppic() {
        return is_uppic;
    }

    public void setIs_uppic(String is_uppic) {
        this.is_uppic = is_uppic;
    }

    public String[] getCustomer_service() {
        return customer_service;
    }

    public void setCustomer_service(String[] customer_service) {
        this.customer_service = customer_service;
    }

    public String getSchedule_starttime() {
        return schedule_starttime;
    }

    public void setSchedule_starttime(String schedule_starttime) {
        this.schedule_starttime = schedule_starttime;
    }

    public String getSchedule_endtime() {
        return schedule_endtime;
    }

    public void setSchedule_endtime(String schedule_endtime) {
        this.schedule_endtime = schedule_endtime;
    }

    public String getEvent_team_server() {
        return event_team_server;
    }

    public void setEvent_team_server(String event_team_server) {
        this.event_team_server = event_team_server;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public Match getSchedule() {
        return schedule;
    }

    public void setSchedule(Match schedule) {
        this.schedule = schedule;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Match getTeam_a() {
        return team_a;
    }

    public void setTeam_a(Match team_a) {
        this.team_a = team_a;
    }

    public Match getTeam_b() {
        return team_b;
    }

    public void setTeam_b(Match team_b) {
        this.team_b = team_b;
    }

    public int getIs_win() {
        return is_win;
    }

    public void setIs_win(int is_win) {
        this.is_win = is_win;
    }

    public String getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(String is_sign) {
        this.is_sign = is_sign;
    }

    public String getSign_starttime() {
        return sign_starttime;
    }

    public void setSign_starttime(String sign_starttime) {
        this.sign_starttime = sign_starttime;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(String leader_id) {
        this.leader_id = leader_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getLeader_game_role() {
        return leader_game_role;
    }

    public void setLeader_game_role(String leader_game_role) {
        this.leader_game_role = leader_game_role;
    }

    public String getSign_times() {
        return sign_times;
    }

    public void setSign_times(String sign_times) {
        this.sign_times = sign_times;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getPk_a() {
        return pk_a;
    }

    public void setPk_a(String pk_a) {
        this.pk_a = pk_a;
    }

    public String getPk_b() {
        return pk_b;
    }

    public void setPk_b(String pk_b) {
        this.pk_b = pk_b;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getA_avatar() {
        return a_avatar;
    }

    public void setA_avatar(String a_avatar) {
        this.a_avatar = a_avatar;
    }

    public String getB_avatar() {
        return b_avatar;
    }

    public void setB_avatar(String b_avatar) {
        this.b_avatar = b_avatar;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public String getPk_status() {
        return pk_status;
    }

    public void setPk_status(String pk_status) {
        this.pk_status = pk_status;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getReg_starttime() {
        return reg_starttime;
    }

    public void setReg_starttime(String reg_starttime) {
        this.reg_starttime = reg_starttime;
    }

    public String getReg_endtime() {
        return reg_endtime;
    }

    public void setReg_endtime(String reg_endtime) {
        this.reg_endtime = reg_endtime;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public String getAndroid_address() {
        return android_address;
    }

    public void setAndroid_address(String android_address) {
        this.android_address = android_address;
    }

    public String getApple_address() {
        return apple_address;
    }

    public void setApple_address(String apple_address) {
        this.apple_address = apple_address;
    }

    public String getRule_url() {
        return rule_url;
    }

    public void setRule_url(String rule_url) {
        this.rule_url = rule_url;
    }

    public String getResult_status() {
        return result_status;
    }

    public void setResult_status(String result_status) {
        this.result_status = result_status;
    }

    public String getResult_url() {
        return result_url;
    }

    public void setResult_url(String result_url) {
        this.result_url = result_url;
    }

    public String getBtn_text() {
        return btn_text;
    }

    public void setBtn_text(String btn_text) {
        this.btn_text = btn_text;
    }

    public String getReward_url() {
        return reward_url;
    }

    public void setReward_url(String reward_url) {
        this.reward_url = reward_url;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getMatch_type() {
        return match_type;
    }

    public void setMatch_type(String match_type) {
        this.match_type = match_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getCover_chart1() {
        return cover_chart1;
    }

    public void setCover_chart1(String cover_chart1) {
        this.cover_chart1 = cover_chart1;
    }

    public String getCover_chart2() {
        return cover_chart2;
    }

    public void setCover_chart2(String cover_chart2) {
        this.cover_chart2 = cover_chart2;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMatch_rule() {
        return match_rule;
    }

    public void setMatch_rule(String match_rule) {
        this.match_rule = match_rule;
    }

    public String getEnd_flag() {
        return end_flag;
    }

    public void setEnd_flag(String end_flag) {
        this.end_flag = end_flag;
    }

    public String getPic_hd() {
        return pic_hd;
    }

    public void setPic_hd(String pic_hd) {
        this.pic_hd = pic_hd;
    }

    public String getPic_pld() {
        return pic_pld;
    }

    public void setPic_pld(String pic_pld) {
        this.pic_pld = pic_pld;
    }

    public String getPic_tj() {
        return pic_tj;
    }

    public void setPic_tj(String pic_tj) {
        this.pic_tj = pic_tj;
    }

    public String getPic_160_120() {
        return pic_160_120;
    }

    public void setPic_160_120(String pic_160_120) {
        this.pic_160_120 = pic_160_120;
    }

    public String getPic_200_113() {
        return pic_200_113;
    }

    public void setPic_200_113(String pic_200_113) {
        this.pic_200_113 = pic_200_113;
    }

    public String getPic_110_110() {
        return pic_110_110;
    }

    public void setPic_110_110(String pic_110_110) {
        this.pic_110_110 = pic_110_110;
    }

    public String getFlower_count() {
        return flower_count;
    }

    public void setFlower_count(String flower_count) {
        this.flower_count = flower_count;
    }

    public String getWinners() {
        return winners;
    }

    public void setWinners(String winners) {
        this.winners = winners;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getPc_rewards() {
        return pc_rewards;
    }

    public void setPc_rewards(String pc_rewards) {
        this.pc_rewards = pc_rewards;
    }

    public String getIos_download() {
        return ios_download;
    }

    public void setIos_download(String ios_download) {
        this.ios_download = ios_download;
    }

    public String getAndroid_download() {
        return android_download;
    }

    public void setAndroid_download(String android_download) {
        this.android_download = android_download;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPic_600_x() {
        return pic_600_x;
    }

    public void setPic_600_x(String pic_600_x) {
        this.pic_600_x = pic_600_x;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    private int is_top;

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    private String title;
    private String join_num;
    private String flag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJoin_num() {
        return join_num;
    }

    public void setJoin_num(String join_num) {
        this.join_num = join_num;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String name_tag;

    public String getName_tag() {
        return name_tag;
    }

    public void setName_tag(String name_tag) {
        this.name_tag = name_tag;
    }
}
