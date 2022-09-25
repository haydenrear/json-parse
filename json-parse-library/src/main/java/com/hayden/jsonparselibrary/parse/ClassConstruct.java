package com.hayden.jsonparselibrary.parse;

import java.io.IOException;
import java.security.ProtectionDomain;

public interface ClassConstruct<FIELD,CLASS> {

    void freeze();

    void writeFile() throws Exception;

    public interface ClassConstructAbstractFactory {
        public <FIELD_C extends FieldConstruct<CLASS_C,FIELD>,CLASS_C extends ClassConstruct<FIELD_C,CLASS>, FIELD,CLASS>
                ClassConstructFactory<FIELD_C,CLASS_C,FIELD,CLASS> create(Class<CLASS_C> clzz, Class<FIELD_C> field);
    }

    public interface ClassConstructFactory<FIELD_C extends FieldConstruct<CLASS_C,FIELD>,CLASS_C extends ClassConstruct<FIELD_C,CLASS>, FIELD,CLASS> {
        public CLASS_C createClassConstruct();

        CLASS_C makeClass(String name);

        public CLASS_C createClassConstruct(String name) throws NotFoundException;
        public FIELD_C createField(CLASS_C prev, String name, CLASS_C toAddTo) throws CannotCompileException;
    }

    ClassConstruct<FIELD,CLASS> makeNestedClass(String name, boolean isStatic);

    void writeFile(String directory) throws CannotCompileException, IOException;

    void addField(FIELD fieldConstruct) throws CannotCompileException;
    void setModifier(int modifier);
    void setModifiers(int modifier);
    void addInterface(ClassConstruct<FIELD,CLASS> classConstruct) throws NotFoundException;

    void prepare();

    Class<?> toClass() throws CannotCompileException;
    Class<?> toClass(ProtectionDomain protectionDomain, ClassLoader loader, Class<?> neighbor) throws CannotCompileException;
    Class<?> toClass(ClassLoader loader, ProtectionDomain protectionDomain)  throws CannotCompileException;
    Class<?> toClass(Class<?> neighbor) throws CannotCompileException;

    CLASS getMember();

    String getName();

}
