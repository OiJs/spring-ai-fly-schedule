package com.nhnacademy.aiflyschedule.config;

import com.nhnacademy.aiflyschedule.mcp.AirlineInfoTool;
import com.nhnacademy.aiflyschedule.mcp.AirportInfoTool;
import com.nhnacademy.aiflyschedule.mcp.CachedAirportInfoTool;
import com.nhnacademy.aiflyschedule.mcp.FlightSearchTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    @Bean
    @Primary
    public ChatClient.Builder ollamaChatClientBuilder(@Qualifier("ollamaChatModel")ChatModel ollamaChatModel,
                                                      FlightSearchTool flightSearchTool,
                                                      AirlineInfoTool airlineInfoTool,
                                                      AirportInfoTool airportInfoTool) {
        return ChatClient.builder(ollamaChatModel)
                .defaultTools(flightSearchTool, airlineInfoTool, airportInfoTool)
                .defaultAdvisors(new SimpleLoggerAdvisor());
    }

    @Bean
    public ChatClient.Builder geminiChatClientBuilder(@Qualifier("googleGenAiChatModel") ChatModel geminiChatModel,
                                                      FlightSearchTool flightSearchTool,
                                                      AirlineInfoTool airlineInfoTool,
                                                      AirportInfoTool airportInfoTool) {
        return ChatClient.builder(geminiChatModel)
                .defaultTools(flightSearchTool, airlineInfoTool, airportInfoTool)
                .defaultAdvisors(new SimpleLoggerAdvisor());
    }
}
