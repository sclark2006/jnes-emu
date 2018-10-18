package com.fclark.emu.nes;

import java.nio.ByteBuffer;
import java.util.Map;

import com.fclark.emu.ProcessingUnit;
import com.fclark.emu.nes.APU.AudioChannel;

/**
 * Emulator for Ricoh 2C02	
 * @author sclark2006
 * @since Oct 11, 2018
 *
 */
public class PPU implements ProcessingUnit {
	public static final int FREQUENCY_DIVIDER = 4;
	public static enum PPURegisters {
		PPUCTRL, PPUMASK, PPUSTATUS, OAMADDR, 
		OAMDATA, PPUSCROLL, PPUADDR, PPUDATA
	}
	
	private static final int INITIAL_MEMORY_ADDRESS = 0x2000;
	private static final int MIRROR_SIZE_BYTES = 1024;

	private int tickCounter;
	private Register[] registers = new Register[PPURegisters.values().length];

	private Register OAMDMA = Register.ofOneByte();

	/**
	 * This could be also implementes like this:
	 * Register[] registers = new Register[8]; or
	 * ByteBuffer registers = ByteBuffer.allocateDirect(8); or
	 * Map<PPURegister,Registers> registerMap
	 */

	public PPU() {
		for(int regIndex = 0; regIndex < 8; regIndex++) {
			registers[regIndex] = Register.ofOneByte();
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
		System.out.print("\n ->ppu cycle\n");
		
	}

	@Override
	public void onInit() {
		int mappedAddress = INITIAL_MEMORY_ADDRESS;
		for(int mirror = 0; mirror < MIRROR_SIZE_BYTES; mirror++) {
			for(int regIndex = 0; regIndex < registers.length; regIndex++) {
				AddressMapper.map(mappedAddress++, registers[regIndex]);
			}
		}
		AddressMapper.map(0x4014, this.OAMDMA);
		
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		
	}

}
