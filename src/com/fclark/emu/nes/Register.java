package com.fclark.emu.nes;

import java.util.BitSet;

public class Register extends BitSet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int maxValue;
	private Register(int nbits){
		super(nbits);
		maxValue = (int) Math.pow(2, nbits);
	}
	
	
	private void increment(boolean positive) {
		for(int bit = this.length() - 1; bit >= 0; bit--) {
			boolean bitValue = this.get(bit);
			this.set(bit, !bitValue);
			if(positive ^ bitValue) break;
		}			
	}
	public int read() {
		int result = 0;

		byte[] bits = this.toByteArray();
		for(byte bit = 0; bit < bits.length; bit++) {			
			result = (bits[bit] | result << 1);
		}
		return result;
	}
	
	public void write(int value) {
		if(value > maxValue)
			throw new IndexOutOfBoundsException(value);
		
		char[] charBits = Integer.toBinaryString(value).toCharArray();
		for(byte bit = 0; bit < this.length(); bit++) {
			this.set(bit, charBits[bit] == '1');
		} 	
	}
	
	
	public int incrementAfter() {
		int result = read();
		increment(true);
		return result;
 	}
	
	public int incrementBefore() {
		increment(true);
		return read();
 	}
	
	public int decrementAfter() {
		int result = read();
		increment(false);
		return result;
 	}
	
	public int decrementBefore() {
		increment(false);
		return read();
 	}
	
	public static Register of8Bits() {
		return new Register(8);
	}
	
	public static Register of(int nBits) {
		return new Register(nBits);
	}
}
