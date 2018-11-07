package com.fclark.emu;

import java.io.IOException;
import java.io.PipedOutputStream;

import com.fclark.emu.devices.NTSCDevice;

class NTSCTVRunner {

	void run() {
		NTSCDevice tv = new NTSCDevice();
		PipedOutputStream videoSourceStream = new PipedOutputStream();
		tv.setVideoSource(videoSourceStream);
		tv.powerOn();
		
		try {
			this.getClass().getResourceAsStream("/mario.png").transferTo(videoSourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new NTSCTVRunner().run();
	}

}
