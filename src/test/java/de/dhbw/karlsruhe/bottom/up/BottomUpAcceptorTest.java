package de.dhbw.karlsruhe.bottom.up;

import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.bottom.up.validation.BottomUpAcceptorValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class BottomUpAcceptorTest {

    @ParameterizedTest
    @ValueSource(strings = {"(())", "()()", "()(())"})
     void checkCorrectBottomUpAcceptorTest(String word) throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");

        BottomUpAcceptor bUAcceptor = new BottomUpAcceptor();
        BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
        assertTrue(bUAcceptorValidation.checkAcceptor(bUAcceptor, word));
    }


    @Test
     void checkWrongBottomUpAcceptorTest() throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");

        BottomUpAcceptor bUAcceptor = new BottomUpAcceptor();
        BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);

        assertFalse(bUAcceptorValidation.checkAcceptor(bUAcceptor, null));
    }


    private String getGrammarAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

}

