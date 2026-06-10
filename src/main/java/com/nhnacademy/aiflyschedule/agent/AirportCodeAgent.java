package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 공항 코드 및 목록을 관리하는 에이전트
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AirportCodeAgent {
    private final ApiClientService apiClientService;
    
    // 캐시 저장소: 맵(조회용) 및 리스트(전체 목록용)
    private static final Map<String, String> AIRPORT_CODE_MAP = new HashMap<>();
    private static final List<AirportInfoResponse> AIRPORT_LIST = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            log.info("AirportCodeAgent: 공항 정보 캐싱 시작...");
            List<AirportInfoResponse> info = apiClientService.getAirportInfo();

            if (info == null || info.isEmpty()) {
                log.warn("API에서 받아온 공항 정보가 없습니다. 초기화를 중단합니다.");
                return;
            }

            AIRPORT_LIST.clear();
            AIRPORT_LIST.addAll(info);

            AIRPORT_CODE_MAP.clear();
            for (AirportInfoResponse airport : info) {
                if (airport.airportName() != null && airport.airportId() != null) {
                    AIRPORT_CODE_MAP.put(airport.airportName().trim(), airport.airportId().trim());
                }
            }

            log.info("공항 코드 {}건 캐싱 완료!", AIRPORT_CODE_MAP.size());

        } catch (Exception e) {
            log.error("공항 코드 초기화 중 예외 발생 (API 통신 실패 등)", e);
        }
    }

    /**
     * 공항 이름으로 코드를 조회합니다.
     */
    public String getAirportCode(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            throw new IllegalArgumentException("공항 이름을 입력해주세요");
        }
        String normalized = airportName.trim();

        if (normalized.matches("NAARK[A-Z]{2}")) {
            return normalized;
        }

        String code = AIRPORT_CODE_MAP.get(normalized);
        if(code == null) {
            log.warn("알 수 없는 공항: {}", airportName);
            throw new IllegalArgumentException("알 수 없는 공항입니다: " + airportName);
        }
        return code;
    }

    /**
     * 캐시된 전체 공항 목록을 반환합니다.
     */
    public List<AirportInfoResponse> getAllAirports() {
        return Collections.unmodifiableList(AIRPORT_LIST);
    }

    public boolean isValidAirport(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            return false;
        }
        return AIRPORT_CODE_MAP.containsKey(airportName.trim());
    }
}
