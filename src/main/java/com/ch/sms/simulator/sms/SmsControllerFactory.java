package com.ch.sms.simulator.sms;

import java.util.HashMap;
import java.util.Map;

import com.ch.sms.simulator.sms.cmpp.CMPPConfigure;
import com.ch.sms.simulator.sms.cmpp.CMPPController;
import com.ch.sms.simulator.sms.sgip.SGIPConfigure;
import com.ch.sms.simulator.sms.sgip.SGIPController;
import com.ch.sms.simulator.sms.smgp.SMGPConfigure;
import com.ch.sms.simulator.sms.smgp.SMGPController;

public class SmsControllerFactory {

	public static Map<String, Controller> controllerMap = new HashMap<>();

	private static class SmsControllerFactoryHolder {
		private static SmsControllerFactory factory = new SmsControllerFactory();
	}

	public static SmsControllerFactory getInstance() {
		return SmsControllerFactoryHolder.factory;
	}

	public Controller createSmsController(AbstractConfigure configure) {

		Controller controller = null;

		if (!controllerMap.containsKey(configure.getId())) {
			controller = newController(configure);
			controllerMap.put(configure.getId(), controller);

		} else {
			controller = controllerMap.get(configure.getId());
		}

		return controller;
	}

	private Controller newController(AbstractConfigure configure) {

		Controller controller ;
		switch (configure.getVersion()) {
		case CMPP30:
		case CMPP20:
			controller = new CMPPController((CMPPConfigure) configure);
			break;
		case SMGP:
			controller = new SMGPController((SMGPConfigure) configure);
			break;
		case SGIP:
			controller = new SGIPController((SGIPConfigure) configure);
			break;
		default:
			controller = null;
		}

		return controller;
	}

}
