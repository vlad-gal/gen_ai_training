package com.epam.training.gen.ai.service;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.epam.training.gen.ai.dto.EmbeddingDTO;
import com.epam.training.gen.ai.dto.ResponseEmbeddingDTO;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService {
  private final OpenAIAsyncClient openAIAsyncClient;
  private final QdrantClient qdrantClient;

  @Value("${embedding.model.name}")
  private String embeddingModelName;

  @Value("${embedding.collection.name}")
  private String collectionName;

  @Override
  public List<EmbeddingItem> buildEmbedding(EmbeddingDTO embeddingDTO) {
    return this.retrieveEmbeddings(embeddingDTO.text()).stream().toList();
  }

  @Override
  public List<ResponseEmbeddingDTO> searchEmbedding(EmbeddingDTO embeddingDTO) {
    List<Float> values =
        this.buildEmbedding(embeddingDTO).stream()
            .map(EmbeddingItem::getEmbedding)
            .flatMap(List::stream)
            .toList();
    try {
      List<Points.ScoredPoint> scoredPoints =
          qdrantClient
              .searchAsync(
                  Points.SearchPoints.newBuilder()
                      .setCollectionName(collectionName)
                      .addAllVector(values)
                      .setWithPayload(enable(true))
                      .setLimit(5)
                      .build())
              .get();
      return scoredPoints.stream()
          .map(
              scoredPoint -> {
                log.info(
                    "Scored Point: id: {}, score: {}, payload map: {}",
                    scoredPoint.getId(),
                    scoredPoint.getScore(),
                    scoredPoint.getPayloadMap());
                return new ResponseEmbeddingDTO(
                    scoredPoint.getId().getUuid(), scoredPoint.getScore());
              })
          .toList();
    } catch (ExecutionException | InterruptedException e) {
      log.error("Error searching for vector: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public String storeEmbedding(EmbeddingDTO embeddingDTO) {
    this.checkCollectionExistence();
    List<Points.PointStruct> pointStructs =
        this.buildEmbedding(embeddingDTO).stream()
            .map(EmbeddingItem::getEmbedding)
            .map(list -> this.buildPointStruct(list, Map.of("text", value(embeddingDTO.text()))))
            .toList();

    try {
      var updateResult = qdrantClient.upsertAsync(collectionName, pointStructs).get();
      log.info(updateResult.getStatus().name());
      return "Stored";
    } catch (ExecutionException | InterruptedException e) {
      log.error("Error saving vector: {}", e.getMessage());
      throw new RuntimeException("Error saving vector", e);
    }
  }

  /**
   * Checks if the collection exists in Qdrant. If it does not exist, a new collection is created.
   */
  private void checkCollectionExistence() {
    try {
      qdrantClient.getCollectionInfoAsync(collectionName).get();
    } catch (Exception ex) {
      log.info("Collection '{}' not found. Creating a new collection...", collectionName);
      this.createCollection();
    }
  }

  /** Creates a new collection in Qdrant with specified vector parameters. */
  private void createCollection() {
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

  /**
   * Constructs a points structure from a list of float values representing a vector.
   *
   * @param points the vector values
   * @return a {@link Points.PointStruct} object containing the vector and associated metadata
   */
  private Points.PointStruct buildPointStruct(
      List<Float> points, Map<String, JsonWithInt.Value> payload) {
    return Points.PointStruct.newBuilder()
        .setId(id(UUID.randomUUID()))
        .setVectors(vectors(points))
        .putAllPayload(payload)
        .build();
  }

  /**
   * Retrieves the embeddings for the given text asynchronously from Azure OpenAI.
   *
   * @param text the text to be embedded
   * @return a list of {@link EmbeddingItem} objects containing the embeddings
   */
  private List<EmbeddingItem> retrieveEmbeddings(String text) {
    return openAIAsyncClient
        .getEmbeddings(embeddingModelName, new EmbeddingsOptions(List.of(text)))
        .block()
        .getData();
  }
}
