package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.bottom.up.validation.BottomUpAcceptorValidation;
import de.dhbw.karlsruhe.controller.dto.ValidationBottomUpParamModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class BottomUpAcceptorController {

    @PostMapping("/api/validate/bottom-up")
    ResponseEntity<Boolean> bottomUp(@RequestBody ValidationBottomUpParamModel validationBottomUpParamModel) {
        validationBottomUpParamModel.getGrammar().splitOrGrammarsIntoSingleRules();
        BottomUpAcceptorValidation bottomUpAcceptorValidation = new BottomUpAcceptorValidation(validationBottomUpParamModel.getGrammar());

        boolean accepted = bottomUpAcceptorValidation.checkAcceptor(validationBottomUpParamModel.getBottomUpAcceptor(), validationBottomUpParamModel.getWord());

        return new ResponseEntity<>(accepted, HttpStatus.OK);
    }
}
