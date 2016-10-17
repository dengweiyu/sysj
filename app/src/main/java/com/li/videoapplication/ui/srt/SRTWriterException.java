
package com.li.videoapplication.ui.srt;
/**
 * 写入字幕异常
 */
public class SRTWriterException extends SRTException {

    private static final long serialVersionUID = 1L;

    public SRTWriterException(String message, Throwable cause) {
        super(message, cause);
    }

    public SRTWriterException(String message) {
        super(message);
    }

    public SRTWriterException(Throwable cause) {
        super(cause);
    }
}
