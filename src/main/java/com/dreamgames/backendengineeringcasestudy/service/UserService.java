package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.dto.request.CreateUserDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.UserDTO;
import com.dreamgames.backendengineeringcasestudy.dto.response.utils.UserDTOMapper;
import com.dreamgames.backendengineeringcasestudy.entity.User;
import com.dreamgames.backendengineeringcasestudy.repository.UserRepository;
import com.dreamgames.backendengineeringcasestudy.utils.NicknameGenerator;
import com.dreamgames.backendengineeringcasestudy.utils.DigestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserDTOMapper userDTOMapper;

  private final NicknameGenerator nicknameGenerator;

  private final DigestUtils digestUtils;

  public UserDTO createUser(CreateUserDTO userDTO) {
    User user = new User();
    user.setUsername(nicknameGenerator.makeNicknameUnique(userDTO.nickname()));
    user.setEmail(userDTO.email());
    user.setPassword(digestUtils.hash(userDTO.password()));
    userRepository.save(user);
    return userDTOMapper.apply(user);
  }

  public UserDTO getUser(Long id) {
    User user = userRepository.findById(id).orElseThrow();
    return userDTOMapper.apply(user);
  }

  public UserDTO getUserByEmail(String email) {
    User user = userRepository.findByEmail(email).orElseThrow();
    return userDTOMapper.apply(user);
  }

}
