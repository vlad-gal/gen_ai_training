package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.azure.AzureOpenAiEmbeddingModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfiguration {

  @Bean
  public OpenAIClient openAIClient(
      @Value("${client-azureopenai-key}") String key,
      @Value("${client-azureopenai-endpoint}") String endpoint) {
    return new OpenAIClientBuilder()
        .credential(new AzureKeyCredential(key))
        .endpoint(endpoint)
        .buildClient();
  }

  @Bean
  public ChatLanguageModel chatLanguageModel(OpenAIClient openAIClient) {
    return AzureOpenAiChatModel.builder()
        .openAIClient(openAIClient)
        .build();
  }

  @Bean
  public DocumentParser documentParser() {
    return new ApacheTikaDocumentParser();
  }

  @Bean
  public EmbeddingModel embeddingModel(
      @Value("${embedding.model.name}") String deploymentName, OpenAIClient openAIClient) {
    return AzureOpenAiEmbeddingModel.builder()
        .openAIClient(openAIClient)
        .deploymentName(deploymentName)
        .build();
  }

  @Bean
  public DocumentSplitter documentSplitter() {
    return DocumentSplitters.recursive(300, 0);
  }

  @Bean
  public ContentRetriever contentRetriever(
      EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
    return EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .maxResults(2)
        .minScore(0.5)
        .build();
  }

  @Bean
  public EmbeddingStoreIngestor ingestor(
      EmbeddingModel embeddingModel,
      EmbeddingStore<TextSegment> embeddingStore,
      DocumentSplitter documentSplitter) {
    return EmbeddingStoreIngestor.builder()
        .embeddingModel(embeddingModel)
        .embeddingStore(embeddingStore)
        .documentSplitter(documentSplitter)
        .build();
  }
}
