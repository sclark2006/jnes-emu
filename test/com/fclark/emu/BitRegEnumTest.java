package com.fclark.emu;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

public class BitRegEnumTest {

    public enum P {//With no BitSet
        C(0), Z(1), I(2), D(3), V(6), N(7);
        //static BitSet raw
        static int SIZE =  8;
        static int MAX_VALUE = (int) Math.pow(2, SIZE );
        private int pos;
        public char value;

        P(int bit) {
            this.pos = bit;
        }

        static void raw(int value) {
            if(value > MAX_VALUE)  throw new IllegalArgumentException("The specified value should not be greater than max value: "+ MAX_VALUE);
            char[] charBits = Integer.toBinaryString(value).toCharArray();
            for (P bit:values()) {
                bit.value = (char)Character.digit(charBits[SIZE - bit.pos - 1],10);
            }
        }
        static String toBinaryString() {
            char[] charBits = new char[SIZE];
            Arrays.fill(charBits,Character.forDigit(0, 10));
            for (P bit:values()) {
                charBits[SIZE - bit.pos - 1] = Character.forDigit(bit.value, 10);
            }
            return String.valueOf(charBits);
        }

        static int raw() {
            return Integer.valueOf(toBinaryString(),2);
        }
    }


    @Test
    void enumRegCanStoreABit_Test() {
        P.C.value = 1;
        assertEquals(1, P.C.value);
    }

    @Test
    void enumRegRaiseExceptionForLargerValues_Test() {
        assertThrows(IllegalArgumentException.class, () -> P.raw(512));
    }

    @Test
    void enumRegCanStoreData_Test() {
        P.raw(213);
        System.out.println("Read as String = " + P.toBinaryString());

        assertEquals(213 & 0b11001111, P.raw());
        assertEquals(0, P.Z.value );
    }
}
