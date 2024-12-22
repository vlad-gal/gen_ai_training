package com.epam.training.gen.ai.api;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.training.gen.ai.dto.EmbeddingDTO;
import com.epam.training.gen.ai.dto.ResponseEmbeddingDTO;
import com.epam.training.gen.ai.service.EmbeddingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("embeddings")
@RequiredArgsConstructor
public class EmbeddingAPI {
  private final EmbeddingService embeddingService;

  @GetMapping("search")
  public List<ResponseEmbeddingDTO> search(@RequestBody EmbeddingDTO embeddingDTO) {
    return embeddingService.searchEmbedding(embeddingDTO);
  }

  @PostMapping("build")
  public List<EmbeddingItem> build(@RequestBody EmbeddingDTO embeddingDTO) {
    return embeddingService.buildEmbedding(embeddingDTO);
  }

  @PostMapping("store")
  public String store(@RequestBody EmbeddingDTO embeddingDTO) {
    return embeddingService.storeEmbedding(embeddingDTO);
  }
}
