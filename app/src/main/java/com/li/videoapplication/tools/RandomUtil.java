package com.li.videoapplication.tools;

import java.util.Random;

public class RandomUtil {
	
	/**
	 * 功能：要生成在[minView,maxView]之间的随机整数
	 */
	public static int getRandom(int min, int max) throws Exception {
		Random random = new Random();
		if (max <= 0){
			return 0;
		}
		int s = random.nextInt(max) % (max - min + 1) + min;
		System.out.println(s);
		return s;
	}
}

