package com.dreamgames.backendengineeringcasestudy.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;

public class JsonReader {
  public static <T> T readJson(String filePath, Class<T> clazz) {
    ObjectMapper objectMapper = new ObjectMapper();
    T result = null;
    try {
      result = objectMapper.readValue(Paths.get(filePath).toFile(), clazz);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}