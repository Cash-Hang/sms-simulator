package com.ch.sms.simulator;

import com.ch.sms.simulator.ui.MainWindow;

public class Simulator {
	
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
