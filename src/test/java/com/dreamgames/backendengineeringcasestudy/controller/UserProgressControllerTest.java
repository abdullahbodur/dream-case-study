package com.dreamgames.backendengineeringcasestudy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dreamgames.backendengineeringcasestudy.dto.request.CreateUserDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.UserDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import com.dreamgames.backendengineeringcasestudy.service.UserProgressService;
import com.dreamgames.backendengineeringcasestudy.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserProgressControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserProgressService userProgressService;

  @MockBean
  private UserService userService;

  @Test
  public void testCreateUser() throws Exception {
    CreateUserDTO requestDTO = new CreateUserDTO("test@test.com",
        "testuser",
        "Password123!",
        "Password123!");

    UserDTO userDTO = new UserDTO(
        1L,
        "test@test.com");

    UserProgressDTO userProgressDTO = new UserProgressDTO(
        1L,
        5000,
        1,
        "testuser",
        Country.UNITED_STATES);

    when(userService.createUser(any(CreateUserDTO.class))).thenReturn(userDTO);
    when(userProgressService.createUser(any(Long.class),
        any(String.class)
    )).thenReturn(userProgressDTO);

    mockMvc.perform(post("/api/v1/userProgress").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDTO))).andExpect(status().isOk());
  }

  @Test
  public void testUpdateLevel() throws Exception {
    Long userId = 1L;

    UserProgressDTO userProgressDTO = new UserProgressDTO(
        1L,
        5000,
        1,
        "testuser",
        Country.UNITED_STATES);

    when(userProgressService.updateLevel(any(Long.class))).thenReturn(userProgressDTO);

    mockMvc.perform(put("/api/v1/userProgress/updateLevel").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userId))).andExpect(status().isOk());
  }
}