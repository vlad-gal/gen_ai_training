package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
  public OpenAIChatCompletion.Builder chatCompletionBuilder(OpenAIAsyncClient openAIAsyncClient) {
    return OpenAIChatCompletion.builder().withOpenAIAsyncClient(openAIAsyncClient);
  }

  @Bean
  public ChatHistory chatHistory() {
    return new ChatHistory();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
