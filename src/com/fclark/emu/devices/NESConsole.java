package com.fclark.emu.devices;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fclark.emu.ConsoleDevice;
import com.fclark.emu.DisplayDevice;
import com.fclark.emu.GameMedia;
import com.fclark.emu.nes.APU;
import com.fclark.emu.nes.AddressMapper;
import com.fclark.emu.nes.CPU;
import com.fclark.emu.nes.Clock;
import com.fclark.emu.nes.PPU;
import com.fclark.emu.nes.io.Cartridge;

public class NESConsole implements ConsoleDevice {
	public static long CLOCK_MASTER_FREQUENCY_HZ = 21441960;

	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private boolean powerOn = false;
	private DisplayDevice display;
	private Clock clock;

	@Override
	public boolean isPowerOn() {
		return powerOn;
	}
	
	@Override
	public void powerOn() {
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
	public void onGameMediaInserted(GameMedia gameMedia) {
		gameMedia.load();
		ByteBuffer[] gameDataMap = gameMedia.getData();

		AddressMapper.map(0x4020, gameDataMap[Cartridge.EXPANSION_ROM]);  //8160 bytes
		AddressMapper.map(0x8000, gameDataMap[Cartridge.PRG_ROM]);  //32768  bytes
	}
}
