package com.dreamgames.backendengineeringcasestudy.entity;

import com.dreamgames.backendengineeringcasestudy.enumaration.Country;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "UserProgress", schema = "public")
public class UserProgress {
  @Id
  private Long id;

  private double coinBalance;

  private int level;

  @Enumerated(EnumType.STRING)
  private Country country;
}
