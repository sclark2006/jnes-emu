package com.fclark.emu;

public class UnsignedByte extends Number {
    private byte value;

    private UnsignedByte(int value) {
        this.value = (byte) value;
    }

    @Override
    public int intValue() {
        return value & 0xFF;
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public float floatValue() {
        return intValue();
    }

    @Override
    public double doubleValue() {
        return intValue();
    }

    public byte byteValue() {
        return value;
    }

    public static UnsignedByte valueOf(int value) {
        if(value > 255 || value < 0) throw new IllegalArgumentException("The value should be between 0 and 255");
        return new UnsignedByte(value);
    }

    public static UnsignedByte valueOf(byte value) {
        return new UnsignedByte(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
