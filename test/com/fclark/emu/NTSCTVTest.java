package com.fclark.emu;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fclark.emu.devices.NTSCDevice;

class NTSCTVTest {

	@Test
	void tvCanPowerOn() {
		NTSCDevice tv = new NTSCDevice(false);
		tv.powerOn();
		assertTrue(tv.isPowerOn());
	}

}
