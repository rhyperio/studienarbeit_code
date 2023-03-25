package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.bottom.up.models.AcceptorDetailResult;
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
    ResponseEntity<AcceptorDetailResult> bottomUp(@RequestBody ValidationBottomUpParamModel validationBottomUpParamModel) {
        validationBottomUpParamModel.getGrammar().splitOrGrammarsIntoSingleRules();
        BottomUpAcceptorValidation bottomUpAcceptorValidation = new BottomUpAcceptorValidation(validationBottomUpParamModel.getGrammar());

        AcceptorDetailResult acceptorDetailResult = bottomUpAcceptorValidation.checkAcceptor(validationBottomUpParamModel.getBottomUpAcceptor(), validationBottomUpParamModel.getWord());

        return new ResponseEntity<>(acceptorDetailResult, HttpStatus.OK);
    }
}
