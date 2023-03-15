package de.dhbw.karlsruhe.top.down.parsing;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;
import de.dhbw.karlsruhe.top.down.parsing.validation.TopDownAcceptorValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TopDownAcceptorValidationTest {

    @Test
    public void correctTopDownParsingTest() throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson("src/test/resources/derivation_tree/grammarCorrect1.json");
        Gson gson = new Gson();

        try (Reader reader = new FileReader("src/test/resources/top_down_parsing/TopDownCorrect.json")) {
            TopDownAcceptor tDAcceptor = gson.fromJson(reader, TopDownAcceptor.class);
            TopDownAcceptorValidation tDAcceptorValidation = new TopDownAcceptorValidation(grammarAsJson);
            String word = "()()";
            assertTrue(tDAcceptorValidation.validateTopDownAcceptor(tDAcceptor, word));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkNullWordTopDownAcceptorValidationTest() throws IOException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");

        Gson gson = new Gson();

        try (Reader reader = new FileReader(
                "src/test/resources/top_down_parsing/TopDownCorrect.json")) {
            TopDownAcceptor tDAcceptor = gson.fromJson(reader, TopDownAcceptor.class);
            TopDownAcceptorValidation tDAcceptorValidation = new TopDownAcceptorValidation(grammarAsJson);
            assertFalse(tDAcceptorValidation.validateTopDownAcceptor(tDAcceptor, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkNullTopDownAcceptorValidationTest() throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");

        TopDownAcceptorValidation tDAcceptorValidation = new TopDownAcceptorValidation(grammarAsJson);
        String word = "(())";
        assertFalse(tDAcceptorValidation.validateTopDownAcceptor(null, word));
    }

    @ParameterizedTest
    @ValueSource(strings = {"src/test/resources/top_down_parsing/TopDownWrongFirstStep.json",
            "src/test/resources/top_down_parsing/TopDownWrongLastStep.json",
            "src/test/resources/top_down_parsing/TopDownWrongProductionStep.json",
            "src/test/resources/top_down_parsing/TopDownWrongReadStep.json"
    })

    public void checkWrongTopDownAcceptorValidationTest(String tDAcceptorPath) throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");
        Gson gson = new Gson();

        try (Reader reader = new FileReader(tDAcceptorPath)) {
            TopDownAcceptor tDAcceptor = gson.fromJson(reader, TopDownAcceptor.class);
            TopDownAcceptorValidation tDAcceptorValidation = new TopDownAcceptorValidation(grammarAsJson);
            String word = "()()";
            assertFalse(tDAcceptorValidation.validateTopDownAcceptor(tDAcceptor, word));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "(((", ")))", "((()())"})
    public void checkWrongWordForTopDownAcceptorValidationTest(String word) throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");
        Gson gson = new Gson();
        try (Reader reader = new FileReader(
                "src/test/resources/top_down_parsing/TopDownCorrect.json")) {
            TopDownAcceptor tDAcceptor = gson.fromJson(reader, TopDownAcceptor.class);
            TopDownAcceptorValidation tDAcceptorValidation = new TopDownAcceptorValidation(grammarAsJson);
            assertFalse(tDAcceptorValidation.validateTopDownAcceptor(tDAcceptor, word));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getGrammarAsJson(String path) throws FileNotFoundException {
        return new Scanner(new File(path)).useDelimiter("\\Z").next();
    }

    @Test
    public void wrongGrammarUsageTest() throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson("src/test/resources/derivation_tree/grammarCorrect1.json");
        Gson gson = new Gson();

        try (Reader reader = new FileReader("src/test/resources/top_down_parsing/TopDownWrongGrammarUsage.json")) {
            TopDownAcceptor tDAcceptor = gson.fromJson(reader, TopDownAcceptor.class);
            TopDownAcceptorValidation tDAcceptorValidation = new TopDownAcceptorValidation(grammarAsJson);
            String word = "()()";
            assertFalse(tDAcceptorValidation.validateTopDownAcceptor(tDAcceptor, word));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
