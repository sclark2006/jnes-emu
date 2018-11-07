package com.fclark.emu.devices;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.JFrame;

import com.fclark.emu.DisplayDevice;

public class NTSCDevice implements Runnable, DisplayDevice {

	public static float FPS = 29.97f;
	public static int TOTAL_SCANLINES = 525;
	public static int VISIBLE_SCANLINES = 480;
	public static int SCREEN_WIDTH = 640;
	public static int SCREEN_HEIGHT = VISIBLE_SCANLINES;

	private boolean powerOn = false;
	private boolean visible;
	JFrame tvFrame;
	private NTSCScreenPanel screen;
	private PipedInputStream videoInputStream = new PipedInputStream();
	

	public NTSCDevice() {
		this.visible = true;
	}

	public NTSCDevice(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void run() {
		
	}

	@Override
	public void powerOn() {
		powerOn = true;
		tvFrame = new JFrame("NTSC TV");
		initTvControls();
		startScreen();

		System.out.println("Power On the TV");
		run(); // This should be launched in a threaded way
	}

	private void initTvControls() {
		tvFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				powerOff();
			}
		});
	}

	@Override
	public void powerOff() {
		try {
			this.videoInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		powerOn = false;
		System.out.println("Power Off the TV");
		System.exit(0);
	}

	@Override
	public boolean isPowerOn() {
		return powerOn;
	}

	public void startScreen() {
	
		this.screen = new NTSCScreenPanel(SCREEN_WIDTH, SCREEN_HEIGHT,videoInputStream );		
		tvFrame.add(this.screen);
		tvFrame.pack();
		tvFrame.setLocationByPlatform(true);
		tvFrame.setVisible(visible);
	}

	@Override
	public void setVideoSource(PipedOutputStream videoSourceStream) {
		try {
			videoSourceStream.connect(this.videoInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public PipedInputStream getVideoInput() {
		return videoInputStream;
	}

}
