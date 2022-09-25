package com.hayden.jsonparselibrary.parse;

import javassist.CtClass;
import javassist.CtField;

import java.util.Optional;

public class JavassistClassInfoFactory implements ClassInfo.ClassInfoFactory<JavassistClassInfo, CtClassDelegate, CtFieldDelegate, CtClass, CtField> {
    @Override
    public JavassistClassInfo createClassInfo(CtClassDelegate classType, Optional<Class<?>> optionalClass) {
        return new JavassistClassInfo(classType, optionalClass);
    }
}
