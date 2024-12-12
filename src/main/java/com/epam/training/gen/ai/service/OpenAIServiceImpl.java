package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.AnswerDTO;
import com.epam.training.gen.ai.plugin.AgeCalculatorPlugin;
import com.epam.training.gen.ai.plugin.WeatherPredictorPlugin;
import com.epam.training.gen.ai.validator.ModelValidator;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIServiceImpl implements OpenAIService {
  private final OpenAIChatCompletion.Builder builder;
  private final ChatHistory chatHistory;
  private final ModelValidator modelValidator;

  @Override
  public AnswerDTO generateAnswer(String prompt, Double temperature, String modelId) {
    modelValidator.validateModel(modelId);

    chatHistory.addUserMessage(prompt);
    OpenAIChatCompletion chatCompletion =
        builder.withDeploymentName(modelId).withModelId(modelId).build();

    var completions =
        chatCompletion
            .getChatMessageContentsAsync(
                chatHistory, this.buildKernel(chatCompletion), this.invocationContext(temperature))
            .block();
    log.info("Completions: {}", completions);

    var message =
        completions.stream()
            .map(ChatMessageContent::getContent)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("No answer");
    log.info("Message: {}", message);
    chatHistory.addAssistantMessage(message);
    log.info("Chat history: {}", chatHistory.getMessages());

    return new AnswerDTO().setAnswer(message);
  }

  private PromptExecutionSettings createPromptExecutionSettings(Double temperature) {
    return PromptExecutionSettings.builder().withTemperature(temperature).build();
  }

  private InvocationContext invocationContext(Double temperature) {
    return InvocationContext.builder()
        .withPromptExecutionSettings(this.createPromptExecutionSettings(temperature))
        .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
        .build();
  }

  private Kernel buildKernel(ChatCompletionService chatCompletionService) {
    return Kernel.builder()
        .withAIService(ChatCompletionService.class, chatCompletionService)
        .withPlugin(
            KernelPluginFactory.createFromObject(
                new WeatherPredictorPlugin(), WeatherPredictorPlugin.class.getSimpleName()))
        .withPlugin(
            KernelPluginFactory.createFromObject(
                new AgeCalculatorPlugin(), AgeCalculatorPlugin.class.getSimpleName()))
        .build();
  }
}
