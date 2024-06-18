package com.dreamgames.backendengineeringcasestudy.client;

import com.dreamgames.backendengineeringcasestudy.dto.request.CreateUserDTO;
import com.dreamgames.backendengineeringcasestudy.dto.request.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppClient {

  private HttpClient httpClient;
  private ObjectMapper objectMapper;
  private String baseURL;

  public AppClient(String baseURL) {
    this.httpClient = HttpClient.newHttpClient();
    this.objectMapper = new ObjectMapper();
    this.baseURL = baseURL;
  }

  public JsonNode createUser(CreateUserDTO createUserDTO) throws Exception {
    String requestBody = objectMapper.writeValueAsString(createUserDTO);
    log.info("Request body: {}", requestBody);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(baseURL + "/api/v1/userProgress"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    log.info("Response: {}", response.body());
    return objectMapper.readTree(response.body());
  }

  public JsonNode updateLevel(UpdateLevelRequest request) throws Exception {
    String requestBody = objectMapper.writeValueAsString(request);
    log.info("Request body: {}", requestBody);
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(new URI(baseURL + "/api/v1/userProgress/updateLevel"))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
        .build();

    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    log.info("Response: {}", response.body());
    return objectMapper.readTree(response.body());
  }
}