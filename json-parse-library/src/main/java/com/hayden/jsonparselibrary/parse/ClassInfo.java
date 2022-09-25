package com.hayden.jsonparselibrary.parse;

import javassist.CtClass;

import java.util.Optional;

public interface ClassInfo<CLASS_C extends ClassConstruct<FIELD_C,CLASS>, FIELD_C extends FieldConstruct<CLASS_C,FIELD>, CLASS, FIELD> {
    CLASS_C clzz();

    Optional<Class<?>> arrClzz();

    public interface ClassInfoFactory<
            INFO extends ClassInfo<CLASS_C, FIELD_C, CLASS, FIELD>,
            CLASS_C extends ClassConstruct<FIELD_C,CLASS>,
            FIELD_C extends FieldConstruct<CLASS_C,FIELD>,
            CLASS, FIELD
            > {
        public INFO createClassInfo(CLASS_C classType, Optional<Class<?>> optionalClass);
    }

}
