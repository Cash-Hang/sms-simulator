package com.ch.sms.simulator.sms.smgp;

import com.ch.sms.simulator.sms.AbstractConfigure;
import com.ch.sms.simulator.sms.SmsVersion;

public class SMGPConfigure extends AbstractConfigure {

	@Override
	public SmsVersion getVersion() {
		return SmsVersion.SMGP;
	}

}
