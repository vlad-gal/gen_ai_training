package com.epam.training.gen.ai.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseModelDTO {
  private List<ModelDTO> data;
}
