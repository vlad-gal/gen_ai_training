package com.epam.training.gen.ai.api;

import com.epam.training.gen.ai.dto.AnswerDTO;
import com.epam.training.gen.ai.service.RagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("rag")
@RequiredArgsConstructor
public class RagAPI {
  private final RagServiceImpl ragService;

  @PostMapping("upload")
  public void upload(@RequestBody MultipartFile file) {
    ragService.upload(file);
  }

  @GetMapping("ask")
  public AnswerDTO generateAnswer(@RequestParam(value = "query") String query) {
    return ragService.generateAnswer(query);
  }
}
