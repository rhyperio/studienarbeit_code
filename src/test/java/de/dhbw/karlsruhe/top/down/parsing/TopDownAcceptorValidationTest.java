package de.dhbw.karlsruhe.top.down.parsing;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;
import de.dhbw.karlsruhe.top.down.parsing.validation.TopDownAcceptorValidation;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

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

    private String getGrammarAsJson(String path) throws FileNotFoundException {
        return new Scanner(new File(path)).useDelimiter("\\Z").next();
    }
}
