package com.ch.sms.simulator.ui.sms;

import java.util.UUID;

import com.ch.sms.simulator.ui.utils.TextVerifyUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sms.simulator.sms.smgp.SMGPConfigure;
import com.ch.sms.simulator.utils.ValidateUtils;

import swing2swt.layout.FlowLayout;

public class SMGPDialog extends Dialog {

	protected SMGPConfigure configure;
	protected Shell shlCmpp;
	private Text textHost;
	private Text textPort;
	private Text textName;
	private Text textAccount;
	private Text textPassword;
	private Label label_1;
	private Text textReport;
	private Label labelReport;
	private Composite composite;
	private Button btnSave;
	private Button btnCancel;
	private Button cbAutoReport;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SMGPDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public SMGPConfigure open() {
		createContents();
		shlCmpp.open();
		shlCmpp.layout();
		init();
		Display display = getParent().getDisplay();
		while (!shlCmpp.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return configure;
	}

	private void init() {
		if (configure != null) {
			this.textName.setText(configure.getName()!=null?configure.getName():StringUtils.EMPTY);
			this.textHost.setText(configure.getHost()!=null?configure.getHost():StringUtils.EMPTY);
			this.textPort.setText(String.valueOf(configure.getPort()));
			this.textAccount.setText(configure.getAccount()!=null?configure.getAccount():StringUtils.EMPTY);
			this.textPassword.setText(configure.getPassword()!=null?configure.getPassword():StringUtils.EMPTY);

			if (configure.autoReport()) {
				this.cbAutoReport.setSelection(true);
				this.textReport.setVisible(true);
				this.labelReport.setVisible(true);
			} else {
				this.textReport.setVisible(false);
				this.labelReport.setVisible(false);
			}

			
			this.textReport.setText(configure.getReport()!=null?configure.getReport():StringUtils.EMPTY);
		}
		
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlCmpp = new Shell(getParent(), SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
		shlCmpp.setText("SMGP");
		shlCmpp.setSize(1024, 600);
		shlCmpp.setImage(SWTResourceManager.getImage(SMGPDialog.class, "/images/sms.jfif"));
		
		GridLayout gl_shlCmpp = new GridLayout(4, false);
		gl_shlCmpp.verticalSpacing = 10;
		shlCmpp.setLayout(gl_shlCmpp);
		
		
		
		Label label = new Label(shlCmpp, SWT.NONE);
		label.setBounds(23, 24, 60, 24);
		label.setText("连接名称");
		
		textName = new Text(shlCmpp, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		textName.setBounds(130, 22, 418, 24);
		
		Label lblNewLabel = new Label(shlCmpp, SWT.NONE);
		lblNewLabel.setBounds(23, 68, 104, 24);
		lblNewLabel.setText("连接主机(Host)");
		
		textHost = new Text(shlCmpp, SWT.BORDER);
		textHost.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				TextVerifyUtils.ipTextVerify(e);
			}
		});
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textHost.setBounds(130, 66, 167, 24);
		textHost.setText("0.0.0.0");
		
		Label lblNewLabel_1 = new Label(shlCmpp, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel_1.setBounds(320, 68, 85, 24);
		lblNewLabel_1.setText("端口号(Port)");
		
		textPort = new Text(shlCmpp, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPort.setBounds(411, 66, 137, 24);
		textPort.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				TextVerifyUtils.numberTextVerify(e);
			}
		});
		
		Label lblaccount = new Label(shlCmpp, SWT.NONE);
		lblaccount.setBounds(23, 163, 99, 24);
		lblaccount.setText("账号(Account)");
		
		textAccount = new Text(shlCmpp, SWT.BORDER);
		textAccount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textAccount.setBounds(130, 161, 167, 24);
		
		Label lblpassword = new Label(shlCmpp, SWT.NONE);
		lblpassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblpassword.setBounds(320, 163, 30, 24);
		lblpassword.setText("密码");
		
		textPassword = new Text(shlCmpp, SWT.BORDER);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPassword.setBounds(411, 163, 137, 24);
		
		label_1 = new Label(shlCmpp, SWT.NONE);
		label_1.setBounds(23, 212, 60, 24);
		label_1.setText("状态报告");
		
		cbAutoReport = new Button(shlCmpp, SWT.CHECK);
		cbAutoReport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (cbAutoReport.getSelection()) {
					textReport.setVisible(true);
					labelReport.setVisible(true);
				} else {
					textReport.setVisible(false);
					labelReport.setVisible(false);
				}
			}
		});
		cbAutoReport.setBounds(130, 210, 83, 24);
		cbAutoReport.setText("自动回复");
		
		labelReport = new Label(shlCmpp, SWT.NONE);
		labelReport.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelReport.setBounds(320, 212, 60, 24);
		labelReport.setText("指定状态");
		labelReport.setVisible(false);
		
		textReport = new Text(shlCmpp, SWT.BORDER);
		textReport.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textReport.setBounds(411, 210, 137, 24);
		textReport.setVisible(false);
		
		
		new Label(shlCmpp, SWT.NONE);
		new Label(shlCmpp, SWT.NONE);
		new Label(shlCmpp, SWT.NONE);
		new Label(shlCmpp, SWT.NONE);
		
		composite = new Composite(shlCmpp, SWT.NONE);
		composite.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_composite.widthHint = 139;
		composite.setLayoutData(gd_composite);
		
		btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result=validateInput();
				if(!result) {
					return;
				}
				if (configure == null) {
					configure = new SMGPConfigure();
					configure.setId(UUID.randomUUID().toString());
				}
				fillConfigure(configure);
			
				shlCmpp.dispose();
			}
		});
		btnSave.setText("保存");
		
		btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				shlCmpp.dispose();
			}
		});
		btnCancel.setText("关闭");

	}

	protected void fillConfigure(SMGPConfigure configure2) {
		configure.setName(this.textName.getText());
		configure.setHost(this.textHost.getText());
		configure.setPort(Integer.parseInt(this.textPort.getText()));
		configure.setAccount(this.textAccount.getText());
		configure.setPassword(this.textPassword.getText());
	
		configure.setAutoReport(this.cbAutoReport.getSelection());
		configure.setReport(this.textReport.getText());
		
	}

	protected boolean validateInput() {
		String name=this.textName.getText();
		if(StringUtils.isBlank(name)) {
			MessageBox box=new  MessageBox(shlCmpp,SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
			box.setText("错误");
			box.setMessage("请输入连接名称");
			box.open();
			this.textName.setFocus();
			return false;
		}
		this.textName.setText(StringUtils.trim(name));
		
		String host=this.textHost.getText();
		if(StringUtils.isBlank(host) || !ValidateUtils.isIP(StringUtils.trim(host))) {
			MessageBox box=new  MessageBox(shlCmpp,SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
			box.setText("错误");
			box.setMessage("连接主机格式错误");
			box.open();
			this.textHost.setFocus();
			return false;
		}
		this.textHost.setText(StringUtils.trim(host));
		
		String port=this.textPort.getText();
		if(StringUtils.isBlank(port)) {
			MessageBox box=new  MessageBox(shlCmpp,SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
			box.setText("错误");
			box.setMessage("请输入主机端口");
			box.open();
			this.textPort.setFocus();
			return false;
		}
		this.textPort.setText(StringUtils.trim(port));
		
		String account=this.textAccount.getText();
		if(StringUtils.isBlank(account)) {
			MessageBox box=new  MessageBox(shlCmpp,SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
			box.setText("错误");
			box.setMessage("请输入连接账号");
			box.open();
			this.textAccount.setFocus();
			return false;
		}
		this.textAccount.setText(StringUtils.trim(account));
		
		String password=this.textPassword.getText();
		if(StringUtils.isBlank(password)) {
			MessageBox box=new  MessageBox(shlCmpp,SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
			box.setText("错误");
			box.setMessage("请输入连接账号密码");
			box.open();
			this.textPassword.setFocus();
			return false;
		}
		this.textPassword.setText(StringUtils.trim(password));
		
		
		
		if(this.cbAutoReport.getSelection()) {
			String report=this.textReport.getText();
			if(StringUtils.isBlank(report)) {
				MessageBox box=new  MessageBox(shlCmpp,SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
				box.setText("错误");
				box.setMessage("请输入自动回复状态报告");
				box.open();
				this.textReport.setFocus();
				return false;
			}
			this.textReport.setText(StringUtils.trim(report));
			
		}
		
		
		return true;
	}
	

}
