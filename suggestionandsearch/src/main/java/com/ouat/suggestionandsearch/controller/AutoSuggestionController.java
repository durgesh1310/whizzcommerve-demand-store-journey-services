package com.ouat.suggestionandsearch.controller;

import static com.ouat.suggestionandsearch.constants.CommonConstant.DEFAULT_TRANSACTION_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ouat.suggestionandsearch.annotations.SessionRequired;
import com.ouat.suggestionandsearch.controller.response.DemandStoreAPIResponse;
import com.ouat.suggestionandsearch.services.AutoSuggestionService;
import com.ouat.suggestionandsearch.util.Utility;

@RestController
@CrossOrigin
@RequestMapping("/auto-suggest")
@SessionRequired
public class AutoSuggestionController {

    @Autowired
    private AutoSuggestionService autoSuggestionService;

    @GetMapping("{query}")
    public ResponseEntity<DemandStoreAPIResponse> getSuggestions(@PathVariable String query,
            @RequestHeader(value = "Transaction-ID",
                    defaultValue = DEFAULT_TRANSACTION_ID) String transactionId) {
        transactionId = getValidTransactionId(transactionId);
        return autoSuggestionService.getSuggestions(query, transactionId);
    }

    
    private String getValidTransactionId(String transactionId) {
        transactionId =
                transactionId.equalsIgnoreCase(DEFAULT_TRANSACTION_ID) ? Utility.getTrackingId()
                        : transactionId;
        return transactionId;
    }

    
}
