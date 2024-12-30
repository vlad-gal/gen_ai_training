package com.epam.training.gen.ai.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantConfiguration {

  @Bean
  public QdrantClient qdrantClient() {
    return new QdrantClient(QdrantGrpcClient.newBuilder("localhost", 6334, false).build());
  }

  @Bean
  public EmbeddingStore<TextSegment> embeddingStore(
      @Value("${rag.collection.name}") String collectionName, QdrantClient qdrantClient) {
    return QdrantEmbeddingStore.builder()
        .client(qdrantClient)
        .collectionName(collectionName)
        .build();
  }
}
