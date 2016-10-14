package com.li.videoapplication.data.qiniu.common;


public final class Config {
	
    public static final String VERSION = "7.0.4";

    public static final String UP_HOST = "upload.qiniu.com";

    public static final String UP_HOST_BACKUP = "up.qiniu.com";

    public static final String UP_IP_BACKUP = "183.136.139.16";

    public static final int CHUNK_SIZE = 256 * 1024;

    public static final int BLOCK_SIZE = 4 * 1024 * 1024;

    public static final int PUT_THRESHOLD = 512 * 1024;

    public static final int CONNECT_TIMEOUT = 10 * 1000;

    public static final int RESPONSE_TIMEOUT = 30 * 1000;

    public static final int RETRY_MAX = 5;

    public static final String UTF_8 = "utf-8";

    public static String defaultUpHost = UP_HOST;

}
