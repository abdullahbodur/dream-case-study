package com.dreamgames.backendengineeringcasestudy.serializer;

import com.dreamgames.backendengineeringcasestudy.dto.response.GroupLeaderboardUserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.serializer.RedisSerializer;

public class GroupLeaderboardUserDTOSerializer implements
    RedisSerializer<List<GroupLeaderboardUserDTO>> {

  private final ObjectMapper objectMapper;

  public GroupLeaderboardUserDTOSerializer() {
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public byte[] serialize(List<GroupLeaderboardUserDTO> dtos) throws SerializationException {
    try {
      return objectMapper.writeValueAsBytes(dtos);
    } catch (IOException e) {
      throw new SerializationException("Error serializing List<GroupLeaderboardUserDTO>", e);
    }
  }

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