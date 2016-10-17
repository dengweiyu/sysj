
package com.li.videoapplication.ui.srt;

//
/**
 * 无效的无效格式异常
 */
public class InvalidSRTException extends SRTException {
    private static final long serialVersionUID = 1L;

    public InvalidSRTException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSRTException(String message) {
        super(message);
    }

    public InvalidSRTException(Throwable cause) {
        super(cause);
    }
}
