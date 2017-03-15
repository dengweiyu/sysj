package com.ifeimo.im.framwork.database;

/**
 * Created by lin on 2017/3/1
 *
 * 数据库字段类。
 *
 */
public final class Fields {


    private static class BaseFields{
        /**
         * 每行的唯一标识
         */
        public static final String ID = "_id";
    }
    private static class UserFields extends BaseFields{
        public static final String MEMBER_ID = "memberId";
    }

    public static class MsgFields extends UserFields{
        public static final int SEND_FINISH = 2001;
        public static final int SEND_UNCONNECT = 2002;
        public static final int SEND_WAITING = 2003;
        private MsgFields(){}
        public static final String MSG_ID = "msgId";
        public static final String CONTENT = "content";
        public static final String SEND_TYPE = "send_type";
        public static final String CREATE_TIME = "create_time";
    }

    /**
     * 群聊
     */
    public static final class GroupChatFields extends MsgFields{
        /**
         * tabele name
         */
        public static final String TB_NAME = "tb_mucc";
        private GroupChatFields(){}

        /**
         * 房间id
         */
        public static final String ROOM_ID = "roomid";
    }

    /**
     * 单聊
     */
    public static final class ChatFields extends MsgFields{
        /**
         * super.MEMBER_ID ，发送者意思
         */


        /**
         * tabele name
         */
        public static final String TB_NAME = "tb_chat";
        private ChatFields(){}

        /**
         * 接收者
         */
        public static final String RECEIVER_ID = "receiverId";
    }

    /**
     * 消息列表
     */
    public static class InformationFields extends MsgFields{
        /**
         * table name
         */
        public static final String TB_NAME = "tb_information";
        private InformationFields(){ }

        /**
         * 对方id
         */
        public static final String OPPOSITE_ID = "opposite_id";
        /**
         * 最后一次会话
         */
        public static final String LAST_CONTENT = "last_content";
        /**
         * 最后一次会话时间
         */
        public static final String LAST_CREATETIME = "last_create_time";
        /**
         * 类型
         */
        public static final String TYPE = "type";//来者类;
        /**
         * AccounFields.MEMBER_NICKNAME as TITLE
         */
        public static final String TITLE = "title";
        /**
         * AccounFields.MEMBER_AVATARURL as PICURL
         */
        public static final String PICURL = "pic_url";
        /**
         * 暂时与title一致,对方姓名，或者房间名字
         */
        @Deprecated
        public static final String NAME = "name";
        /**
         * 未读数量
         */
        public static final String UNREAD_COUNT = "un_read";
        /**
         * 是否自己发送
         */
        public static final String IS_ME_SEND = "is_mesend";

    }

    /**
     * 账户
     */
    public static final class AccounFields extends UserFields{
        /**
         * table name
         */
        public static final String TB_NAME = "tb_accoun";
        private AccounFields(){}
        /**
         * 昵称
         */
        public static final String MEMBER_NICKNAME = "member_nick_name";
        /**
         * 头像
         */
        public static final String MEMBER_AVATARURL = "avatarUrl";

        /**
         * 账户类型 未使用！！！
         */
        public static final String TYPE = "member_type";

    }

    /**
     * 订阅号(房间，广告，服务，内容等)
     */
    public static final class SubscriptionFields extends UserFields{

        public static final String TB_NAME = "tb_subscription";
        private SubscriptionFields(){}

        public static final String SUBSCRIPTION_ID = "subscription_id";
        public static final String TYPE = "subscription_type";
        public static final String NAME = "subscription_name";
        public static final String PICURL = "subscription_pic_url";
    }

}
