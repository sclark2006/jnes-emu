package com.fclark.emu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnsignedByteTest {
    @Test
    public void unsignedByteThrowsExceptionOnLargeValues() {
       assertThrows(IllegalArgumentException.class, () ->  UnsignedByte.valueOf(256));
    }

    @Test
    public void unsignedByteThrowsExceptionOnNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> UnsignedByte.valueOf(-5));
    }

    @Test
    public void unsignedByteFromIntToInt() {
        var uByte = UnsignedByte.valueOf(255);
        assertEquals(255, uByte.intValue());
    }

    @Test
    public void unsignedByteFromByteToInt() {
        var uByte = UnsignedByte.valueOf((byte)130);
        assertEquals(130, uByte.intValue());
    }

    @Test
    public void unsignedByteFromByteToByte() {
        var uByte = UnsignedByte.valueOf(130);
        assertEquals((byte)130, uByte.byteValue());
    }

    @Test
    public void unsignedByteSetValue() {
        var uByte = UnsignedByte.valueOf(3);
        assertEquals(3, uByte.byteValue());
        uByte.setValue(5);
        assertEquals(5, uByte.byteValue());
    }

    @Test
    public void unsignedByteToLong() {
        var uByte = UnsignedByte.valueOf(255);
        assertEquals(255L, uByte.longValue());
    }

    @Test
    public void unsignedByteToShort() {
        var uByte = UnsignedByte.valueOf(255);
        assertEquals(255, uByte.shortValue() );
    }

    @Test
    public void unsignedByteToDouble() {
        var uByte = UnsignedByte.valueOf(255);
        assertEquals(255.0d, uByte.doubleValue() );
    }

    @Test
    public void unsignedByteToFloat() {
        var uByte = UnsignedByte.valueOf(255);
        assertEquals(255.0F, uByte.floatValue() );
    }
}
