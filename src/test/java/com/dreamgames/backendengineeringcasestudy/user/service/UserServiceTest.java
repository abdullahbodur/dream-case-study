package com.dreamgames.backendengineeringcasestudy.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.dreamgames.backendengineeringcasestudy.api.dto.request.CreateUserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.UserDTOMapper;
import com.dreamgames.backendengineeringcasestudy.user.entity.User;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.user.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.common.utils.DigestUtils;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserDTOMapper userDTOMapper;
  @Mock
  private DigestUtils digestUtils;

  @BeforeEach
  public void setUp() {
    openMocks(this);
  }

  @AfterEach
  public void tearDown() {
    userService = null;
  }


  @Test
  public void testCreateUser() {
    CreateUserDTO createUserDTO = new CreateUserDTO(
        "test@test.com",
        "test",
        "password",
        "password"
    );

    User user = new User();
    user.setEmail("test@test.com");
    user.setPassword("hashed_password");

    UserDTO userDTO = new UserDTO(
        1L,
        "test@test.com"
    );
    when(digestUtils.hash(anyString())).thenReturn("hashed_password");
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userDTOMapper.apply(any(User.class))).thenReturn(userDTO);
    UserDTO result = userService.createUser(createUserDTO);

    assertEquals(result.getEmail(), "test@test.com");

    verify(digestUtils, times(1)).hash(anyString());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  public void testGetUser() {
    Long id = 2L;
    User user = new User();
    user.setId(id);
    user.setEmail("test@test.com");
    user.setPassword("hashed_password");

    UserDTO userDTO = new UserDTO(
        id,
        "test@test.com"
    );

    when(userRepository.findById(any())).thenReturn(Optional.of(user));
    when(userDTOMapper.apply(any(User.class))).thenReturn(userDTO);

    UserDTO result = userService.getUser(id);

    assertEquals(result.getId(), id);
    assertEquals(result.getEmail(), "test@test.com");

    verify(userRepository, times(1)).findById(anyLong());
    verify(userDTOMapper, times(1)).apply(any(User.class));
  }

  @Test
  public void testGetUserByEmail() {
    String email = "test@test.com";
    User user = new User();
    user.setId(1L);
    user.setEmail(email);
    user.setPassword("hashed_password");

    UserDTO userDTO = new UserDTO(
        1L,
        email
    );

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
    when(userDTOMapper.apply(any(User.class))).thenReturn(userDTO);

    UserDTO result = userService.getUserByEmail(email);

    assertEquals(result.getId(), 1L);
    assertEquals(result.getEmail(), email);

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(userDTOMapper, times(1)).apply(any(User.class));
  }

  @Test
  public void testGetUserNotFound() {
    Long id = 2L;
    when(userRepository.findById(id)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> {
      userService.getUser(id);
    });
  }

  @Test
  public void testGetUserByEmailNotFound() {
    String email = "nonexistent@test.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> {
      userService.getUserByEmail(email);
    });
  }
}