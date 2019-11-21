package com.fclark.emu;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

public class BitRegClassTest {

    public static class RegBit {

        public RegBit(int bitPos) {
        }
    }

//    union regtype // PPU register file
//
//    {
//        u32 value;
//        // Reg0 (write)             // Reg1 (write)             // Reg2 (read)
//        RegBit< 0, 8, u32 > sysctrl;
//        RegBit< 8, 8, u32 > dispctrl;
//        RegBit< 16, 8, u32 > status;
//        RegBit< 0, 2, u32 > BaseNTA;
//        RegBit< 8, 1, u32 > Grayscale;
//        RegBit< 21, 1, u32 > SPoverflow;
//        RegBit< 2, 1, u32 > Inc;
//        RegBit< 9, 1, u32 > ShowBG8;
//        RegBit< 22, 1, u32 > SP0hit;
//        RegBit< 3, 1, u32 > SPaddr;
//        RegBit< 10, 1, u32 > ShowSP8;
//        RegBit< 23, 1, u32 > InVBlank;
//        RegBit< 4, 1, u32 > BGaddr;
//        RegBit< 11, 1, u32 > ShowBG;    // Reg3 (write)
//        RegBit< 5, 1, u32 > SPsize;
//        RegBit< 12, 1, u32 > ShowSP;
//        RegBit< 24, 8, u32 > OAMaddr;
//        RegBit< 6, 1, u32 > SlaveFlag;
//        RegBit< 11, 2, u32 > ShowBGSP;
//        RegBit< 24, 2, u32 > OAMdata;
//        RegBit< 7, 1, u32 > NMIenabled;
//        RegBit< 13, 3, u32 > EmpRGB;
//        RegBit< 26, 6, u32 > OAMindex;
//    }
//
//    reg;

    public static class StatusFlag {
        BitSet raw = new BitSet(8);
        //            u8 raw;
        RegBit C = new RegBit(0),
                Z = new RegBit(1),
                I = new RegBit(2),
                D = new RegBit(3),
                V = new RegBit(6),
                N = new RegBit(7);
    }

    @Test
    void printFieldsTest() {
        var P = new StatusFlag();
        Arrays.stream(StatusFlag.class.getDeclaredFields()).forEach(field -> System.out.println(field.getName() + ": " + field.getType().getName()));
    }
}
