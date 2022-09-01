package ru.job4j.shortcut.service.codegenerator;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class CodeGeneratorImpl implements CodeGenerator {

    public static final int CHARACTERS_NUMBER = 7;

    @Override
    public String generateCode() {
        return RandomStringUtils.randomAlphanumeric(CHARACTERS_NUMBER);
    }
}
