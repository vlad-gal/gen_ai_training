package com.epam.training.gen.ai.service;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.training.gen.ai.dto.EmbeddingDTO;
import com.epam.training.gen.ai.dto.ResponseEmbeddingDTO;
import java.util.List;

public interface EmbeddingService {

  /**
   * Build embedding for the given text
   *
   * @param embeddingDTO text to build embedding for
   * @return list of embedding items
   */
  List<EmbeddingItem> buildEmbedding(EmbeddingDTO embeddingDTO);

  /**
   * Search for embeddings
   *
   * @param embeddingDTO text to search for
   * @return list of embeddings
   */
  List<ResponseEmbeddingDTO> searchEmbedding(EmbeddingDTO embeddingDTO);

  /**
   * Store embedding
   *
   * @param embeddingDTO text to store
   * @return "Stored" if successful
   */
  String storeEmbedding(EmbeddingDTO embeddingDTO);
}
