package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.AnswerDTO;
import com.epam.training.gen.ai.util.QdrantUtil;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagServiceImpl implements RagService {
  private final EmbeddingStoreIngestor ingestor;
  private final DocumentParser documentParser;
  private final ContentRetriever contentRetriever;
  private final ChatLanguageModel chatModel;
  private final QdrantUtil qdrantUtil;

  @Value("${rag.collection.name}")
  private String collectionName;

  @Override
  public void upload(MultipartFile file) {
    try {
      InputStream inputStream = file.getInputStream();
      Document document = documentParser.parse(inputStream);
      ingestor.ingest(document);
    } catch (IOException e) {
      log.error("Error while uploading file", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public AnswerDTO generateAnswer(String query) {
    qdrantUtil.checkCollectionExistence(collectionName);
    ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

    Assistant build =
        AiServices.builder(Assistant.class)
            .chatLanguageModel(chatModel)
            .contentRetriever(contentRetriever)
            .chatMemory(chatMemory)
            .build();

    return build.answer(query);
  }
}
