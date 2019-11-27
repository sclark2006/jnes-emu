package com.fclark.emu.nes;

import java.lang.reflect.Field;

public class Register {

	private final int nbits;
	private final int maxValue;
	private int value;

	public Register(int nbits) {
		this.nbits = nbits;
		maxValue = (int) Math.pow(2, nbits) - 1;
	}

	public int size() {
		return nbits;
	}

	private int readFieldAsInt(Field field) {
		try {
			if(field.getType().equals(byte.class)) return field.getByte(this);
			if(field.getType().equals(boolean.class))  return field.getBoolean(this) ? 1:0;
			if(field.getType().equals(char.class))  return field.getChar (this);
			if(field.getType().equals(int.class)) return field.getInt(this);
			if(field.getType().equals(short.class)) return field.getShort(this);
			if(field.getType().equals(long.class)) return (int) field.getLong(this);
			if(field.getType().equals(float.class)) return (int) field.getFloat(this);
			if(field.getType().equals(double.class)) return (int) field.getDouble(this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		throw new NumberFormatException(field.getType().getName() + " is not a numeric type");
	}

	private void writeFieldAsInt(Field field, long value) {
		try {
			if(field.getType().equals(byte.class)) field.setByte(this, (byte) (value & 0xFF));
			else if(field.getType().equals(boolean.class)) field.setBoolean(this, value != 0);
			else if(field.getType().equals(char.class))  field.setChar (this, (char)value);
			else if(field.getType().equals(int.class))  field.setInt(this, (int) value);
			else if(field.getType().equals(short.class)) field.setShort(this, (short) value);
			else if(field.getType().equals(long.class))  field.setLong(this, value);
			else if(field.getType().equals(float.class)) field.setFloat(this, value);
			else if(field.getType().equals(double.class)) field.setDouble(this, value);
			else throw new NumberFormatException(field.getType().getName() + " is not a numeric type");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public int read() {
		var fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].isAnnotationPresent(Bit.class))
			{
				var bitNo = fields[i].getAnnotation(Bit.class).value();
				fields[i].setAccessible(true);
				value |= ( readFieldAsInt(fields[i]) << bitNo);
			}
		}
		return value;
	}

	public void write(int value) {
		if(value > maxValue || value < 0)  throw new IllegalArgumentException("The specified value should be between0 and "+ maxValue);
		this.value = value;

		var fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			boolean allBits = fields[i].isAnnotationPresent(AllBits.class);
			if(allBits || fields[i].isAnnotationPresent(Bit.class))
			{
				var bitNo = allBits ? i : fields[i].getAnnotation(Bit.class).value();
				fields[i].setAccessible(true);
				writeFieldAsInt(fields[i], (this.value >> bitNo) & 1);
			}
		}
	}

	public int inc(int value) {
		int newValue = this.read() + value;  this.write( newValue); return newValue;
	}
}
