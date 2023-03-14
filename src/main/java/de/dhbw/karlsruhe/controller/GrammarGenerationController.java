package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.grammar.generation.GrammarGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarPatternProductionsGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarProbabilityGeneration;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.Probability;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class GrammarGenerationController {

    @PostMapping("/api/get/grammar/probability")
    ResponseEntity<Grammar> getProbabilityGeneratedGrammar(@RequestBody Probability probability) {
        GrammarGeneration grammarGeneration = new GrammarProbabilityGeneration(probability);
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
