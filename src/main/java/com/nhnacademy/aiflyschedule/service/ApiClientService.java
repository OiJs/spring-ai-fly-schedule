package com.nhnacademy.aiflyschedule.service;

import com.nhnacademy.aiflyschedule.config.DataGoKrApiProperties;
import com.nhnacademy.aiflyschedule.dto.response.*;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiClientService {
    private final DataGoKrApiProperties apiProperties;
    private final RestClient restClient = RestClient.create();

    // 항공편 정보 조회
    public List<FlightInfoResponse> getFlightSchedule(String depAirportId, String arrAirportId, String date) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("depAirportId", depAirportId);
        params.add("arrAirportId", arrAirportId);
        params.add("depPlandTime", date);

        return fetchFromApi("/GetFlightOpratInfoList", params,
                new ParameterizedTypeReference<ApiResponseWrapper<FlightInfoResponse>>() {});
    }

    // 국내 공항 목록 조회
    public List<AirportInfoResponse> getAirportInfo() {
        return fetchFromApi("/GetArprtList", new LinkedMultiValueMap<>(),
                new ParameterizedTypeReference<ApiResponseWrapper<AirportInfoResponse>>() {});
    }

    // 국내 항공사 목록 조회
    public List<AirlineInfoResponse> getAirlineInfo() {
        return fetchFromApi("/GetAirmanList", new LinkedMultiValueMap<>(),
                new ParameterizedTypeReference<ApiResponseWrapper<AirlineInfoResponse>>() {});
    }

    // 공통 API 호출 메서드
    private <T> List<T> fetchFromApi(String endPoint, MultiValueMap<String, String> params,
                                     ParameterizedTypeReference<ApiResponseWrapper<T>> typeRef) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiProperties.getUrl() + endPoint)
                    .queryParam("serviceKey", apiProperties.getServiceKey())
                    .queryParam("_type", "json")
                    .queryParams(params)
                    .build(true)
                    .toUriString();

            log.info("API 호출: {}", url);

            ApiResponseWrapper<T> response = restClient.get()
                    .uri(URI.create(url))
                    .retrieve()
                    .body(typeRef);

            if (response != null && response.response() != null) {
                var header = response.response().header();
                if ("00".equals(header.resultCode())) {
                    return response.response().body().items().getItem();
                } else {
                    log.error("API 에러: {} - {}", header.resultCode(), header.resultMessage());
                }
            }
        } catch (Exception e) {
            log.error("API 호출 중 예외 발생: {}", e.getMessage());
        }
        return Collections.emptyList();
    }
}