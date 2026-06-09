package com.nhnacademy.aiflyschedule.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {

    /**
     * 두 숫자의 합을 계산하는 Tool
     *
     * LLM은 이 메서드의 description을 보고 언제 호출할지 결정합니다.
     *
     * @param a 첫 번째 숫자 (LLM이 자동으로 추출한 값)
     * @param b 두 번째 숫자 (LLM이 자동으로 추출한 값)
     * @return 두 숫자의 합 (LLM이 이 결과를 사용하여 답변 생성)
     */
    @Tool(description = "두 숫자의 합을 계산합니다.")  // LLM에게 이 함수의 용도 설명
    public int add(
            @ToolParam(description = "첫 번째 숫자") int a,  // 파라미터 설명 (LLM이 이해)
            @ToolParam(description = "두 번째 숫자") int b) {

        // LLM이 자동으로 호출
        return a + b;
    }

    /**
     * 두 숫자의 곱을 계산하는 Tool
     *
     * @Tool 어노테이션이 있으면 LLM이 호출할 수 있습니다.
     */
    @Tool(description = "두 숫자의 곱을 계산합니다.")
    public int multiply(
            @ToolParam(description = "첫 번째 숫자") int a,
            @ToolParam(description = "두 번째 숫자") int b) {

        return a * b;
    }
}