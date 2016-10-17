
package com.li.videoapplication.ui.srt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 写入字幕
 */
public class SRTWriter {

    public static void write(File srtFile, SRTInfo srtInfo) throws SRTWriterException {
        // PrintWriter pw = new PrintWriter(srtFile)
        // PrintWriter pw = new PrintWriter(srtFile, "UTF-8")
        try (PrintWriter pw = new PrintWriter(srtFile, "UTF-8")) {
            for (SRT srt : srtInfo) {
                pw.println(srt.number);
                pw.println(
                        SRTTimeFormat.format(srt.startTime) +
                                SRTTimeFormat.TIME_DELIMITER +
                                SRTTimeFormat.format(srt.endTime));
                for (String text : srt.text) {
                    pw.println(text);
                }
                //空格
                pw.println();
            }
        } catch (IOException e) {
            throw new SRTWriterException(e);
        }
    }
}
