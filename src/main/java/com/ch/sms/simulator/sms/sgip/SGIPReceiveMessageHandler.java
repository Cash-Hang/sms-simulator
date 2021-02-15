package com.ch.sms.simulator.sms.sgip;

import com.ch.sms.simulator.sms.MessageCallBack;
import com.ch.sms.simulator.sms.ReceiveMessageHandler;
import com.ch.sms.simulator.sms.SmsControllerFactory;
import org.apache.commons.codec.binary.StringUtils;

import com.zx.sms.codec.sgip12.msg.SgipReportRequestMessage;
import com.zx.sms.codec.sgip12.msg.SgipSubmitRequestMessage;
import com.zx.sms.codec.sgip12.msg.SgipSubmitResponseMessage;
import com.zx.sms.session.cmpp.SessionState;

import io.netty.channel.ChannelHandlerContext;

public class SGIPReceiveMessageHandler extends ReceiveMessageHandler<SGIPConfigure> {

	private SGIPConfigure configure;

	private volatile static boolean connected = false;
	
	private static  Object lockObj=new Object();

	public SGIPReceiveMessageHandler(MessageCallBack callback, SGIPConfigure configure) {
		super(callback);
		this.configure = configure;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt == SessionState.Connect) {
			
			if (!connected) {
				
				synchronized (lockObj) {
					if (!connected) {
						SGIPController controller = (SGIPController) SmsControllerFactory.getInstance()
								.createSmsController(configure);
						controller.addClientHandler(new SGIPReceiveMessageHandler(this.getCallBack(), configure));
						controller.openClient();

						connected = true;
					}
				}
				
			}

		}

		ctx.fireUserEventTriggered(evt);
	}

	@Override
	protected void handleReceivedMessage(ChannelHandlerContext ctx, Object msg, SGIPConfigure configure) {

		if (msg instanceof SgipSubmitRequestMessage) {
			SgipSubmitRequestMessage submit = (SgipSubmitRequestMessage) msg;
			SgipSubmitResponseMessage resp = new SgipSubmitResponseMessage(submit.getHeader());
			resp.setTimestamp(submit.getTimestamp());
			resp.setResult((short) 0);

			ctx.channel().writeAndFlush(resp);

			if (configure.isAutoReport()) {

				SgipReportRequestMessage report = new SgipReportRequestMessage();
				report.setSequenceId(resp.getSequenceNumber());
				if (StringUtils.equals(configure.getReport(), "0")) {
					report.setState((short) 0);
				} else {
					report.setState((short) 2);
				}
				
				try {
					report.setState(Short.parseShort(configure.getReport()));
				}catch(Exception e) {
					report.setState((short)2);
				}
				
				SmsControllerFactory.getInstance().createSmsController(configure).mo(report);
			}

		}

	}

	@Override
	protected SGIPConfigure getConfigure() {
		return configure;
	}

}
