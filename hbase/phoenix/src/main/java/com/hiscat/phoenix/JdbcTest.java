package com.hiscat.phoenix;

import java.sql.*;

/**
 * @author hiscat
 */
public class JdbcTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        try (Connection connection = DriverManager.getConnection("jdbc:phoenix:hadoop104,hadoop105,hadoop106");
             PreparedStatement statement = connection.prepareStatement("select * from \"test\"");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                System.out.printf("id:%s,name:%s,address:%s\n", resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"));
            }
        }

    }
}
