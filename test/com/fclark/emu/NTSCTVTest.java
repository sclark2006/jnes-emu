package com.fclark.emu;

import com.fclark.emu.devices.NTSCDevice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NTSCTVTest {

	@Test
	void tvCanPowerOn() {
		NTSCDevice tv = new NTSCDevice(false);
		tv.powerOn();
		assertTrue(tv.isPowerOn());
	}

}
