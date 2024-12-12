package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

public class WeatherPredictorPlugin {

  @DefineKernelFunction(name = "weatherPrediction")
  public String calculateAge(@KernelFunctionParameter(name = "city") String city) {
    return "In %s now is sunny weather".formatted(city);
  }
}
