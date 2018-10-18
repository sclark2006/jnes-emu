package com.fclark.emu.devices;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fclark.emu.ConsoleDevice;
import com.fclark.emu.DisplayDevice;
import com.fclark.emu.nes.APU;
import com.fclark.emu.nes.AddressMapper;
import com.fclark.emu.nes.CPU;
import com.fclark.emu.nes.Clock;
import com.fclark.emu.nes.PPU;
import com.fclark.emu.nes.io.Memory;

public class NESConsole implements ConsoleDevice {
	public static long CLOCK_MASTER_FREQUENCY_HZ = 21441960;
	private static final int INITIAL_MEMORY_ADDRESS = 0x0000;
	private static final int MIRROR_SIZE_BYTES = 4;
	
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private boolean powerOn = false;
	private DisplayDevice display;
	private ByteBuffer ramMemory = ByteBuffer.allocateDirect(2048);
	private Clock clock;

	@Override
	public boolean isPowerOn() {
		return powerOn;
	}
	
	@Override
	public void powerOn() {
		mapMemory();
		clock = Clock.of(CLOCK_MASTER_FREQUENCY_HZ);
		clock.subscribe(new CPU());
		clock.subscribe(new PPU());
		clock.subscribe(new APU());
		executorService.execute(clock);
		powerOn = true;
		waitForPowerOff();
	}
	
	public void powerOff() {
		powerOn = false;
	}
	
	private void mapMemory() {
		int mirrorAddress = INITIAL_MEMORY_ADDRESS;
		int ramSize = ramMemory.array().length;
		for(int mirror = 0; mirror < MIRROR_SIZE_BYTES; mirror++) {
			AddressMapper.map(mirrorAddress, ramMemory); 
			mirrorAddress += ramSize;
		}
	}

	private void waitForPowerOff() {
		while(powerOn) {
			try {
				executorService.awaitTermination(5, TimeUnit.SECONDS);
				powerOn = !	executorService.isTerminated();
			} catch (InterruptedException e) {
				powerOn = false;
				executorService.shutdown();
			}
		}
		if(!executorService.isShutdown())
			executorService.shutdownNow();
	}

	@Override
	public void castToDisplay(DisplayDevice display) {
		this.display = display;
	}

	@Override
	public void onGameSelected() {
		// TODO Auto-generated method stub
		
	}
}
