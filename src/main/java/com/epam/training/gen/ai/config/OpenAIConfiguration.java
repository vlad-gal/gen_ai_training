package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfiguration {

  @Bean
  public OpenAIAsyncClient openAIAsyncClient(
      @Value("${client-azureopenai-key}") String key,
      @Value("${client-azureopenai-endpoint}") String endpoint) {
    return new OpenAIClientBuilder()
        .credential(new AzureKeyCredential(key))
        .endpoint(endpoint)
        .buildAsyncClient();
  }

  @Bean
  public ChatCompletionService chatCompletionService(
      @Value("${client-azureopenai-deployment-name}") String modelId,
      OpenAIAsyncClient openAIAsyncClient) {
    return OpenAIChatCompletion.builder()
        .withOpenAIAsyncClient(openAIAsyncClient)
        .withModelId(modelId)
        .build();
  }

  @Bean
  public Kernel kernel(ChatCompletionService chatCompletionService) {
    return Kernel.builder()
        .withAIService(ChatCompletionService.class, chatCompletionService)
        .build();
  }
}
