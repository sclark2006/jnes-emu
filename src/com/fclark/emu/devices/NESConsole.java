package com.fclark.emu.devices;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fclark.emu.ConsoleDevice;
import com.fclark.emu.DisplayDevice;
import com.fclark.emu.GameMedia;
import com.fclark.emu.nes.APU;
import com.fclark.emu.nes.AddressDecoder;
import com.fclark.emu.nes.CPU;
import com.fclark.emu.nes.Clock;
import com.fclark.emu.nes.PPU;
import com.fclark.emu.nes.Register;
import com.fclark.emu.nes.io.Cartridge;

public class NESConsole implements ConsoleDevice {
	public static long CLOCK_MASTER_FREQUENCY_HZ = 21441960;
	

	private static final int MIRROR_SIZE_BYTES = 4;

	private static final int _2KB = 2048;
	private static final int _8KB = 8192;
	
	
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private boolean powerOn = false;
	private Clock clock;
	private AddressDecoder addressDecoder = new AddressDecoder();
	private CPU cpu;
	private PPU ppu;
	private APU apu;
	private byte[] RAM = new byte[_2KB];
	private byte[] SRAM = new byte[_8KB];

	private Register OAMDMA = Register.of8Bits(),
			JOY1 = Register.of8Bits(),
			JOY2 = Register.of8Bits();
	
	
	public NESConsole() {
		this.addressDecoder  = new AddressDecoder();
	}
	
	@Override
	public boolean isPowerOn() {
		return powerOn;
	}
	
	@Override
	public void powerOn() {
		mapMemory();
		cpu = new CPU(addressDecoder);
		ppu = new PPU(addressDecoder);
		apu = new APU(addressDecoder);
		clock = Clock.of(CLOCK_MASTER_FREQUENCY_HZ);
		clock.subscribe(cpu).subscribe(ppu).subscribe(apu);
		executorService.execute(clock);
		powerOn = true;
		waitForPowerOff();
	}
	
	public void powerOff() {
		powerOn = false;
	}
	
	private void mapMemory() {
		//Map RAM
		int mirrorAddress = 0x0000;
		for(int mirror = 0; mirror < MIRROR_SIZE_BYTES; mirror++) {
			addressDecoder.map(mirrorAddress, RAM); 
			mirrorAddress +=  RAM.length;
		}
		//Map SRAM
		addressDecoder.map(0x6000, SRAM);
		
		addressDecoder.map(0x4014, this.OAMDMA);
		addressDecoder.map(0x4016, this.JOY1);
		addressDecoder.map(0x4017, this.JOY2);
		
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
		if(display != null) {
			display.setVideoSource(ppu.getVideoOutputStream() );
		}
	}

	@Override
	public void onGameMediaInserted(GameMedia gameMedia) {
		gameMedia.load();
		ByteBuffer[] gameDataMap = gameMedia.getData();

		addressDecoder.map(0x4020, gameDataMap[Cartridge.EXPANSION_ROM]);  //8160 bytes
		addressDecoder.map(0x8000, gameDataMap[Cartridge.PRG_ROM]);  //32768  bytes
	}
}
