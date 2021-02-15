package com.ch.sms.simulator.sms;

public interface Configure {
	

	/**
	 * 自动回复状态报告
	 * @return
	 */
	boolean autoReport();
	
	/**
	 * 状态报告
	 * @return
	 */
	String report();
	
}
