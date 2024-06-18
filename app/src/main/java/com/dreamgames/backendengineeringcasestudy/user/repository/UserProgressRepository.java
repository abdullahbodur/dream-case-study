package com.dreamgames.backendengineeringcasestudy.user.repository;

import com.dreamgames.backendengineeringcasestudy.user.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The UserProgressRepository interface extends JpaRepository and provides methods to interact with the UserProgress entities in the database.
 * As of now, it does not include any custom queries. All basic CRUD operations are provided by the JpaRepository.
 */
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
}