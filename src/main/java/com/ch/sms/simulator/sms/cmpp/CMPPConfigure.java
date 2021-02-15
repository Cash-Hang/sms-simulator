package com.ch.sms.simulator.sms.cmpp;

import com.ch.sms.simulator.sms.AbstractConfigure;
import com.ch.sms.simulator.sms.SmsVersion;

public class CMPPConfigure extends AbstractConfigure {

	private SmsVersion version;

	public void setVersion(SmsVersion version) {
		this.version = version;
	}

	@Override
	public SmsVersion getVersion() {
		return version;
	}

}
