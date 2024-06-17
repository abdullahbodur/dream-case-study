package com.dreamgames.backendengineeringcasestudy.common.utils;


import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class DigestUtils {

  public String hash(String data) {
    return Hashing.sha256()
        .hashString(data, StandardCharsets.UTF_8)
        .toString();
  }
}
