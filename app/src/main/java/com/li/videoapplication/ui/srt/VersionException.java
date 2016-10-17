
package com.li.videoapplication.ui.srt;

/**
 * 版本异常
 */
public class VersionException extends SRTException {
    private static final long serialVersionUID = 1L;


    public VersionException(String message, Throwable cause) {
        super(message, cause);
    }


    public VersionException(String message) {
        super(message);
    }

    public VersionException(Throwable cause) {
        super(cause);
    }
}
