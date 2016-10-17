
package com.li.videoapplication.ui.srt;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 读取字幕
 */
public class SRTReader {
    
    public static SRTInfo read(File srtFile) throws InvalidSRTException, SRTReaderException {
        if (!srtFile.exists()) {
            throw new SRTReaderException(srtFile.getAbsolutePath() + " does not exist");
        }
        if (!srtFile.isFile()) {
            throw new SRTReaderException(srtFile.getAbsolutePath() + " is not a regular file");
        }
        
        SRTInfo srtInfo = new SRTInfo();
        // BufferedReader br = new BufferedReader(new FileReader(srtFile))
        // BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srtFile), "UTF-8"))
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srtFile), "UTF-8"))) {
            while (true) {
                srtInfo.add(parse(br));
            }
        } catch (EOFException e) {
            // Do nothing
        } catch (IOException e) {
            throw new SRTReaderException(e);
        }
        
        return srtInfo;
    }
    
    private static SRT parse(BufferedReader br) throws IOException, EOFException {
        String nString = br.readLine();
        if (nString == null) {
            throw new EOFException();
        }
        
        int subtitleNumber = -1;
        try {
            subtitleNumber = Integer.parseInt(nString);
        } catch (NumberFormatException e) {
            throw new InvalidSRTException(
                nString + " has an invalid subtitle number");
        }
        
        String tString = br.readLine();
        if (tString == null) {
            throw new InvalidSRTException(
                "Start time and end time information is not present");
        }
        String[] times = tString.split(SRTTimeFormat.TIME_DELIMITER);
        if (times.length != 2) {
            throw new InvalidSRTException(
                tString + " needs to be seperated with " + SRTTimeFormat.TIME_DELIMITER);
        }
        Date startTime = null;
        try {
            startTime = SRTTimeFormat.parse(times[0]);
        } catch (ParseException e) {
            throw new InvalidSRTException(
                times[0] + " has an invalid time format");
        }
        
        Date endTime = null;
        try {
            endTime = SRTTimeFormat.parse(times[1]);
        } catch (ParseException e) {
            throw new InvalidSRTException(
                times[1] + " has an invalid time format");
        }
        
        List<String> subtitleLines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                break;
            }
            subtitleLines.add(line);
        }
        
        if (subtitleLines.size() == 0) {
            throw new InvalidSRTException("Missing subtitle text information");
        }
        
        return new SRT(subtitleNumber, startTime, endTime, subtitleLines);
    }
}
