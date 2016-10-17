
package com.li.videoapplication.ui.srt;

/**
 * 读取字幕异常
 */
public class SRTReaderException extends SRTException {

    private static final long serialVersionUID = 1L;

    public SRTReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SRTReaderException(String message) {
        super(message);
    }

    public SRTReaderException(Throwable cause) {
        super(cause);
    }
}
