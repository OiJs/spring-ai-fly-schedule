package com.nhnacademy.aiflyschedule.context;

import com.nhnacademy.aiflyschedule.dto.response.FlightSearchResult;

public class FlightSearchContext {
    private static final ThreadLocal<FlightSearchResult> CONTEXT = new ThreadLocal<>();

    public static void setResult(FlightSearchResult result) {
        CONTEXT.set(result);
    }

    public static FlightSearchResult getResult() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
