package com.hayden.jsonparselibrary.parse;

import javassist.CtField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Modifier;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CtFieldDelegate implements FieldConstruct<CtClassDelegate,CtField> {

    CtField ctField;


    @Override
    public FieldConstruct<CtClassDelegate,CtField> createNewFrom(CtClassDelegate from, String fieldName,
                                        CtClassDelegate toAddTo) throws Exception {
        return new CtFieldDelegate(new CtField(from.thisCtClass, fieldName, toAddTo.thisCtClass));
    }

    @Override
    public void setModifier(int modifier) {
        ctField.setModifiers(modifier);
    }

    @Override
    public CtField getDelegate() {
        return ctField;
    }
}
