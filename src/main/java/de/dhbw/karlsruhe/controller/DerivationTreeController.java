package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.controller.dto.ValidationTreeParamModel;
import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class DerivationTreeController {

  @PostMapping("/api/validate/tree")
  ResponseEntity<Boolean> validationTree(@RequestBody ValidationTreeParamModel validationTreeParamModel) {
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(validationTreeParamModel.getGrammar());

    boolean accepted = derivationTreeValidation.checkTree(validationTreeParamModel.getDerivationTree(), validationTreeParamModel.getWord());

    return new ResponseEntity<>(accepted, HttpStatus.OK);
  }

}
