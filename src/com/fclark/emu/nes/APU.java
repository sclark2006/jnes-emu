package com.fclark.emu.nes;

import java.nio.ByteBuffer;
import java.util.Map;

import com.fclark.emu.ProcessingUnit;
import com.fclark.emu.devices.NESConsole;

public class APU implements ProcessingUnit {

	public static final long FREQUENCY_DIVIDER = NESConsole.CLOCK_MASTER_FREQUENCY_HZ / 60; // to get 60Hz 
	public static enum AudioChannel {
		PULSE1, PULSE2, TRIANGLE, NOISE, DMC
	}
	private static final int INITIAL_MEMORY_ADDRESS = 0x4000;
	private static final int CHANNEL_SIZE_IN_BYTES = 4;
	
	private Map<AudioChannel,ByteBuffer> channelsMap;
	long tickCounter = 0;
	
	public APU() {
		for(AudioChannel channel: AudioChannel.values()) {
			channelsMap.put(channel, ByteBuffer.allocateDirect(CHANNEL_SIZE_IN_BYTES));
		}
	}
	
	@Override
	public void onClockCycle() {
		tickCounter = ++tickCounter % FREQUENCY_DIVIDER;
		if(tickCounter == 0) {
			process();
		}
	}
	
	@Override
	public void process() {
		System.out.print("\n ->apu cycle\n");
	}

	@Override
	public void onInit() {
		System.out.print("Init the APU");
		int mappedAddress = INITIAL_MEMORY_ADDRESS;
		for(AudioChannel channel: AudioChannel.values()) {
			AddressMapper.map(mappedAddress, this.channelsMap.get(channel));
			mappedAddress+= CHANNEL_SIZE_IN_BYTES;
		}
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		
	}

}
