package com.ch.sms.simulator.sms.sgip;

import com.ch.sms.simulator.sms.AbstractConfigure;
import com.ch.sms.simulator.sms.SmsVersion;

public class SGIPConfigure extends AbstractConfigure {

	private String clientAccount;

	private String clientPassword;

	private String clientHost;

	private int clientPort;

	private long nodeId;

	@Override
	public SmsVersion getVersion() {
		return SmsVersion.SGIP;
	}

	public String getClientAccount() {
		return clientAccount;
	}

	public void setClientAccount(String clientAccount) {
		this.clientAccount = clientAccount;
	}

	public String getClientPassword() {
		return clientPassword;
	}

	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}

	public int getClientPort() {
		return clientPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

}
