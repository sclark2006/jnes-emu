package com.fclark.emu;

public interface EmulationDevice {
	void powerOn();
	void powerOff();
	boolean isPowerOn();
}
