package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.controller.dto.SpecificValidationDto;
import de.dhbw.karlsruhe.derivation.specificDerivation.models.SpecificDerivationValidationDetailResult;
import de.dhbw.karlsruhe.derivation.specificDerivation.validation.SpecificDerivationValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class SpecificValidationController {

  @PostMapping("/api/validate/specific-derivation")
  ResponseEntity<SpecificDerivationValidationDetailResult> getSpecificDerivation(@RequestBody SpecificValidationDto specificValidationDto) {
    specificValidationDto.getGrammar().splitOrGrammarsIntoSingleRules();

    SpecificDerivationValidation specificDerivationValidation = new SpecificDerivationValidation();
    SpecificDerivationValidationDetailResult result = specificDerivationValidation.checkDerivation(
        specificValidationDto.getDirection(),
        specificValidationDto.getGrammar(),
        specificValidationDto.getDerivationList(),
        specificValidationDto.getWord()
    );

    return new ResponseEntity<>(result, HttpStatus.OK);

  }

}
