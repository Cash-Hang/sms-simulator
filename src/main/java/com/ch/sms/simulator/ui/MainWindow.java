package com.ch.sms.simulator.ui;

import java.util.HashMap;
import java.util.Map;

import com.ch.sms.simulator.sms.AbstractConfigure;
import com.ch.sms.simulator.sms.Controller;
import com.ch.sms.simulator.sms.SmsControllerFactory;
import com.ch.sms.simulator.ui.sms.CMPPDialog;
import com.ch.sms.simulator.ui.sms.ChannelComp;
import com.ch.sms.simulator.ui.sms.SGIPDialog;
import com.ch.sms.simulator.ui.sms.SMGPDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ch.sms.simulator.sms.cmpp.CMPPConfigure;
import com.ch.sms.simulator.sms.cmpp.CMPPReceiveMessageHandler;
import com.ch.sms.simulator.sms.sgip.SGIPConfigure;
import com.ch.sms.simulator.sms.sgip.SGIPReceiveMessageHandler;
import com.ch.sms.simulator.sms.smgp.SMGPConfigure;
import com.ch.sms.simulator.sms.smgp.SMGPReceiveMessageHandler;

public class MainWindow {

	private static Map<String, AbstractConfigure> configMap = new HashMap<>();
	private static Map<String, ChannelComp> compMap = new HashMap<>();

	protected Shell shell;
	private Table table;
	private Composite compRight;

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.SHELL_TRIM);
		shell.setImage(SWTResourceManager.getImage(MainWindow.class, "/images/sms.jfif"));
		shell.setText("短信模拟器");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(shell, SWT.NONE);
		sashForm.setLayout(new FillLayout());

		Composite compLeft = new Composite(sashForm, SWT.NONE);
		compLeft.setLayout(new GridLayout(2, false));

		Text text = new Text(compLeft, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnNewButton = new Button(compLeft, SWT.NONE);
		btnNewButton.setToolTipText("新建通道");
		btnNewButton.setText("+");

		GridData gd_btnNewButton = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 50;
		gd_btnNewButton.heightHint = 50;
		btnNewButton.setLayoutData(gd_btnNewButton);

		Menu menu = new Menu(btnNewButton);
		MenuItem mtCmpp = new MenuItem(menu, SWT.NONE);
		mtCmpp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				CMPPDialog cmppDialog = new CMPPDialog(shell, SWT.DIALOG_TRIM);
				CMPPConfigure config = cmppDialog.open();
				if (config != null) {
					configMap.put(config.getId(), config);
					TableItem item = new TableItem(table, SWT.NONE);
					Controller controller = SmsControllerFactory.getInstance().createSmsController(config);
					ChannelComp cc = new ChannelComp(compRight, SWT.NONE);
					cc.setConfigure(config);
					compMap.put(config.getId(), cc);

					controller.addBusinessHandler(new CMPPReceiveMessageHandler(cc, config));
					controller.start();
					item.setText(new String[] { config.getId(), config.getName(), config.getVersion().name(), "启动" });

					table.deselectAll();
					table.select(table.getItemCount() - 1);

					StackLayout layout = (StackLayout) compRight.getLayout();
					layout.topControl = cc;
					compRight.layout();

				}

			}
		});
		mtCmpp.setText("CMPP");
		btnNewButton.setMenu(menu);

		MenuItem mtSMGP = new MenuItem(menu, SWT.NONE);
		mtSMGP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SMGPDialog smgpDialog = new SMGPDialog(shell, SWT.DIALOG_TRIM);
				SMGPConfigure config=	smgpDialog.open();
				
				if (config != null) {
					configMap.put(config.getId(), config);
					TableItem item = new TableItem(table, SWT.NONE);
					Controller controller = SmsControllerFactory.getInstance().createSmsController(config);
					ChannelComp cc = new ChannelComp(compRight, SWT.NONE);
					cc.setConfigure(config);
					compMap.put(config.getId(), cc);

					controller.addBusinessHandler(new SMGPReceiveMessageHandler(cc, config));
					controller.start();
					item.setText(new String[] { config.getId(), config.getName(), config.getVersion().name(), "启动" });

					table.deselectAll();
					table.select(table.getItemCount() - 1);

					StackLayout layout = (StackLayout) compRight.getLayout();
					layout.topControl = cc;
					compRight.layout();

				}
			}
		});
		mtSMGP.setText("SMGP");

		MenuItem mtSGIP = new MenuItem(menu, SWT.NONE);
		mtSGIP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SGIPDialog sgipDialog = new SGIPDialog(shell, SWT.DIALOG_TRIM);
				SGIPConfigure config=	sgipDialog.open();
				
				if (config != null) {
					configMap.put(config.getId(), config);
					TableItem item = new TableItem(table, SWT.NONE);
					Controller controller = SmsControllerFactory.getInstance().createSmsController(config);
					ChannelComp cc = new ChannelComp(compRight, SWT.NONE);
					cc.setConfigure(config);
					compMap.put(config.getId(), cc);

					controller.addBusinessHandler(new SGIPReceiveMessageHandler(cc, config));
					controller.start();
					item.setText(new String[] { config.getId(), config.getName(), config.getVersion().name(), "启动" });

					table.deselectAll();
					table.select(table.getItemCount() - 1);

					StackLayout layout = (StackLayout) compRight.getLayout();
					layout.topControl = cc;
					compRight.layout();

				}
				
			}
		});
		mtSGIP.setText("SGIP");

		table = new Table(compLeft, SWT.BORDER | SWT.FULL_SELECTION);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("编号");

		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(400);
		tblclmnName.setText("通道");

		TableColumn tblclmnVersion = new TableColumn(table, SWT.NONE);
		tblclmnVersion.setWidth(150);
		tblclmnVersion.setText("版本");

		TableColumn tblclmnStatus = new TableColumn(table, SWT.NONE);
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("状态");
		
		TableCursor tbc=new TableCursor(table,SWT.NONE);
		
		Menu menuTblRow = new Menu(tbc);
		tbc.setMenu(menuTblRow);
		
		MenuItem mntmDelChannel = new MenuItem(menuTblRow, SWT.NONE);
		mntmDelChannel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item=tbc.getRow();
				if(item==null) {
					return;
				}
				int idx=table.getSelectionIndex();
				
				String channelId=item.getText(0);
				table.remove(idx);
				
				AbstractConfigure configure=	configMap.remove(channelId);
				if(configure!=null) {
					Controller controller = SmsControllerFactory.getInstance().createSmsController(configure);
					if(controller!=null) {
						controller.stop();
					}
				}
				
				compMap.remove(channelId);
				StackLayout layout = (StackLayout) compRight.getLayout();
				layout.topControl = null;
				compRight.layout();
				
				
				
			}
		});
		mntmDelChannel.setText("删除通道");
		tbc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item=tbc.getRow();
				if(item==null) {
					return;
				}
				
				String id=item.getText(0);
				
				ChannelComp cc=compMap.get(id);
				
				if(cc!=null) {
					StackLayout layout = (StackLayout) compRight.getLayout();
					layout.topControl = cc;
					compRight.layout();
				}
				
			}
		});
		

		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Button btnNewButton = (Button) event.getSource();
				Rectangle bounds = btnNewButton.getBounds();
				Point point = btnNewButton.getParent().toDisplay(bounds.x, bounds.y + bounds.height);
				menu.setLocation(point);
				menu.setVisible(true);

			}
		});

		compRight = new Composite(sashForm, SWT.NONE);
		compRight.setLayout(new StackLayout());
		sashForm.setWeights(new int[] { 3, 7 });

	}
}
