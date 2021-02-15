package com.ch.sms.simulator.ui.sms;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sms.simulator.sms.AbstractConfigure;
import com.ch.sms.simulator.sms.AbstractController;
import com.ch.sms.simulator.sms.Controller;
import com.ch.sms.simulator.sms.MessageCallBack;
import com.ch.sms.simulator.sms.SmsControllerFactory;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

public class ChannelComp extends Composite implements MessageCallBack {
	private Text textSpNumber;
	private Text textMobile;
	private Text textMoContent;
	private Composite composite;
	private Button btnSendMo;
	private Text textLog;

	private AbstractConfigure configure;
	private Label lblNewLabel;
	private Text textChannelId;
	private Label lblNewLabel_1;
	private Text textChannelName;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ChannelComp(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("通道编号");

		textChannelId = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		textChannelId.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textChannelId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("通道名称");

		textChannelName = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		textChannelName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textChannelName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lbSpNumber = new Label(this, SWT.NONE);
		lbSpNumber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbSpNumber.setText("接入号");

		textSpNumber = new Text(this, SWT.BORDER);
		textSpNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lbMobile = new Label(this, SWT.NONE);
		lbMobile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbMobile.setText("手机号");

		textMobile = new Text(this, SWT.BORDER);
		textMobile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lbMoContent = new Label(this, SWT.NONE);
		lbMoContent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbMoContent.setText("上行内容");

		textMoContent = new Text(this, SWT.BORDER);
		GridData gd_textMoContent = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 2);
		gd_textMoContent.heightHint = 131;
		textMoContent.setLayoutData(gd_textMoContent);
		new Label(this, SWT.NONE);

		composite = new Composite(this, SWT.BORDER);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		btnSendMo = new Button(composite, SWT.NONE);
		btnSendMo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String srcId = textMobile.getText();
				String destId = textSpNumber.getText();
				String content = textMoContent.getText();
				
				if(StringUtils.length(content)>70) {
					MessageBox box=new  MessageBox(ChannelComp.this.getShell(),SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
					box.setText("错误");
					box.setMessage("上行内容超过70字了");
					box.open();
					return;
				}

				if (StringUtils.isNoneBlank(srcId) && StringUtils.isNoneBlank(destId)
						&& StringUtils.isNoneBlank(content)) {
					sendMoMessage(srcId, destId, content);
				}

			}
		});
		btnSendMo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnSendMo.setBounds(0, 0, 91, 25);
		btnSendMo.setText("发送");

		textLog = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textLog.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textLog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Menu menu = new Menu(textLog);
		textLog.setMenu(menu);
		
		MenuItem mntmClear = new MenuItem(menu, SWT.NONE);
		mntmClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textLog.setText("");
			}
		});
		mntmClear.setText("清空");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void onReceiveMessage(Object msg) {

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				textLog.append(msg.toString());
				textLog.append("\n");

			}
		});

	}

	private void sendMoMessage(String srcId, String destId, String content) {
		Controller controller = SmsControllerFactory.getInstance().createSmsController(configure);
		if (controller != null) {
			AbstractController absc = (AbstractController) controller;
			absc.mo(srcId, destId, content);
		}
	}

	public void setConfigure(AbstractConfigure configure) {
		this.configure = configure;
		if(this.configure!=null) {
			this.textChannelId.setText(configure.getId());
			this.textChannelName.setText(configure.getName());
		}
	}
}
