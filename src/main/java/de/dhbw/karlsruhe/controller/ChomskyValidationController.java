package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.chomsky.validation.CNFStepByStepValidation;
import de.dhbw.karlsruhe.chomsky.validation.ValidateGrammarCNF;
import de.dhbw.karlsruhe.controller.dto.ValidationCNFParamModel;
import de.dhbw.karlsruhe.controller.dto.ValidationCNFSBSParamModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ChomskyValidationController {
    @PostMapping("/api/validate/chomsky/form")
    ResponseEntity<Boolean> cnf(@RequestBody ValidationCNFParamModel validationCNFParamModel) {
        validationCNFParamModel.getGrammar().splitOrGrammarsIntoSingleRules();

        ValidateGrammarCNF validateGrammarCNF = new ValidateGrammarCNF();
        boolean validationResult = validateGrammarCNF.validateGrammarIsInCNF(validationCNFParamModel.getGrammar());

        return new ResponseEntity<>(validationResult, HttpStatus.OK);
    }

    @PostMapping("/api/validate/chomsky/stepbystep")
    ResponseEntity<Boolean> cnfStepByStep(@RequestBody ValidationCNFSBSParamModel validationCNFSBSParamModel) {
        validationCNFSBSParamModel.getGrammar().splitOrGrammarsIntoSingleRules();

        CNFStepByStepValidation cnfStepByStepValidation = new CNFStepByStepValidation();
        boolean validationResult = cnfStepByStepValidation.validateGrammarIsInCorrectCNF(validationCNFSBSParamModel.getGrammar(), validationCNFSBSParamModel.getGrammarStepsToValidate());

        return new ResponseEntity<>(validationResult, HttpStatus.OK);
    }
}
