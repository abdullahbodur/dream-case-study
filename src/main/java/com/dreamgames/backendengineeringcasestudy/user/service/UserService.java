package com.dreamgames.backendengineeringcasestudy.user.service;

import com.dreamgames.backendengineeringcasestudy.api.dto.request.CreateUserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.UserDTO;
import com.dreamgames.backendengineeringcasestudy.api.dto.response.utils.UserDTOMapper;
import com.dreamgames.backendengineeringcasestudy.user.entity.User;
import com.dreamgames.backendengineeringcasestudy.exceptions.EntityNotFoundException;
import com.dreamgames.backendengineeringcasestudy.user.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.common.utils.DigestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user progress. This service is responsible for creating, updating,
 * and retrieving user progress records. It also checks if a user meets the minimum requirements
 * for a new tournament, and handles the withdrawal and deposit of coins for a user.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserDTOMapper userDTOMapper;
  private final DigestUtils digestUtils;

  /**
   * Creates a new user with the provided details.
   *
   * @param userDTO The data transfer object containing the details of the user to be created.
   * @return UserDTO The data transfer object containing the details of the created user.
   */
  public UserDTO createUser(CreateUserDTO userDTO) {
    User user = new User();
    user.setEmail(userDTO.email());
    user.setPassword(digestUtils.hash(userDTO.password()));
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException(
          "User with email " + userDTO.email() + " already exists.");
    }
    return userDTOMapper.apply(user);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to be retrieved.
   * @return UserDTO The data transfer object containing the details of the retrieved user.
   * @throws EntityNotFoundException if no user is found with the provided ID.
   */
  public UserDTO getUser(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("User not found with ID: " + id));
    return userDTOMapper.apply(user);
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email The email of the user to be retrieved.
   * @return UserDTO The data transfer object containing the details of the retrieved user.
   * @throws EntityNotFoundException if no user is found with the provided email.
   */
  public UserDTO getUserByEmail(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new EntityNotFoundException("User not found with email: " + email));
    return userDTOMapper.apply(user);
  }

}
