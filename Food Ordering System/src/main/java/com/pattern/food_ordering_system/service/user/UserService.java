package com.pattern.food_ordering_system.service.user;

import com.pattern.food_ordering_system.entity.User;
import com.pattern.food_ordering_system.repository.UserRepo;
import com.pattern.food_ordering_system.utils.HandleUploading;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserService {
    public static String login(String username, String password) {
        User user = UserRepo.findUserByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            UserSessionInitializer userSession = SessionInitializerFactory.getUserSession(user.getRole());
            userSession.loadUserData();
            return userSession.getTargetScene();
        }

        else {
            throw new RuntimeException("Username or Password is incorrect!");
        }
    }

    public static String signup(String username, String password, String zone, String role, String phoneNumber, ImageView logoPreview) {
        User user = UserRepo.findUserByUsername(username);

        if (user != null) return "This username is already taken. Please choose another one !";
        else {
            Image image = logoPreview.getImage();
            String imagePath = "default";
            if (image != null) {
                String url = image.getUrl();
                String imageName = url.substring(url.lastIndexOf('/') + 1); // get image name
                if (!imageName.equals("user-icon-SYS.png")) {
                    imagePath = HandleUploading.upload(logoPreview, imageName);
                    imagePath = "\\src\\main\\resources\\users-images\\" + imagePath;
                }
            }
            if (UserRepo.save(username, password, zone, role, phoneNumber, imagePath))
                return "Account Created Successfully";
            return "Account creation failed. Please try again.";
        }
    }
}
