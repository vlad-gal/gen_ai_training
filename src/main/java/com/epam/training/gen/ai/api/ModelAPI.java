package com.epam.training.gen.ai.api;

import com.epam.training.gen.ai.dto.ModelDTO;
import com.epam.training.gen.ai.service.ModelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("models")
@RequiredArgsConstructor
public class ModelAPI {
  private final ModelService modelService;

  @GetMapping
  public List<ModelDTO> getModels() {
    return modelService.getModels();
  }
}
