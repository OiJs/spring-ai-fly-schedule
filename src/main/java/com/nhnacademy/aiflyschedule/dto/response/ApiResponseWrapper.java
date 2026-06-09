package com.nhnacademy.aiflyschedule.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiResponseWrapper<T>(
        @JsonProperty("response") ApiResponse<T> response
) {
    public record ApiResponse<T>(
            @JsonProperty("header") ResponseHeader header,
            @JsonProperty("body") ResponseBody<T> body
    ) {}

    public record ResponseHeader(
            @JsonProperty("resultCode") String resultCode,
            @JsonProperty("resultMsg") String resultMessage
    ) {}

    public record ResponseBody<T>(
            @JsonProperty("items") ItemsContainer<T> items,
            @JsonProperty("numOfRows") Integer numOfRows,
            @JsonProperty("pageNo") Integer pageNo,
            @JsonProperty("totalCount") Integer totalCount
    ) {}

    public record ItemsContainer<T>(
            @JsonProperty("item") List<T> item
    ) {
        public List<T> getItem() {
            return item != null ? item : Collections.emptyList();
        }
    }
}