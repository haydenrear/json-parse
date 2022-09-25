package com.hayden.jsonparselibrary.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.CtClass;
import javassist.CtField;

public class JavassistDynamicParseJson extends DynamicParseJson<JavassistClassInfo, CtFieldDelegate, CtClassDelegate, CtField, CtClass> {

    public JavassistDynamicParseJson(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    ClassConstruct.ClassConstructFactory<CtFieldDelegate, CtClassDelegate, CtField, CtClass> getConstructAbstractFactory() {
        return new CtClassConstructFactory();
    }

    @Override
    ClassInfo.ClassInfoFactory<JavassistClassInfo, CtClassDelegate, CtFieldDelegate, CtClass, CtField> getClassInfoFactory() {
        return new JavassistClassInfoFactory();
    }
}
