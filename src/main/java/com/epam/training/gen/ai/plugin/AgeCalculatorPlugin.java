package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import java.time.LocalDate;
import java.time.Period;

public class AgeCalculatorPlugin {

  @DefineKernelFunction(name = "ageCalculation")
  public String calculateAge(@KernelFunctionParameter(name = "date") String date) {
    LocalDate birthDate = LocalDate.parse(date);
    LocalDate currentDate = LocalDate.now();
    Period period = Period.between(birthDate, currentDate);
    return "Your age is: %d years, %d months, and %d days"
        .formatted(period.getYears(), period.getMonths(), period.getDays());
  }
}
