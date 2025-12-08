package com.ouat.suggestionandsearch.controller.response;

import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.suggestionandsearch.dtos.autosearch.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class RecommendationResponse {

    private List<Details> results;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class Details {
        String key;
        List<ProductDto> values;
    }
}
