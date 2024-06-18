package com.dreamgames.backendengineeringcasestudy.common.utils;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

/**
 * The DigestUtils class provides utility methods for hashing data.
 * It is annotated with @Component, meaning that Spring will automatically create an instance of this class.
 */
@Component
public class DigestUtils {

  /**
   * This method hashes the input data using SHA-256 and returns the hashed value as a string.
   *
   * @param data The data to be hashed.
   * @return The hashed value of the input data.
   */
  public String hash(String data) {
    return Hashing.sha256()
        .hashString(data, StandardCharsets.UTF_8)
        .toString();
  }
}