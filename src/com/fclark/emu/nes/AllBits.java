package com.fclark.emu.nes;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllBits { }
