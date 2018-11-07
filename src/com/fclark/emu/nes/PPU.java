package com.fclark.emu.nes;

import java.io.PipedOutputStream;

import com.fclark.emu.ProcessingUnit;

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
	private AddressDecoder addressMapper;
	private PipedOutputStream videoOutputStream;

	

	/**
	 * This could be also implementes like this:
	 * Register[] registers = new Register[8]; or
	 * ByteBuffer registers = ByteBuffer.allocateDirect(8); or
	 * Map<PPURegister,Registers> registerMap
	 * @param addressMapper 
	 */

	public PPU(AddressDecoder addressMapper) {
		this.addressMapper = addressMapper;
		for(int regIndex = 0; regIndex < 8; regIndex++) {
			registers[regIndex] = Register.of8Bits();
		}
		videoOutputStream = new PipedOutputStream();
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
	public void onPowerUp() {
		int mappedAddress = INITIAL_MEMORY_ADDRESS;
		for(int mirror = 0; mirror < MIRROR_SIZE_BYTES; mirror++) {
			for(int regIndex = 0; regIndex < registers.length; regIndex++) {
				addressMapper.map(mappedAddress++, registers[regIndex]);
			}
		}		
		
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		
	}

	public PipedOutputStream getVideoOutputStream() {
		return this.videoOutputStream;
	}

}
