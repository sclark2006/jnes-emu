package com.fclark.emu;


import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UnionTest {
    public static class Union<T> {
        T obj;
        Union(Class<T> clazz) {
            try {
                this.obj = (T) clazz.getDeclaredConstructors()[0].newInstance();
            } catch(Exception e){
                e.printStackTrace();
            }
        }


        public T get() {
            int val = 0;
            var fields = obj.getClass().getDeclaredFields();
            try {
                for (int i = 0; i < fields.length; i++) {
                    val |= fields[i].getInt(obj);
                    System.out.println(fields[i].getInt(obj));
                }
                System.out.println("val="+val);
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setByte(obj, (byte) val);
                }

            }catch(IllegalAccessException i) {
                i.printStackTrace();
            }
            return (T) obj;
        }
    }

    static class PType {
        byte C, D;
    }

    @Test
    void objectAccessTest() {
        var P = new Union<PType>(PType.class);

        P.get().C = 4;
        assertEquals(4, P.get().C );
    }

    @Test
    void unionEqualizationTest() {
        var P = new Union<PType>(PType.class);

        P.get().C = 4;
        assertEquals(4, P.get().D );
    }

}
