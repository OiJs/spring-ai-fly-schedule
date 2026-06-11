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
    
    // static 제거: 테스트 고립성 및 동적인 상태 관리 보장
    private final Map<String, String> airportCodeMap = new HashMap<>();
    private final List<AirportInfoResponse> airportList = new ArrayList<>();

    /**
     * 서버 시작 시 공항 정보 미리 캐싱
     */
    @PostConstruct
    public void init() {
        try {
            log.info("AirportCodeAgent: 공항 정보 캐싱 시작...");
            List<AirportInfoResponse> info = apiClientService.getAirportInfo();

            if (info == null || info.isEmpty()) {
                log.warn("API에서 받아온 공항 정보가 없습니다. 초기화를 중단합니다.");
                return;
            }

            airportList.clear();
            airportList.addAll(info);

            airportCodeMap.clear();
            for (AirportInfoResponse airport : info) {
                if (airport.airportName() != null && airport.airportId() != null) {
                    airportCodeMap.put(airport.airportName().trim(), airport.airportId().trim());
                }
            }

            log.info("공항 코드 {}건 캐싱 완료!", airportCodeMap.size());

        } catch (Exception e) {
            log.error("공항 코드 초기화 중 예외 발생 (API 통신 실패 등)", e);
        }
    }

    /**
     * 공항 이름으로 코드를 조회합니다.
     * @param airportName 공항 이름
     * @return 공항 이름에 해당 하는 코드 반환
     */
    public String getAirportCode(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            throw new IllegalArgumentException("공항 이름을 입력해주세요");
        }
        String normalized = airportName.trim();

        if (normalized.matches("NAARK[A-Z]{2}")) {
            return normalized;
        }

        String code = airportCodeMap.get(normalized);
        if(code == null) {
            log.warn("알 수 없는 공항: {}", airportName);
            throw new IllegalArgumentException("알 수 없는 공항입니다: " + airportName);
        }
        return code;
    }

    /**
     *
     * @return 캐시된 전체 공항 목록을 반환
     */
    public List<AirportInfoResponse> getAllAirports() {
        return Collections.unmodifiableList(airportList);
    }

    public boolean isValidAirport(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            return false;
        }
        return airportCodeMap.containsKey(airportName.trim());
    }
}
