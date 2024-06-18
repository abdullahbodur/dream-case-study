package com.dreamgames.backendengineeringcasestudy.api.dto.response.serializer;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * The GroupLeaderboardUserDTOSerializer class implements the RedisSerializer interface and provides methods to serialize and deserialize List<GroupLeaderboardUserDTO> objects.
 * It uses Jackson's ObjectMapper to convert between List<GroupLeaderboardUserDTO> and byte array.
 */
public class GroupLeaderboardUserDTOSerializer implements
    RedisSerializer<List<GroupLeaderboardUserDTO>> {

  private final ObjectMapper objectMapper;

  /**
   * The constructor initializes the ObjectMapper.
   */
  public GroupLeaderboardUserDTOSerializer() {
    this.objectMapper = new ObjectMapper();
  }

  public GroupLeaderboardUserDTOSerializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * This method serializes a List<GroupLeaderboardUserDTO> into a byte array.
   *
   * @param dtos The List<GroupLeaderboardUserDTO> to serialize.
   * @return A byte array representing the serialized List<GroupLeaderboardUserDTO>.
   * @throws SerializationException if an error occurs during serialization.
   */
  @Override
  public byte[] serialize(List<GroupLeaderboardUserDTO> dtos) throws SerializationException {
    try {
      return objectMapper.writeValueAsBytes(dtos);
    } catch (IOException e) {
      throw new SerializationException("Error serializing List<GroupLeaderboardUserDTO>", e);
    }
  }

  /**
   * This method deserializes a byte array into a List<GroupLeaderboardUserDTO>.
   *
   * @param bytes The byte array to deserialize.
   * @return A List<GroupLeaderboardUserDTO> representing the deserialized byte array.
   * @throws SerializationException if an error occurs during deserialization.
   */
  @Override
  public List<GroupLeaderboardUserDTO> deserialize(byte[] bytes) throws SerializationException {
    try {
      if (bytes == null) {
        return null;
      }
      return objectMapper.readValue(bytes, new TypeReference<List<GroupLeaderboardUserDTO>>() {
      });
    } catch (IOException e) {
      throw new SerializationException("Error deserializing List<GroupLeaderboardUserDTO>", e);
    }
  }
}