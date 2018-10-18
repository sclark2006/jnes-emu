package com.fclark.emu.devices;

import com.fclark.emu.DisplayDevice;

public class NTSCScreen implements Runnable, DisplayDevice {

	private boolean powerOn = false;

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void powerOn() {
		powerOn = true;
		System.out.println("Power On the TV");		
	}

	@Override
	public void powerOff() {
		powerOn = true;
		System.out.println("Power Off the TV");
	}

	@Override
	public boolean isPowerOn() {
		return powerOn;
	}

}
