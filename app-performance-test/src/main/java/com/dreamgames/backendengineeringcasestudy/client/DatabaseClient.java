package com.dreamgames.backendengineeringcasestudy.client;

import com.dreamgames.backendengineeringcasestudy.configuration.TestConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseClient {

  public void executeStatement(String query) {

    try (Connection conn = DriverManager.getConnection(TestConfiguration.DATABASE_URL,
        TestConfiguration.DATABASE_USERNAME, TestConfiguration.DATABASE_PASSWORD);
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {

        // do something with the extracted data...
      }
    } catch (SQLException e) {
      // handle the exception
    }
  }
}
