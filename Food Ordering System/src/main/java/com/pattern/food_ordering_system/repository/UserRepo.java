package com.pattern.food_ordering_system.repository;

import com.pattern.food_ordering_system.config.DBConnection;
import com.pattern.food_ordering_system.entity.User;
import com.pattern.food_ordering_system.model.user.Role;
import com.pattern.food_ordering_system.model.user.UserFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepo {
    public static User findUserByUsername(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, username);
            User user = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = UserFactory.createUser(rs.getString("role"));

                user.setId(rs.getLong("user_id"));
                user.setUserName(rs.getString("username"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setRole(Role.valueOf(rs.getString("role")));
                user.setPassword(rs.getString("password"));
                user.setUserImgPath(rs.getString("image_path"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean save(String username, String password, String zone, String role, String phoneNumber, String imagePath) {
        String sql = "INSERT INTO users(username, password, role, zone, phone_number, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role.toUpperCase());
            ps.setString(4, zone);
            ps.setString(5, phoneNumber);
            ps.setString(6, imagePath);
            int isAdded = ps.executeUpdate();

            if (isAdded > 0 && role.equalsIgnoreCase(Role.RESTAURANT.toString())) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long newRestaurantId = generatedKeys.getLong(1);
                    System.out.println("ID >>>>>>>>> " + newRestaurantId);
                    RestaurantRepo.addRestaurantIdToRestaurants(newRestaurantId);
                }
            }
            return isAdded > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}