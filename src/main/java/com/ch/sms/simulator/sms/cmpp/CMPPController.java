package com.ch.sms.simulator.sms.cmpp;

import java.nio.charset.Charset;

import com.ch.sms.simulator.sms.AbstractConfigure;
import com.ch.sms.simulator.sms.AbstractController;
import com.zx.sms.codec.cmpp.msg.CmppDeliverRequestMessage;
import com.zx.sms.codec.cmpp.msg.DefaultHeader;
import com.zx.sms.codec.cmpp.msg.Header;
import com.zx.sms.common.util.MsgId;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointEntity.ChannelType;
import com.zx.sms.connect.manager.ServerEndpoint;
import com.zx.sms.connect.manager.cmpp.CMPPServerChildEndpointEntity;
import com.zx.sms.connect.manager.cmpp.CMPPServerEndpointEntity;

public class CMPPController extends AbstractController {

	public CMPPController(CMPPConfigure configure) {
		super(configure);
	
	}

	@Override
	protected EndpointEntity createServer(AbstractConfigure configure) {
		
		CMPPConfigure cmppConfigure=(CMPPConfigure)configure;
		
		CMPPServerEndpointEntity server = new CMPPServerEndpointEntity();
		server.setId(cmppConfigure.getId());
		server.setHost(cmppConfigure.getHost());
		server.setPort(cmppConfigure.getPort());
		server.setValid(true);
		server.setUseSSL(false);
		
		CMPPServerChildEndpointEntity child = new CMPPServerChildEndpointEntity();
		child.setId(cmppConfigure.getId()+"_child");
		child.setChartset(Charset.forName("utf-8"));
		child.setGroupName(cmppConfigure.getId()+"_child");
		child.setUserName(cmppConfigure.getAccount());
		child.setPassword(cmppConfigure.getPassword());

		child.setValid(true);
		child.setVersion(cmppConfigure.getVersion().getVersion());

		child.setMaxChannels((short) 20);
		child.setRetryWaitTimeSec((short) 30);
		child.setMaxRetryCnt((short) 1);
		
		child.setWriteLimit(200);
		child.setReadLimit(200);
		
		if (!this.serverhandlers.isEmpty()) {
			child.setBusinessHandlerSet(serverhandlers);
		}
		server.addchild(child);
		
		return server;
	}

	@Override
	public void mo(String srcId, String destId, String content) {
		Header header = new DefaultHeader();
		// 取时间，用来查看编码解码时间

		CmppDeliverRequestMessage msg = new CmppDeliverRequestMessage(header);
		msg.setDestId(destId);
		msg.setSrcterminalId(srcId);
		// 70个汉字
		msg.setMsgContent(content);
		msg.setMsgId(new MsgId());
		msg.setSrcterminalType((short) 1);
		header.setSequenceId((int)System.nanoTime());
		mo(msg);
		
	}

	@Override
	protected EndpointEntity getChild(ServerEndpoint serverEndpoint, AbstractConfigure configure) {
		return serverEndpoint.getChild(configure.getAccount(),ChannelType.DUPLEX);
	}

}
