package com.ch.sms.simulator.sms;

import java.util.ArrayList;
import java.util.List;

import com.zx.sms.BaseMessage;
import com.zx.sms.common.util.ChannelUtil;
import com.zx.sms.connect.manager.EndpointConnector;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.connect.manager.ServerEndpoint;
import com.zx.sms.handler.api.BusinessHandlerInterface;

public abstract class AbstractController implements Controller {

	/**
	 * 
	 */
	private AbstractConfigure configure;
	
	final EndpointManager manager = EndpointManager.INS;

	protected volatile EndpointEntity server;
	
	private volatile boolean opening = false;
	
	protected List<BusinessHandlerInterface> serverhandlers = new ArrayList<>();
	
	public AbstractController(AbstractConfigure configure) {
		super();
		this.configure = configure;
		
	};
	
	public void start() {
		this.server=createServer(this.configure);
		manager.openEndpoint(this.server);
	
		opening=true;
	}
	
	public void stop() {
		if(this.server==null) {
			return;
		}
	
		manager.close(this.server);
		opening=false;
		
	}
	
	public boolean opening() {
		return this.opening;
	}
	
	

	public void mo(BaseMessage message) {
		if(!this.opening) {
			return;
		}
		
		if(server instanceof ServerEndpoint) {
			ServerEndpoint  serverEndpoint=(ServerEndpoint) server;
			sendMoMessage(serverEndpoint,message);
		}
	
	}
	
	public abstract void mo(String srcId,String destId,String content);

	protected void sendMoMessage(ServerEndpoint serverEndpoint, BaseMessage message) {
		EndpointEntity endpointEntity= getChild(serverEndpoint,configure); 
		EndpointConnector<EndpointEntity> connector=	endpointEntity.getSingletonConnector();
		if(connector.getConnectionNum()>0) {
			ChannelUtil.asyncWriteToEntity(endpointEntity, message);
		}
		
		
	}

	protected abstract EndpointEntity getChild(ServerEndpoint serverEndpoint,AbstractConfigure configure);

	protected abstract EndpointEntity createServer(AbstractConfigure configure2);
	
	public  void addBusinessHandler(BusinessHandlerInterface bhi) {
		serverhandlers.add(bhi);
	}
	
}
