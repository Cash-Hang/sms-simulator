package com.ch.sms.simulator.sms;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public enum SmsVersion {
	CMPP20("1",(short)0x20,"CMPP20"),
	CMPP30("2",(short)0x30,"CMPP30"),
	SMGP("3",(short)0x13,"SMGP"),
	SGIP("4",(short)0x12,"SGIP"),
	SMPP("5",(short)0x34,"SMPP");
	private String code;
	
	private short version;
	
	private String name;
	
	private static Map<String,SmsVersion> enumMap=new HashMap<>();
	
	static {
		for(SmsVersion version:SmsVersion.values()) {
			enumMap.put(version.code, version);
		}
	}

	private SmsVersion(String code, short version, String name) {
		this.code = code;
		this.version = version;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public short getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}
	
	
	public static SmsVersion valueOfCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}
		return enumMap.get(code);
	}
	

	

	

}
