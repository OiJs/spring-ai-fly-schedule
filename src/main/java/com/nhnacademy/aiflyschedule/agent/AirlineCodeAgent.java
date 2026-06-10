package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.AirlineInfoResponse;
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
 * 항공사 코드 및 목록을 관리하는 에이전트
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AirlineCodeAgent {
    private final ApiClientService apiClientService;

    // 캐시 저장소: 맵(조회용) 및 리스트(전체 목록용)
    private static final Map<String, String> AIRLINE_ID_MAP = new HashMap<>();
    private static final List<AirlineInfoResponse> AIRLINE_LIST = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            log.info("AirlineCodeAgent: 항공사 정보 캐싱 시작...");
            List<AirlineInfoResponse> info = apiClientService.getAirlineInfo();

            if (info == null || info.isEmpty()) {
                log.warn("API에서 받아온 항공사 정보가 없습니다. 초기화를 중단합니다.");
                return;
            }

            AIRLINE_LIST.clear();
            AIRLINE_LIST.addAll(info);

            AIRLINE_ID_MAP.clear();
            for (AirlineInfoResponse airline : info) {
                if (airline.airlineName() != null && airline.airlineId() != null) {
                    AIRLINE_ID_MAP.put(airline.airlineName().trim(), airline.airlineId().trim());
                }
            }

            log.info("항공사 코드 {}건 캐싱 완료!", AIRLINE_ID_MAP.size());

        } catch (Exception e) {
            log.error("항공사 코드 초기화 중 예외 발생 (API 통신 실패 등)", e);
        }
    }

    /**
     * 항공사 이름으로 ID를 조회합니다.
     */
    public String getAirlineId(String airlineName) {
        if (airlineName == null || airlineName.isBlank()) {
            throw new IllegalArgumentException("항공사 이름을 입력해주세요");
        }
        String normalized = airlineName.trim();

        String id = AIRLINE_ID_MAP.get(normalized);
        if (id == null) {
            log.warn("알 수 없는 항공사: {}", airlineName);
            return "알 수 없는 항공사입니다: " + airlineName;
        }
        return id;
    }

    /**
     * 캐시된 전체 항공사 목록을 반환합니다.
     */
    public List<AirlineInfoResponse> getAllAirlines() {
        return Collections.unmodifiableList(AIRLINE_LIST);
    }
}
