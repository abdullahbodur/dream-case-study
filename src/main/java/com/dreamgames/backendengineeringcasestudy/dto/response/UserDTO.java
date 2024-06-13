package com.dreamgames.backendengineeringcasestudy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

  private Long id;
  private String username;
  private String email;
}
