package com.fclark.emu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.ParameterizedType;
import java.util.BitSet;

public class BitRegClassTest {

    public static class RegBitG<T extends Number> {
        BitSet bitSet;
        int bitNo, nBits;
        int maxValue;
        long value;
        Class<T> classType;

        protected void init(int bitNo, int nBits) {
            this.bitNo = bitNo;
            this.nBits = nBits;
            this.bitSet = new BitSet(nBits);
            this.maxValue = (int) Math.pow(2, nBits );
            classType = (Class<T>) this.getClass().getGenericSuperclass().getClass();
        }

        public static <T extends Number> RegBitG<T> of(int bitNo) {
            return of(bitNo,1);
        }
        public static <T extends Number> RegBitG<T> of(int bitNo, int nBits) {
            RegBitG<T> result = new RegBitG<>();
            result.init(bitNo, nBits);
            return result;
        }

        public void set(T value) {
            if(value.intValue() > maxValue)
                throw new IllegalArgumentException("The specified value should not be greater than : "+ maxValue);

            char[] charBits = Long.toBinaryString(value.longValue()).toCharArray();
            for(byte bit = 0; bit < this.bitSet.size(); bit++) {
                this.bitSet.set(bit, charBits[bit] == '1');
            }
        }

        public T get() {
            long result = 0;

            byte[] bits = this.bitSet.toByteArray();
            for(byte bit = 0; bit < bits.length; bit++) {
                result = (bits[bit] | result << 1);
            }
            //classType.getMethod("valueOf").
            return (T) Byte.valueOf((byte) result);
        }
    }

    public static class StatusFlag extends RegBitG<Byte>{
        StatusFlag() {
            init(0,8);
        }
        RegBitG<Byte> C = RegBitG.of(0),
                Z = RegBitG.of(1),
                I = RegBitG.of(2),
                D = RegBitG.of(3);

    }

    @Test
    void regBitCanStoreValue_Test() {
        StatusFlag P = new StatusFlag();
        P.set((byte) 25);
        System.out.println("Read as String = " + P );

        assertEquals(25, P.get().intValue());
        assertEquals(0, P.Z.value );
    }

}
