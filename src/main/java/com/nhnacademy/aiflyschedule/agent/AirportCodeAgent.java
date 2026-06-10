package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
//TODO: ApiClientService 호출해서 사용?
// 현재는 하드 코딩
public class AirportCodeAgent {
    private final ApiClientService apiClientService;
    private static final Map<String, String> AIRPORT_CODE_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            List<AirportInfoResponse> info = apiClientService.getAirportInfo();

            if (info == null || info.isEmpty()) {
                log.warn("API에서 받아온 공항 정보가 없습니다. 초기화를 중단합니다.");
                return;
            }

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

    public String getAirportCode(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            throw new IllegalArgumentException("공항 이름을 입력해주세요");
        }
        String normalized = airportName.trim();

        if (normalized.matches("NAARK[A-Z]{2}")) {
            log.info("공항 코드 입력됨: {}", normalized);
            return normalized;
        }

        String code = AIRPORT_CODE_MAP.get(normalized);

        if(code == null) {
            log.warn("알 수 없는 공항: {}", airportName);
            throw new IllegalArgumentException("알 수 없는 공항입니다: " + airportName);
        }
        log.info("공항 코드 변환: {} → {}", airportName, code);
        return code;
    }

    public boolean isValidAirport(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            return false;
        }
        return AIRPORT_CODE_MAP.containsKey(airportName.trim());
    }
}
