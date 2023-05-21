package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.controller.dto.PatternDTO;
import de.dhbw.karlsruhe.grammar.generation.GrammarConcatenationGeneration;
import de.dhbw.karlsruhe.controller.dto.ProbabilityDto;
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
    ResponseEntity<Grammar> getProbabilityGeneratedGrammar(@RequestBody ProbabilityDto probabilityDto) {
        GrammarGeneration grammarGeneration = new GrammarProbabilityGeneration(probabilityDto.fromDto());
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

    @PostMapping("/api/get/grammar/pattern/withLimits")
    ResponseEntity<Grammar> getPatternGeneratedGrammarWithLimits(@RequestBody PatternDTO patternDTO) {
        GrammarPatternProductionsGeneration grammarGeneration = new GrammarPatternProductionsGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar(patternDTO.getCountTerminals(),patternDTO.getCountNonTerminals());
        //generatedGrammar.mergeOrGrammarsIntoSingleRules();
        return new ResponseEntity<>(generatedGrammar, HttpStatus.OK);
    }

    @GetMapping("/api/get/grammar/concatenation")
    ResponseEntity<Grammar> getConcatenationGeneratedGrammar() {
        GrammarGeneration grammarGeneration = new GrammarConcatenationGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar();
        generatedGrammar.mergeOrGrammarsIntoSingleRules();
        return new ResponseEntity<>(generatedGrammar, HttpStatus.OK);
    }

}
