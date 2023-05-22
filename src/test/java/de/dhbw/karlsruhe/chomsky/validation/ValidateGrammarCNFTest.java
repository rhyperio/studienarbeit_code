package de.dhbw.karlsruhe.chomsky.validation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.services.GrammarService;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidateGrammarCNFTest {
    @Test
    public void grammarInCNF() throws FileNotFoundException {
        ValidateGrammarCNF validateGrammarCNF = new ValidateGrammarCNF();
        String grammarAsJson = getGrammarAsJson("src/test/resources/chomsky/grammarChomskyCorrect.json");

        GrammarService grammarService = new GrammarService(grammarAsJson);
        Grammar grammarToCheck = grammarService.getParsedGrammar();

        assertTrue(validateGrammarCNF.validateGrammarIsInCNF(grammarToCheck));
    }

    @Test
    public void grammarNotInCNF() throws FileNotFoundException {
        ValidateGrammarCNF validateGrammarCNF = new ValidateGrammarCNF();
        String grammarAsJson = getGrammarAsJson("src/test/resources/chomsky/grammarChomskyFalse.json");

        GrammarService grammarService = new GrammarService(grammarAsJson);
        Grammar grammarToCheck = grammarService.getParsedGrammar();

        assertFalse(validateGrammarCNF.validateGrammarIsInCNF(grammarToCheck));
    }

    private String getGrammarAsJson(String path) throws FileNotFoundException {
        return new Scanner(new File(path)).useDelimiter("\\Z").next();
    }
}
