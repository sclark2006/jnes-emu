package com.fclark.emu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AnyTest {

    @Test
    void bigDecimalTest() {
        BigDecimal bd = new BigDecimal(new char[]{'2','5'});
        assertEquals(BigDecimal.valueOf(25), bd);
    }

    @Test
    void bigIntegerTest() {
        var bi = new BigInteger(new byte[]{25});
        assertEquals(BigInteger.valueOf(25), bi);
    }

    @Test
    public void charTest() {
        char charTest = (char) -1000;
        System.out.println(charTest);
        System.out.println((int) charTest);
    }
    @Test
    public void unsignedOperations() {
        System.out.println(Integer.numberOfTrailingZeros(25));
        System.out.println(Integer.lowestOneBit(-1));
        System.out.println(Integer.bitCount(20));

    }

    @Test
    void addressCompositionTest() {
        short address = (short) 0b1111111101010101;
        assertEquals(0b1111111101010101, address & 0xFFFF);

        byte value;

        value = (byte) 0b11110000;
        address = (short) ((address & 0xFF) | value<<8); //change hi byte
        assertEquals(0b1111000001010101,address & 0xFFFF);

        value = 0b00001111;
        address = (short) ((address & 0xFF00) | value); //change lo byte
        assertEquals(0b1111000000001111,address & 0xFFFF);


    }
}
