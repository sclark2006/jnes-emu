package com.fclark.emu.nes;

public class Register {
	private int value;
	private int bitSize;
	
	private Register(int bitSize){
		this.bitSize = bitSize;
	}
	
	public void set(int value) {
		//TODO: Validate according size;
		this.value = value;
	}
	
	public int get() {
		return value;
	}
	
	public void setBit(int bit) {
		this.value |= 1 << bit;
	}
	
	public void clearBit(int bit) {
		this.value &= ~(1 << bit);
	}
	
	public int  increment() {
		return ++value;
	}
	
	public int decrement() {
		return --value;
	}

	public int getBitSize() {
		return bitSize;
	}
	
	public static Register ofOneByte() {
		return new Register(8);
	}
	
	public static Register of(int nBits) {
		return new Register(nBits);
	}
}
