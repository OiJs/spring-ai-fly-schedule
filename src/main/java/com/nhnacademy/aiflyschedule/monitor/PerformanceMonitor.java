package com.nhnacademy.aiflyschedule.monitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Tool 성능 모니터링
 */
@Component
public class PerformanceMonitor {
    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitor.class);

    //Tool별 호출 횟수
    private final ConcurrentHashMap<String, AtomicInteger> callCounts = new ConcurrentHashMap<>();

    //Tool별 총 실행 시간
    private final ConcurrentHashMap<String, AtomicLong> totalDurations = new ConcurrentHashMap<>();

    // Tool별 최소/최대 실행 시간
    private final ConcurrentHashMap<String, AtomicLong> minDurations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> maxDurations = new ConcurrentHashMap<>();

    public void recordToolCall(String toolName, long duration) {
        //호출 횟수 증가
        callCounts.computeIfAbsent(toolName, k -> new AtomicInteger(0)).incrementAndGet();

        //총 실행 시간 증가
        totalDurations.computeIfAbsent(toolName, k -> new AtomicLong(0)).addAndGet(duration);

        minDurations.computeIfAbsent(toolName, k -> new AtomicLong(Long.MAX_VALUE))
                .updateAndGet(current -> Math.min(current, duration));

        maxDurations.computeIfAbsent(toolName, k -> new AtomicLong(0))
                .updateAndGet(current -> Math.max(current, duration));
    }

    /**
     * 성능 통계 출력
     */
    //TODO: monitor
    // 통계 출력, reset 어디서 해야할지 Scheduler 이용 or Controller 호출?
    public void printStatistics() {
        log.info("==================== Tool 성능 통계 ====================");

        callCounts.forEach((toolName, count) -> {
            long total = totalDurations.get(toolName).get();
            long min = minDurations.get(toolName).get();
            long max = maxDurations.get(toolName).get();
            double avg = (double) total / count.get();

            log.info("Tool: {}", toolName);
            log.info("  호출 횟수: {}", count.get());
            log.info("  총 실행 시간: {}ms ({}초)", total, total / 1000.0);
            log.info("  평균 실행 시간: {:.2f}ms", avg);
            log.info("  최소 실행 시간: {}ms", min);
            log.info("  최대 실행 시간: {}ms", max);
            log.info("--------------------------------------------------");
        });

        log.info("======================================================");
    }

    public void reset() {
        callCounts.clear();
        totalDurations.clear();
        minDurations.clear();
        maxDurations.clear();
        log.info("성능 통계가 초기화되었습니다.");
    }
}
