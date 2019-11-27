package com.fclark.emu;

import com.fclark.emu.nes.Bit;
import com.fclark.emu.nes.Register;

public class JNesEmu {

    //    typedef uint_least32_t u32;
//    typedef uint_least16_t u16;
//    typedef uint_least8_t   u8;
//    typedef  int_least8_t   s8;
    static class IO {

        public static int JoyRead(int i) {
            return 0;
        }

        public static void JoyStrobe(int v) {
        }
    }

    static class GamePak {

        public static int Access(int addr, int v, boolean write) {
            return 0;
        }
    }

    static class PPU {
        static void tick() {
        }

        static int Access(int index, int v, boolean write)
        {
            return 0;
        }

    }

    static class APU {
        static void tick() {
        }

        public static int Read() {
            return 0;
        }

        public static void Write(int i, int v) {
        }
    }


    static class CPU {
        int RAM[] = new int[0x800];
        boolean reset = true, nmi = false, nmi_edge_detected = false, intr = false;

        //template<boolean write> u8 MemAccess(u16 addr, u8 v=0);
        int RB(int addr) {
            return MemAccess(0, addr);
        }

        int WB(int addr, int v) {
            return MemAccess(1,addr,v);
        }

        static void tick() {
            // PPU clock: 3 times the CPU rate
            for (var n = 0; n < 3; ++n) PPU.tick();
            // APU clock: 1 times the CPU rate
            for (var n = 0; n < 1; ++n) APU.tick();
        }

        //Read
        int MemAccess(int writeFlag, int addr) {
            return  MemAccess(writeFlag, addr, 0);
        }

        //Write
        int MemAccess(int writeFlag, int addr, int v) {
            boolean write = writeFlag == 1;
            // Memory writes are turned into reads while reset is being signalled
            if(reset && write) return MemAccess(0, addr);

            tick();
            // Map the memory from CPU's viewpoint.
            /**/ if(addr < 0x2000) { int r = RAM[addr & 0x7FF]; if(!write)return r; r=v; }
            else if (addr < 0x4000) return PPU.Access (addr & 7, v, write);
            else if (addr < 0x4018)
                switch (addr & 0x1F) {
                    case 0x14: // OAM DMA: Copy 256 bytes from RAM into PPU's sprite memory
                        if (write) for (int b = 0; b < 256; ++b) WB(0x2004, RB((v & 7) * 0x0100 + b));
                        return 0;
                    case 0x15:
                        if (!write) return APU.Read (); APU.Write(0x15, v);
                        break;
                    case 0x16:
                        if (!write) return IO.JoyRead (0); IO.JoyStrobe (v);
                        break;
                    case 0x17:
                        if (!write) return IO.JoyRead (1); // write:passthru
                    default:
                        if (!write) break;
                        APU.Write (addr & 0x1F, v);
                }
            else return GamePak.Access (addr, v, write);
            return 0;
        }

        // CPU registers:
        int PC=0xC000;
        int A=0,X=0,Y=0,S=0;
        /* Status flags: */
        Register P = new Register(8) {
            @Bit(0) byte C; // carry
            @Bit(1) byte Z; // zero
            @Bit(2) byte I; // interrupt enable/disable
            @Bit(3) byte D; // decimal mode (unsupported on NES, but flag exists)
            // 4,5 (0x10,0x20) don't exist
            @Bit(6) byte V; // overflow
            @Bit(7) byte N; // negative
        };

        int wrap(int oldaddr, int newaddr)  { return ((oldaddr & 0xFF00) + newaddr); }
        void Misfire(short old, short addr) { int q = wrap(old, addr); if(q != addr) RB(q); }
        int   Pop()        { return RB(0x100 | ++S); }
        void Push(int v)   { WB(0x100 | S--, v); }

    }
}