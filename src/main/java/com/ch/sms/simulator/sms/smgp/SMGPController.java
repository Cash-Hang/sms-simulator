package com.ch.sms.simulator.sms.smgp;

import com.ch.sms.simulator.sms.AbstractConfigure;
import com.ch.sms.simulator.sms.AbstractController;
import com.zx.sms.codec.smgp.msg.MsgId;
import com.zx.sms.codec.smgp.msg.SMGPDeliverMessage;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointEntity.ChannelType;
import com.zx.sms.connect.manager.EndpointEntity.SupportLongMessage;
import com.zx.sms.connect.manager.ServerEndpoint;
import com.zx.sms.connect.manager.smgp.SMGPServerChildEndpointEntity;
import com.zx.sms.connect.manager.smgp.SMGPServerEndpointEntity;

public class SMGPController extends AbstractController {

	public SMGPController(AbstractConfigure configure) {
		super(configure);
	}

	@Override
	protected EndpointEntity createServer(AbstractConfigure configure2) {
		SMGPConfigure configure = (SMGPConfigure) configure2;

		SMGPServerEndpointEntity server = new SMGPServerEndpointEntity();
		server.setId(configure.getId());
		server.setHost(configure.getHost());
		server.setPort(configure.getPort());
		server.setValid(true);
		// 使用ssl加密数据流
		server.setUseSSL(false);

		SMGPServerChildEndpointEntity child = new SMGPServerChildEndpointEntity();
		child.setId(configure.getId() + "_child");
		child.setClientID(configure.getAccount());
		child.setPassword(configure.getPassword());

		child.setValid(true);
		child.setChannelType(ChannelType.DUPLEX);
		child.setClientVersion((byte) configure.getVersion().getVersion());
		child.setMaxChannels((short) 3);
		child.setRetryWaitTimeSec((short) 30);
		child.setMaxRetryCnt((short) 1);
		child.setReSendFailMsg(false);
		child.setIdleTimeSec((short) 15);
		child.setSupportLongmsg(SupportLongMessage.SEND); // 接收长短信时不自动合并

		if (!this.serverhandlers.isEmpty()) {
			child.setBusinessHandlerSet(serverhandlers);
		}
		server.addchild(child);

		return server;
	}

	@Override
	public void mo(String srcId, String destId, String content) {
		SMGPDeliverMessage msg = new SMGPDeliverMessage();
		msg.setDestTermId(destId);
		
		msg.setMsgContent(content);
		msg.setMsgId(new MsgId());
		msg.setSrcTermId(srcId);
		mo(msg);
	}

	@Override
	protected EndpointEntity getChild(ServerEndpoint serverEndpoint, AbstractConfigure configure) {
		return serverEndpoint.getChild(configure.getAccount(),ChannelType.DUPLEX);
	}

}
