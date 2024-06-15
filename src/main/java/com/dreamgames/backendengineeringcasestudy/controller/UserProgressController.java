package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.dto.request.CreateUserDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.UserDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.UserProgressDTO;
import com.dreamgames.backendengineeringcasestudy.service.UserProgressService;
import com.dreamgames.backendengineeringcasestudy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller class for managing user progress. It handles HTTP requests related to user progress.
 */
@RestController
@RequestMapping("/api/v1/userProgress")
@RequiredArgsConstructor
public class UserProgressController {

  private final UserProgressService userProgressService;
  private final UserService userService;

  /**
   * method is used to create a new user and their progress. It first creates a user using the
   * UserService and then creates a progress record for the user using the UserProgressService.
   *
   * @param requestDTO request DTO that contains the details of the user to be created.
   * @return UserProgressDTO returns a DTO object that contains the user's progress details.
   */
  @PostMapping
  public UserProgressDTO createUser(
      @RequestBody
      @Validated
      CreateUserDTO requestDTO) {
    UserDTO userDTO = userService.createUser(requestDTO);
    return userProgressService.createUser(userDTO.getId());
  }

  /**
   * method is used to update the level of a user. It uses the UserProgressService to update the
   * level of the user.
   *
   * @param userId ID of the user whose level is to be updated.
   * @return UserProgressDTO returns a DTO object that contains the user's progress details.
   */
  @PutMapping("/updateLevel")
  public UserProgressDTO updateLevel(
      @Validated
      Long userId) {
    return userProgressService.updateLevel(userId);
  }
}