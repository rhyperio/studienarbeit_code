package de.dhbw.karlsruhe.top.down.parsing.validation;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.services.GrammarService;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;

import java.util.List;

public class TopDownAcceptorValidation {

    private GrammarService grammarService;
    private List<GrammarRule> grammarRules;

    public TopDownAcceptorValidation(String grammarAsJson) {
        this.grammarService = new GrammarService(grammarAsJson);
        this.grammarRules = this.grammarService.getGrammarRules();
    }

    public boolean validateTopDownAcceptor(TopDownAcceptor tDAcceptor, String word) {
        return false;
    }
}
