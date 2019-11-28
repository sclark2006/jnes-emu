package com.fclark.emu.nes;

import com.fclark.emu.MemoryNotMappedException;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AddressDecoder {
	private Set<AddressRange<?>> addressSet;
	private short nextWriteReadAddress;

	public AddressDecoder() {
		addressSet = new HashSet<>();
	}
	
	private AddressRange<?> decodeMemoryBuffer(int address) {
		Optional<AddressRange<?>> rangeFound = this.addressSet.stream().filter((range) -> range.getStartAddress() >= address && address <= range.getEndAddress()).findFirst();
		return rangeFound.isPresent() ? rangeFound.get() : null;
	}
	
	public ByteBuffer map(int startAddress, ByteBuffer byteBuffer) {
		this.addressSet.add(new AddressRange<ByteBuffer>(startAddress, 
						startAddress + byteBuffer.array().length - 1, 
						byteBuffer));
		return byteBuffer;
	}
	
	public byte[] map(int startAddress, byte[] byteBuffer) {
		this.addressSet.add(new AddressRange<byte[]>(startAddress, 
				startAddress + byteBuffer.length - 1, 
				byteBuffer));
		return byteBuffer;
	}
	
	public Register map(int startAddress, Register register) {
		this.addressSet.add(new AddressRange<Register>(startAddress, 
				startAddress + (int)(Math.ceil(register.size() / 8) - 1),
				register));
		return register;
	}


	public int readAt(int address) {
		AddressRange<?> memoryRange = decodeMemoryBuffer(address);
		if(memoryRange == null)
			throw new MemoryNotMappedException(address);
		 int result;
		 if(memoryRange.getByteData() instanceof Register) {
			result = ((Register)memoryRange.getByteData()).read();
		 } else if(memoryRange.getByteData() instanceof ByteBuffer) {
			result = ((ByteBuffer)memoryRange.getByteData()).getInt(memoryRange.getRelativeAddress(address));
		 }else {
			result = (int) ((byte[])memoryRange.getByteData())[memoryRange.getRelativeAddress(address)];
		 }
		 
		 return result;
	}

	public void writeAt(int address, int value) {
		AddressRange<?> memoryRange = decodeMemoryBuffer(address);
		if(memoryRange == null)
			throw new MemoryNotMappedException(address);
		int relativePos = memoryRange.getRelativeAddress(address);
		if(memoryRange.getByteData() instanceof Register) {
			((Register)memoryRange.getByteData()).write(value);
		 } else if(memoryRange.getByteData() instanceof ByteBuffer) {
			 
			 ((ByteBuffer)memoryRange.getByteData()).putInt(relativePos, value);
		 }else {
			((byte[])memoryRange.getByteData())[relativePos] = (byte) value;
		 }
	}
	
	public void writeRange(int startAddress, int endAddress, int value) {
		for(int index = startAddress; index <= endAddress; index++) {
			 writeAt(index, value);
		}		
	}

	public void setAddress16(short address) {
		this.nextWriteReadAddress = (short) (address & 0xFFFF);
	}
	public void setAddress8Lo(byte loByteAddress) {
		this.nextWriteReadAddress = (short) ((nextWriteReadAddress & 0xFF00) | loByteAddress); //change lo byte
	}
	public void setAddress8Hi(byte hiByteAddress) {
		this.nextWriteReadAddress = (short) ((nextWriteReadAddress & 0xFF) | hiByteAddress << 8); //change hi byte
	}
	public void write(byte value) {
		this.writeAt(this.nextWriteReadAddress & 0xFFFF, value & 0xFF);
	}

	public byte read() {
		return (byte) this.readAt(this.nextWriteReadAddress & 0xFFFF);
	}
	
	public static class AddressRange<T> {
		private final int startAddress;
		private final int endAddress;
		private final T byteData;
		
		public AddressRange(int startAddress, int endAddress, T byteData) {
			super();
			this.startAddress = startAddress;
			this.endAddress = endAddress;
			this.byteData = byteData;
		}
		public int getStartAddress() {
			return startAddress;
		}
		public int getEndAddress() {
			return endAddress;
		}
		public T getByteData() {
			return byteData;
		}
		public int getRelativeAddress(int address) {
			return address - startAddress;
		}
		
	}



}
