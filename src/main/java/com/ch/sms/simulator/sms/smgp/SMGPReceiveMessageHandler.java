package com.ch.sms.simulator.sms.smgp;

import com.ch.sms.simulator.sms.MessageCallBack;
import com.ch.sms.simulator.sms.ReceiveMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.zx.sms.codec.smgp.msg.SMGPDeliverMessage;
import com.zx.sms.codec.smgp.msg.SMGPReportData;
import com.zx.sms.codec.smgp.msg.SMGPSubmitMessage;
import com.zx.sms.codec.smgp.msg.SMGPSubmitRespMessage;
import com.zx.sms.common.util.CachedMillisecondClock;

import io.netty.channel.ChannelHandlerContext;

public class SMGPReceiveMessageHandler extends ReceiveMessageHandler<SMGPConfigure> {

	private SMGPConfigure configure;

	public SMGPReceiveMessageHandler(MessageCallBack callback, SMGPConfigure configure) {
		super(callback);
		this.configure = configure;
	}

	@Override
	protected void handleReceivedMessage(ChannelHandlerContext ctx, Object msg, SMGPConfigure configure) {

		if (msg instanceof SMGPSubmitMessage) {
			SMGPSubmitMessage submitMsg=(SMGPSubmitMessage)msg;
			SMGPSubmitRespMessage resp = new SMGPSubmitRespMessage();
			resp.setSequenceNo(submitMsg.getSequenceNo());
			resp.setStatus(0);
			ctx.channel().writeAndFlush(resp);
			
			if (configure.isAutoReport()) {
				SMGPDeliverMessage deliver = new SMGPDeliverMessage();
				deliver.setDestTermId(submitMsg.getSrcTermId());
				deliver.setMsgId(resp.getMsgId());
				deliver.setSrcTermId(submitMsg.getDestTermIdArray()[0]);
				deliver.setMsgFmt(submitMsg.getMsgFmt());
				
				SMGPReportData report  = new SMGPReportData();
				if(StringUtils.isNotBlank(configure.getReport()) && StringUtils.length(configure.getReport())<=7) {
					report.setStat(configure.getReport());
					if("DELIVRD".equals(configure.getReport())) {
						report.setErr("000");
					}else {
						report.setErr("001");
					}
				}else {
					report.setStat("DELIVRD");
					report.setErr("000");
				}
				
				String t = DateFormatUtils.format(CachedMillisecondClock.INS.now(), "yyMMddHHmm");
				report.setDoneTime(t);
				report.setSubTime(t);
				
				report.setMsgId(resp.getMsgId());
				report.setSub("001");
				report.setDlvrd("001");
				report.setTxt("");
				deliver.setReport(report);
				
				ctx.executor().submit(new Runnable() {
					public void run() {
						ctx.channel().writeAndFlush(deliver);
					}
				});
			}
			
			
		}

	}

	@Override
	protected SMGPConfigure getConfigure() {
		return configure;
	}

}
