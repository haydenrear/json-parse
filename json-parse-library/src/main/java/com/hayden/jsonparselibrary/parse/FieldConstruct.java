package com.hayden.jsonparselibrary.parse;

import java.lang.reflect.Modifier;

public interface FieldConstruct<CLASS, FIELD> {

    public FieldConstruct<CLASS,FIELD> createNewFrom(CLASS from, String fieldName, CLASS toAddTo) throws Exception;
    public void setModifier(int modifier);
    default void setModifiers(int modifier) {
        setModifier(modifier);
    }
    public FIELD getDelegate();

}
