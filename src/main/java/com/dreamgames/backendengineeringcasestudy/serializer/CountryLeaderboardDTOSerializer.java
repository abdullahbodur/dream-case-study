package com.dreamgames.backendengineeringcasestudy.serializer;

import com.dreamgames.backendengineeringcasestudy.dto.response.CountryLeaderboardDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.serializer.RedisSerializer;

public class CountryLeaderboardDTOSerializer implements
    RedisSerializer<List<CountryLeaderboardDTO>> {

  private final ObjectMapper objectMapper;

  public CountryLeaderboardDTOSerializer() {
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public byte[] serialize(List<CountryLeaderboardDTO> dtos) throws SerializationException {
    try {
      return objectMapper.writeValueAsBytes(dtos);
    } catch (IOException e) {
      throw new SerializationException("Error serializing List<CountryLeaderboardDTO>", e);
    }
  }

  @Override
  public List<CountryLeaderboardDTO> deserialize(byte[] bytes) throws SerializationException {
    try {
      if (bytes == null) {
        return null;
      }
      return objectMapper.readValue(bytes, new TypeReference<List<CountryLeaderboardDTO>>() {
      });
    } catch (IOException e) {
      throw new SerializationException("Error deserializing List<CountryLeaderboardDTO>", e);
    }
  }
}