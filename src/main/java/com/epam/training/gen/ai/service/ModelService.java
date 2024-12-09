package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.ModelDTO;
import java.util.List;

public interface ModelService {
  /**
   * Get all models
   *
   * @return list of models
   */
  List<ModelDTO> getModels();
}
