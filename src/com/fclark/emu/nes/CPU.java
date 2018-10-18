package com.fclark.emu.nes;

import com.fclark.emu.ProcessingUnit;
import com.fclark.emu.nes.io.Memory;

/*6502 - Ricoh 2A03*/
public class CPU implements ProcessingUnit {
	private static final int FREQUENCY_DIVIDER = 12;

	
	int cycleCounter = 0;
	private Register A = Register.ofOneByte(), 
			X = Register.ofOneByte(), Y = Register.ofOneByte(),
			PC = Register.of(16),
			S = Register.ofOneByte(),
			P = Register.ofOneByte();
	
	private int[] instructions = {};
	
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

	private byte readMemoryAt(int address) {
		//AddressMapper.readAt(address)
		return 0;
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		
	}


}
