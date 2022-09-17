package ru.job4j.shortcut.service.codegenerator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeGeneratorImplTest {

    CodeGenerator codeGenerator = new CodeGeneratorImpl();

    @Test
    void generateCode_shouldReturnAnyStringWith7Characters() {
        String random = codeGenerator.generateCode();
        assertEquals(7, random.length());
        Assertions.assertThat(random).doesNotContainAnyWhitespaces();
    }
}
