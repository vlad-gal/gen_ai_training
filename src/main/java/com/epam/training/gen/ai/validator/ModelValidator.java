package com.epam.training.gen.ai.validator;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ModelValidator {
  @Value("${client-azureopenai-deployment-names}.split(\",\")")
  private List<String> deploymentNames;

  public void validateModel(String modelId) {
    if (deploymentNames.isEmpty()) {
      throw new IllegalArgumentException("No deployment names found");
    }
    if (!deploymentNames.contains(modelId)) {
      throw new IllegalArgumentException("Model not found");
    }
  }
}
