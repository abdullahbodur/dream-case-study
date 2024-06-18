package com.dreamgames.backendengineeringcasestudy.api.dto.response.serializer;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.CountryLeaderboardDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * The CountryLeaderboardDTOSerializer class implements the RedisSerializer interface and provides methods to serialize and deserialize List<CountryLeaderboardDTO> objects.
 * It uses Jackson's ObjectMapper to convert between List<CountryLeaderboardDTO> and byte array.
 */
public class CountryLeaderboardDTOSerializer implements
    RedisSerializer<List<CountryLeaderboardDTO>> {

  private final ObjectMapper objectMapper;

  /**
   * The constructor initializes the ObjectMapper.
   */
  public CountryLeaderboardDTOSerializer() {
    this.objectMapper = new ObjectMapper();
  }

  /**
   * This method serializes a List<CountryLeaderboardDTO> into a byte array.
   *
   * @param dtos The List<CountryLeaderboardDTO> to serialize.
   * @return A byte array representing the serialized List<CountryLeaderboardDTO>.
   * @throws SerializationException if an error occurs during serialization.
   */
  @Override
  public byte[] serialize(List<CountryLeaderboardDTO> dtos) throws SerializationException {
    try {
      return objectMapper.writeValueAsBytes(dtos);
    } catch (IOException e) {
      throw new SerializationException("Error serializing List<CountryLeaderboardDTO>", e);
    }
  }

  /**
   * This method deserializes a byte array into a List<CountryLeaderboardDTO>.
   *
   * @param bytes The byte array to deserialize.
   * @return A List<CountryLeaderboardDTO> representing the deserialized byte array.
   * @throws SerializationException if an error occurs during deserialization.
   */
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