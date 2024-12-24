package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.AnswerDTO;
import org.springframework.web.multipart.MultipartFile;

public interface RagService {
  /**
   * Uploads the file to the vector storage
   *
   * @param file the file to upload
   */
  void upload(MultipartFile file);

  /**
   * Generates an answer for the given query
   *
   * @param query the query to generate an answer for
   * @return the generated answer
   */
  AnswerDTO generateAnswer(String query);
}
