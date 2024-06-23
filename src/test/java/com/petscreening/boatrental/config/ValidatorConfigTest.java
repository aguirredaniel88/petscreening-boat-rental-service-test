package com.petscreening.boatrental.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ValidatorConfigTest {

    @Autowired
    private Validator validator;

    @Test
    public void testValidatorBeanPresence() {
        assertThat(validator).isNotNull();
    }
}
