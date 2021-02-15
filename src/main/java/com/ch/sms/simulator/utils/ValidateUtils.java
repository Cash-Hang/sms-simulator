package com.ch.sms.simulator.utils;

import java.util.regex.Pattern;

public class ValidateUtils {
	
	public static boolean isIP(String str) {
		 
		// 匹配 1
		// String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
		// 匹配 2
		String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
 
		// 匹配1 和匹配2均可实现Ip判断的效果
		Pattern pattern = Pattern.compile(regex);
 
		return pattern.matcher(str).matches();
 
	}

}
