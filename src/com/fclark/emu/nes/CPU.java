package com.fclark.emu.nes;

import java.nio.ByteBuffer;

import com.fclark.emu.ProcessingUnit;
import com.fclark.emu.nes.io.Memory;

/*6502 - Ricoh 2A03*/
public class CPU implements ProcessingUnit {
	private static final int FREQUENCY_DIVIDER = 12;
	private static final int RAM_START_ADDRESS = 0x0000;
	private static final int SRAM_START_ADDRESS = 0x6000;
	private static final int MIRROR_SIZE_BYTES = 4;
	private Register A = Register.ofOneByte(), 
			X = Register.ofOneByte(), Y = Register.ofOneByte(),
			PC = Register.of(16),
			S = Register.ofOneByte(),
			P = Register.ofOneByte();
	
	private ByteBuffer RAM = ByteBuffer.allocateDirect(2048);
	private ByteBuffer SRAM = ByteBuffer.allocateDirect(8192);
	private Register OAMDMA = Register.ofOneByte();
	private Register JOY1 = Register.ofOneByte();
	private Register JOY2 = Register.ofOneByte();
	
	
	int cycleCounter = 0;
	private int[] instructions = {};
	
	
	public CPU() {
		mapMemory();
	}
	
	@Override
	public void onInit() {
		System.out.println("Init the CPU");
		P.set(0x30);
		A.set(0x0);
		X.set(0x0);
		Y.set(0x0);
		S.set(0xFD);	
	}
	
	@Override
	public void onClockCycle() {
		cycleCounter = ++cycleCounter % FREQUENCY_DIVIDER;
		if(cycleCounter == 0) {
			process();
		}
	}
	
	@Override
	public void process() {
		System.out.print("\n ->cpu cycle\n");
		byte opcode = readMemoryAt(PC.increment());
		//decodeAndExecute(instructionsTable.get(opcode));
	}
	
	private void mapMemory() {
		int mirrorAddress = RAM_START_ADDRESS;
		int ramSize = RAM.array().length;
		for(int mirror = 0; mirror < MIRROR_SIZE_BYTES; mirror++) {
			AddressMapper.map(mirrorAddress, RAM); 
			mirrorAddress += ramSize;
		}
		AddressMapper.map(SRAM_START_ADDRESS, SRAM);
		AddressMapper.map(0x4014, this.OAMDMA);
		AddressMapper.map(0x4016, this.JOY1);
		AddressMapper.map(0x4017, this.JOY2);
		
	}

	private byte readMemoryAt(int address) {
		//AddressMapper.readAt(address)
		return 0;
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		
	}


}
