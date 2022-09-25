package com.hayden.jsonparselibrary.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hayden.jsonparselibrary.dynamic.LookupClzz;
import org.apache.commons.lang3.ClassUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public abstract class DynamicParseJson<CLASS_INFO extends ClassInfo<CLASS_C, FIELD_C, CLASS, FIELD>, FIELD_C extends FieldConstruct<CLASS_C,FIELD>,CLASS_C extends ClassConstruct<FIELD_C,CLASS>, FIELD,CLASS> {

    ObjectMapper objectMapper;

    private static final LookupClzz neighbor = new LookupClzz();

    abstract ClassConstruct.ClassConstructFactory<FIELD_C,CLASS_C,FIELD,CLASS> getConstructAbstractFactory();
    abstract ClassInfo.ClassInfoFactory<CLASS_INFO, CLASS_C, FIELD_C, CLASS, FIELD> getClassInfoFactory();

    public DynamicParseJson(
            ObjectMapper objectMapper
    )
    {
        this.objectMapper = objectMapper;
    }

    public Optional<CLASS_INFO> dynamicParse(
            String data,
            String name,
            @Nullable CLASS_C parentClass,
            @Nullable String directoryName
    ) throws DynamicParsingException
    {

        CLASS_C newClass = Optional.ofNullable(parentClass).orElseGet(() -> getConstructAbstractFactory().makeClass(name));

        if(newClass == null)
            return Optional.empty();

        newClass.setModifiers(Modifier.PUBLIC);

        try {
            newClass.addInterface(getConstructAbstractFactory().createClassConstruct("java.io.Serializable"));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            var jsonParser = new JSONParser();
            Object parsed = jsonParser.parse(data);
            if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                dynamicParse(
                        obj,
                        newClass,
                        directoryName
                );
            } else if (parsed instanceof JSONArray) {
                JSONArray arr = (JSONArray) parsed;
                return Optional.of(dynamicParse(
                        arr,
                        newClass,
                        name + "List",
                        directoryName
                ));
            } else {
                throw new DynamicParsingException("Object parsed neither object nor array");
            }
            if (directoryName != null) {
                newClass.writeFile(directoryName);
            }
            return Optional.of(getClassInfoFactory().createClassInfo(
                    newClass,
                    Optional.empty()
            ));
        } catch (ParseException | CannotCompileException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        return Optional.empty();


    }

    public CLASS_INFO dynamicParse(
            JSONArray arr,
            CLASS_C newClass,
            String prev,
            @Nullable String directoryName
    ) throws DynamicParsingException
    {
        if (arr.size() == 0) {
            try {
                addFieldToCtClass(
                        newClass,
                        prev,
                        getConstructAbstractFactory().createClassConstruct(makeArray(
                                String.class,
                                1
                        ).getName())
                );
            } catch (NotFoundException e) {
                e.printStackTrace();
                System.out.println("unable to add field for value with no values");
            }
        } else {
            for (var toParse : arr) {
                if (ClassUtils.isPrimitiveOrWrapper(toParse.getClass()) || toParse instanceof String) {
                    try {
                        var ctArray = getConstructAbstractFactory().createClassConstruct(makeArray(
                                toParse.getClass(),
                                1
                        ).getName());
                        addFieldToCtClass(
                                newClass,
                                prev,
                                ctArray
                        );
                        break;
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (toParse instanceof JSONObject) {
                    JSONObject object = (JSONObject) toParse;
                    if (justAMap(arr)) {
                        try {
                            var mapArray = makeArray(
                                    Map.class,
                                    1
                            );
                            addFieldToCtClass(
                                    newClass,
                                    prev,
                                    getConstructAbstractFactory().createClassConstruct(mapArray.getName())
                            );
                            return getClassInfoFactory().createClassInfo(
                                    newClass,
                                    Optional.of(mapArray)
                            );
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        var clzz = innerDynamicAndCreateCtClass(
                                object,
                                newClass,
                                directoryName,
                                prev,
                                classInfo -> makeArray(
                                        classInfo,
                                        1
                                )
                        );
                        return getClassInfoFactory().createClassInfo(
                                newClass,
                                clzz
                        );
                    }
                } else if (toParse instanceof JSONArray) {
                    JSONArray innerArr = (JSONArray) toParse;
                    var primOrOpt = findIfPrimitive(
                            innerArr,
                            2
                    );
                    if (primOrOpt.isPresent()) {
                        var primOr = primOrOpt.get();
                        if (primOr.isPrim) {
                            try {
                                var arrCtClzz = getConstructAbstractFactory().createClassConstruct(makeArray(
                                        primOr.primType,
                                        primOr.depth
                                ).getName());
                                addFieldToCtClass(
                                        newClass,
                                        prev,
                                        arrCtClzz
                                );
                                return getClassInfoFactory().createClassInfo(
                                        newClass,
                                        Optional.of(primOr.primType())
                                );
                            } catch (NotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            return innerDynamicAndCreateCtClass(
                                    primOr.jo,
                                    newClass,
                                    directoryName,
                                    prev,
                                    classInfo -> makeArray(
                                            classInfo,
                                            primOr.depth
                                    )
                            ).flatMap(clzz -> Optional.of(getClassInfoFactory().createClassInfo(
                                    newClass,
                                    Optional.of(clzz)
                            ))).orElseThrow();
                        }
                        break;
                    } else throw new DynamicParsingException("Problem recursively finding depth of array ..");
                }
                break;
            }

        }
        return null;
    }

    public void dynamicParse(
            JSONObject obj,
            CLASS_C newClass,
            @Nullable String directoryName
    )
            throws DynamicParsingException
    {
        for (var toParse : obj.entrySet()) {
            if (toParse instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) toParse;
                if (entry.getKey() instanceof String) {
                    String key = (String) entry.getKey();
                    if (entry.getValue() == null) {
                        try {
                            var fieldToAdd = getConstructAbstractFactory().createClassConstruct(String.class.getName());
                            addFieldToCtClass(
                                    newClass,
                                    key,
                                    fieldToAdd
                            );
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e) {

                        }
                    } else if (ClassUtils.isPrimitiveOrWrapper(entry.getValue().getClass()) || entry.getValue() instanceof String) {
                        try {
                            var ctClass = getConstructAbstractFactory().createClassConstruct(entry.getValue().getClass().getName());
                            addFieldToCtClass(
                                    newClass,
                                    key,
                                    ctClass
                            );
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e) {

                        }
                    } else if (entry.getValue() instanceof JSONObject) {
                        JSONObject object = (JSONObject) entry.getValue();
                        innerDynamicAndCreateCtClass(
                                object,
                                newClass,
                                directoryName,
                                key,
                                classInfo -> {
                                    try {
                                        return classInfo.clzz().toClass(Thread.currentThread().getContextClassLoader(), neighbor.getClass().getProtectionDomain());
                                    } catch (CannotCompileException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                        );
                    } else if (entry.getValue() instanceof JSONArray) {
                        JSONArray arr = (JSONArray) entry.getValue();
                        dynamicParse(
                                arr,
                                newClass,
                                key,
                                directoryName
                        );
                    }
                }
            }
        }
    }

    private Optional<Class<?>> innerDynamicAndCreateCtClass(
            JSONObject object,
            CLASS_C newClass,
            String directoryName,
            String prev,
            Function<CLASS_INFO, Class<?>> createClzz
    ) throws DynamicParsingException
    {
        var innerDynamic = dynamicParse(
                object.toJSONString(),
                prev,
                (CLASS_C) newClass.makeNestedClass(
                        prev,
                        true
                ),
                directoryName
        );
        return innerDynamic.map(createClzz::apply)
                .map(clzz -> {
                    try {
                        addFieldToCtClass(
                                newClass,
                                prev,
                                getConstructAbstractFactory().createClassConstruct(clzz.getName())
                        );
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {

                    }
                    return clzz;
                })
                .map(clzz -> {
                    try {
                        objectMapper.registerSubtypes(
                                Class.forName(innerDynamic.get().clzz().getName()),
                                clzz
                        );
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return clzz;
                });
    }

    private Class<?> makeArray(
            CLASS_INFO innerDynamic,
            int depth
    )
    {
        Class<?> clzz = null;
        try {
            clzz = makeArray(
                    Class.forName(innerDynamic.clzz().getName()),
                    depth
            );
        } catch (ClassNotFoundException e) {
            try {
                clzz = makeArray(
                        innerDynamic.clzz().toClass(Thread.currentThread().getContextClassLoader(), neighbor.getClass().getProtectionDomain()),
                        depth
                );
            } catch (CannotCompileException ex) {
            }
        }
        return clzz;
    }

    private Class<?> makeArray(
            Class<?> clzz,
            int depth
    )
    {
        return depth == 1
                ? Array.newInstance(
                clzz,
                1
        ).getClass()
                : Array.newInstance(
                        clzz,
                        depth,
                        1
                ).getClass();
    }

    private boolean justAMap(JSONArray arr)
    {
        Set<String> prev = null;
        for (var a : arr) {
            if (a instanceof JSONObject) {
                JSONObject obj = (JSONObject) a;
                if(arr.size() == 1){
                    return false;
                }
                else if (obj.keySet().stream().findFirst().get() instanceof String) {
                    if (prev == null)
                        prev = obj.keySet();
                    else return !prev.containsAll((Set<String>) obj.keySet());
                }
            }
        }
        return true;
    }


    private void addFieldToCtClass(
            CLASS_C newClass,
            String key,
            CLASS_C ctClass
    )
    {
        try {
            newClass.prepare();
            FIELD_C field = getConstructAbstractFactory().createField(ctClass, key, newClass);
            field.setModifiers(Modifier.PUBLIC);
            newClass.addField(field);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public static final class PrimOrObj {
        private final boolean isPrim;
        private final int depth;
        private final JSONObject jo;
        private final Class<?> primType;

        public PrimOrObj(
                boolean isPrim,
                int depth,
                JSONObject jo,
                Class<?> primType
        )
        {
            this.isPrim = isPrim;
            this.depth = depth;
            this.jo = jo;
            this.primType = primType;
        }

        public boolean isPrim()
        {
            return isPrim;
        }

        public int depth()
        {
            return depth;
        }

        public JSONObject jo()
        {
            return jo;
        }

        public Class<?> primType()
        {
            return primType;
        }

    }

    public Optional<PrimOrObj> findIfPrimitive(
            JSONArray jsonArray,
            int depth
    )
    {
        for (var j : jsonArray) {
            if (ClassUtils.isPrimitiveOrWrapper(j.getClass()) || j instanceof String) {
                return Optional.of(new PrimOrObj(
                        true,
                        depth,
                        null,
                        j.getClass()
                ));
            } else if (j instanceof JSONArray) {
                JSONArray ja = (JSONArray) j;
                return findIfPrimitive(
                        ja,
                        depth + 1
                );
            } else if (j instanceof JSONObject) {
                JSONObject jo = (JSONObject) j;
                return Optional.of(new PrimOrObj(
                        false,
                        depth,
                        jo,
                        null
                ));
            }
        }
        return Optional.empty();
    }


}
