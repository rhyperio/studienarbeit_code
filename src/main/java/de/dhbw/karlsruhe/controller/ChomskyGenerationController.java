package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.chomsky.generation.ChomskyDirectGeneration;
import de.dhbw.karlsruhe.chomsky.generation.ChomskyTransformationGeneration;
import de.dhbw.karlsruhe.models.Grammar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class ChomskyGenerationController {
    @GetMapping("/api/get/chomsky/direkt")
    ResponseEntity<Grammar> getDirektGeneratedChomskyGrammar() {
        ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();
        Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();
        generatedGrammar.mergeOrGrammarsIntoSingleRules();
        return new ResponseEntity<>(generatedGrammar, HttpStatus.OK);
    }

    @GetMapping("/api/get/chomsky/transformed")
    ResponseEntity<Grammar> getTransformGeneratedChomskyGrammar() {
        ChomskyTransformationGeneration chomskyTransformationGeneration = new ChomskyTransformationGeneration();
        Grammar generatedGrammar = chomskyTransformationGeneration.generateChomskyGrammar();
        generatedGrammar.mergeOrGrammarsIntoSingleRules();
        return new ResponseEntity<>(generatedGrammar, HttpStatus.OK);
    }
}
