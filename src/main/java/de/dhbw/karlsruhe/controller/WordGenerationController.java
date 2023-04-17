package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.controller.dto.WordGenerationCharacterDto;
import de.dhbw.karlsruhe.controller.dto.WordGenerationProductionDto;
import de.dhbw.karlsruhe.controller.dto.WordReadActionLimitationDto;
import de.dhbw.karlsruhe.grammar.generation.GrammarGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarPatternProductionsGeneration;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.word.generation.ParserLimitation;
import de.dhbw.karlsruhe.word.generation.WordGeneration;
import de.dhbw.karlsruhe.word.generation.WordLimitationsNotFulfillableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class WordGenerationController {

  @PostMapping("/api/get/word")
  ResponseEntity<String> getWord(@RequestBody Grammar grammar) {
    grammar.splitOrGrammarsIntoSingleRules();
    WordGeneration wordGeneration = new WordGeneration(grammar);

    try {
      return new ResponseEntity<>(wordGeneration.generateWord(), HttpStatus.OK);
    } catch (WordLimitationsNotFulfillableException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

  }

  @PostMapping(value = "/api/get/word/character-limitation")
  ResponseEntity<String> getWordWithLimitation(@RequestBody WordGenerationCharacterDto wordGenerationCharacterDto) {
    wordGenerationCharacterDto.getGrammar().splitOrGrammarsIntoSingleRules();
    WordGeneration wordGeneration = new WordGeneration(wordGenerationCharacterDto.getGrammar());

    try {
      return new ResponseEntity<>(wordGeneration.generateWord(
          wordGenerationCharacterDto.getMinWordLength(),
          wordGenerationCharacterDto.getMaxWordLength(),
          wordGenerationCharacterDto.getMaxProductionCount()
      ), HttpStatus.OK);
    } catch (WordLimitationsNotFulfillableException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/api/get/word/production-limitation")
  ResponseEntity<String> getWordWithLimitation(@RequestBody WordGenerationProductionDto wordGenerationProductionDto) {
    wordGenerationProductionDto.getGrammar().splitOrGrammarsIntoSingleRules();
    WordGeneration wordGeneration = new WordGeneration(wordGenerationProductionDto.getGrammar());

    try {
      return new ResponseEntity<>(wordGeneration.generateWord(
          wordGenerationProductionDto.getMaxProductionCount()
      ), HttpStatus.OK);
    } catch (WordLimitationsNotFulfillableException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/api/get/word/ReadAction-Limitation")
  ResponseEntity<String> getWordWithLimitation(@RequestBody WordReadActionLimitationDto wordReadActionLimitationDto) {
    wordReadActionLimitationDto.getGrammar().splitOrGrammarsIntoSingleRules();
    WordGeneration wordGeneration = new WordGeneration(wordReadActionLimitationDto.getGrammar());

    try {
      return new ResponseEntity<>(wordGeneration.generateWord(
              new ParserLimitation(wordReadActionLimitationDto.getMaxReadCount(), wordReadActionLimitationDto.getMaxActionCount())
      ), HttpStatus.OK);
    } catch (WordLimitationsNotFulfillableException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

}
