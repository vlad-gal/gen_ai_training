package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.AnswerDTO;

public interface OpenAIService {
  /**
   * Generate answer based on the prompt
   *
   * @param prompt prompt to generate answer
   * @param temperature temperature of the answer
   * @param modelId model id to generate answer
   * @return {@link AnswerDTO} with generated answer
   */
  AnswerDTO generateAnswer(String prompt, Double temperature, String modelId);
}
