package com.epam.training.gen.ai.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AnswerDTO {
    private String answer;
}
