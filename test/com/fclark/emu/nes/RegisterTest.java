package com.fclark.emu.nes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    @Test
    void registerCanStoreSize_Test() {
        var P = new Register(8);
        assertEquals(8, P.size());
    }

    @Test
    void registerCanWriteValue_Test() {
        var P = new Register(8);
        P.write(25);
        assertEquals(25, P.read());
    }

    @Test
    void registerRaiseExceptionWhenExceedMaxValue_Test() {
        assertThrows(IllegalArgumentException.class, () ->  new Register(8).write(512));
    }

    @Test
    void registerRaiseExceptionWhenLowerMinValue_Test() {
        assertThrows(IllegalArgumentException.class, () -> new Register(8).write(-5));
    }

    @Test
    void registerCanWriteReadFields_Test() {
        var P = new Register(8) {
            byte C;
        };
        P.C = 12;
        assertEquals(12, P.C);
    }

    @Test
    void registerNoAnnotatedFieldsDoesntAffectRegister_Test() {
        var P = new Register(8) {
            byte C;
        };
        P.C = 12;
        assertEquals(0, P.read());
    }


    @Test
    void registerAnnotatedFieldsAffectRegister_Test() {
        var P = new Register(8) {
            @Bit(0) byte C; // carry
            @Bit(1) byte Z; // zero
            @Bit(2) byte I; // interrupt enable/disable
            @Bit(3) byte D; // decimal mode (unsupported on NES, but flag exists)
            // 4,5 (0x10,0x20) don't exist
            @Bit(6) byte V; // overflow
            @Bit(7) byte N; // negative

        };
        P.C = 1; P.Z = 0; P.I = 1; P.D = 0;
        P.V = 1; P.N = 1;

        assertEquals(0b11000101, P.read());
    }

    @Test
    void registerValueAffectsAnnotatedFields_Test() {
        var P = new Register(8) {
            @Bit(0) byte C; // carry
            @Bit(1) byte Z; // zero
            @Bit(2) byte I; // interrupt enable/disable
            @Bit(3) byte D; // decimal mode (unsupported on NES, but flag exists)
            // 4,5 (0x10,0x20) don't exist
            @Bit(6) byte V; // overflow
            @Bit(7) byte N; // negative
        };
        P.write(213);
        assertEquals(0b11010101, P.read());
        assertEquals(1, P.C);
        assertEquals(0, P.Z);
        assertEquals(1, P.I);
        assertEquals(0, P.D);
        assertEquals(1, P.V);
        assertEquals(1, P.N);
    }

    @Test
    void registerSetAllBits_Test() {
         var  P = new  Register(8) {
             @AllBits byte C, Z, I, D, UNUSED4,UNUSED5, V, N;
        };
        P.write(213);
        assertEquals(0b11010101, P.read());
        assertEquals(1, P.C);
        assertEquals(0, P.Z);
        assertEquals(1, P.I);
        assertEquals(0, P.D);
        assertEquals(1, P.V);
        assertEquals(1, P.N);
    }
}
