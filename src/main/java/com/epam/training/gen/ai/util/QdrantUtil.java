package com.epam.training.gen.ai.util;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QdrantUtil {
  private final QdrantClient qdrantClient;

  /**
   * Checks if the collection exists in Qdrant. If it does not exist, a new collection is created.
   */
  public void checkCollectionExistence(String collectionName) {
    try {
      qdrantClient.getCollectionInfoAsync(collectionName).get();
    } catch (Exception ex) {
      log.info("Collection '{}' not found. Creating a new collection...", collectionName);
      this.createCollection(collectionName);
    }
  }

  /** Creates a new collection in Qdrant with specified vector parameters. */
  private void createCollection(String collectionName) {
    try {
      var result =
          qdrantClient
              .createCollectionAsync(
                  collectionName,
                  Collections.VectorParams.newBuilder()
                      .setDistance(Collections.Distance.Cosine)
                      .setSize(1536)
                      .build())
              .get();
      log.info("Collection was created: [{}]", result.getResult());
    } catch (ExecutionException | InterruptedException e) {
      log.error("Error creating collection: {}", e.getMessage());
      throw new RuntimeException("Error creating collection", e);
    }
  }
}
