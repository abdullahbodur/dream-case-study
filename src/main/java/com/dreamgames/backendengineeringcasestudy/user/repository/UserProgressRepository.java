package com.dreamgames.backendengineeringcasestudy.user.repository;

import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
}
