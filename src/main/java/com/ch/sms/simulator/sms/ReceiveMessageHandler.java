package com.ch.sms.simulator.sms;

import com.zx.sms.BaseMessage;
import com.zx.sms.handler.api.AbstractBusinessHandler;

import io.netty.channel.ChannelHandlerContext;

public abstract class ReceiveMessageHandler<T extends Configure> extends AbstractBusinessHandler {
	
	private MessageCallBack callback;
	
	
	
	
	public ReceiveMessageHandler(MessageCallBack callback) {
		super();
		this.callback = callback;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		if(msg instanceof BaseMessage) {
			
			if(getCallBack()!=null) {
				getCallBack().onReceiveMessage(msg);
			}
			
		}
		
		handleReceivedMessage(ctx,msg,this.getConfigure());
		
	}
	
	protected abstract void handleReceivedMessage(ChannelHandlerContext ctx, Object msg, T configure);

	protected abstract T getConfigure();

	protected  MessageCallBack getCallBack() {
		return callback;
	}
	
	@Override
	public String name() {
		
		return this.getClass().getName();
	}

}
