package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.grammar.generation.GrammarGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarPatternProductionsGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarProbabilityGeneration;
import de.dhbw.karlsruhe.models.Grammar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class GrammarGenerationController {

    @GetMapping("/api/get/grammar/probability")
    ResponseEntity<Grammar> getProbabilityGeneratedGrammar() {
        GrammarGeneration grammarGeneration = new GrammarProbabilityGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar();
        generatedGrammar.mergeOrGrammarsIntoSingleRules();
        return new ResponseEntity<>(generatedGrammar, HttpStatus.OK);
    }

    @GetMapping("/api/get/grammar/pattern")
    ResponseEntity<Grammar> getPatternGeneratedGrammar() {
        GrammarGeneration grammarGeneration = new GrammarPatternProductionsGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar();
        generatedGrammar.mergeOrGrammarsIntoSingleRules();
        return new ResponseEntity<>(generatedGrammar, HttpStatus.OK);
    }

}
