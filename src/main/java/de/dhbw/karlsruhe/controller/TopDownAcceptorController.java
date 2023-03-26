package de.dhbw.karlsruhe.controller;

import de.dhbw.karlsruhe.controller.dto.ValidationTopDownParamModel;
import de.dhbw.karlsruhe.top.down.parsing.models.TDAcceptorDetailResult;
import de.dhbw.karlsruhe.top.down.parsing.validation.TopDownAcceptorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class TopDownAcceptorController {

    @PostMapping("/api/validate/top-down")
    ResponseEntity<TDAcceptorDetailResult> topDown(@RequestBody ValidationTopDownParamModel validationTopDownParamModel) {
        validationTopDownParamModel.getGrammar().splitOrGrammarsIntoSingleRules();
        TopDownAcceptorValidation topDownAcceptorValidation = new TopDownAcceptorValidation(validationTopDownParamModel.getGrammar());

        TDAcceptorDetailResult tdAcceptorDetailResult = topDownAcceptorValidation.validateTopDownAcceptor(validationTopDownParamModel.getTopDownAcceptor(), validationTopDownParamModel.getWord());

        return new ResponseEntity<>(tdAcceptorDetailResult, HttpStatus.OK);
    }
}
