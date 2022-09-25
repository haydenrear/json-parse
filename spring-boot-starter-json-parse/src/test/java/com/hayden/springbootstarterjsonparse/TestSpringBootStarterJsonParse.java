package com.hayden.springbootstarterjsonparse;

import com.hayden.jsonparselibrary.parse.DynamicParseJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = DynamicParseConfig.class)
@ExtendWith(SpringExtension.class)
public class TestSpringBootStarterJsonParse {

    @Autowired
    DynamicParseJson dynamicParseJson;

    @Test
    public void test() {
        assertThat(dynamicParseJson).isNotNull();
    }

}
