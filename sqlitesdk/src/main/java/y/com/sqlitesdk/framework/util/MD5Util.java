package y.com.sqlitesdk.framework.util;

import java.security.MessageDigest;

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
	private static String string2MD5(String string){
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
}
