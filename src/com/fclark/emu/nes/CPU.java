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

	Register A = new Register(8), X = new Register(8), Y = new Register(8),
			PC = new Register(16), S = new Register(8);

	class StatusFlagsRegister extends Register {
		StatusFlagsRegister() {super(8);}
		@AllBits public byte C, Z, I, D, UNUSED4,UNUSED5, V, N;
	}
	StatusFlagsRegister P = new StatusFlagsRegister();


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
		this.addressDecoder.writeRange(0, 0x07FF, 0); //internal memory
	}
	

	@Override
	public void onReset() {
		S.inc(-3);
		P.I = 1;
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
		PC.inc(1);
		INSTRUCTIONS_MAP.get(instruction).accept(this);
		
		//PC.incrementBefore();
	}
	
	static {
		INSTRUCTIONS_MAP = new Hashtable<>();
		INSTRUCTIONS_MAP.put(0x0,(cpu) -> {});
	}
}
