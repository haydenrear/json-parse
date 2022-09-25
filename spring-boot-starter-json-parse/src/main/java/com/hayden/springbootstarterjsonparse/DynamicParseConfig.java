package com.hayden.springbootstarterjsonparse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hayden.jsonparselibrary.decompile.Decompile;
import com.hayden.jsonparselibrary.decompile.DecompilePrinter;
import com.hayden.jsonparselibrary.decompile.LoadClass;
import com.hayden.jsonparselibrary.parse.DynamicParseJson;
import com.hayden.jsonparselibrary.parse.JavassistDynamicParseJson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DynamicParseJson.class)
public class DynamicParseConfig {

    @Bean
    @ConditionalOnMissingBean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    JavassistDynamicParseJson dynamicParseJson(ObjectMapper objectMapper) {
        return new JavassistDynamicParseJson(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    LoadClass loadClass() {
        return new LoadClass();
    }

    @Bean
    @ConditionalOnMissingBean
    DecompilePrinter decompilePrinter() {
        return new DecompilePrinter();
    }

    @Bean
    @ConditionalOnMissingBean
    Decompile decompile(LoadClass loadClass, DecompilePrinter decompilePrinter) {
        return new Decompile(decompilePrinter, loadClass);
    }

}
