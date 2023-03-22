package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.controller.dto.ValidationTreeParamModel;
import de.dhbw.karlsruhe.derivation.tree.models.DetailResult;
import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class DerivationTreeController {

  @PostMapping("/api/validate/tree")
  ResponseEntity<DetailResult> validationTree(@RequestBody ValidationTreeParamModel validationTreeParamModel) {
    validationTreeParamModel.getGrammar().splitOrGrammarsIntoSingleRules();
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(validationTreeParamModel.getGrammar());

    DetailResult detailResult = derivationTreeValidation.checkTree(validationTreeParamModel.getDerivationTree(), validationTreeParamModel.getWord());

    return new ResponseEntity<>(detailResult, HttpStatus.OK);
  }

}
