package com.hayden.jsonparselibrary.parse;

import javassist.CtClass;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.security.ProtectionDomain;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtClassDelegate implements ClassConstruct<CtFieldDelegate, CtClass> {

    CtClass thisCtClass;


    @Override
    public void freeze() {
        thisCtClass.freeze();
    }

    @Override
    public ClassConstruct<CtFieldDelegate, CtClass> makeNestedClass(String name, boolean isStatic) {
        return new CtClassDelegate(thisCtClass.makeNestedClass(name, isStatic));
    }

    @Override
    public void writeFile(String directory) throws CannotCompileException, IOException {
        try {
            this.thisCtClass.writeFile(directory);
        } catch (javassist.CannotCompileException e) {
            throw new CannotCompileException(e.getReason());
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public void addField(CtFieldDelegate fieldConstruct) throws CannotCompileException {
        try {
            this.thisCtClass.addField(fieldConstruct.ctField);
        } catch (javassist.CannotCompileException e) {
            throw new CannotCompileException(e.getReason());
        }
    }

    @Override
    public void setModifier(int modifier) {
        this.thisCtClass.setModifiers(modifier);
    }

    @Override
    public void setModifiers(int modifier) {
        this.thisCtClass.setModifiers(modifier);
    }

    @Override
    public void addInterface(ClassConstruct<CtFieldDelegate, CtClass> classConstruct) {
        thisCtClass.addInterface(classConstruct.getMember());
    }

    @Override
    public void prepare() {
        if(this.thisCtClass.isFrozen())
            this.thisCtClass.defrost();
    }


    @Override
    public Class<?> toClass() throws CannotCompileException {
        try {
            return thisCtClass.toClass();
        } catch (javassist.CannotCompileException e) {
            throw new CannotCompileException(e.getReason());
        }
    }

    @Override
    public Class<?> toClass(ProtectionDomain protectionDomain, ClassLoader loader, Class<?> neighbor) throws CannotCompileException {
        try {
            return this.thisCtClass.toClass(loader, protectionDomain);
        } catch (javassist.CannotCompileException e) {
            throw new CannotCompileException(e.getReason());
        }
    }

    @Override
    public Class<?> toClass(ClassLoader loader, ProtectionDomain protectionDomain) throws CannotCompileException {
        try {
            return thisCtClass.toClass(loader, protectionDomain);
        } catch (javassist.CannotCompileException e) {
            throw new CannotCompileException(e.getReason());
        }
    }

    @Override
    public Class<?> toClass(Class<?> neighbor) throws CannotCompileException {
        try {
            return this.thisCtClass.toClass(neighbor);
        } catch (javassist.CannotCompileException e) {
            throw new CannotCompileException(e.getReason());
        }
    }

    @Override
    public CtClass getMember() {
        return thisCtClass;
    }

    @Override
    public String getName() {
        return thisCtClass.getName();
    }

    @Override
    public void writeFile() throws NotFoundException {
        try {
            this.thisCtClass.writeFile();
        } catch (NotFoundException | IOException | javassist.CannotCompileException e) {
            throw new NotFoundException(e.toString());
        }
    }
}
