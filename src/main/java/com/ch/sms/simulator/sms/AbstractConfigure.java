package com.ch.sms.simulator.sms;

public abstract class AbstractConfigure implements Configure {

	private String id;

	private String name;

	private String host;

	private int port;

	private String account;

	private String password;

	private boolean autoReport;

	private String report;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAutoReport() {
		return autoReport;
	}

	public void setAutoReport(boolean autoReport) {
		this.autoReport = autoReport;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	@Override
	public boolean autoReport() {
		return this.autoReport;
	}

	@Override
	public String report() {
		return this.report;
	}

	public abstract SmsVersion getVersion();

}
