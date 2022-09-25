package com.hayden.jsonparselibrary.parse;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class CtClassConstructFactory implements ClassConstruct.ClassConstructFactory<CtFieldDelegate, CtClassDelegate, CtField, CtClass> {
    @Override
    public CtClassDelegate createClassConstruct() {
        return null;
    }

    @Override
    public CtClassDelegate makeClass(String name) {
        return new CtClassDelegate(ClassPool.getDefault().makeClass(name));
    }
    @Override
    public CtClassDelegate createClassConstruct(String name) throws com.hayden.jsonparselibrary.parse.NotFoundException {
        try {
            return new CtClassDelegate(ClassPool.getDefault().getCtClass(name));
        } catch (NotFoundException e) {
            throw new com.hayden.jsonparselibrary.parse.NotFoundException();
        }
    }

    @Override
    public CtFieldDelegate createField(CtClassDelegate prev, String name, CtClassDelegate toAddTo) throws com.hayden.jsonparselibrary.parse.CannotCompileException {
        try {
            return new CtFieldDelegate(new CtField(prev.thisCtClass, name, toAddTo.thisCtClass));
        } catch (CannotCompileException e) {
            throw new com.hayden.jsonparselibrary.parse.CannotCompileException(e.getReason());
        }
    }

}
