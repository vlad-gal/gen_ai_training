package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.AnswerDTO;

public interface Assistant {
  AnswerDTO answer(String query);
}
