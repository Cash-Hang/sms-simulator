package com.ch.sms.simulator.sms;

import com.zx.sms.BaseMessage;
import com.zx.sms.handler.api.BusinessHandlerInterface;

public interface Controller {
	
	void start();
	
	void stop();
	
	void mo(BaseMessage message);
	
	boolean opening();
	
	void addBusinessHandler(BusinessHandlerInterface bhi);
}
