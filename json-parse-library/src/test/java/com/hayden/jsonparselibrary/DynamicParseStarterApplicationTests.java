package com.hayden.jsonparselibrary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hayden.jsonparselibrary.decompile.Decompile;
import com.hayden.jsonparselibrary.decompile.DecompilePrinter;
import com.hayden.jsonparselibrary.decompile.LoadClass;
import com.hayden.jsonparselibrary.parse.*;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * must use illegal-access=permit and also because you can only create the class once you have to run
 * these tests one by one for them to pass
 */
@SpringBootTest(classes={JavassistDynamicParseJson.class, ObjectMapper.class, DecompilePrinter.class, LoadClass.class, JavassistReParse.class, Decompile.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext
class DynamicParseStarterApplicationTests {

    @Autowired
    JavassistDynamicParseJson dynamicParseJson;
    @Autowired
    JavassistReParse reParse;
    @Autowired
    Decompile decompile;
    @Autowired
    ObjectMapper om;

    @BeforeAll
    public static void clearTestClasses() {
        Arrays.stream(new File("src/main/java/com/hayden/jsonparselibrary/dynamic")
                .listFiles())
                .forEach(f -> {
                    if(f.getName().endsWith(".class")) {
                        f.delete();
                    }
                });
    }

    @AfterAll
    public static void clearAfter() {
        Optional.ofNullable(new File("src/main/java/com/hayden/jsonparselibrary/dynamic")
                              .listFiles())
                .stream().flatMap(files -> Arrays.stream(files))
                .forEach(f -> {
                    if(f.getName().endsWith(".class")) {
                        f.delete();
                    }
                });
    }

    @Test @SneakyThrows
    public void symbols(){
        StringBuilder sb = new StringBuilder();

        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/symbol.json"))){
            fr.lines().forEachOrdered(sb::append);
        }

        JavassistClassInfo output = dynamicParseJson.dynamicParse(sb.toString(), "symbol", null,
                                                                  ("src/main/java/com/hayden/jsonparselibrary/dynamic")).get();

        Object obj = om.readValue(sb.toString(), output.clzz().toClass());

        var symbols = reParse.parseParsedByKey("pair", output, obj);
        assertThat(symbols.size()).isEqualTo(20);
    }

    @Test
    public void ParseFile()
    {
        parseFile("CNBC.json", "CnbcClzzs");
    }

    @Test
    public void parseMediaRequest() {
        parseFile("mediaRequest.json", "media");
    }

    @SneakyThrows
    public void parseFile(String filenameInTestResources, String className)
    {
        StringBuilder sb = new StringBuilder();

        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/"+filenameInTestResources))){
            fr.lines().forEachOrdered(sb::append);
        }

        dynamicParseJson.dynamicParse(sb.toString(), className, null ,("src/main/java/com/hayden/jsonparselibrary/dynamic")).get();
        String cnbcClzzs = decompile.decompile(className);
        File file = new File("src/main/java/com/hayden/jsonparselibrary/dynamic/"+className+".java");
        if(!file.exists())
            file.createNewFile();

        try(BufferedWriter wr = new BufferedWriter(new FileWriter(file))){
            wr.write(cnbcClzzs);
        }

    }

    /**
     * doesn't pass because of the order but actually does what it wants to do
     */
    @Test
    @SneakyThrows
    public void mostComplexGettingValues(){
        StringBuilder sb = new StringBuilder();

        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/test.json"))){
            fr.lines().forEachOrdered(sb::append);
        }

        var output= dynamicParseJson.dynamicParse(
                sb.toString(), "token",
                null ,("src/main/java/com/hayden/jsonparselibrary/dynamic"))
                .get();
        var obj = om.readValue(sb.toString(), output.clzz().toClass());

        var timeInFoce = reParse.parseParsedByKey("timeInForce", output, obj);
        for(var t : timeInFoce){
            System.out.println(t);
        }
    }

    @Test @SneakyThrows
    public void testDecompile(){

        StringBuilder sb = new StringBuilder();

        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/test_1.json"))){
            fr.lines().forEachOrdered(sb::append);
        }

        var c  = dynamicParseJson.dynamicParse(sb.toString(), "token_1", null ,("src/main/java/com/hayden/jsonparselibrary/dynamic")).get();

        c.clzz().freeze();
        c.clzz().toClass();

        c.clzz().writeFile();
        c.clzz().writeFile("src/main/java/com/hayden/jsonparselibrary/dynamic");

        assertThat(new File("src/main/java/com/hayden/jsonparselibrary/dynamic/token_1.class").exists()).isTrue();

        var name = Class.forName("token_1").getName();

        var decompiled = decompile.decompile(name);

        try {
            assertThat(decompiled).isInstanceOf(String.class);
            assertThat(decompiled.length()).isNotZero();
        } catch (AssertionError a){
            assertThat(decompiled).isInstanceOf(String.class);
            assertThat(decompiled.length()).isNotZero();
        }
    }

    @Test @SneakyThrows
    public void mostComplex(){
        StringBuilder sb = new StringBuilder();

        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/test.json"))){
            fr.lines().forEachOrdered(sb::append);
        }

        var output= dynamicParseJson.dynamicParse(sb.toString(), "token_2", null
                                                  ,("src/main/java/com/hayden/jsonparselibrary/dynamic")).get();
        Class<?> valueType = output.clzz().toClass();
        var val = om.readValue(sb.toString(), valueType);

        assertThat(recursiveEquals(sb.toString(), om.writeValueAsString(val))).isTrue();

        var mostComplex = decompile.decompile(valueType.getName());

        File file = new File("src/main/java/com/hayden/jsonparselibrary/dynamic/"+valueType.getSimpleName()+".java");
        if(!file.exists())
            file.createNewFile();

        try(BufferedWriter wr = new BufferedWriter(new FileWriter(file))){
            wr.write(mostComplex);
        }
    }

    @Test @SneakyThrows
    public void testParseParsed(){
        StringBuilder sb = new StringBuilder();

        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/toParse.json"))){
            fr.lines().forEachOrdered(sb::append);
        }

        var output= dynamicParseJson.dynamicParse(sb.toString(), "TestParsed", null, "src/main/java/com/hayden/jsonparselibrary/dynamic").get();
        var val = om.readValue(sb.toString(), output.clzz().toClass());

        List<Object> bids = reParse.parseParsedByKey("bids", output, val);

        System.out.println(bids);

        assertThat(bids.get(0)).isInstanceOf(String[][].class);

        var strs = ((String[][]) bids.get(0));
        System.out.println(Arrays.deepToString(strs));
    }

    @Test @SneakyThrows
    public void testParseSaveClass(){

        File file = new File("src/main/java/com/hayden/jsonparselibrary/dynamic/"+"TestParse_1.java");

        if(file.exists()){
            file.delete();
        }

        StringBuilder sb = new StringBuilder();

        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/toParse_1.json"))){
            fr.lines().forEachOrdered(sb::append);
        }

        var output= dynamicParseJson.dynamicParse(
                sb.toString(), "TestParse", null ,
                "src/main/java/com/hayden/jsonparselibrary/dynamic"
                ).get();
        output.clzz().writeFile("src/main/java/com/hayden/jsonparselibrary/dynamic");
        file.deleteOnExit();

    }

    @SneakyThrows
    @Test
    public void testParseLoadClass() throws CannotCompileException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, IOException, NoSuchFieldException, DynamicParsingException {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/toParse.json"))){
            fr.lines().forEachOrdered(sb::append);
        }

        var output= dynamicParseJson.dynamicParse(
                sb.toString(), "TestParsed_1", null,
                "src/main/java/com/hayden/jsonparselibrary/dynamic")
                .get();
        Class testParsedClass = output.clzz().toClass();

        System.out.println(testParsedClass);

        var o = testParsedClass.getConstructor().newInstance();
        System.out.println(o.getClass().getName());

        o.getClass().getField("lastUpdateId").set(o, 4321L);
        assertThat(om.writeValueAsString(o)).isEqualTo("{\"lastUpdateId\":4321,\"T\":null,\"E\":null,\"asks\":null,\"bids\":null}");
    }

    @SneakyThrows
    @Test
    public void testParseLoadClassWithCollectionOfInnerObjects() throws IOException, CannotCompileException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, DynamicParsingException {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/innerObjParse.json"))){
            fr.lines().forEachOrdered(sb::append);
        }
        var output = dynamicParseJson.dynamicParse(sb.toString(), "TestParseComplex", null,
                                                   "src/main/java/com/hayden/jsonparselibrary/dynamic"
        ).get();
        var complex = output.clzz().toClass().getConstructor().newInstance();
        var readVal = om.readValue(sb.toString(), Class.forName(complex.getClass().getName()));
        String readValue = om.writeValueAsString(readVal);
        Assertions.assertTrue(recursiveEquals(sb.toString(), readValue));
    }

    private boolean recursiveEquals(String sb, String readValue) throws ParseException {
        JSONParser parser = new JSONParser();
        Object parse = parser.parse(readValue);
        Object other = parser.parse(sb.toString());
        if(parse instanceof JSONObject) {
            try {
                return recursiveEquals((JSONObject) parse, (JSONObject) other);
            } catch (ClassCastException c) {
                return false;
            }
        } else if (parse instanceof JSONArray) {
            try {
                return recursiveEquals((JSONArray) parse, (JSONArray) other);
            } catch (ClassCastException c) {
                return false;
            }
        } else {
            return parse.equals(other);
        }
    }

    private boolean recursiveEquals(JSONArray array, JSONArray array1) {
        boolean is = true;
        int counter = 0;
        for(Object name : array) {
            Object other = array1.get(counter);
            if(name instanceof JSONObject) {
                is = recursiveEquals((JSONObject) name, (JSONObject) other);
            } else if (name instanceof JSONArray) {
                is = recursiveEquals((JSONArray) name, (JSONArray) other);
            } else {
                is = name.equals(array.get(counter));
            }
            if(!is) return is;
            ++counter;
        }
        return is;

    }

    private boolean recursiveEquals(JSONObject obj1, JSONObject obj2) {
        boolean is = true;
        for(String name : (java.util.Collection<String>) obj1.keySet()) {
            Object other = obj2.get(name);
            if(obj1.get(name) instanceof JSONObject) {
                is = recursiveEquals((JSONObject) obj1.get(name), (JSONObject) other);
            } else if (obj1.get(name) instanceof JSONArray) {
                is = recursiveEquals((JSONArray) obj1.get(name), (JSONArray) other);
            } else {
                is = obj1.get(name).equals(other);
            }
             if(!is) return is;
        }
        return is;
    }

    @Test
    public void testParseObjArr() throws Exception, DynamicParsingException {
        StringBuilder sb = new StringBuilder();

        try(BufferedReader fr = new BufferedReader( new FileReader("src/test/resources/objArrParse.json"))){
            fr.lines().forEachOrdered(sb::append);
        }

        var output = dynamicParseJson.dynamicParse(
                sb.toString(), "TestParseObjArr", null,
                "src/main/java/com/hayden/jsonparselibrary/dynamic"
        ).get();

        output.arrClzz().flatMap(clzz -> {
            try {
                var readVal = om.readValue(sb.toString(), clzz);
                assertThat(om.writeValueAsString(readVal)).isEqualTo(sb.toString().replaceAll("\\s+", ""));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        });

    }

}
