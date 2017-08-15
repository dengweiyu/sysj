package com.li.videoapplication.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能：MD5工具
 */
public class MD5Util {
	
    public static final String KEY = "youyi_app#cndatacom";//8字节的密钥

    public static String convert(String string){
		return string2MD5(KEY + "#" + string); 
	}
	
	/**
	 * 功能：MD5加码 生成32位md5码
	 * @param string
	 * @return
	 */
	public static String string2MD5(String string){
		MessageDigest md = null;
		try{
			md = MessageDigest.getInstance("MD5");
		}catch (Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = string.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md.digest(byteArray);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++){
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				sb.append("0");
			sb.append(Integer.toHexString(val));
		}
		return sb.toString();
	}


	public static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest;
		FileInputStream in;
		byte buffer[] = new byte[1024];
		int len;
		digest = MessageDigest.getInstance("MD5");
		in = new FileInputStream(file);
		while ((len = in.read(buffer, 0, 1024)) != -1) {
			digest.update(buffer, 0, len);
		}
		in.close();
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
}
