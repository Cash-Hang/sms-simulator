package com.ch.sms.simulator.sms.sgip;

import java.util.ArrayList;
import java.util.List;

import com.ch.sms.simulator.sms.AbstractConfigure;
import com.ch.sms.simulator.sms.AbstractController;
import com.zx.sms.BaseMessage;
import com.zx.sms.codec.cmpp.msg.DefaultHeader;
import com.zx.sms.codec.cmpp.msg.Header;
import com.zx.sms.codec.sgip12.msg.SgipDeliverRequestMessage;
import com.zx.sms.common.util.ChannelUtil;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointEntity.ChannelType;
import com.zx.sms.connect.manager.EndpointEntity.SupportLongMessage;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.connect.manager.ServerEndpoint;
import com.zx.sms.connect.manager.sgip.SgipClientEndpointEntity;
import com.zx.sms.connect.manager.sgip.SgipServerChildEndpointEntity;
import com.zx.sms.connect.manager.sgip.SgipServerEndpointEntity;
import com.zx.sms.handler.api.BusinessHandlerInterface;

public class SGIPController extends AbstractController {

	private SGIPConfigure configure;

	protected List<BusinessHandlerInterface> clientHandlers = new ArrayList<>();

	public SGIPController(SGIPConfigure configure) {
		super(configure);
		this.configure = configure;
	}

	@Override
	protected EndpointEntity createServer(AbstractConfigure configure2) {
		SGIPConfigure configure = (SGIPConfigure) configure2;
		SgipServerEndpointEntity server = new SgipServerEndpointEntity();
		server.setId(configure.getId());
		server.setHost(configure.getHost());
		server.setPort(configure.getPort());
		server.setValid(true);
		// 使用ssl加密数据流
		server.setUseSSL(false);

		SgipServerChildEndpointEntity child = new SgipServerChildEndpointEntity();
		child.setId(configure.getId() + "_child");
		child.setLoginName(configure.getAccount());
		child.setLoginPassowrd(configure.getPassword());
		child.setNodeId(configure.getNodeId());
		child.setValid(true);
		child.setChannelType(ChannelType.DOWN);
		child.setMaxChannels((short) 3);
		child.setRetryWaitTimeSec((short) 30);
		child.setMaxRetryCnt((short) 1);
		child.setReSendFailMsg(false);
		child.setIdleTimeSec((short) 30);
		child.setWriteLimit(200);
		child.setReadLimit(200);
		child.setSupportLongmsg(SupportLongMessage.BOTH);

		if (!this.serverhandlers.isEmpty()) {
			child.setBusinessHandlerSet(serverhandlers);
		}
		server.addchild(child);

		return server;
	}
	


	public void openClient() {
		SgipClientEndpointEntity client = createClient();
		EndpointManager.INS.openEndpoint(client);
		EndpointManager.INS.startConnectionCheckTask();
	}

	private SgipClientEndpointEntity createClient() {
		SgipClientEndpointEntity client = new SgipClientEndpointEntity();
		client.setId(configure.getId() + "_client");
		client.setHost(configure.getClientHost());
		client.setPort(configure.getClientPort());
		client.setLoginName(configure.getClientAccount());
		client.setLoginPassowrd(configure.getClientPassword());
		client.setChannelType(ChannelType.UP);
		client.setNodeId(3073100002L);
		client.setMaxChannels((short) 1);
		client.setRetryWaitTimeSec((short) 100);
		client.setUseSSL(false);
		client.setReSendFailMsg(false);
		client.setIdleTimeSec((short) 120);
		client.setWriteLimit(200);
		client.setReadLimit(200);
		if (!clientHandlers.isEmpty()) {
			client.setBusinessHandlerSet(clientHandlers);
		}
		return client;
	}

	public void addClientHandler(BusinessHandlerInterface haneler) {
		clientHandlers.add(haneler);
	}

	@Override
	public void stop() {
		EndpointEntity endpointEntity = EndpointManager.INS.getEndpointEntity(configure.getId() + "_client");
		if (endpointEntity != null) {
			EndpointManager.INS.close(endpointEntity);
		}
		super.stop();
	}

	@Override
	protected void sendMoMessage(ServerEndpoint serverEndpoint, BaseMessage message) {
		EndpointEntity endpointEntity = EndpointManager.INS.getEndpointEntity(configure.getId() + "_client");
		if (endpointEntity != null) {
			ChannelUtil.asyncWriteToEntity(endpointEntity, message);
		}
	}

	@Override
	public void mo(String srcId, String destId, String content) {
		Header header=new DefaultHeader();
		
		
		SgipDeliverRequestMessage msg=new SgipDeliverRequestMessage(header);
		msg.setMsgContent(content);
		msg.setUsernumber(srcId);
		msg.setSpnumber(destId);
		
		mo(msg);
	}

	@Override
	protected EndpointEntity getChild(ServerEndpoint serverEndpoint, AbstractConfigure configure) {
		
		return null;
	}

}
