package de.dhbw.karlsruhe.derivation.leftRight;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.dhbw.karlsruhe.derivation.leftRight.models.Derivation;
import de.dhbw.karlsruhe.derivation.leftRight.models.SpecificDerivationValidationDetailResult;
import de.dhbw.karlsruhe.derivation.leftRight.validation.SpecificDerivationValidation;
import de.dhbw.karlsruhe.models.Grammar;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpecificDerivationValidationTest {

    @Test
    public void correctLeftDerivation() throws FileNotFoundException {
        Grammar grammar = getGrammar(
                "src/test/resources/derivation/grammarCorrect1.json");
        List<String> derivationList = getDerivationList(
                "src/test/resources/derivation/derivation1.json");

        SpecificDerivationValidation specificDerivationValidation = new SpecificDerivationValidation();
        assertEquals(specificDerivationValidation
                .checkDerivation(Derivation.LEFT, grammar, derivationList, "(())"), new SpecificDerivationValidationDetailResult(true));
    }

    @Test
    public void correctRightDerivation() throws FileNotFoundException {
        Grammar grammar = getGrammar(
                "src/test/resources/derivation/grammarCorrect1.json");
        List<String> derivationList = getDerivationList(
                "src/test/resources/derivation/derivation1.json");

        SpecificDerivationValidation specificDerivationValidation = new SpecificDerivationValidation();
        assertEquals(specificDerivationValidation.checkDerivation(Derivation.LEFT, grammar, derivationList, "(())"),
                new SpecificDerivationValidationDetailResult(true));
    }

    @Test
    public void wrongLeftDerivation() throws FileNotFoundException {
        Grammar grammar = getGrammar(
                "src/test/resources/derivation/grammarCorrect1.json");
        List<String> derivationList = getDerivationList(
                "src/test/resources/derivation/derivation1.json");

        SpecificDerivationValidation specificDerivationValidation = new SpecificDerivationValidation();
        assertEquals(specificDerivationValidation
                .checkDerivation(Derivation.LEFT, grammar, derivationList, "(())"),
                new SpecificDerivationValidationDetailResult(true));
    }

    @Test
    public void wrongRightDerivation() throws FileNotFoundException {
        Grammar grammar = getGrammar(
                "src/test/resources/derivation/grammarCorrect1.json");
        List<String> derivationList = getDerivationList(
                "src/test/resources/derivation/derivation1.json");

        SpecificDerivationValidation specificDerivationValidation = new SpecificDerivationValidation();
        assertEquals(specificDerivationValidation.
                checkDerivation(Derivation.LEFT, grammar, derivationList, "(())"),
                new SpecificDerivationValidationDetailResult(true));
    }

    private Grammar getGrammar(String path) throws FileNotFoundException {
        return new Gson().fromJson(new Scanner(new File(path)).useDelimiter("\\Z").next(), Grammar.class);
    }

    private List<String> getDerivationList(String path) throws FileNotFoundException {
        String input = new Scanner(new File(path)).useDelimiter("\\Z").next();
        String derivations = new Gson().fromJson(input, JsonObject.class).get("derivation").getAsString();
        return Arrays.stream(derivations.split("->")).map(String::trim).toList();
    }
}
