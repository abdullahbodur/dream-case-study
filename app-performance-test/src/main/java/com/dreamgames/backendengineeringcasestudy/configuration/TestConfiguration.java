package com.dreamgames.backendengineeringcasestudy.configuration;

public class TestConfiguration {
  // get from env
  public static final String BASE_URL = System.getenv("BASE_URL") != null ? System.getenv("BASE_URL") : "http://localhost:8080";
  public static final String DATABASE_URL = System.getenv("DATABASE_URL") != null ? System.getenv("DATABASE_URL") : "jdbc:mysql://localhost:3306/mysql-db";
  public static final String DATABASE_USERNAME = System.getenv("DATABASE_USERNAME") != null ? System.getenv("DATABASE_USERNAME") : "username";
  public static final String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD") != null ? System.getenv("DATABASE_PASSWORD") : "password";
}
