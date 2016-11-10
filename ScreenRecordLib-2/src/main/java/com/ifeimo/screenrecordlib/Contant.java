package com.ifeimo.screenrecordlib;

public class Contant {

    /**
     * 回调消息
     */
    public static class Msg {

        /**
         * 上下文为空
         */
        public static final int CONTEXT_IS_NULL = 3001;

        /**
         * 文件目录无效
         */
        public static final int PATH_IS_INVALID = 3002;

        /**
         * 应用未获取Root权限
         */
        public static final int ROOT_IS_INVALID = 3003;

        /**
         * 4.4以下录屏没有暂停
         */
        public static final int API44_DPWN_HAS_NOT_PAUSE = 3004;

        /**
         * 录屏状态，录屏中，暂停，已完成
         */
        public static final int STATE_RECORDING = 4001;
        public static final int STATE_PAUSE = 4002;
        public static final int STATE_COMPLATED = 4003;
        public static final int STATE_TIMEING = 4004;
    }
}
