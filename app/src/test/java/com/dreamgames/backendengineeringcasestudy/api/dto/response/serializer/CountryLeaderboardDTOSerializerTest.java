package com.dreamgames.backendengineeringcasestudy.api.dto.response.serializer;

import com.dreamgames.backendengineeringcasestudy.api.dto.response.CountryLeaderboardDTO;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.jupiter.MockitoExtension;
import org.hibernate.type.SerializationException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryLeaderboardDTOSerializerTest {

  private CountryLeaderboardDTOSerializer serializer;

  @BeforeEach
  void setUp() {
    serializer = new CountryLeaderboardDTOSerializer();
  }

  @Test
  @DisplayName("Should serialize list of CountryLeaderboardDTOs")
  void shouldSerialize() {
    List<CountryLeaderboardDTO> dtos = Arrays.asList(new CountryLeaderboardDTO(
        Country.UNITED_STATES, 0), new CountryLeaderboardDTO(Country.GERMANY, 1));
    byte[] expectedBytes = "[{\"country\":\"UNITED_STATES\",\"totalScore\":0},{\"country\":\"GERMANY\",\"totalScore\":1}]".getBytes();
    byte[] actualBytes = serializer.serialize(dtos);
    assertArrayEquals(expectedBytes, actualBytes);
  }

  @Test
  @DisplayName("Should throw SerializationException when serialization fails")
  void shouldThrowOnSerializationFailure() throws JsonProcessingException {
    List<CountryLeaderboardDTO> dtos = Arrays.asList(new CountryLeaderboardDTO(),
        new CountryLeaderboardDTO());
    ObjectMapper objectMapper = mock(ObjectMapper.class);
    when(objectMapper.writeValueAsBytes(dtos)).thenThrow(JsonProcessingException.class);
    serializer = new CountryLeaderboardDTOSerializer(objectMapper);
    assertThrows(SerializationException.class, () -> serializer.serialize(dtos));
  }

  @Test
  @DisplayName("Should deserialize byte array to list of CountryLeaderboardDTOs")
  void shouldDeserialize() {
    String json = "[{\"country\":\"UNITED_STATES\",\"totalScore\":0},{\"country\":\"GERMANY\",\"totalScore\":1}]";
    byte[] bytes = json.getBytes();
    List<CountryLeaderboardDTO> expected = Arrays.asList(
        new CountryLeaderboardDTO(Country.UNITED_STATES, 0),
        new CountryLeaderboardDTO(Country.GERMANY, 1));
    assertEquals(expected, serializer.deserialize(bytes));
  }

  @Test
  @DisplayName("Should return null when deserializing null")
  void shouldReturnNullWhenDeserializingNull() {
    assertNull(serializer.deserialize(null));
  }

  @Test
  @DisplayName("Should throw SerializationException when deserialization fails")
  void shouldThrowOnDeserializationFailure() throws IOException {
    byte[] bytes = "serialized data".getBytes();
    ObjectMapper objectMapper = mock(ObjectMapper.class);
    when(objectMapper.readValue(bytes, List.class)).thenThrow(JsonProcessingException.class);
    serializer = new CountryLeaderboardDTOSerializer(objectMapper);
    assertThrows(PotentialStubbingProblem.class, () -> serializer.deserialize(bytes));
  }
}