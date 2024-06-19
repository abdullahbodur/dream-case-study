package com.dreamgames.backendengineeringcasestudy.api.dto.response.serializer;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.GroupLeaderboardUserDTO;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.type.SerializationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupLeaderboardUserDTOSerializerTest {


  private GroupLeaderboardUserDTOSerializer serializer;

  @BeforeEach
  void setUp() {
    serializer = new GroupLeaderboardUserDTOSerializer();
  }

  @Test
  @DisplayName("Should serialize list of GroupLeaderboardUserDTOs")
  void shouldSerialize() {
    List<GroupLeaderboardUserDTO> dtos = Arrays.asList(new GroupLeaderboardUserDTO(
            1L, "user1", Country.UNITED_STATES, 0),
        new GroupLeaderboardUserDTO(2L, "user2", Country.GERMANY, 1));

    byte[] actualBytes = serializer.serialize(dtos);

    String expectedJson = "[{\"userId\":1,\"nickname\":\"user1\",\"country\":\"UNITED_STATES\",\"tournamentScore\":0},{\"userId\":2,\"nickname\":\"user2\",\"country\":\"GERMANY\",\"tournamentScore\":1}]";

    assertEquals(expectedJson, new String(actualBytes));
  }

  @Test
  @DisplayName("Should throw SerializationException when serialization fails")
  void shouldThrowSerializationException() throws JsonProcessingException {
    ObjectMapper objectMapper = mock(ObjectMapper.class);
    when(objectMapper.writeValueAsBytes(any())).thenThrow(JsonProcessingException.class);
    serializer = new GroupLeaderboardUserDTOSerializer(objectMapper);

    assertThrows(SerializationException.class, () -> serializer.serialize(List.of()));
  }

  @Test
  @DisplayName("Should deserialize list of GroupLeaderboardUserDTOs")
  void shouldDeserialize() {
    String json = "[{\"userId\":1,\"nickname\":\"user1\",\"country\":\"UNITED_STATES\",\"tournamentScore\":0},{\"userId\":2,\"nickname\":\"user2\",\"country\":\"GERMANY\",\"tournamentScore\":1}]";
    byte[] bytes = json.getBytes();

    List<GroupLeaderboardUserDTO> actualDtos = serializer.deserialize(bytes);

    List<GroupLeaderboardUserDTO> expectedDtos = Arrays.asList(
        new GroupLeaderboardUserDTO(1L, "user1", Country.UNITED_STATES, 0),
        new GroupLeaderboardUserDTO(2L, "user2", Country.GERMANY, 1));

    assertEquals(expectedDtos.size(), actualDtos.size());
    for (int i = 0; i < expectedDtos.size(); i++) {
      assertEquals(expectedDtos.get(i).getUserId(), actualDtos.get(i).getUserId());
      assertEquals(expectedDtos.get(i).getNickname(), actualDtos.get(i).getNickname());
      assertEquals(expectedDtos.get(i).getCountry(), actualDtos.get(i).getCountry());
      assertEquals(expectedDtos.get(i).getTournamentScore(),
          actualDtos.get(i).getTournamentScore());
    }
  }

  @Test
  @DisplayName("Should return null when deserializing null bytes")
  void shouldReturnNullWhenDeserializingNullBytes() {
    assertNull(serializer.deserialize(null));
  }

}