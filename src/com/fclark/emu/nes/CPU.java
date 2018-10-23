package com.fclark.emu.nes;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.Consumer;

import com.fclark.emu.ProcessingUnit;

/*6502 - Ricoh 2A03*/
public class CPU implements ProcessingUnit {
	private static final int FREQUENCY_DIVIDER = 12;
	private static Map<Integer, Consumer<CPU>> INSTRUCTIONS_MAP;	
	private final AddressDecoder addressDecoder;
	
	private Register A = Register.of8Bits(), 
			X = Register.of8Bits(), Y = Register.of8Bits(),
			PC = Register.of(16),
			S = Register.of8Bits(),
			P = Register.of8Bits(); //Status
	 

	int cycleCounter = 0;	
	
	public CPU(AddressDecoder addressMapper) {
		this.addressDecoder = addressMapper;
	}
	
	@Override
	public void onPowerUp() {
		System.out.println("Init the CPU");
		P.write(0x34);
		A.write(0x0);
		X.write(0x0);
		Y.write(0x0);
		S.write(0xFD);
		this.addressDecoder.writeAt(0x4017, 0x0); //frame irq enabled
		this.addressDecoder.writeAt(0x4015, 0x0); //all channels disabled
		this.addressDecoder.writeRange(0x4000, 0x400F, 0);
		this.addressDecoder.writeRange(0, 0x07FF, 0); 
	}
	

	@Override
	public void onReset() {
		System.out.println("CPU Reset");
		S.write(S.read() - 3);
		P.set(StatusFlag.INTERRUPT_DISABLE.getBit()); // P.or(new BitSet(0x4));
		this.addressDecoder.writeAt(0x4015, 0x0); //all channels disabled
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
		//TODO: check interrupt
		//TODO: Detect addressing mode
		int instruction = this.addressDecoder.readAt(PC.read());
		
		INSTRUCTIONS_MAP.get(instruction).accept(this);
		
		PC.incrementBefore();
	}
	


	public static enum StatusFlag {
		CARRY(0), ZERO(1), INTERRUPT_DISABLE(2), DECIMAL_MODE(3),
		BREAK(4), UNUSED(5), OVERFLOW(6), SIGN(7);
		
		private final int bit;

		private StatusFlag(int bit) {
			this.bit = bit;
		}
		
		public int getBit() {
			return bit;
		}
		
	}
	
	static {
		INSTRUCTIONS_MAP = new Hashtable<>();
		INSTRUCTIONS_MAP.put(0x0,(cpu) -> {});
	}
}
