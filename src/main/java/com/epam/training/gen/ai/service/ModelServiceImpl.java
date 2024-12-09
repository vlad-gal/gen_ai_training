package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.ModelDTO;
import com.epam.training.gen.ai.dto.ResponseModelDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {
  private final RestTemplate restTemplate;

  @Value("${client-azureopenai-endpoint}")
  private String endpoint;

  @Value("${client-azureopenai-key}")
  private String key;

  @Override
  public List<ModelDTO> getModels() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Api-Key", key);
    HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
    ResponseEntity<ResponseModelDTO> exchange =
        restTemplate.exchange(
            endpoint + "/openai/deployments", HttpMethod.GET, entity, ResponseModelDTO.class);
    return exchange.getBody().getData();
  }
}
