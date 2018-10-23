package com.fclark.emu.nes;

public class MemoryNotMappedException extends IndexOutOfBoundsException {

	public MemoryNotMappedException(int address) {
		super(address);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
