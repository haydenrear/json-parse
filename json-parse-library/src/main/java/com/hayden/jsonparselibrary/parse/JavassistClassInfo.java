package com.hayden.jsonparselibrary.parse;

import javassist.CtClass;
import javassist.CtField;

import java.util.Optional;


/**
 * Standard class for information about classes used to make classes
 */
public class JavassistClassInfo implements ClassInfo<CtClassDelegate, CtFieldDelegate, CtClass, CtField>{

    CtClassDelegate clzz;
    Optional<Class<?>> arrClzz;

    public JavassistClassInfo(
            CtClassDelegate clzz,
            Optional<Class<?>> arrClzz
    ) {
        this.clzz = clzz;
        this.arrClzz = arrClzz;
    }

    @Override
    public CtClassDelegate clzz() {
        return clzz;
    }

    @Override
    public Optional<Class<?>> arrClzz() {
        return arrClzz;
    }

}
