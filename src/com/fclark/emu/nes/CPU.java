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

	Register A /*Accumulator*/ = new Register(8), X = new Register(8), Y = new Register(8),
			PC /*Program Counter*/= new Register(16), S /* Stack Pointer*/ = new Register(8) ;

	class StatusFlagsRegister extends Register {
		StatusFlagsRegister() {super(8);}
		@AllBits public byte C, Z, I, D, B,U, V, N;
	}
	StatusFlagsRegister P = new StatusFlagsRegister();

	enum AddressingMode {
		ABSOLUTE(3), ABSOLUTE_X(3), ABSOLUTE_Y(3), ACCUMULATOR(1), IMMEDIATE(2), IMPLIED(1),
		INDEXED_INDIRECT(2), INDIRECT(3), INDIRECT_INDEXED(2), RELATIVE(1),ZERO_PAGE(2),
		ZERO_PAGE_X(2), ZERO_PAGE_Y(2);
		byte bytes;
		AddressingMode(int bytes) { this.bytes = (byte)bytes; }
	}

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

	public void NMI(){}

	public void IRQ() {}
	
	@Override
	public void process() {
		System.out.print("\n ->cpu cycle\n");
		//TODO: check interrupt
		//TODO: Detect addressing mode
		int instruction = this.addressDecoder.readAt(PC.read());
		INSTRUCTIONS_MAP.get(instruction).accept(this);
		//PC.incrementBefore();
	}
	
	static {
		INSTRUCTIONS_MAP = new Hashtable<>();
		INSTRUCTIONS_MAP.put(0X69,(cpu) -> { });
	}
}
