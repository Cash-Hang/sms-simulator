package com.ch.sms.simulator.ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Text;

public class TextVerifyUtils {
	
	
	public static void ipTextVerify(VerifyEvent e) {
		Text text=(Text)	e.getSource();
		String ipText = text.getText();
          char c = e.character;
          if (c == '.') {
                String[] segments = ipText.split("//.");
                if (segments.length > 3) { 
                       int pIndex = ipText.indexOf('.', e.start);
                       text.setSelection(pIndex + 1, pIndex + 1);
                       e.doit = false;
                       return;
                }
         } else {
                if (e.start < ipText.length()) {
                       char currChar = ipText.charAt(e.start);
                       if (currChar == '.') {
                             // 按下"backspace"键时光标前移一位
                             if (c == '\b') {
                                    text.setSelection(e.start, e.start);
                                    e.doit = false;
                                    return;
                             }
  
                             // 按下"Delete"键时光标后移一位
                             if (c == 127) {
                                    text.setSelection(e.end, e.end);
                                    e.doit = false;
                                    return;
                             }
  
                             // 按下数字键时
                             if (Character.isDigit(c)) {
                                    int point = ipText.lastIndexOf('.', e.start - 1);
                                    if (e.start - point > 3) {
                                          text.setSelection(e.start + 1, e.start + 1);
                                          e.doit = false;
                                          return;
                                    }
                             }
                       }
                }
                if (Character.isDigit(c)) {
                       int fpoint = ipText.lastIndexOf('.', e.start - 1);
                       int bpoint = ipText.indexOf('.', e.start);
                       if ((bpoint > 0 && bpoint - fpoint > 3) ||
                                    (bpoint < 0 && ipText.length() - fpoint > 3)) {
                             e.doit = false;
                             return;
                       }
                } else if ((c != '\b') && (c != 127) && 
                             !(c == 0 && e.start == 0 && e.end == ipText.length())) { // 处理粘贴的问题
                       e.doit = false;
                       return;
                } 
         }
	}
	
	public static void numberTextVerify(VerifyEvent e) {
		Pattern pattern = Pattern.compile("[0-9]\\d*");
		Matcher matcher = pattern.matcher(e.text);
		if (matcher.matches()) { // 处理数字
			e.doit = true;
		} else if (e.text.length() > 0) { // 有字符情况,包含中文、空格
			e.doit = false;
		} else {
			// 控制键
			e.doit = true;
		}
	}

	 public static boolean isboolIp(String ipAddress) {
	        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"; 
	        Pattern pattern = Pattern.compile(ip);
	        Matcher matcher = pattern.matcher(ipAddress);
	        return matcher.matches();
	    }
}
