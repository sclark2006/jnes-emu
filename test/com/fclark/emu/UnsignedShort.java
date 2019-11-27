package com.fclark.emu;

public class UnsignedShort extends Number {
    private short value;

    private UnsignedShort(int value) {
        this.value = (short) value;
    }

    public void setValue(int value) {
        if(value > 65535 || value < 0) throw new IllegalArgumentException("The value should be between 0 and 65535");
        this.value = (byte)value;
    }
    @Override
    public int intValue() {
        return value & 0xFFFF;
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

    public static UnsignedShort valueOf(int value) {
        if(value > 65535 || value < 0) throw new IllegalArgumentException("The value should be between 0 and 65535");
        return new UnsignedShort(value);
    }

    public static UnsignedShort valueOf(short value) {
        return new UnsignedShort(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
