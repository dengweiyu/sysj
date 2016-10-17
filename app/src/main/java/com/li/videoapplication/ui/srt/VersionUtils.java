
package com.li.videoapplication.ui.srt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 版本工具
 */
public class VersionUtils {

    private static final String UPDATE_URL = "";
    
    public static String getLatestVersion() {
        BufferedReader br = null;
        try {
            URL url = new URL(UPDATE_URL);
            URLConnection conn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return br.readLine();
        } catch (MalformedURLException e) {
            throw new VersionException(e);
        } catch (IOException e) {
            throw new VersionException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new VersionException(e);
                }
            }
        }
    }
    
    /**
     * Compares the two versions.
     * 
     * @param version1 the version 1
     * @param version2 the version 2
     * @return 0 if both versions are the same
     *         -1 if version1 is less than version2
     *         1 if version1 is greater than version2
     */
    public static int compare(String version1, String version2) {
        String[] ver1 = version1.split("\\.");
        String[] ver2 = version2.split("\\.");
        if (ver1.length != 3 && ver2.length != 3) {
            throw new VersionException(
                "Version must be in the format of <major>.<minor>.<subminor>");
        }
        // compare the major versions
        int majorVer1 = Integer.parseInt(ver1[0]);
        int majorVer2 = Integer.parseInt(ver2[0]);
        if (majorVer1 < majorVer2) {
            return -1;
        } else if (majorVer1 > majorVer2) {
            return 1;
        } else {
            // compare the minor versions
            int minorVer1 = Integer.parseInt(ver1[1]);
            int minorVer2 = Integer.parseInt(ver2[1]);
            if (minorVer1 < minorVer2) {
                return -1;
            } else if (minorVer1 > minorVer2) {
                return 1;
            } else {
                // compare subminor versions
                int subminorVer1 = Integer.parseInt(ver1[2]);
                int subminorVer2 = Integer.parseInt(ver2[2]);
                if (subminorVer1 < subminorVer2) {
                    return -1;
                } else if (subminorVer1 > subminorVer2) {
                    return 1;
                } else { 
                    return 0;
                }
            }
        }
    }
}
