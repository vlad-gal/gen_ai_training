package com.epam.training.gen.ai.api;

import com.epam.training.gen.ai.dto.AnswerDTO;
import com.epam.training.gen.ai.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/prompt")
@RequiredArgsConstructor
public class PromptAPI {
    private final OpenAIService openAIService;

    @GetMapping
    public AnswerDTO getAnswer(@RequestParam(value = "prompt") String prompt) {
        return openAIService.generateAnswer(prompt);
    }
}
