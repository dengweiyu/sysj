
package com.li.videoapplication.ui.srt;
/**
 * 字幕异常
 */
public class SRTException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SRTException(String message, Throwable cause) {
        super(message, cause);
    }

    public SRTException(String message) {
        super(message);
    }

    public SRTException(Throwable cause) {
        super(cause);
    }
}
