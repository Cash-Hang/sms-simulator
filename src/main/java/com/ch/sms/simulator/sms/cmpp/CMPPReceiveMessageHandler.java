package com.ch.sms.simulator.sms.cmpp;

import com.ch.sms.simulator.sms.MessageCallBack;
import com.ch.sms.simulator.sms.ReceiveMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.zx.sms.codec.cmpp.msg.CmppDeliverRequestMessage;
import com.zx.sms.codec.cmpp.msg.CmppReportRequestMessage;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.codec.cmpp.msg.CmppSubmitResponseMessage;
import com.zx.sms.common.util.CachedMillisecondClock;

import io.netty.channel.ChannelHandlerContext;

public class CMPPReceiveMessageHandler extends ReceiveMessageHandler<CMPPConfigure> {

	private CMPPConfigure configure;

	public CMPPReceiveMessageHandler(MessageCallBack callback, CMPPConfigure configure) {
		super(callback);
		this.configure = configure;
	}

	@Override
	protected void handleReceivedMessage(ChannelHandlerContext ctx, Object msg, CMPPConfigure configure) {

		if (msg instanceof CmppSubmitRequestMessage) {

			CmppSubmitRequestMessage e = (CmppSubmitRequestMessage) msg;
			CmppSubmitResponseMessage resp = new CmppSubmitResponseMessage(e.getHeader().getSequenceId());
			resp.setResult(0);

			ctx.channel().writeAndFlush(resp);

			if (configure.isAutoReport()) {

				final CmppDeliverRequestMessage deliver = new CmppDeliverRequestMessage();
				deliver.setDestId(e.getSrcId());
				deliver.setSrcterminalId(e.getDestterminalId()[0]);
				CmppReportRequestMessage report = new CmppReportRequestMessage();
				report.setDestterminalId(deliver.getSrcterminalId());
				report.setMsgId(resp.getMsgId());
				String t = DateFormatUtils.format(CachedMillisecondClock.INS.now(), "yyMMddHHmm");
				report.setSubmitTime(t);
				report.setDoneTime(t);

				if (StringUtils.isNotBlank(configure.getReport()) && StringUtils.length(configure.getReport()) <= 7) {
					report.setStat(configure.getReport());
				} else {
					report.setStat("DELIVRD");
				}

				report.setSmscSequence(0);
				deliver.setReportRequestMessage(report);
				ctx.executor().submit(new Runnable() {
					public void run() {
						ctx.channel().writeAndFlush(deliver);
					}
				});
			}

		}

	}

	@Override
	protected CMPPConfigure getConfigure() {
		return configure;
	}

}
