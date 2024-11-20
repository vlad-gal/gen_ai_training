package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.dto.AnswerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {
    private final OpenAIAsyncClient openAIAsyncClient;

    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;

    @Override
    public AnswerDTO generateAnswer(String prompt) {
        var completions = openAIAsyncClient
                .getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(new ChatRequestUserMessage(prompt))))
                .block();
        var message = completions.getChoices().stream()
                .map(choice -> choice.getMessage().getContent())
                .findFirst().orElse("No answer");


        return new AnswerDTO().setAnswer(message);
    }
}
